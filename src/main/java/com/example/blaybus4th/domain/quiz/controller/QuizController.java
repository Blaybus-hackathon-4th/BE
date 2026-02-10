package com.example.blaybus4th.domain.quiz.controller;

import com.example.blaybus4th.domain.quiz.dto.QuizRequestSTO;
import com.example.blaybus4th.domain.quiz.dto.QuizResponseDTO;
import com.example.blaybus4th.domain.quiz.service.QuizService;
import com.example.blaybus4th.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quizzes")
public class QuizController {

    private final QuizService quizService;

    /**
     * 퀴즈 생성 api
     * 담당자 : 이소정
     */
    @Operation(summary = "퀴즈 생성",description = "학습 이력을 분석하여 퀴즈를 생성합니다.")
    @PostMapping
    public ApiResponse<QuizResponseDTO.createQuizDTO> createQuiz(
            @RequestBody QuizRequestSTO.ModelInteractionRequestDTO request
    ) {
        return ApiResponse.onSuccess(
                quizService.generateQuiz(request)
        );
    }
}