package inha.dayoook_e.common.exceptions.handler;


import inha.dayoook_e.common.code.BaseErrorCode;
import inha.dayoook_e.common.exceptions.BaseException;

public class ExceptionHandler extends BaseException {
    public ExceptionHandler(BaseErrorCode errorCode) {super(errorCode);}
}
