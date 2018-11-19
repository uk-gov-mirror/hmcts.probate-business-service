package uk.gov.hmcts.probate.services.businessdocuments.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.probate.services.businessdocuments.controllers.BusinessDocumentController;
import uk.gov.hmcts.probate.services.businessdocuments.model.CheckAnswersSummary;
import uk.gov.hmcts.probate.services.businessdocuments.model.LegalDeclaration;
import uk.gov.hmcts.probate.services.businessdocuments.services.PDFGenerationService;

@RunWith(MockitoJUnitRunner.class)
public class BusinessDocumentControllerTest {

    BusinessDocumentController businessDocumentController;

    @Mock
    PDFGenerationService pdfGenerationService;

    CheckAnswersSummary checkAnswersSummary;

    LegalDeclaration legalDeclaration;

    @Before
    public void setUp() {
        businessDocumentController = new BusinessDocumentController(pdfGenerationService);
        checkAnswersSummary = new CheckAnswersSummary();
        legalDeclaration = new LegalDeclaration();

    }

    @Test
    public void shouldGenerateACheckAnswersSummaryPDF() {
        ResponseEntity<byte[]> result = businessDocumentController.generateCheckAnswersSummaryPDF(checkAnswersSummary, "authorisation");
    }

    @Test
    public void shouldGenerateALegalDeclarationPDF() {
        ResponseEntity<byte[]> result = businessDocumentController.generateLegalDeclarationPDF(legalDeclaration, "authorisation");
    }
}
