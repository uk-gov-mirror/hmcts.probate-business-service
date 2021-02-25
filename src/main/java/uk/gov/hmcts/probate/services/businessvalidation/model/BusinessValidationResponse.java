package uk.gov.hmcts.probate.services.businessvalidation.model;

import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessValidationResponse implements Serializable {


    private BusinessValidationStatus status;
    private List<FieldError> errors;
    private List<BusinessValidationError> businessErrors;

    public BusinessValidationResponse(BusinessValidationStatus status, List<FieldError> fieldErrors,
                                      List<BusinessValidationError> businessErrors) {
        this.status = status;
        this.errors = fieldErrors;
        this.businessErrors = businessErrors;
    }

    public BusinessValidationStatus getStatus() {
        return status;
    }


    public List<BusinessValidationError> getErrors() {
        List<BusinessValidationError> allErrors = errors
            .stream()
            .map(error -> new BusinessValidationError().generateError(error.getField(), error.getDefaultMessage()))
            .collect(Collectors.toList());
        allErrors.addAll(businessErrors);
        return allErrors;
    }


}
