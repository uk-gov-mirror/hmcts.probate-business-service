package uk.gov.hmcts.probate.services.businessdocuments.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName(value = "legalDeclaration")
public class LegalDeclaration extends BusinessDocument {


    @JsonProperty("headers")
    private List<String> headers;

    @JsonProperty("sections")
    private List<DeclarationSection> sections;

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }



}
