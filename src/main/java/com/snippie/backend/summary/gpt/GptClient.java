package com.snippie.backend.summary.gpt;

import com.snippie.backend.common.exception.ErrorCode;
import com.snippie.backend.common.exception.SnippieException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GptClient {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    private final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public String callGpt(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_URL, request, Map.class);

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

            return (String) message.get("content");

        } catch (HttpClientErrorException e) {
            // 인증 에러나 잘못된 요청
            throw new SnippieException(ErrorCode.GPT_API_ERROR, "GPT 요청 오류: " + e.getMessage());

        } catch (HttpServerErrorException e) {
            // OpenAI 서버 문제
            throw new SnippieException(ErrorCode.GPT_API_ERROR, "GPT 서버 오류: " + e.getMessage());

        } catch (ResourceAccessException e) {
            // 타임아웃, 네트워크 장애
            throw new SnippieException(ErrorCode.GPT_API_ERROR, "GPT 응답 지연 또는 네트워크 오류");

        } catch (Exception e) {
            // 예상 못 한 모든 에러
            throw new SnippieException(ErrorCode.GPT_API_ERROR, "GPT 호출 중 알 수 없는 오류");
        }
    }
}
