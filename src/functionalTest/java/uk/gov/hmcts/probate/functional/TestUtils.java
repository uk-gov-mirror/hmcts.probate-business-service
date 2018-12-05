package uk.gov.hmcts.probate.functional;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@ContextConfiguration(classes = TestContextConfiguration.class)
@Component
public class TestUtils {

    @Autowired
    private IdamTokenGenerator idamTokenGenerator;

    public String getJsonFromFile(String fileName) {
        try {
            File file = ResourceUtils.getFile(this.getClass().getResource("/json/" + fileName));
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Headers getHeaders(String sessionId) {
        return Headers.headers(
                new Header("Content-Type", ContentType.JSON.toString()),
                new Header("Session-ID", sessionId));
    }

    public Map<String, Object> getDocumentUploadHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", idamTokenGenerator.generateUserTokenWithNoRoles());
        headers.put("user-id", idamTokenGenerator.getUserId());
        return headers;
    }
}
