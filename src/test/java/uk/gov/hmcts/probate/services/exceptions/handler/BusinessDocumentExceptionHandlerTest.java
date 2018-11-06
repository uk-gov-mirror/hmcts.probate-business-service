package uk.gov.hmcts.probate.services.exceptions.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import uk.gov.hmcts.probate.services.exceptions.BusinessDocumentException;
import uk.gov.hmcts.probate.services.exceptions.FileSystemException;
import uk.gov.hmcts.probate.services.exceptions.PDFGenerationException;
import uk.gov.hmcts.probate.services.exceptions.model.ErrorResponse;
import uk.gov.hmcts.reform.pdf.service.client.exception.PDFServiceClientException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class BusinessDocumentExceptionHandlerTest {

    private static final String EXCEPTION_MESSAGE = "Message";

    private PDFServiceClientException pdfClientException;

    @Mock
    Exception exception;

    private FileSystemException fileSystemException;

    private BusinessDocumentException businessDocumentException;

    private PDFGenerationException pdfGenerationException;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;
    @Mock
    private WebRequest mockWebRequest;
    @Mock
    private HttpHeaders mockHttpHeaders;

    @InjectMocks
    private BusinessDocumentExceptionHandler businessDocumentExceptionHandler;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldConvertPDFServiceClientExceptionsToInternalServerErrorCodes() {
        pdfClientException = new PDFServiceClientException(EXCEPTION_MESSAGE, exception);

        ResponseEntity<ErrorResponse> response = businessDocumentExceptionHandler.handle(pdfClientException);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(BusinessDocumentExceptionHandler.PDF_CLIENT_ERROR, response.getBody().getError());
        assertEquals(EXCEPTION_MESSAGE, response.getBody().getMessage());
    }

    @Test
    public void shouldConvertBusinessDocumentExceptionsToInternalServerErrorCodes() {
        businessDocumentException = new BusinessDocumentException(EXCEPTION_MESSAGE, exception);
        ResponseEntity<ErrorResponse> response = businessDocumentExceptionHandler.handle(businessDocumentException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(BusinessDocumentExceptionHandler.BUSINESS_DOC_ERROR, response.getBody().getError());
        assertEquals(EXCEPTION_MESSAGE, response.getBody().getMessage());
    }

    @Test
    public void shouldConvertFileSytemExceptionsToInternalServerErrorCodes() {
        fileSystemException = new FileSystemException(EXCEPTION_MESSAGE, exception);
        ResponseEntity<ErrorResponse> response = businessDocumentExceptionHandler.handle(fileSystemException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(BusinessDocumentExceptionHandler.FILE_SYSTEM_ERROR, response.getBody().getError());
        assertEquals(EXCEPTION_MESSAGE, response.getBody().getMessage());
    }

    @Test
    public void shouldConvertPDFGenerationExceptionsToInternalServerErrorCodes() {
        pdfGenerationException = new PDFGenerationException(EXCEPTION_MESSAGE, exception);
        ResponseEntity<ErrorResponse> response = businessDocumentExceptionHandler.handle(pdfGenerationException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(BusinessDocumentExceptionHandler.PDF_GENERATION_EXCEPTION, response.getBody().getError());
        assertEquals(EXCEPTION_MESSAGE, response.getBody().getMessage());
    }

    @Test
    public void shouldHandleMethodArgumentNotValid() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        List<FieldError> fieldErrors = new ArrayList<>();
        FieldError fieldError1 = new FieldError("code","field1", "Cannot be blank");
        FieldError fieldError2 = new FieldError("code","field2", "Cannot be empty");
        fieldErrors.add(fieldError1);
        fieldErrors.add(fieldError2);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity responseEntity = businessDocumentExceptionHandler.handleMethodArgumentNotValid(methodArgumentNotValidException, mockHttpHeaders, HttpStatus.OK, mockWebRequest);

        assertThat(responseEntity.getStatusCode().value(), is(HttpStatus.UNPROCESSABLE_ENTITY.value()));
        assertThat(((ErrorResponse) responseEntity.getBody()).getError(), is("Json Error"));
        assertThat(((ErrorResponse) responseEntity.getBody()).getMessage(),is("field1.field2"));
    }

}
