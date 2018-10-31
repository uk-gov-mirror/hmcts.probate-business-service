package uk.gov.hmcts.probate.services.businessdocuments.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeclarationSection implements Serializable {

    @NotBlank
    @JsonProperty("title")
    private String title;

    @NotBlank
    @JsonProperty("headingType")
    private String headingType;

    @JsonProperty("declarationItems")
    private List<DeclarationItem> declarationItems = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DeclarationItem> getDeclarationItems() {
        return declarationItems;
    }

    public void setDeclarationItems(List<DeclarationItem> declarationItems) {
        this.declarationItems = declarationItems;
    }

    public String getHeadingType() {
        return headingType;
    }

    public void setHeadingType(String headingType) {
        this.headingType = headingType;
    }
}
