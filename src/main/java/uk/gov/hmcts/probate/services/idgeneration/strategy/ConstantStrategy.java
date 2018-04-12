package uk.gov.hmcts.probate.services.idgeneration.strategy;

import java.util.Map;

public class ConstantStrategy implements IdGenerationStrategy {

    public static final String CONSTANT = "ID";

    @Override
    public String generate(Map<String, String> data) {
        return CONSTANT;
    }
}
