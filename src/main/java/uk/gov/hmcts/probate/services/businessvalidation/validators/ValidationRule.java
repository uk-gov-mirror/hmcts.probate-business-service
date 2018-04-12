package uk.gov.hmcts.probate.services.businessvalidation.validators;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.services.businessvalidation.model.BusinessValidationError;
import uk.gov.hmcts.probate.services.businessvalidation.model.FormData;

import java.util.Optional;

@Component
public interface ValidationRule {

    String BUSINESS_ERROR = "businessError";

    Optional<BusinessValidationError> validate(FormData form);

}
