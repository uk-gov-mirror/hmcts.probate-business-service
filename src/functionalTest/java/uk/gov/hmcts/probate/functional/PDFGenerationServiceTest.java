package uk.gov.hmcts.probate.functional;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class PDFGenerationServiceTest extends IntegrationTestBase {


    private static final String JSON_FILE_NAME = "generatePdfRequest.json";

    @Test
    public void verifyEmptyRequestReturnsError() {
        SerenityRest.given().relaxedHTTPSValidation().headers(utils.getHeadersWithServiceToken())
                .contentType("application/json")
                .body("")
                .when().post(pdfServiceUrl + "/pdfs")
                .then().assertThat().statusCode(400);
    }


    @Test
    public void verifyValidJSONIsAccepted() {
        SerenityRest.given().relaxedHTTPSValidation().headers(utils.getHeadersWithServiceToken())
                .contentType("application/json")
                .body(utils.getJsonFromFile(JSON_FILE_NAME))
                .when().post(pdfServiceUrl + "/pdfs")
                .then().assertThat().statusCode(200);
    }


}
