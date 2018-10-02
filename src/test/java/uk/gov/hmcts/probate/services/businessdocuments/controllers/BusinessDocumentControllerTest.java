package uk.gov.hmcts.probate.services.businessdocuments.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.probate.services.businessdocuments.BusinessDocumentController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BusinessDocumentControllerTest {

    private static final String DOCUMENT_SERVICE_URL = "/businessDocument";
    private static final String CHECK_ANSWER_URL = "/generate-check-answers";



    @Autowired
    private BusinessDocumentController businessDocumentController;


    @Test
    public void generatePDFDocument() {
        byte[] pdfBytes = businessDocumentController.generatePDFDocument("Some json");
        //assertThat(new String(pdfBytes), Matchers.is("Hello"));
    }
}
