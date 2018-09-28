package uk.gov.hmcts.probate.services.businessdocuments.services;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.services.businessdocuments.model.CheckAnswersDTO;

@Component
public class PdfDocumentGenerationService {

    public byte[] generatePDFDocument (CheckAnswersDTO checkAnswerDTO, String documentType) {

        // call restfull service here and populate bytes received
      return "Hello".getBytes();
    }

}
