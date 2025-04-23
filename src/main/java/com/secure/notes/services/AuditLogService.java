package com.secure.notes.services;

import com.secure.notes.models.AuditLog;
import com.secure.notes.models.Note;

import java.util.List;

public interface AuditLogService {
    void logNoteCreation(String username, Note note, String title);

    void logNoteUpdate(String username, Note note, String title);

    void logNoteDeletion(String username, Long noteId, String title);

    List<AuditLog> getAllAuditLogs();

    List<AuditLog> getAuditLogsForNoteId(Long id);
}
