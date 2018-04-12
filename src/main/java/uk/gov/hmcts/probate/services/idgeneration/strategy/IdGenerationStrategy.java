package uk.gov.hmcts.probate.services.idgeneration.strategy;

import java.util.Map;

public interface IdGenerationStrategy {

    String generate(Map<String, String> data);
}
