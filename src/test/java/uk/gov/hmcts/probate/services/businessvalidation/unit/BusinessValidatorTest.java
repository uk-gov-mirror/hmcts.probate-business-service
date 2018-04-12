package uk.gov.hmcts.probate.services.businessvalidation.unit;

import org.junit.Before;
import org.junit.Test;
import uk.gov.hmcts.probate.services.businessvalidation.model.BusinessValidationError;
import uk.gov.hmcts.probate.services.businessvalidation.model.FormData;
import uk.gov.hmcts.probate.services.businessvalidation.validators.BusinessValidator;
import uk.gov.hmcts.probate.services.businessvalidation.validators.DobBeforeDodRule;
import uk.gov.hmcts.probate.services.businessvalidation.validators.ValidationRule;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

public class BusinessValidatorTest {

    private ValidationRule failingRule;
    private ValidationRule passingRule;
    private BusinessValidationError validationError;

    @Before
    public void setUp() throws Exception {
        validationError = new BusinessValidationError().generateError(ValidationRule.BUSINESS_ERROR, DobBeforeDodRule.CODE);
        failingRule = form -> Optional.of(validationError);
        passingRule = form -> Optional.empty();

    }

    @Test
    public void testWithFailingRule() throws Exception {
        BusinessValidator businessValidator = new BusinessValidator(singletonList(failingRule));

        List<BusinessValidationError> validationErrors = businessValidator.validateForm(new FormData());

        assertThat(validationErrors, containsInAnyOrder(validationError));

    }

    @Test
    public void testWithPassingRule() throws Exception {
        BusinessValidator businessValidator = new BusinessValidator(singletonList(passingRule));

        List<BusinessValidationError> validationErrors = businessValidator.validateForm(new FormData());

        assertThat(validationErrors.isEmpty(), is(true));
    }

    @Test
    public void testWithTwoValidationRules() throws Exception {
        BusinessValidator businessValidator = new BusinessValidator(asList(passingRule, failingRule));

        List<BusinessValidationError> validationErrors = businessValidator.validateForm(new FormData());

        assertThat(validationErrors, containsInAnyOrder(validationError));
    }
}