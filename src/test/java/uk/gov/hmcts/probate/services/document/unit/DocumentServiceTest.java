package uk.gov.hmcts.probate.services.document.unit;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.probate.services.document.DocumentService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.document.domain.UploadResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceTest {

    @Mock
    private AuthTokenGenerator authTokenGenerator;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ObjectMapper objectMapper;

    private DocumentService documentService;

    private static final String AUTH_TOKEN = "authToken";
    private static final String DUMMY_OAUTH_2_TOKEN = "oauth2Token";
    private static final String USER_ID = "tom@email.com";

    @Before
    public void setUp() {
        documentService = new DocumentService(authTokenGenerator, restTemplate);
        ReflectionTestUtils.setField(documentService,
                "documentManagementUrl", "http://localhost:8383");
    }

    @Test
    public void shouldUploadDocumentsToEvidenceManagement() throws IOException {
        MockMultipartFile file = new MockMultipartFile("filename.png", "filename.png", "image/png", "some xml".getBytes());
        String response = new String(Files.readAllBytes(Paths.get("src/test/resources/files", "response.json")));

        List<MultipartFile> files = Collections.singletonList(file);
        when(restTemplate.postForObject(eq("http://localhost:8383/documents"), any(HttpEntity.class), eq(String.class))).thenReturn(response);

        UploadResponse actualUploadResponse = this.documentService.upload(DUMMY_OAUTH_2_TOKEN, AUTH_TOKEN, USER_ID, files);
        assertThat(actualUploadResponse, instanceOf(UploadResponse.class));
    }

    @Test
    public void shouldDeleteDocumentFromEvidenceManagement() {
        ResponseEntity response = mock(ResponseEntity.class);
        when(authTokenGenerator.generate()).thenReturn(AUTH_TOKEN);
        when(documentService.delete(USER_ID, "file")).thenReturn(response);

        ResponseEntity actualResponse = documentService.delete(USER_ID, "file");
        assertThat(actualResponse, equalTo(response));
    }
}