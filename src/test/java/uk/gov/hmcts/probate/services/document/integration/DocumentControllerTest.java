package uk.gov.hmcts.probate.services.document.integration;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.probate.services.document.DocumentService;
import uk.gov.hmcts.probate.services.document.controllers.DocumentController;
import uk.gov.hmcts.probate.services.document.validators.DocumentValidation;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.document.domain.Document;
import uk.gov.hmcts.reform.document.domain.UploadResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DocumentControllerTest {

    private static final String EXPECTED_HREF = "http://href";

    private static final String VALID_FILE_NAME = "valid_file.png";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @MockBean
    private AuthTokenGenerator authTokenGenerator;

    private DocumentValidation documentValidation;

    private DocumentController documentController;

    private static final String DUMMY_OAUTH_2_TOKEN = "oauth2Token";
    private static final String USER_ID = "tom@email.com";
    private static final String AUTH_TOKEN = "authToken";

    @Before
    public void setUp() {
        when(authTokenGenerator.generate()).thenReturn(AUTH_TOKEN);
        documentValidation = new DocumentValidation();
        ReflectionTestUtils.setField(documentValidation,
                "allowedFileExtensions", ".pdf .jpeg .bmp .tif .tiff .png .pdf");
        ReflectionTestUtils.setField(documentValidation,
                "allowedMimeTypes", "image/jpeg application/pdf image/tiff image/png image/bmp");

        documentController = new DocumentController(documentService, documentValidation, authTokenGenerator);
    }

    @Test
    public void shouldReturnErrorIfThereAreNoFilesInTheRequest() {
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("Error: no files passed");

        List<String> actualResult = documentController.upload(DUMMY_OAUTH_2_TOKEN, USER_ID,null);
        assertThat(actualResult, equalTo(expectedResult));
    }

    @Test
    public void shouldReturnErrorForEmptyFileList() {
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("Error: no files passed");

        List<String> actualResult = documentController.upload(DUMMY_OAUTH_2_TOKEN, USER_ID, Collections.emptyList());
        assertThat(actualResult, equalTo(expectedResult));
    }

    @Test
    public void shouldReturnErrorForTooManyFiles() {
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("Error: too many files");

        MultipartFile file = Mockito.mock(MultipartFile.class);
        List<MultipartFile> files = new ArrayList<>();
        for(int i = 1; i <= 11; i ++) {
            files.add(file);
        }

        List<String> actualResult = documentController.upload(DUMMY_OAUTH_2_TOKEN, USER_ID, files);
        assertThat(actualResult, equalTo(expectedResult));

    }

    @Test
    public void shouldReturnErrorForInvalidFileExtension() {
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("Error: invalid file type");
        MockMultipartFile file = new MockMultipartFile("testData", "filename.txt", "text/plain", "some xml".getBytes());
        List<MultipartFile> files = new ArrayList<>();
        files.add(file);

        List<String> actualResult = documentController.upload(DUMMY_OAUTH_2_TOKEN, USER_ID, files);
        assertThat(actualResult, equalTo(expectedResult));
    }

    @Test
    public void shouldDeleteFile() {
        ResponseEntity response = mock(ResponseEntity.class);
        when(documentService.delete(USER_ID, "file")).thenReturn(response);

        ResponseEntity actualResponse = documentController.delete(USER_ID, "file");
        assertThat(actualResponse, equalTo(response));
    }

    @Test
    public void shouldDeleteFileAndReturnAppropriateResponse() throws Exception {
        mockMvc.perform(delete("/document/delete/document-id")
                .header("user-id", USER_ID)
                .content("[]")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());
    }


    @Test
    public void shouldUploadSuccessfully() throws IOException {
        final byte[] bytes = IOUtils.toByteArray(getClass().getResourceAsStream("/files/" + VALID_FILE_NAME));


        MockMultipartFile file = new MockMultipartFile(VALID_FILE_NAME, VALID_FILE_NAME, "image/jpeg", bytes);
        List<MultipartFile> files = new ArrayList<>();
        files.add(file);

        UploadResponse uploadResponse = createUploadResponse();
        when(documentService.upload(eq(DUMMY_OAUTH_2_TOKEN), eq(AUTH_TOKEN), eq(USER_ID), eq(files)))
                .thenReturn(uploadResponse);

        List<String> actualResult = documentController.upload(DUMMY_OAUTH_2_TOKEN, USER_ID, files);
        assertThat(actualResult, hasItems());
    }

    private UploadResponse createUploadResponse() {
        UploadResponse response = mock(UploadResponse.class);
        UploadResponse.Embedded embedded = mock(UploadResponse.Embedded.class);
        when(response.getEmbedded()).thenReturn(embedded);
        Document document = createDocument();
        when(embedded.getDocuments()).thenReturn(Collections.singletonList(document));
        return response;
    }

    private Document createDocument() {
        Document document = new Document();
        Document.Links links = new Document.Links();
        Document.Link link = new Document.Link();
        link.href = EXPECTED_HREF;
        links.self = link;
        document.links = links;
        return document;
    }
}
