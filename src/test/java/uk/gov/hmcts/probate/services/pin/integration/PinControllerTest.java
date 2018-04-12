package uk.gov.hmcts.probate.services.pin.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.probate.services.businessvalidation.util.TestUtils;

import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PinControllerTest {

    private static final String SERVICE_URL = "/pin";
    private static final String TEST_PHONE_NUMBER = "07700900111";

    private MediaType contentType = new MediaType(MediaType.TEXT_PLAIN.getType(),
            MediaType.TEXT_PLAIN.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private TestUtils utils;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        Arrays.stream(converters)
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findFirst()
                .ifPresent(converter -> this.mappingJackson2HttpMessageConverter = converter);
    }

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void generatePin() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(SERVICE_URL + "/" + TEST_PHONE_NUMBER)
                .header("Session-Id", "1234567890")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string(lessThanOrEqualTo("999999")));
    }
}