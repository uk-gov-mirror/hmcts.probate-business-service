package uk.gov.hmcts.probate.services.businessdocuments.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import uk.gov.hmcts.probate.services.businessdocuments.services.FileSystemResourceService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class BulkScanCoverSheetTest {

    public static final String VALID_BULK_SCAN_COVER_SHEET_JSON = "businessdocuments/validBulkScanCoverSheet.json";
    public static final String VALID_BULK_SCAN_COVER_SHEET_STATIC_TEXT_OVERRIDE_JSON = "businessdocuments/validBulkScanCoverSheetStaticTextOverride.json";
    public static final String INVALID_BULK_SCAN_COVER_SHEET_JSON = "businessdocuments/invalidBulkScanCoverSheet.json";
    private ObjectMapper objectMapper;

    private FileSystemResourceService fileSystemResourceService;
    private Validator validator;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE,true);
        fileSystemResourceService = new FileSystemResourceService();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreateACoverSheetInstance() throws IOException {
        Optional<FileSystemResource> optional = getFile(VALID_BULK_SCAN_COVER_SHEET_JSON);
        BulkScanCoverSheet coverSheet = objectMapper.readValue(optional.get().getFile(), BulkScanCoverSheet.class);
        assertThat(coverSheet, is(notNullValue()));
        Set<ConstraintViolation<BulkScanCoverSheet>> violations = validator.validate(coverSheet);
        assertThat(violations, is(empty()));
        assertThat(coverSheet.getTitle(), is(equalTo(BulkScanCoverSheet.DEFUALT_TITLE)));
        assertThat(coverSheet.getApplicantAddressIntro(), is(equalTo(BulkScanCoverSheet.DEFUALT_APPLICANT_ADDRESS_INTRO)));
        assertThat(coverSheet.getCaseReferenceIntro(), is(equalTo(BulkScanCoverSheet.DEFUALT_CASE_REFERENCE_INTRO)));
        assertThat(coverSheet.getSubmitAddressIntro(), is(equalTo(BulkScanCoverSheet.DEFUALT_SUBMIT_ADDRESS_INTRO)));
        assertThat(coverSheet.getApplicantAddress(), is(equalTo("20 White City\nLondon\nW12 7PD")));
        assertThat(coverSheet.getCaseReference(), is(equalTo("1542-9021-4510-0350")));
        assertThat(coverSheet.getSubmitAddress(), is(equalTo("Divorce Service\nPO BOX 123\nExela BSP Services\nHarlow\nCM19 5QS")));
    }
    
    @Test
    public void shouldCreateACoverSheetInstanceWithStaticTextOverride() throws IOException {
        Optional<FileSystemResource> optional = getFile(VALID_BULK_SCAN_COVER_SHEET_STATIC_TEXT_OVERRIDE_JSON);
        BulkScanCoverSheet coverSheet = objectMapper.readValue(optional.get().getFile(), BulkScanCoverSheet.class);
        assertThat(coverSheet, is(notNullValue()));
        Set<ConstraintViolation<BulkScanCoverSheet>> violations = validator.validate(coverSheet);
        assertThat(violations, is(empty()));
        assertThat(coverSheet.getTitle(), is(equalTo("Cover Sheet")));
        assertThat(coverSheet.getApplicantAddressIntro(), is(equalTo("The applicants address")));
        assertThat(coverSheet.getCaseReferenceIntro(), is(equalTo("This is the Case Reference Number")));
        assertThat(coverSheet.getSubmitAddressIntro(), is(equalTo("This is the place to send the documents")));
        assertThat(coverSheet.getApplicantAddress(), is(equalTo("20 White City\nLondon\nW12 7PD")));
        assertThat(coverSheet.getCaseReference(), is(equalTo("1542-9021-4510-0350")));
        assertThat(coverSheet.getSubmitAddress(), is(equalTo("Divorce Service\nPO BOX 123\nExela BSP Services\nHarlow\nCM19 5QS")));
    }
    
    @Test
    public void shouldFailToCreateACoverSheetInstance() throws IOException {
        Optional<FileSystemResource> optional = getFile(INVALID_BULK_SCAN_COVER_SHEET_JSON);
        BulkScanCoverSheet coverSheet = objectMapper.readValue(optional.get().getFile(), BulkScanCoverSheet.class);
        Set<ConstraintViolation<BulkScanCoverSheet>> violations = validator.validate(coverSheet);
        assertThat(violations, is(not(empty())));
        assertThat(violations.size(), is(equalTo(3)));
    }

    private Optional<FileSystemResource> getFile(String fileName) {
        return fileSystemResourceService.getFileSystemResource(fileName);
    }
}
