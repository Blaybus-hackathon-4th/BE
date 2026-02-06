package com.example.blaybus4th.domain.note.controller;


import com.example.blaybus4th.domain.member.dto.request.UpdateNoteRequest;
import com.example.blaybus4th.domain.member.dto.response.InstitutionsListResponse;
import com.example.blaybus4th.domain.note.dto.request.CreateNoteRequest;
import com.example.blaybus4th.domain.note.dto.response.NotesListResponse;
import com.example.blaybus4th.domain.note.service.NoteService;
import com.example.blaybus4th.global.annotation.InjectMemberId;
import com.example.blaybus4th.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final NoteService noteService;


    /**
     * 노트 생성
     * 담당자 : 김영욱
     * object 레파지토리 생성시 주석 해제
     */
//    @PostMapping()
//    public ApiResponse<String> createNote(@InjectMemberId Long memberId, @RequestBody CreateNoteRequest request){
//        noteService.createNote(memberId,request);
//        return ApiResponse.onSuccess("노트 생성이 완료되었습니다.");
//    }

    /**
     * 노트 조회
     * 담당자 : 김영욱
     */
    @GetMapping()
    public ApiResponse<List<NotesListResponse>> getNotesList(@InjectMemberId Long memberId, @RequestParam Long objectId){
        return ApiResponse.onSuccess(noteService.getNotesList(memberId,objectId));
    }

    /**
     * 노트 수정
     * 담당자 : 김영욱
     */
    @PatchMapping("/{noteId}")
    public ApiResponse<String> updateNote(@PathVariable Long noteId,@RequestBody UpdateNoteRequest request){
        noteService.updateNote(noteId,request);
        return ApiResponse.onSuccess("노트 수정이 완료되었습니다.");
    }

    /**
     * 노트 삭제
     * 담당자 : 김영욱
     */
    @DeleteMapping("/{noteId}")
    public ApiResponse<String> deleteNote(@PathVariable Long noteId){
        noteService.deleteNote(noteId);
        return ApiResponse.onSuccess("노트 삭제가 완료되었습니다.");
    }


}
