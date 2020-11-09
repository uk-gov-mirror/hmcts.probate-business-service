package uk.gov.hmcts.probate.services.businessdocuments.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import uk.gov.hmcts.probate.config.PDFServiceConfiguration;
import uk.gov.hmcts.probate.services.businessdocuments.model.DocumentType;
import uk.gov.hmcts.probate.services.businessdocuments.services.FileSystemResourceService;
import uk.gov.hmcts.probate.services.businessdocuments.services.PDFGenerationService;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.probate.model.documents.BusinessDocument;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactFolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Executor;
import org.json.JSONException;
import org.junit.After;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@PactTestFor(providerName = "rpePdfService_PDFGenerationEndpointV2", port = "8891")
@PactFolder("pacts")
@SpringBootTest({
    "service.pdf.service.uri : http://localhost:8891/pdfs"
})
public class PdfServiceConsumerTest {

    private static final String SERVICE_AUTHORIZATION_HEADER = "ServiceAuthorization";

    @Autowired
    PDFGenerationService pdfGenerationService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private AuthTokenGenerator serviceTokenGenerator;

    //@MockBean
    //private TemplateManagementService templateManagementService;

    @Mock
    private CheckAnswersSummary mockCheckAnswersSummary;

    @MockBean
    private PDFServiceConfiguration pdfServiceConfiguration;

    @MockBean
    private FileSystemResourceService fileSystemResourceService;

    private final String someServiceAuthToken = "someServiceAuthToken";
    private final String template = "<html><body><div>Case number: {{ caseNo }}</div></body></html>";

    private Map placeholders = Map.of("caseNo", "12345");

    private String someJSON = "{\"test\":\"json\"}";



    @BeforeEach
    public void setUpEachTest() throws InterruptedException, IOException {
        Thread.sleep(2000);

        try {
            when(objectMapper.writeValueAsString(Mockito.any(CheckAnswersSummary.class))).thenReturn(someJSON);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @After
    void teardown() {
        Executor.closeIdleConnections();
    }
                                                              // TBD consumer 'Name'
    @Pact(provider = "rpePdfService_PDFGenerationEndpointV2", consumer = "probate_documentGeneratorClient")
    RequestResponsePact generatePdfFromTemplate(PactDslWithProvider builder) throws JSONException, IOException {
        // @formatter:off

        return builder
            .given("A request to generate a Probate pdf document")
            .uponReceiving("a request to generate a PDF document with a template")
            .method("POST")
            .headers(SERVICE_AUTHORIZATION_HEADER, someServiceAuthToken)
            //.body(createJsonObject(new GenerateDocumentRequest(template, placeholders)),
            //    "application/vnd.uk.gov.hmcts.pdf-service.v2+json;charset=UTF-8")
            .path("/pdfs")
            .willRespondWith()
            .withBinaryData("".getBytes(), "application/octet-stream")
            .matchHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, "application/pdf")
            .status(HttpStatus.SC_OK)
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "generatePdfFromTemplate")
    public void verifyGeneratePdfFromTemplatePact() throws IOException, JSONException {

        Map<String, Object> placeholders = new HashMap<>();
        placeholders.put("caseNo", "12345");

        when(pdfServiceConfiguration.getTemplatesDirectory()).thenReturn("templateDirectory");
        when(fileSystemResourceService.getFileFromResourceAsString(Mockito.anyString())).thenReturn("templateAsString");

        //when(templateManagementService.getTemplateByName("someTemplateName")).thenReturn(template.getBytes());
        when(serviceTokenGenerator.generate()).thenReturn(someServiceAuthToken);

        byte[] response = pdfGenerationService.generatePdf(mockCheckAnswersSummary, DocumentType.CHECK_ANSWERS_SUMMARY);

        assertThat(response , notNullValue());

    }

//    private File getFile(String fileName) throws FileNotFoundException {
//        return ResourceUtils.getFile(this.getClass().getResource("/json/" + fileName));
//    }
//
//    protected String createJsonObject(Object obj) throws JSONException, IOException {
//        return objectMapper.writeValueAsString(obj);
//    }

//    private GenerateDocumentRequest buildGenerateDocumentRequest() {
//        Map<String, Object> placeholders = new HashMap<>();
//        placeholders.put("caseNo", "12345");
//
//        return new GenerateDocumentRequest(template, placeholders);
//
//    }
}
