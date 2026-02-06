package com.example.blaybus4th.domain.note.dto.response;

import com.example.blaybus4th.domain.note.entity.Note;

public record NotesListResponse (
        Long noteId,
        String noteContent
){

    public static NotesListResponse from(Note note) {
        return new NotesListResponse(note.getNoteId(), note.getNoteContent());
    }


}
