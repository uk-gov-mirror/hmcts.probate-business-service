package uk.gov.hmcts.probate.services.exceptions.handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.probate.services.exceptions.BusinessDocumentException;
import uk.gov.hmcts.probate.services.exceptions.model.ErrorResponse;
import uk.gov.hmcts.reform.pdf.service.client.exception.PDFServiceClientException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class BusinessDocumentExceptionHandlerTest {

    private static final String EXCEPTION_MESSAGE = "Message";

    @Mock
    private PDFServiceClientException pdfClientException;

    @Mock
    private BusinessDocumentException businessDocumentException;

    @InjectMocks
    private BusinessDocumentExceptionHandler businessDocumentExceptionHandler;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldConvertPDFServiceClientExceptionsToInternalServerErrorCodes() {
        when(pdfClientException.getMessage()).thenReturn(EXCEPTION_MESSAGE);

        ResponseEntity<ErrorResponse> response = businessDocumentExceptionHandler.handle(pdfClientException);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(BusinessDocumentExceptionHandler.PDF_CLIENT_ERROR, response.getBody().getError());
        assertEquals(EXCEPTION_MESSAGE, response.getBody().getMessage());
    }

    @Test
    public void shouldConvertBusinessDocumentExceptionsToInternalServerErrorCodes() {
        when(businessDocumentException.getMessage()).thenReturn(EXCEPTION_MESSAGE);

        ResponseEntity<ErrorResponse> response = businessDocumentExceptionHandler.handle(businessDocumentException);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(BusinessDocumentExceptionHandler.BUSINESS_DOC_ERROR, response.getBody().getError());
        assertEquals(EXCEPTION_MESSAGE, response.getBody().getMessage());
    }

}
