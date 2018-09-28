package uk.gov.hmcts.probate.services.businessdocuments.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.probate.services.businessdocuments.model.CheckAnswersDTO;
import uk.gov.hmcts.probate.services.businessdocuments.model.PdfResponse;
import uk.gov.hmcts.probate.services.businessdocuments.services.PdfDocumentGenerationService;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RequiredArgsConstructor
@RequestMapping(value = "/document", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
@RestController
public class BusinessDocumentController {

    private final PdfDocumentGenerationService pdfDocumentGenerationService;

    @PostMapping (path = "/generate-check-answers", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PdfResponse> generatePDFDocument(@RequestBody CheckAnswersDTO checkAnswerDTO) {

        byte[] pdfBytes = pdfDocumentGenerationService.generatePDFDocument(checkAnswerDTO, "CHECK_ANSWERS");
        PdfResponse response = new PdfResponse();
        response.setBytes(pdfBytes);
        return ResponseEntity.ok(response);
    }
}
