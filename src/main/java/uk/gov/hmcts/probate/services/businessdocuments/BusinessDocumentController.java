package uk.gov.hmcts.probate.services.businessdocuments;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.probate.services.businessdocuments.DocumentType;
import uk.gov.hmcts.probate.services.businessdocuments.PDFGenerationService;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RequiredArgsConstructor
@RequestMapping(value = "/businessDocument", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
@RestController
public class BusinessDocumentController {

    private final PDFGenerationService pdfDocumentGenerationService;

    @PostMapping (path = "/generate-check-answers", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = "application/pdf")
    public byte[] generatePDFDocument(@RequestBody String json) {

        return pdfDocumentGenerationService.generatePdf(json, DocumentType.CHECK_ANSWERS_SUMMARY);
    }
}
