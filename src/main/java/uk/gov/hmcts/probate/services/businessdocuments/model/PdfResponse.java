package uk.gov.hmcts.probate.services.businessdocuments.model;

import lombok.Data;

@Data
public class PdfResponse {

    private byte[] bytes;
    private String documentTitle;
}
