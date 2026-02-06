package com.example.blaybus4th.domain.note.service;

import com.example.blaybus4th.domain.member.dto.request.UpdateNoteRequest;
import com.example.blaybus4th.domain.member.entity.Member;
import com.example.blaybus4th.domain.member.repository.MemberRepository;
import com.example.blaybus4th.domain.note.dto.request.CreateNoteRequest;
import com.example.blaybus4th.domain.note.dto.response.NotesListResponse;
import com.example.blaybus4th.domain.note.entity.Note;
import com.example.blaybus4th.domain.note.repository.NoteRepository;
import com.example.blaybus4th.domain.object.entity.Object;
import com.example.blaybus4th.global.apiPayload.code.GeneralErrorCode;
import com.example.blaybus4th.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final MemberRepository memberRepository;
//    private final ObjectRepository objectRepository;


    /**
     * Object 레파지토리 생성시 주석 해제
     */
//    @Transactional
//    public void createNote(Long memberId, CreateNoteRequest request) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new GeneralException(GeneralErrorCode.MEMBER_NOT_FOUND));
//        Object object = objectRepository.findById(request.getObjectId())
//                .orElseThrow(() -> new IllegalArgumentException("Object not found"));
//        Note note = Note.createNote(member, object, request.getNoteContent());
//        noteRepository.save(note);
//
//    }
    @Transactional
    public List<NotesListResponse> getNotesList(Long memberId, Long objectId) {

        List<Note> result = noteRepository.findByMemberIdAndObjectId(memberId,objectId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.FAIL_TO_RETRIEVE_NOTES));

        return result.stream()
                .map(NotesListResponse::from)
                .toList();
    }
    @Transactional
    public void updateNote(Long noteId,UpdateNoteRequest request) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.FAIL_TO_UPDATE_NOTE));
       note.updateNote(request.getNoteContent());
    }

    @Transactional
    public void deleteNote(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.FAIL_TO_DELETE_NOTE));
        noteRepository.delete(note);

    }
}
