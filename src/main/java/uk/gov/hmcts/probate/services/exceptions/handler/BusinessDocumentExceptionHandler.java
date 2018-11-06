package uk.gov.hmcts.probate.services.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.gov.hmcts.probate.services.exceptions.BusinessDocumentException;
import uk.gov.hmcts.probate.services.exceptions.FileSystemException;
import uk.gov.hmcts.probate.services.exceptions.PDFGenerationException;
import uk.gov.hmcts.probate.services.exceptions.model.ErrorResponse;
import uk.gov.hmcts.reform.pdf.service.client.exception.PDFServiceClientException;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@ResponseBody
class BusinessDocumentExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String BUSINESS_DOC_ERROR = "Business Doc Error";
    public static final String PDF_CLIENT_ERROR = "PDF Client Error";
    public static final String JSON_VALIDATION_ERROR = "Json Error";
    public static final String FILE_SYSTEM_ERROR = "File System Error";
    public static final String PDF_GENERATION_EXCEPTION = "PDF Generation Error";


    @ExceptionHandler(BusinessDocumentException.class)
    public ResponseEntity<ErrorResponse> handle(BusinessDocumentException exception) {

        log.warn("Business Document exception: {}", exception.getMessage(), exception);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), BUSINESS_DOC_ERROR, exception.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errorResponse, headers, HttpStatus.valueOf(errorResponse.getCode()));
    }

    @ExceptionHandler(PDFServiceClientException.class)
    public ResponseEntity<ErrorResponse> handle(PDFServiceClientException exception) {
        log.warn("PDF Service Client exception: {}", exception.getMessage(), exception);

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), PDF_CLIENT_ERROR, exception.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(errorResponse, headers, HttpStatus.valueOf(errorResponse.getCode()));
    }

    @ExceptionHandler(FileSystemException.class)
    public ResponseEntity<ErrorResponse> handle(FileSystemException exception) {
        log.warn("File system exception: {}", exception.getMessage(), exception);

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), FILE_SYSTEM_ERROR, exception.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(errorResponse, headers, HttpStatus.valueOf(errorResponse.getCode()));
    }

    @ExceptionHandler(PDFGenerationException.class)
    public ResponseEntity<ErrorResponse> handle(PDFGenerationException exception) {
        log.warn("PDF generation exception: {}", exception.getMessage(), exception);

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), PDF_GENERATION_EXCEPTION, exception.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(errorResponse, headers, HttpStatus.valueOf(errorResponse.getCode()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        log.warn("Jackson validation exception: {}", ex.getMessage(), ex);
        String fieldPath = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getField)
                .collect(Collectors.joining("."));

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), JSON_VALIDATION_ERROR, fieldPath);

        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errorResponse, headers, HttpStatus.UNPROCESSABLE_ENTITY);
    }


}
