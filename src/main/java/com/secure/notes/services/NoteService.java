package com.secure.notes.services;

import com.secure.notes.models.Note;

import java.util.List;

public interface NoteService {
    Note createNoteForUser(String username, String title, String content);

    Note updateNoteForUser(Long noteId, String title, String content, String username);

    void deleteNoteForUser(Long noteId, String username, String title);

    List<Note> getNotesForUser(String username);
}
