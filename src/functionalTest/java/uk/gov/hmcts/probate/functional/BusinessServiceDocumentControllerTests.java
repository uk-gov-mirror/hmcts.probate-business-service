package uk.gov.hmcts.probate.functional;

import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

import java.io.IOException;

import static org.mockito.BDDMockito.given;

@RunWith(SerenityRunner.class)
public class BusinessServiceDocumentControllerTests extends IntegrationTestBase {

    private static final String AUTH_TOKEN = "authToken";

    private static final String DUMMY_OAUTH_2_TOKEN = "oauth2Token";

    private static final String USER_ID = "tom@email.com";

//    @Test
//    public void testValidDocument() throws IOException {
//        final byte[] bytes = IOUtils.toByteArray(getClass().getResourceAsStream("/files/valid_file.png"));
//
//        Response response = SerenityRest.given()
//                .headers(utils.getDocumentUploadHeaders("jbhvhvjhvjh", "tom@email.com"))
//                .multiPart("file", "myFile", bytes, "image/png")
//                .contentType("multipart/form-data")
//                .post(businessServiceUrl + "/document/upload")
//                .andReturn();
//        System.out.println(response.body().prettyPrint());
//        Assert.assertEquals(HttpStatus.OK.value(), response.statusCode());
//    }

    @Test
    public void testInvalidDocument() throws IOException {
        final byte[] bytes = IOUtils.toByteArray(getClass().getResourceAsStream("/files/invalid.txt"));

        Response response = SerenityRest.given().relaxedHTTPSValidation()
                .headers(utils.getDocumentUploadHeaders("jbhvhvjhvjh", "tom@email.com"))
                .multiPart("file", "myFile", bytes, "text/plain")
                .contentType("multipart/form-data")
                .post(businessServiceUrl + "/document/upload")
                .andReturn();
        Assert.assertTrue(response.body().prettyPrint().contains("Error: invalid file type"));
    }

    @Test
    public void testNoDocumentsUploaded() {
        Response response = SerenityRest.given().relaxedHTTPSValidation()
                .headers(utils.getDocumentUploadHeaders("jbhvhvjhvjh", USER_ID))
                .multiPart("file", "myFile")
                .contentType("multipart/form-data")
                .post(businessServiceUrl + "/document/upload")
                .andReturn();
        Assert.assertTrue(response.body().prettyPrint().contains("Error: no files passed"));
    }

    @Test
    public void testInvalidToken() throws IOException {
        final byte[] bytes = IOUtils.toByteArray(getClass().getResourceAsStream("/files/valid_file.png"));

        Response response = SerenityRest.given().relaxedHTTPSValidation()
                .headers(utils.getDocumentUploadHeaders("jbhvhvjhvjh", USER_ID))
                .multiPart("file", "myFile", bytes, "image/png")
                .contentType("multipart/form-data")
                .post(businessServiceUrl + "/document/upload")
                .andReturn();
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.statusCode());
    }

    @Test
    public void testInvalidCredentials() throws IOException {
        final byte[] bytes = IOUtils.toByteArray(getClass().getResourceAsStream("/files/valid_file.png"));

        Response response = SerenityRest.given().relaxedHTTPSValidation()
                .headers(utils.getDocumentUploadHeaders("jbhvhvjhvjh", "invalid_user_credentials"))
                .multiPart("file", "myFile", bytes, "image/png")
                .contentType("multipart/form-data")
                .post(businessServiceUrl + "/document/upload")
                .andReturn();
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.statusCode());
    }

    @Test
    public void testInvalidUrlRoute() {
        SerenityRest.given().relaxedHTTPSValidation()
                .headers(utils.getDocumentUploadHeaders("jbhvhvjhvjh", "tom@email.com"))
                .when().post(businessServiceUrl + "/document/invalid_path")
                .then().assertThat().statusCode(404);
    }

    @Test
    public void testDeleteValidDocument() {
        SerenityRest.given().relaxedHTTPSValidation()
                .headers(utils.getDocumentDeleteHeaders(DUMMY_OAUTH_2_TOKEN, USER_ID))
                .when().delete(businessServiceUrl + "/document/delete/22e31950-f26d-46b2-8008-6e1476633ea6")
                .then().assertThat().statusCode(204);
    }

    @Test
    public void testDeleteInvalidDocument() {
        SerenityRest.given().relaxedHTTPSValidation()
                .headers(utils.getDocumentDeleteHeaders("jbhvhvjhvjh", "tom@email.com"))
                .when().delete(businessServiceUrl + "/document/delete/invalid_file_path")
                .then().assertThat().statusCode(500);
    }
}
