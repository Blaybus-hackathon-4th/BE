package com.example.blaybus4th.global.test;

import com.example.blaybus4th.global.apiPayload.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/health")
    public ApiResponse<Map<String, String>> health() {
        return ApiResponse.onSuccess(Map.of("status", "UP"));
    }
}