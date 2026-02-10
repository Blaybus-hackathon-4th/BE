package com.example.blaybus4th.domain.pdf.controller;

import com.example.blaybus4th.domain.member.dto.response.InstitutionsListResponse;
import com.example.blaybus4th.domain.pdf.dto.response.PdfResponse;
import com.example.blaybus4th.domain.pdf.service.PdfService;
import com.example.blaybus4th.global.annotation.InjectMemberId;
import com.example.blaybus4th.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pdf")
public class PdfController {

    private final PdfService pdfService;

    @Operation(summary = "PDF 생성 API",description = "오브젝트의 PDF를 생성합니다.")
    @GetMapping("/{objectId}")
    public ApiResponse<PdfResponse> createPdf(
            @PathVariable Long objectId,
            @InjectMemberId Long memberId,
            @RequestParam String intent){
        return ApiResponse.onSuccess(pdfService.createPdf(objectId,memberId,intent));
    }


}
