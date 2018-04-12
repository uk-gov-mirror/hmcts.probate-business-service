package uk.gov.hmcts.probate.services.businessvalidation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.util.Date;

@JsonRootName("iht")
public class InheritanceTax implements Serializable{

    @JsonProperty("netValue")
    private float netValue;

    @JsonProperty("grossValue")
    private float grossValue;

    public float getNetValue() {
        return netValue;
    }

    public float getGrossValue() {
        return grossValue;
    }

    public void setNetValue(float netValue) {
        this.netValue = netValue;
    }

    public void setGrossValue(float grossValue) {
        this.grossValue = grossValue;
    }

}
