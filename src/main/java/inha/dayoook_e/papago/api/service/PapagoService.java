package inha.dayoook_e.papago.api.service;

import inha.dayoook_e.papago.api.controller.dto.request.PapagoRequest;
import inha.dayoook_e.papago.api.controller.dto.response.PapagoResponse;

public interface PapagoService {

    PapagoResponse translate(PapagoRequest request);

}
