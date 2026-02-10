package com.example.blaybus4th.domain.pdf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PdfResponse {

    private String title;
    private String overview;
    private String analysis;
    private String conclusion;
    private String[] keywords;

    public static PdfResponse from(PdfResponse response){
        return new PdfResponse(
                response.getTitle(),
                response.getOverview(),
                response.getAnalysis(),
                response.getConclusion(),
                response.getKeywords()
        );

    }


}
