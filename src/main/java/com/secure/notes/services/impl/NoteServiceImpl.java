package com.secure.notes.services.impl;

import com.secure.notes.models.Note;
import com.secure.notes.repositories.NoteRepository;
import com.secure.notes.services.AuditLogService;
import com.secure.notes.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private AuditLogService auditLogService;
    
    @Transactional
    @Override
    public Note createNoteForUser(String username, String content, String title) {
        Note note = new Note();
        note.setTitle(title); // Correctly set the title
        note.setContent(content);
        note.setOwnerUsername(username);
        Note savedNote = noteRepository.save(note);
        auditLogService.logNoteCreation(username, note, title);
        return savedNote;
    }

    @Transactional
    @Override
    public Note updateNoteForUser(Long noteId, String title, String content, String username) {
        Note note = noteRepository.findById(noteId).orElseThrow(()
                -> new RuntimeException("Note not found"));
        note.setTitle(title);
        note.setContent(content);
        note.setOwnerUsername(username);
        Note updatedNote = noteRepository.save(note);
        auditLogService.logNoteUpdate(username, note, title);
        return updatedNote;
    }

    @Override
    public void deleteNoteForUser(Long noteId, String username, String title) {
        Note note = noteRepository.findById(noteId).orElseThrow(
                () -> new RuntimeException("Note not found")
        );
        auditLogService.logNoteDeletion(username, noteId, title);
        noteRepository.delete(note);
    }

    @Transactional
    @Override
    public List<Note> getNotesForUser(String username) {
        List<Note> personalNotes = noteRepository
                .findByOwnerUsername(username);
        return personalNotes;
    }
}


