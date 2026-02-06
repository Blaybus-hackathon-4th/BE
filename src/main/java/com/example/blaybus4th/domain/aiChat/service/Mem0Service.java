package com.example.blaybus4th.domain.aiChat.service;

import com.example.blaybus4th.domain.aiChat.tool.MemberContextHolder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Getter
public class Mem0Service {

    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl;

    public Mem0Service(@Value("${mem0_base_url}") String baseUrl, RestClient.Builder restClientBuilder
    ) {
        this.baseUrl = baseUrl;
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
        log.info("Mem0 Service 초기화됨. Base URL: [{}]", baseUrl);
    }


    // 현재 로그인한 사용자의 ID를 Context에서 꺼내 저장
    public void addMemory(String text) {
        String memberId = MemberContextHolder.get();
        if (memberId == null) {
            log.warn("🚨 [Mem0] 로그인 정보(Context)가 없어 기억을 저장하지 않습니다.");
            return;
        }
        // 기존 메서드 호출
        addMemory(memberId, text);
    }



    // 현재 로그인한 사용자의 ID를 Context에서 꺼내 검색
    public String searchMemory(String text) {
        String memberId = MemberContextHolder.get();
        if (memberId == null) {
            log.warn("🚨 [Mem0] 로그인 정보(Context)가 없어 기억을 검색할 수 없습니다.");
            return "";
        }
        // 기존 메서드 호출
        return searchMemory(memberId, text);
    }


    // mem0 기억 저장
    public void addMemory(String memberId, String text){
        String requestUrl = baseUrl + "/v1/memories"; // URL
        Map<String, Object> map = Map.of(
                "user_id", memberId,
                "messages", List.of(
                        Map.of("role", "user", "content", text)
                )
        );

        try{
            log.info("🚀 [Mem0 저장 요청] URL: {}", requestUrl);
            log.info("📦 [Mem0 저장 바디] {}", objectMapper.writeValueAsString(map));

            restClient.post()
                    .uri("/v1/memories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(map)
                    .retrieve()
                    .toBodilessEntity();

            log.info("mem0 저장 성공 : memberId = {}, text = {}", memberId, text);

        }catch (RestClientResponseException e) {
            // 서버가 4xx, 5xx 에러를 뱉었을 때 상세 내용 출력
            log.error("🔥 [Mem0 저장 에러] 상태 코드: {}", e.getStatusCode());
            log.error("🔥 [Mem0 에러 응답 본문] {}", e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("🔥 [Mem0 저장 시스템 에러] {}", e.getMessage());
        }
    }


    // mem0 기억 검색
    public String searchMemory(String memberId, String text){
        String requestUrl = baseUrl + "/v1/memories/search";

        // 도커 서버가 기대하는 포맷: { "user_id": "...", "query": "..." }
        Map<String, Object> body = Map.of(
                "user_id", memberId,
                "query", text
        );

        try{
            Map<String, Object> response = restClient.post()
                    .uri("/v1/memories/search")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (response == null || response.isEmpty()) {
                return "";
            }

            List<Map<String, Object>> results = objectMapper.convertValue(
                    response.get("results"),
                    new TypeReference<List<Map<String, Object>>>() {}
            );

            StringBuilder sb = new StringBuilder();
            for (Map<String, Object> item : results) {
                if (item.containsKey("memory")) {
                    sb.append(item.get("memory")).append("\n");
                }
            }

            log.debug("mem0 검색 성공 : memberId = {}, text = {}, result = {}", memberId, text, sb.toString());

            return sb.toString();

        }catch (Exception e){
            log.error("mem0 검색 실패 : {}", e.getMessage());
            return "";
        }
    }



    @Getter
    public static class Mem0ListResponse {
        private List<Mem0Item> items;
        private Integer total;
        private Integer page;
        private Integer size;
        private Integer pages;
    }

    @Getter
    public static class Mem0Item {
        private String id;
        private String content;   // Swagger response에 content
        private Long created_at;
        private List<String> categories;
        private Map<String, Object> metadata_;
    }


}
