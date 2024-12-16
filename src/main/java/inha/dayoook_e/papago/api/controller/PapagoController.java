package inha.dayoook_e.papago.api.controller;

import inha.dayoook_e.common.BaseResponse;
import inha.dayoook_e.papago.api.controller.dto.request.PapagoRequest;
import inha.dayoook_e.papago.api.controller.dto.response.PapagoResponse;
import inha.dayoook_e.papago.api.service.PapagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static inha.dayoook_e.common.code.status.SuccessStatus.PAPAGO_TRANSLATE_OK;

/**
 * PapagoController는 파파고 관련 엔드포인트를 처리.
 */
@Slf4j
@Tag(name = "papago controller", description = "파파고 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/papago")
public class PapagoController {

    private final PapagoService papagoService;

    /**
     * 텍스트 번역
     *
     * @param request 번역할 텍스트 요청
     * @return PapagoResponse
     */
    @PostMapping("/translate")
    @Operation(summary = "텍스트 번역 API", description = "파파고 API를 사용하여 텍스트를 번역합니다.")
    public BaseResponse<PapagoResponse> translate(@RequestBody @Valid PapagoRequest request) {
        return BaseResponse.of(PAPAGO_TRANSLATE_OK, papagoService.translate(request));
    }

}
