package uk.gov.hmcts.probate.services.businessdocuments;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.probate.services.businessvalidation.model.CheckAnswersSummary;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping(value = "/businessDocument")
@RestController
public class BusinessDocumentController {

    private final PDFGenerationService pdfDocumentGenerationService;


    @PostMapping(path = "/generateCheckAnswersSummaryPDF", consumes = javax.ws.rs.core.MediaType.APPLICATION_JSON, produces = "application/pdf")
    public byte[] generateCheckAnswersSummaryPDF(@Valid @RequestBody CheckAnswersSummary checkAnswersSummary, @RequestHeader("ServiceAuthorization") String authorization) {

        final byte[] bytes = pdfDocumentGenerationService.generatePdf(authorization, checkAnswersSummary,DocumentType.CHECK_ANSWERS_SUMMARY);
        return bytes;
    }


}
