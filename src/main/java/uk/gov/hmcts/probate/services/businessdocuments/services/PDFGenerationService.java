package uk.gov.hmcts.probate.services.businessdocuments.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.config.PDFServiceConfiguration;
import uk.gov.hmcts.probate.services.businessdocuments.exceptions.PDFGenerationException;
import uk.gov.hmcts.probate.services.businessdocuments.model.DocumentType;
import uk.gov.hmcts.reform.pdf.service.client.PDFServiceClient;
import uk.gov.hmcts.reform.probate.model.documents.BusinessDocument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class PDFGenerationService {
    private static final String HTML = ".html";
    public static final int ROLE = 200;
    private final FileSystemResourceService fileSystemResourceService;
    private final PDFServiceConfiguration pdfServiceConfiguration;
    private final ObjectMapper objectMapper;
    private final PDFServiceClient pdfServiceClient;

    public byte[] generatePdf(BusinessDocument businessDocument, DocumentType documentType) {

        byte[] postResult;

        try {
            postResult = generateFromHtml(businessDocument, documentType.getTemplateName());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new PDFGenerationException(e.getMessage(), e);
        }
        return postResult;
    }

    byte[] generateFromHtml(BusinessDocument businessDocument, String templateName)
        throws IOException {
        String templatePath = pdfServiceConfiguration.getTemplatesDirectory() + templateName + HTML;
        String templateAsString = fileSystemResourceService.getFileFromResourceAsString(templatePath);
        Map<String, Object> paramMap = asMap(objectMapper.writeValueAsString(businessDocument));


        // Generate PDF using existing service
        byte[] pdfBytes = pdfServiceClient.generateFromHtml(templateAsString.getBytes(), paramMap);

        // Generate PDF with tags
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        pdfDocument.setTagged();
        // Configure for accessibility
        ConverterProperties props = new ConverterProperties();
        props.setTagWorkerFactory(new DefaultTagWorkerFactory());
        HtmlConverter.convertToPdf(new String(pdfBytes, StandardCharsets.UTF_8), pdfDocument, props);
        pdfDocument.close();
        writer.close();
        return outputStream.toByteArray();
    }

    public Map<String, Object> asMap(String placeholderValues) {
        try {

            ObjectMapper mappy = new ObjectMapper();
            return mappy.readValue(placeholderValues,
                new TypeReference<HashMap<String, Object>>() {
                });
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new PDFGenerationException(e.getMessage(), e);
        }
    }


}
