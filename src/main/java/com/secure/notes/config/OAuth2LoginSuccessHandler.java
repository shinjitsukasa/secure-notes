package com.secure.notes.config;

import com.secure.notes.models.AppRole;
import com.secure.notes.models.Role;
import com.secure.notes.models.User;
import com.secure.notes.repositories.RoleRepository;
import com.secure.notes.security.jwt.JwtUtils;
import com.secure.notes.security.services.UserDetailsImpl;
import com.secure.notes.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

        @Autowired
        private final UserService userService;

        @Autowired
        private final JwtUtils jwtUtils;

        @Autowired
        RoleRepository roleRepository;

        @Value("${frontend.url}")
        private String frontendUrl;

        String username;
        String idAttributeKey;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws ServletException, IOException {
                OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
                String provider = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
                DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
                Map<String, Object> attributes = principal.getAttributes();

                // Extract common attributes with null safety
                String email = Optional.ofNullable(attributes.get("email"))
                                .map(Object::toString)
                                .orElse("");
                String name = Optional.ofNullable(attributes.get("name"))
                                .map(Object::toString)
                                .orElse("");

                // Set provider-specific attributes
                switch (provider) {
                        case "github":
                                username = Optional.ofNullable(attributes.get("login"))
                                                .map(Object::toString)
                                                .orElse("github_user_" + UUID.randomUUID().toString().substring(0, 8));
                                idAttributeKey = "id";
                                break;
                        case "google":
                                username = email.split("@")[0];
                                idAttributeKey = "sub";
                                break;
                        case "facebook":
                                username = attributes.getOrDefault("name", "facebook_user").toString()
                                                .replaceAll("\\s+", "").toLowerCase();
                                idAttributeKey = "id";
                                break;
                        case "apple":
                                username = attributes.getOrDefault("email", "apple_user").toString().split("@")[0];
                                idAttributeKey = "sub";
                                break;
                        default:
                                username = "user_" + UUID.randomUUID().toString().substring(0, 8);
                                idAttributeKey = "id";
                }

                // Ensure email is not empty, create consistent dummy email if needed
                if (email.trim().isEmpty()) {
                        String baseUsername = !username.isEmpty() ? username
                                        : (!name.isEmpty() ? name.replaceAll("\\s+", "").toLowerCase()
                                                        : "user_" + UUID.randomUUID().toString().substring(0, 8));
                        email = baseUsername + "@" + provider + ".auth";
                }

                final String finalEmail = email; // Create final variable for lambda

                System.out.println("OAuth2 Login Details - Email: " + finalEmail +
                                ", Name: " + name +
                                ", Username: " + username +
                                ", Provider: " + provider);

                userService.findByEmail(finalEmail)
                                .ifPresentOrElse(
                                                user -> handleExistingUser(user, attributes, idAttributeKey,
                                                                oAuth2AuthenticationToken),
                                                () -> handleNewUser(finalEmail, username, provider, attributes,
                                                                idAttributeKey,
                                                                oAuth2AuthenticationToken));

                this.setAlwaysUseDefaultTargetUrl(true);
                handleJwtTokenAndRedirect(username, finalEmail, authentication, request, response);
        }

        // Add these private helper methods
        private void handleExistingUser(User user, Map<String, Object> attributes,
                        String idAttributeKey, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
                DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                                List.of(new SimpleGrantedAuthority(user.getRole().getRoleName().name())),
                                attributes,
                                idAttributeKey);
                Authentication securityAuth = new OAuth2AuthenticationToken(
                                oauthUser,
                                List.of(new SimpleGrantedAuthority(user.getRole().getRoleName().name())),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                SecurityContextHolder.getContext().setAuthentication(securityAuth);
        }

        private void handleNewUser(String email, String username, String provider,
                        Map<String, Object> attributes, String idAttributeKey,
                        OAuth2AuthenticationToken oAuth2AuthenticationToken) {
                User newUser = new User();
                Optional<Role> userRole = roleRepository.findByRoleName(AppRole.ROLE_USER);
                if (userRole.isEmpty()) {
                        throw new RuntimeException("Default role not found");
                }
                newUser.setRole(userRole.get());
                newUser.setEmail(email);
                newUser.setUserName(username);
                newUser.setSignUpMethod(provider);
                userService.registerUser(newUser);

                DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                                List.of(new SimpleGrantedAuthority(newUser.getRole().getRoleName().name())),
                                attributes,
                                idAttributeKey);
                Authentication securityAuth = new OAuth2AuthenticationToken(
                                oauthUser,
                                List.of(new SimpleGrantedAuthority(newUser.getRole().getRoleName().name())),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                SecurityContextHolder.getContext().setAuthentication(securityAuth);
        }

        private void handleJwtTokenAndRedirect(String username, String email,
                        Authentication authentication,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException, ServletException {
                DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
                Set<SimpleGrantedAuthority> authorities = new HashSet<>(oauth2User.getAuthorities().stream()
                                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                                .collect(Collectors.toList()));

                User user = userService.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName().name()));

                boolean is2faEnabled = user.isTwoFactorEnabled();

                UserDetailsImpl userDetails = new UserDetailsImpl(
                                null,
                                username,
                                email,
                                null,
                                is2faEnabled,
                                authorities);

                String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
                String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/redirect")
                                .queryParam("token", jwtToken)
                                .build().toUriString();

                this.setDefaultTargetUrl(targetUrl);
                super.onAuthenticationSuccess(request, response, authentication);
        }
}