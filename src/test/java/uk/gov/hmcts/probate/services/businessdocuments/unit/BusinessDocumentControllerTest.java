package uk.gov.hmcts.probate.services.businessdocuments.unit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.probate.services.businessdocuments.BusinessDocumentController;
import uk.gov.hmcts.probate.services.businessdocuments.PDFGenerationService;
import uk.gov.hmcts.probate.services.businessvalidation.util.TestUtils;
import uk.gov.hmcts.probate.services.invitation.PersistenceClient;
import uk.gov.service.notify.NotificationClient;

import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BusinessDocumentControllerTest {

    private static final String CHECK_ANSWERS_SUMMARY_URL = "/businessDocument/generateCheckAnswersSummaryPDF";
    private static final String LEGAL_DECLARATION_URL = "/businessDocument/generateLegalDeclarationPDF";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private TestUtils utils;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private PDFGenerationService pdfGenerationService;

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
    public void shouldConsumeValidCheckSummaryMessage() throws Exception {
        ResultActions resultActions = mockMvc.perform(post(CHECK_ANSWERS_SUMMARY_URL)
                .header("serviceAuthorization", "dummyKey")
                .content(utils.getJSONFromFile("businessdocument/checkAnswersSimpleSummary.json"))
                .contentType(contentType))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldThrowAnExceptionOnInValidCheckSummaryMessage() throws Exception {
        ResultActions resultActions = mockMvc.perform(post(CHECK_ANSWERS_SUMMARY_URL)
                .header("serviceAuthorization", "dummyKey")
                .content(utils.getJSONFromFile("businessdocument/invalidCheckAnswersSummary.json"))
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldConsumeValidLegalDeclarationMessage() throws Exception {
        ResultActions resultActions = mockMvc.perform(post(LEGAL_DECLARATION_URL)
                .header("serviceAuthorization", "dummyKey")
                .content(utils.getJSONFromFile("businessdocument/validLegalDeclaration.json"))
                .contentType(contentType))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldThrowAnExceptionOnInValidLegalDeclarationMessage() throws Exception {
        ResultActions resultActions = mockMvc.perform(post(LEGAL_DECLARATION_URL)
                .header("serviceAuthorization", "dummyKey")
                .content(utils.getJSONFromFile("businessdocument/invalidLegalDeclaration.json"))
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

}
