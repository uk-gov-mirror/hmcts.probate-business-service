package uk.gov.hmcts.probate.services.pin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.services.idgeneration.IdGeneratorService;
import uk.gov.hmcts.probate.services.pin.controllers.PinController;
import uk.gov.service.notify.NotificationClient;
import uk.gov.service.notify.NotificationClientException;
import uk.gov.service.notify.SendSmsResponse;

import java.util.HashMap;
import java.util.Map;

@Component
public class PinService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PinController.class);

    @Value("${services.notify.pin.templateId}")
    String templateId;

    @Value("${services.notify.pin.bilingualTemplateId}")
    String bilingualTemplateId;

    @Autowired
    private NotificationClient notificationClient;

    @Autowired
    private IdGeneratorService pinGeneratorService;


    public String generateAndSend(String phoneNumber, Boolean isBilingual) throws NotificationClientException {
        String pin = pinGeneratorService.generate();
        LOGGER.info("Generated pin: [{}] , phoneNumber: [{}]", pin, phoneNumber);
        SendSmsResponse smsResponse = notificationClient
            .sendSms(isBilingual ? bilingualTemplateId : templateId, phoneNumber, createPersonalisation(pin), pin);
        LOGGER.info("smsResponse: {}", smsResponse);
        return pin;
    }

    private Map<String, String> createPersonalisation(String pin) {
        HashMap<String, String> personalisation = new HashMap<>();
        personalisation.put("pin", pin);
        return personalisation;
    }
}
