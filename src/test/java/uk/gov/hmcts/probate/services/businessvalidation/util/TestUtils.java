/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.gov.hmcts.probate.services.businessvalidation.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.stereotype.Component;

@Component
public class TestUtils {

    public String getJSONFromFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/test/resources", fileName)));
    }
}
