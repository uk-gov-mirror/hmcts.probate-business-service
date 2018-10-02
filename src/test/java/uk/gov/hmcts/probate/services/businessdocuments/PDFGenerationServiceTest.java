package uk.gov.hmcts.probate.services.businessdocuments;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PDFGenerationServiceTest {

    @Autowired
    private PDFGenerationService pdfGenerationService;

    @Test
    public void shouldGenerateACheckAnswersDocument() {
        byte[] bytes = pdfGenerationService.generatePdf("{\"caseName\": \"Ruban\"}", DocumentType.CHECK_ANSWERS_SUMMARY);
        System.out.println(bytes);
    }
}
