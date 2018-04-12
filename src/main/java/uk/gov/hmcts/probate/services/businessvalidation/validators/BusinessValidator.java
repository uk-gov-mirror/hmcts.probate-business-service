package uk.gov.hmcts.probate.services.businessvalidation.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.services.businessvalidation.model.BusinessValidationError;
import uk.gov.hmcts.probate.services.businessvalidation.model.FormData;

import java.util.LinkedList;
import java.util.List;

@Component
public class BusinessValidator {

    private List<ValidationRule> validationRules;

    @Autowired
    public BusinessValidator(List<ValidationRule> validationRules) {
        this.validationRules = validationRules;
    }

    public List<BusinessValidationError> validateForm(FormData form) {
        List<BusinessValidationError> errorList = new LinkedList<>();

        for (ValidationRule rule : validationRules) {
            rule.validate(form).ifPresent(errorList::add);
        }
        return errorList;
    }
}
