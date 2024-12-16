package inha.dayoook_e.papago.api.service;

import inha.dayoook_e.common.exceptions.BaseException;
import inha.dayoook_e.papago.api.controller.dto.request.PapagoRequest;
import inha.dayoook_e.papago.api.controller.dto.response.PapagoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static inha.dayoook_e.common.code.status.ErrorStatus.TRANSLATION_FAILED;

/**
 * PapagoServiceImpl은 파파고 관련 비즈니스 로직을 처리.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PapagoServiceImpl implements  PapagoService {

    private final RestTemplate restTemplate;

    @Value("${papago.client-id}")
    private String clientId;

    @Value("${papago.client-secret}")
    private String clientSecret;

    public PapagoResponse translate(PapagoRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("source", request.source());
        body.add("target", request.target());
        body.add("text", request.text());
        body.add("honorific", "true");

        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://naveropenapi.apigw.ntruss.com/nmt/v1/translation",
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            Map<String, Object> message = (Map<String, Object>) response.getBody().get("message");
            Map<String, String> result = (Map<String, String>) message.get("result");

            return new PapagoResponse(result.get("translatedText"));
        } catch (Exception e) {
            log.error("Translation failed: ", e);
            throw new BaseException(TRANSLATION_FAILED);
        }
    }
}
