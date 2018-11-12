package uk.gov.hmcts.probate.services.document.unit;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.ir.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.probate.services.document.DocumentService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.document.DocumentUploadClientApi;
import uk.gov.hmcts.reform.document.domain.UploadResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceTest {

    @Mock
    private AuthTokenGenerator authTokenGenerator;
    @Mock
    private RestTemplate restTemplate;

    private DocumentService documentService;

    private static final String AUTH_TOKEN = "authToken";
    private static final String USER_ID = "tom@email.com";

    @Before
    public void setUp() {
        documentService = new DocumentService(authTokenGenerator, restTemplate);
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
