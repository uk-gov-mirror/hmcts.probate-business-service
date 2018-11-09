package uk.gov.hmcts.probate.services.businessdocuments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.probate.services.businessdocuments.model.CheckAnswersSummary;
import uk.gov.hmcts.probate.services.businessdocuments.model.LegalDeclaration;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/businessDocument")
@RestController
public class BusinessDocumentController {

    private final PDFGenerationService pdfDocumentGenerationService;


    @PostMapping(path = "/generateCheckAnswersSummaryPDF", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public byte[] generateCheckAnswersSummaryPDF(@Valid @RequestBody CheckAnswersSummary checkAnswersSummary, @RequestHeader("ServiceAuthorization") String authorization) {

        log.info("call to generateCheckAnswersSummaryPDF()");

        return pdfDocumentGenerationService.generatePdf(authorization, checkAnswersSummary,DocumentType.CHECK_ANSWERS_SUMMARY);
    }

    @PostMapping(path = "/generateLegalDeclarationPDF", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public byte[] generateLegalDeclarationPDF(@Valid @RequestBody LegalDeclaration legalDeclaration, @RequestHeader("ServiceAuthorization") String authorization) {

        log.info("call to generateLegalDeclarationPDF()");

        return pdfDocumentGenerationService.generatePdf(authorization, legalDeclaration, DocumentType.LEGAL_DECLARATION);
    }

}
