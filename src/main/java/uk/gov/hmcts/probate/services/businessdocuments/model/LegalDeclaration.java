package uk.gov.hmcts.probate.services.businessdocuments.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@JsonRootName(value = "legalDeclaration")
public class LegalDeclaration extends BusinessDocument {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @JsonProperty("headers")
    private List<String> headers;

    @JsonProperty("sections")
    private List<DeclarationSection> sections;

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



}
