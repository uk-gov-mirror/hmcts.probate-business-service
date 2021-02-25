package uk.gov.hmcts.probate.services.businessvalidation.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

public class Deceased implements Serializable {

    @JsonProperty("dob_date")
    private Date dateOfBirth;

    @JsonProperty("dod_date")

    private Date dateOfDeath;

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }
}
