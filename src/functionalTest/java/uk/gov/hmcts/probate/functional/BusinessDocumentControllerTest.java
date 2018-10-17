package uk.gov.hmcts.probate.functional;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class BusinessDocumentControllerTest extends IntegrationTestBase {

    private static final String JSON_FILE_NAME = "checkAnswersSummary.json";


    @Test
    public void verifyEmptyRequestReturnsError() {
        SerenityRest.given().relaxedHTTPSValidation().headers(utils.getHeadersWithServiceToken())
                .body("")
                .when().post(businessServiceUrl + "/businessDocument/generateSummaryPDF")
                .then().assertThat().statusCode(400);
    }


    @Test
    public void verifyValidJSONIsAccepted() {
        SerenityRest.given().relaxedHTTPSValidation().headers(utils.getHeadersWithServiceToken())
                .body(utils.getJsonFromFile(JSON_FILE_NAME))
                .when().post(businessServiceUrl + "/businessDocument/generateSummaryPDF")
                .then().assertThat().statusCode(200);
    }


}
