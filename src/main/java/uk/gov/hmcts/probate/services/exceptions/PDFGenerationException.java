package uk.gov.hmcts.probate.services.exceptions;

public class PDFGenerationException extends RuntimeException {

    public PDFGenerationException(String message) {
        super(message);
    }

    public PDFGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
