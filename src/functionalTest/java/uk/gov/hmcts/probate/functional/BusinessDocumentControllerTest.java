package uk.gov.hmcts.probate.functional;

import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import io.restassured.RestAssured;
import net.thucydides.core.annotations.Pending;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SpringIntegrationSerenityRunner.class)
public class BusinessDocumentControllerTest extends IntegrationTestBase {

    private static final String SUMMARY_JSON = "checkAnswersMultipleExecutorsSummary.json";
    private static final String VALID_LEGAL_DEC_JSON = "validLegalDeclaration.json";
    private static final String BUSINESS_DOC_URL = "/businessDocument";
    private static final String CHECK_ANSWERS_SUMMARY_URL = "/generateCheckAnswersSummaryPDF";
    private static final String LEGAL_DECLARATION_URL ="/generateLegalDeclarationPDF";

    @Test
    public void verifyEmptyCheckAnswersSummaryRequestReturnsError() {
        RestAssured.given().relaxedHTTPSValidation().headers(utils.getHeadersWithServiceToken())
                .body("")
                .when().post(businessServiceUrl + BUSINESS_DOC_URL + CHECK_ANSWERS_SUMMARY_URL)
                .then().assertThat().statusCode(400);
    }


    @Test
    public void verifyValidcheckAnswersSummaryJSONIsAccepted() {
        RestAssured.given().relaxedHTTPSValidation().headers(utils.getHeadersWithServiceToken())
                .body(utils.getJsonFromFile(SUMMARY_JSON))
                .when().post(businessServiceUrl + BUSINESS_DOC_URL + CHECK_ANSWERS_SUMMARY_URL)
                .then().assertThat().statusCode(200);
    }

    @Test
    public void verifyEmptyLegalDeclarationRequestReturnsError() {
        RestAssured.given().relaxedHTTPSValidation().headers(utils.getHeadersWithServiceToken())
                .body("")
                .when().post(businessServiceUrl + BUSINESS_DOC_URL + LEGAL_DECLARATION_URL)
                .then().assertThat().statusCode(400);
    }


    @Test
    @Pending
    public void verifyValidLegalDeclarationJSONIsAccepted() {
        RestAssured.given().relaxedHTTPSValidation().headers(utils.getHeadersWithServiceToken())
                .body(utils.getJsonFromFile(VALID_LEGAL_DEC_JSON))
                .when().post(businessServiceUrl + BUSINESS_DOC_URL + LEGAL_DECLARATION_URL)
                .then().assertThat().statusCode(200);
    }

}
