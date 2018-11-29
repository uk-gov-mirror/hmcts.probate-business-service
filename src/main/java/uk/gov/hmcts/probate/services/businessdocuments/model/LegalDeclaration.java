package uk.gov.hmcts.probate.services.businessdocuments.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@JsonRootName(value = "legalDeclaration")
public class LegalDeclaration implements BusinessDocument {

    @NotEmpty
    @JsonProperty("headers")
    private List<String> headers;

    @NotEmpty
    @Valid
    @JsonProperty("sections")
    private List<DeclarationSection> sections = new ArrayList<>();

    @NotBlank
    @JsonProperty("dateCreated")
    private String dateCreated;

    @NotBlank
    @JsonProperty("deceased")
    private String deceased;

    public String getDeceased() {
        return deceased;
    }

    public void setDeceased(String deceased) {
        this.deceased = deceased;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<DeclarationSection> getSections() {
        return sections;
    }

    public void setSections(List<DeclarationSection> sections) {
        this.sections = sections;
    }

}
