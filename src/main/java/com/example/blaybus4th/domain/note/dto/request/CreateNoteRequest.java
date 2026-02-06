package com.example.blaybus4th.domain.note.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
public class CreateNoteRequest {

    private Long objectId;
    private String noteContent;

}
