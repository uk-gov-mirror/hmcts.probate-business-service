package uk.gov.hmcts.probate.services.businessdocuments.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CheckListItemType {
    @JsonProperty("textOnly")
    TEXT_ONLY,
    @JsonProperty("textWithLink")
    TEXT_WITH_LINK

}
