package uk.gov.hmcts.probate.services.idgeneration.strategy;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class PinStrategy implements IdGenerationStrategy {

    public static final int MIN = 100000;
    public static final int MAX = 1000000;

    @Override
    public String generate(Map<String, String> data) {
        Integer randomNum = ThreadLocalRandom.current().nextInt(MIN, MAX);
        return randomNum.toString();
    }
}
