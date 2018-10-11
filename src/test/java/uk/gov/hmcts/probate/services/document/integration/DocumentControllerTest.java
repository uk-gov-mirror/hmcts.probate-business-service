package uk.gov.hmcts.probate.services.document.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.probate.services.document.DocumentService;
import uk.gov.hmcts.probate.services.document.controllers.DocumentController;
import uk.gov.hmcts.probate.services.document.exception.DocumentsMissingException;
import uk.gov.hmcts.probate.services.document.exception.UnSupportedDocumentTypeException;
import uk.gov.hmcts.probate.services.document.validators.DocumentValidation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DocumentControllerTest {

    @Mock
    private DocumentService documentService;
    @Mock
    private DocumentValidation documentValidation;

    private DocumentController documentController;

    private static final String DUMMY_OAUTH_2_TOKEN = "oauth2Token";
    private static final String USER_ID = "tom@email.com";

    @Before
    public void setUp() {
        documentController = new DocumentController(documentService, documentValidation);
    }

    @Test(expected = DocumentsMissingException.class)
    public void shouldThrowDocumentsMissingExceptionIfThereAreNoFilesInTheRequest() {
        documentController.upload(DUMMY_OAUTH_2_TOKEN, USER_ID,null);
    }

    @Test(expected = DocumentsMissingException.class)
    public void shouldThrowDocumentsMissingExceptionForEmptyFileList() {
        documentController.upload(DUMMY_OAUTH_2_TOKEN, USER_ID, Collections.emptyList());
    }

    @Test(expected = UnSupportedDocumentTypeException.class)
    public void shouldThrowUnSupportedDocumentTypeExceptionForTooManyFiles() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        List<MultipartFile> files = new ArrayList<>();
        for(int i = 1; i <= 11; i ++) {
            files.add(file);
        }
        documentController.upload(DUMMY_OAUTH_2_TOKEN, USER_ID, files);
    }

    @Test(expected = UnSupportedDocumentTypeException.class)
    public void shouldThrowUnSupportedDocumentTypeExceptionInvalidFileExtension() {
        MockMultipartFile file = new MockMultipartFile("testData", "filename.txt", "text/plain", "some xml".getBytes());
        List<MultipartFile> files = new ArrayList<>();
        files.add(file);
        documentController.upload(DUMMY_OAUTH_2_TOKEN, USER_ID, files);
    }
}
