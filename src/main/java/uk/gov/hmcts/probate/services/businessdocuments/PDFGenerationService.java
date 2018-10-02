package uk.gov.hmcts.probate.services.businessdocuments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.probate.insights.AppInsights;

import java.net.URI;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Slf4j
@RequiredArgsConstructor
@Component
public class PDFGenerationService {
    private final RestTemplate restTemplate;
    private final FileSystemResourceService fileSystemResourceService;
    private final PDFServiceConfiguration pdfServiceConfiguration;
   // private final AppInsights appInsights;

    private static final String PARAMETER_TEMPLATE = "template";
    private static final String PARAMETER_VALUES = "values";

    public byte[] generatePdf(String pdfGenerationData, DocumentType documentType) {
        URI uri = URI.create(String.format("%s%s", pdfServiceConfiguration.getUrl(), pdfServiceConfiguration.getPdfApi()));

        HttpEntity<MultiValueMap<String, Object>> multipartRequest = createMultipartPostRequest(
                documentType.getTemplateName(), pdfGenerationData);

        //appInsights.trackEvent(REQUEST_SENT, uri.toString());
        byte[] postResult = new byte[1];
        try {
            ByteArrayResource responseResource = restTemplate.postForObject(uri, multipartRequest, ByteArrayResource.class);
            postResult = responseResource.getByteArray();
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage(), e);
        //    throw new ClientException(e.getStatusCode().value(), e.getMessage());
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
        //    throw new ConnectionException("Could not connect to PDF service: " + e.getMessage());
        }
        return postResult;
    }

    private HttpEntity<MultiValueMap<String, Object>> createMultipartPostRequest(String pdfTemplateFileName, String
            pdfGenerationData) {
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();

        String templatePath = pdfServiceConfiguration.getTemplatesDirectory() + pdfTemplateFileName + ".html";

        StringBuffer sb = new StringBuffer();
        sb.append("{\"template\":\"");
        sb.append(fileSystemResourceService.getFileFromResourceAsString(templatePath));
        sb.append("\",\n\"values\":\"{}\n}");


        HttpHeaders postHeaders = new HttpHeaders();
        //postHeaders.set(HttpHeaders.CONTENT_TYPE, MULTIPART_FORM_DATA_VALUE);
        postHeaders.add("ServiceAuthorization","blah");
        postHeaders.add(HttpHeaders.CONTENT_TYPE, "application/vnd.uk.gov.hmcts.pdf-service.v2+json;charset=UTF-8");
        postHeaders.add(HttpHeaders.ACCEPT, "application/pdf");
        return new HttpEntity<>(sb.toString(), postHeaders);
    }

}
