package uk.gov.hmcts.probate.functional;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class BusinessDocumentControllerTest extends IntegrationTestBase {

    private static final String SUMMARY_JSON = "checkAnswersMultipleExecutorsSummary.json";
    private static final String VALID_LEGAL_DEC_JSON = "validLegalDeclaration.json";
    private static final String BUSINESS_DOC_URL = "/businessDocument";
    private static final String CHECK_ANSWERS_SUMMARY_URL = "/generateCheckAnswersSummaryPDF";
    private static final String LEGAL_DECLARATION_URL ="/generateLegalDeclarationPDF";

    @Test
    public void verifyEmptyCheckAnswersSummaryRequestReturnsError() {
        SerenityRest.given().relaxedHTTPSValidation().headers(utils.getHeadersWithServiceToken())
                .body("")
                .when().post(businessServiceUrl + BUSINESS_DOC_URL + CHECK_ANSWERS_SUMMARY_URL)
                .then().assertThat().statusCode(400);
    }


    @Test
    public void verifyValidcheckAnswersSummaryJSONIsAccepted() {
        SerenityRest.given().relaxedHTTPSValidation().headers(utils.getHeadersWithServiceToken())
                .body(utils.getJsonFromFile(SUMMARY_JSON))
                .when().post(businessServiceUrl + BUSINESS_DOC_URL + CHECK_ANSWERS_SUMMARY_URL)
                .then().assertThat().statusCode(200);
    }

    @Test
    public void verifyEmptyLegalDeclarationRequestReturnsError() {
        SerenityRest.given().relaxedHTTPSValidation().headers(utils.getHeadersWithServiceToken())
                .body("")
                .when().post(businessServiceUrl + BUSINESS_DOC_URL + LEGAL_DECLARATION_URL)
                .then().assertThat().statusCode(400);
    }


    @Test
    public void verifyValidLegalDeclarationJSONIsAccepted() {
        SerenityRest.given().relaxedHTTPSValidation().headers(utils.getHeadersWithServiceToken())
                .body(utils.getJsonFromFile(VALID_LEGAL_DEC_JSON))
                .when().post(businessServiceUrl + BUSINESS_DOC_URL + LEGAL_DECLARATION_URL)
                .then().assertThat().statusCode(200);
    }

}
