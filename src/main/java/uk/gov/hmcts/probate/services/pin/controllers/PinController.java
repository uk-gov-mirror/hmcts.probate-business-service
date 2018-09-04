package uk.gov.hmcts.probate.services.pin.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.probate.services.pin.PinService;
import uk.gov.service.notify.NotificationClientException;

@RestController
public class PinController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PinController.class);
    @Autowired
    private PinService pinService;

    @RequestMapping(path = "/pin", method = RequestMethod.GET)
    public ResponseEntity<String> invite(@RequestParam String phoneNumber,
                         @RequestHeader("Session-Id") String sessionId) throws NotificationClientException, UnsupportedEncodingException {
 	
		phoneNumber = URLDecoder.decode(phoneNumber, java.nio.charset.StandardCharsets.UTF_8.toString());
		
		phoneNumber = phoneNumber.replaceAll("[ \\(\\)\\[\\]-]", "");		
		
		if ( !phoneNumber.matches("(\\+)*[0-9]+") ) {
			LOGGER.error("Unable to validate phoneNumber");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		return ResponseEntity.ok(pinService.generateAndSend(phoneNumber));
    }

    @RequestMapping(path = "/pin/{phoneNumber}", method = RequestMethod.GET)
    public String inviteLegacy(@PathVariable String phoneNumber,
                           @RequestHeader("Session-Id") String sessionId) throws NotificationClientException {
	     
	    LOGGER.info("Processing session id " + sessionId);
        return pinService.generateAndSend(phoneNumber);
    }
    
}
