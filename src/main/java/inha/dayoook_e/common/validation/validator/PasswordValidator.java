package inha.dayoook_e.common.validation.validator;

import inha.dayoook_e.common.validation.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 비밀번호 유효성 검증을 위한 Validator.
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    // 사용자 비밀번호 정규 표현식 = 8~20글자 하나 이상의 알파벳, 하나 이상의 숫자, 하나 이상의 특수 문자
    private static final String PASSWORD_REGEX = "^(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$";

    @Override
    public void initialize(ValidPassword constraintAnnotation) {}

    /**
     * 비밀번호 유효성 검증.
     *
     * @param value 비밀번호
     * @param context 제약 위반 시 제약 위반 정보를 제공하는 객체
     * @return 비밀번호가 유효하면 true, 유효하지 않으면 false
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(PASSWORD_REGEX);
    }
}