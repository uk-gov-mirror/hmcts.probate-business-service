package uk.gov.hmcts.probate.services.businessdocuments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestOperations;
import uk.gov.hmcts.probate.services.businessvalidation.model.CheckAnswersSummary;
import uk.gov.hmcts.probate.services.exceptions.FileSystemException;
import uk.gov.hmcts.probate.services.exceptions.PDFGenerationException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PDFGenerationServiceTest {

    @Mock
    private RestOperations restOperations;

    @Mock
    private FileSystemResourceService fileSystemResourceService;

    @Mock
    private PDFServiceConfiguration pdfServiceConfiguration;

    private ObjectMapper objectMapper;

    @Mock
    private CheckAnswersSummary checkAnswerSummary;

    private PDFGenerationService pdfGenerationService;

    private String someJSON = "{\"test\":\"json\"}";

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        pdfGenerationService = new PDFGenerationService(restOperations, fileSystemResourceService, pdfServiceConfiguration, objectMapper);
        when(pdfServiceConfiguration.getTemplatesDirectory()).thenReturn("templateDirectory");
        when(fileSystemResourceService.getFileFromResourceAsString(Mockito.anyString())).thenReturn("templateAsString");
    }

    @Test(expected = PDFGenerationException.class)
    public void shouldCatchJsonProcessingExceptionAndRethrowAsPDFGenerationException () throws Exception {
        byte[] pdfInBytes = pdfGenerationService.generatePdf("serviceToken", checkAnswerSummary, DocumentType.CHECK_ANSWERS_SUMMARY);
    }

    @Test(expected = FileSystemException.class)
    public void shouldThrowFileSystemException () throws Exception {
        when(fileSystemResourceService.getFileFromResourceAsString(Mockito.anyString())).thenThrow(new FileSystemException("File System Exception"));
        byte[] pdfInBytes = pdfGenerationService.generatePdf("serviceToken", checkAnswerSummary, DocumentType.CHECK_ANSWERS_SUMMARY);
    }
}
