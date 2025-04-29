package com.secure.notes.controllers;

import com.secure.notes.models.Note;
import com.secure.notes.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
// @CrossOrigin(
//     origins = "http://localhost:3000", // Allow React frontend
//     allowedHeaders = {"Authorization", "Content-Type", "X-XSRF-TOKEN"}, // Allow custom headers
//     allowCredentials = "true" // Allow cookies and credentials
// )
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping
    public Note createNote(@RequestBody Map<String, String> requestBody,
                        @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        String title = requestBody.get("title");
        String content = requestBody.get("content"); // Ensure content is correctly retrieved
        System.out.println("USER DETAILS: " + username);
        return noteService.createNoteForUser(username, content, title);
    }

    @GetMapping
    public List<Note> getUserNotes(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("USER DETAILS: " + username);
        return noteService.getNotesForUser(username);
    }

    @PutMapping("/{noteId}")
    public Note updateNote(@PathVariable Long noteId,
                       @RequestBody Map<String, String> requestBody,
                       @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        String title = requestBody.get("title");
        String content = requestBody.get("content");
    return noteService.updateNoteForUser(noteId, title, content, username);
}

    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable Long noteId, String title,
                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        noteService.deleteNoteForUser(noteId, username, title);
    }
}
