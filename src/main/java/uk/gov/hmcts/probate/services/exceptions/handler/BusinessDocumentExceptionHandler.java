package uk.gov.hmcts.probate.services.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.gov.hmcts.probate.services.exceptions.BusinessDocumentException;
import uk.gov.hmcts.probate.services.exceptions.model.ErrorResponse;
import uk.gov.hmcts.reform.pdf.service.client.exception.PDFServiceClientException;

@Slf4j
@ControllerAdvice
class BusinessDocumentExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String BUSINESS_DOC_ERROR = "Business Doc Error";
    public static final String INVALID_REQUEST = "Invalid Request";
    public static final String PDF_CLIENT_ERROR = "PDF Client Error";
    public static final String CONNECTION_ERROR = "Connection error";

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
        log.warn("PDDF Service Client exception: {}", exception.getMessage(), exception);

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), PDF_CLIENT_ERROR, exception.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(errorResponse, headers, HttpStatus.valueOf(errorResponse.getCode()));
    }


}
