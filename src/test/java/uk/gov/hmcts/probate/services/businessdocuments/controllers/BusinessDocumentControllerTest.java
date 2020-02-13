package uk.gov.hmcts.probate.services.businessdocuments.controllers;

import org.hamcrest.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.probate.services.businessdocuments.services.PDFGenerationService;
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.Declaration;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class BusinessDocumentControllerTest {

    BusinessDocumentController businessDocumentController;

    @Mock
    PDFGenerationService pdfGenerationService;

    CheckAnswersSummary checkAnswersSummary;

    LegalDeclaration legalDeclaration;

    BulkScanCoverSheet coverSheet;

    @Before
    public void setUp() {
        businessDocumentController = new BusinessDocumentController(pdfGenerationService);
        checkAnswersSummary = new CheckAnswersSummary();
        legalDeclaration = new LegalDeclaration();
        legalDeclaration.setDeclarations(Arrays.asList(new Declaration()));
        coverSheet = new BulkScanCoverSheet();
    }

    @Test
    public void shouldGenerateACheckAnswersSummaryPDF() {
        ResponseEntity<byte[]> result = businessDocumentController.generateCheckAnswersSummaryPDF(checkAnswersSummary, "authorisation");
    }

    @Test
    public void shouldGenerateALegalDeclarationPDF() {
        ResponseEntity<byte[]> result = businessDocumentController.generateLegalDeclarationPDF(legalDeclaration, "authorisation");
        Assert.assertThat(legalDeclaration.getDeclarations().size(), CoreMatchers.is(1));
        Assert.assertThat(legalDeclaration.getDeclarations().stream().findFirst().get().isLastDeclaration(), CoreMatchers.is(Boolean.TRUE));
    }

    @Test
    public void shouldGenerateABulkScanCoverSheetPDF() {
        ResponseEntity<byte[]> result = businessDocumentController.generateBulkScanCoverSheetPDF(coverSheet, "authorisation");
    }
}
