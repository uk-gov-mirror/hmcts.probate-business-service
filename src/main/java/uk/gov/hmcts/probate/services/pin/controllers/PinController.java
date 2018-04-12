package uk.gov.hmcts.probate.services.pin.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.probate.services.pin.PinService;
import uk.gov.service.notify.NotificationClientException;

@RestController
public class PinController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PinController.class);
    @Autowired
    private PinService pinService;

    @RequestMapping(path = "/pin/{phoneNumber}", method = RequestMethod.GET)
    public String invite(@PathVariable String phoneNumber,
                         @RequestHeader("Session-Id") String sessionId) throws NotificationClientException {

        LOGGER.info("Processing session id " + sessionId);

        return pinService.generateAndSend(phoneNumber);
    }
}
