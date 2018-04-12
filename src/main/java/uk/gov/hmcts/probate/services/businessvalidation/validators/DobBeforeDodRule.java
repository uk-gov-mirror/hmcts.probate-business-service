package uk.gov.hmcts.probate.services.businessvalidation.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.services.businessvalidation.model.BusinessValidationError;
import uk.gov.hmcts.probate.services.businessvalidation.model.Deceased;
import uk.gov.hmcts.probate.services.businessvalidation.model.FormData;

import java.util.Date;
import java.util.Optional;

@Component
public class DobBeforeDodRule implements ValidationRule {

    public static final String CODE = "dodBeforeDob";

    private BusinessValidationError businessValidationError;

    @Autowired
    public DobBeforeDodRule(BusinessValidationError businessValidationError) {
        this.businessValidationError = businessValidationError;
    }

    @Override
    public Optional<BusinessValidationError> validate(FormData form) {

        Deceased deceased = form.getDeceased();
        Date dob = deceased != null ? deceased.getDateOfBirth() : null;
        Date dod = deceased != null ? deceased.getDateOfDeath() : null;

        if (dod != null && dod.before(dob)) {
            return Optional.of(this.businessValidationError.generateError(BUSINESS_ERROR, CODE));
        }
        return Optional.empty();
    }
}