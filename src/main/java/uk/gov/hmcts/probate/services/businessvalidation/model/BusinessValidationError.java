package uk.gov.hmcts.probate.services.businessvalidation.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Locale;

@Component
public class BusinessValidationError implements Serializable {

    private static MessageSource messageSource;

    private String param;
    private String code;
    private String msg;

    public BusinessValidationError() {
        super();
    }

    private BusinessValidationError(String param, String code) {
        this.param = param;
        this.code = code;
        this.msg = getMessageFromBundle(code);
    }

    private static MessageSource getMessageSource() {
        return messageSource;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        BusinessValidationError.messageSource = messageSource;
    }

    public BusinessValidationError generateError(String param, String code) {
        return new BusinessValidationError(param, code);
    }

    private String getMessageFromBundle(String code) {
        return getMessageSource() == null ? code : getMessageSource().getMessage(code, null, Locale.UK);
    }

    public String getParam() {
        return param;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
