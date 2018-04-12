package uk.gov.hmcts.probate.services.invitation.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InviteData {

    @JsonProperty("id")
    private String id;

    @JsonProperty("formdataId")
    private String formdataId;

    @JsonProperty("mainExecutorName")
    private String mainExecutorName;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    public InviteData(String id, String formdataId, String email, String phoneNumber, String mainExecutorName) {
        this.id = id;
        this.formdataId = formdataId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.mainExecutorName = mainExecutorName;
    }

    public String getId() {
        return id;
    }
}
