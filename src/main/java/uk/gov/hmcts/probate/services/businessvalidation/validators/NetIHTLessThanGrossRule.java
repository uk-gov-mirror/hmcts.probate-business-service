package uk.gov.hmcts.probate.services.businessvalidation.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.services.businessvalidation.model.BusinessValidationError;
import uk.gov.hmcts.probate.services.businessvalidation.model.FormData;
import uk.gov.hmcts.probate.services.businessvalidation.model.InheritanceTax;

import java.util.Optional;

@Component
public class NetIHTLessThanGrossRule implements ValidationRule {

    public static final String CODE = "ihtNetGreaterThanGross";

    private BusinessValidationError businessValidationError;

    @Autowired
    NetIHTLessThanGrossRule(BusinessValidationError businessValidationError) {
        this.businessValidationError = businessValidationError;
    }

    @Override
    public Optional<BusinessValidationError> validate(FormData form) {
        InheritanceTax iht = form.getIht();

        if (iht != null && iht.getNetValue() > iht.getGrossValue()) {
            return Optional.of(businessValidationError.generateError(BUSINESS_ERROR, CODE));
        }

        return Optional.empty();
    }
}