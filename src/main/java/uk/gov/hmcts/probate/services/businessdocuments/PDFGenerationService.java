package uk.gov.hmcts.probate.services.businessdocuments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import uk.gov.hmcts.probate.services.businessdocuments.model.BusinessDocument;
import uk.gov.hmcts.probate.services.exceptions.BusinessDocumentException;
import uk.gov.hmcts.probate.services.exceptions.PDFGenerationException;
import uk.gov.hmcts.reform.pdf.service.client.PDFServiceClient;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
@Component
public class PDFGenerationService {
    public static final MediaType API_VERSION = MediaType
            .valueOf("application/vnd.uk.gov.hmcts.pdf-service.v2+json;charset=UTF-8");
    public static final String SERVICE_AUTHORIZATION_HEADER = "ServiceAuthorization";
    private final RestOperations restOperations;
    private final FileSystemResourceService fileSystemResourceService;
    private final PDFServiceConfiguration pdfServiceConfiguration;
    private final ObjectMapper objectMapper;

    public byte[] generatePdf(String serviceAuthToken, BusinessDocument businessDocument, DocumentType documentType) {

        byte[] postResult;

        try {
            postResult = generateFromHtml(serviceAuthToken, businessDocument, documentType.getTemplateName());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new PDFGenerationException(e.getMessage(), e);
        }
        return postResult;
    }

    private byte[] generateFromHtml(String serviceAuthToken, BusinessDocument businessDocument, String templateName) throws JsonProcessingException {
        URI uri = URI.create(String.format("%s%s", pdfServiceConfiguration.getUrl(), pdfServiceConfiguration.getPdfApi()));

        Supplier<String> supplier = () -> serviceAuthToken;

        PDFServiceClient pdfServiceClient = new PDFServiceClient(restOperations, objectMapper, supplier, uri);

        String templatePath = pdfServiceConfiguration.getTemplatesDirectory() + templateName + ".html";
        String templateAsString = fileSystemResourceService.getFileFromResourceAsString(templatePath);

        Map<String, Object> paramMap = asMap(objectMapper.writeValueAsString(businessDocument));
        return pdfServiceClient.generateFromHtml(templateAsString.getBytes(), paramMap);
    }

    private Map<String, Object> asMap(String placeholderValues) {
        try {

            ObjectMapper mappy = new ObjectMapper();
            return mappy.readValue(placeholderValues,
                    new TypeReference<HashMap<String, Object>>() {
                    });
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BusinessDocumentException(e.getMessage(), e);
        }
    }


}
