// // package com.secure.notes.security;

// // import org.springframework.beans.factory.annotation.Value;
// // import org.springframework.context.annotation.Bean;
// // import org.springframework.context.annotation.Configuration;
// // import org.springframework.web.servlet.config.annotation.CorsRegistry;
// // import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// // @Configuration
// // public class WebConfig implements WebMvcConfigurer {

// //     @Value("${frontend.url}")
// //     private String frontendUrl;

// //     @Bean
// //     public WebMvcConfigurer corsConfigurer() {
// //         return new WebMvcConfigurer() {
// //             @Override
// //             public void addCorsMappings(CorsRegistry registry) {
// //                 registry.addMapping("/api/notes")
// //                         .allowedOrigins(frontendUrl)
// //                         .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
// //                         .allowedHeaders("*", "Authorization", "X-XSRF-TOKEN")
// //                         .allowCredentials(true)
// //                         .maxAge(3600);

// //                 registry.addMapping("/api/notes/**")
// //                         .allowedOrigins(frontendUrl)
// //                         .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
// //                         .allowedHeaders("*")
// //                         .allowCredentials(true)
// //                         .maxAge(3600);

// //                 registry.addMapping("/api/auth/**")
// //                         .allowedOrigins(frontendUrl)
// //                         .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
// //                         .allowedHeaders("*")
// //                         .allowCredentials(true)
// //                         .maxAge(3600);
                
// //                 registry.addMapping("/api/admin/**")
// //                         .allowedOrigins(frontendUrl)
// //                         .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
// //                         .allowedHeaders("*")
// //                         .allowCredentials(true)
// //                         .maxAge(3600);

// //                 registry.addMapping("/api/csrf-token")
// //                         .allowedOrigins(frontendUrl)
// //                         .allowedMethods("GET")
// //                         .allowedHeaders("*")
// //                         .allowCredentials(true)
// //                         .maxAge(3600);
// //             }
// //         };
// //     }
// // }

// package com.secure.notes.security;

// import java.util.Arrays;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class WebConfig implements WebMvcConfigurer {

//     @Value("${frontend.url}")
//     private String frontendUrl;

//     @Bean
//     public WebMvcConfigurer corsConfigurer() {
//         return new WebMvcConfigurer() {
//             @Override
//             public void addCorsMappings(CorsRegistry registry) {
//                 registry.addMapping("/**")
//                         .allowedOrigins(frontendUrl)
//                         .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                         .allowedHeaders("Authorization",
//                                       "Accept",
//                                       "X-Requested-With",
//                                       "Content-Type",
//                                       "Access-Control-Request-Method",
//                                       "Access-Control-Request-Headers")
//                         .exposedHeaders("Access-Control-Allow-Origin",
//                                         "Access-Control-Allow-Credentials")
//                         .allowCredentials(true)
//                         .maxAge(3600);
//             }
//         };
//     }
// }

//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         return request -> {
//             CorsConfiguration config = new CorsConfiguration();
//             config.setAllowCredentials(true);
//             config.setAllowedOrigins(Arrays.asList("*"));
//             config.addAllowedOrigin(frontendUrl);
//             config.addAllowedHeader("*");
//             config.addAllowedMethod("*");
//             config.setMaxAge(3600L);
//             return config;
//         };
//     }
// }
