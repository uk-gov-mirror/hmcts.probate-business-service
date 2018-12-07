package uk.gov.hmcts.probate.functional;


import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class IdamTokenGenerator {

    @Value("${idam.redirect_uri}")
    private String redirectUri;

    @Value("${idam.username}")
    private String idamUsername;

    @Value("${idam.userpassword}")
    private String idamPassword;

    @Value("${idam.secret}")
    private String secret;

    @Value("${idam.url}")
    private String idamUserBaseUrl;

    private String userToken;

    private String idamCreateUrl() {
        return idamUserBaseUrl + "/testing-support/accounts";
    }

    public String getUserId() {
        String userid_local = "" + RestAssured.given()
                .header("Authorization", userToken)
                .get(idamUserBaseUrl + "/details")
                .body()
                .path("id");
        return userid_local;
    }


    public String generateUserTokenWithNoRoles() {
        userToken = generateClientToken();
        return userToken;
    }

    private String generateClientToken() {
        String code = generateClientCode();
        String token = "";

        ResponseBody responseBody = RestAssured.given().post(idamUserBaseUrl + "/oauth2/token?code=" + code +
                "&client_secret=" + secret +
                "&client_id=probate" +
                "&redirect_uri=" + redirectUri +
                "&grant_type=authorization_code")
                .body();
        token = responseBody.path("access_token");

        return "Bearer " + token;
    }

    private String generateClientCode() {
        String code = "";
        final String encoded = Base64.getEncoder().encodeToString((idamUsername + ":" + idamPassword).getBytes());
        code = RestAssured.given().baseUri(idamUserBaseUrl)
                .header("Authorization", "Basic " + encoded)
                .post("/oauth2/authorize?response_type=code&client_id=probate&redirect_uri=" + redirectUri)
                .body().path("code");
        System.out.println("********************** idamUserBaseUrl: "  + idamUserBaseUrl + "**********************");
        System.out.println("********************** redirectUri: "  + redirectUri + "**********************");
        System.out.println("********************** CODE: "  + code + "**********************");
        return code;

    }
}
