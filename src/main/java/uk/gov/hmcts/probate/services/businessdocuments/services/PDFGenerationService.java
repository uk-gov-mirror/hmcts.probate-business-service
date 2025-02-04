package uk.gov.hmcts.probate.services.businessdocuments.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.tagging.PdfStructElem;
import com.itextpdf.kernel.pdf.tagging.PdfStructTreeRoot;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.layout.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.config.PDFServiceConfiguration;
import uk.gov.hmcts.probate.services.businessdocuments.exceptions.PDFGenerationException;
import uk.gov.hmcts.probate.services.businessdocuments.model.DocumentType;
import uk.gov.hmcts.reform.pdf.service.client.PDFServiceClient;
import uk.gov.hmcts.reform.probate.model.documents.BusinessDocument;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

        // Add tagging for accessibility using iText
        ByteArrayOutputStream taggedPdfOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(taggedPdfOutputStream);
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(new ByteArrayInputStream(pdfBytes)), writer);
        pdfDocument.setTagged();
        Document document = new Document(pdfDocument);
        document.setProperty(ROLE, StandardRoles.DOCUMENT);

        // Step 1: Get the structure tree root
        PdfStructTreeRoot structRoot = pdfDocument.getStructTreeRoot();

        // Step 2: Define the document structure
        PdfStructElem docElem = new PdfStructElem(pdfDocument, PdfName.Document);

        structRoot.addKid(docElem);

        // Step 3: Create a TABLE structure inside the document
        PdfStructElem tableElem = new PdfStructElem(pdfDocument, PdfName.Table);
        docElem.addKid(tableElem); // Attach TABLE to the document structure

        // Step 4: Add a ROW to the table
        PdfStructElem rowElem = new PdfStructElem(pdfDocument, PdfName.TR);
        tableElem.addKid(rowElem); // Attach ROW inside TABLE

        // Step 5: Add CELLS inside the row
        PdfStructElem cellElem1 = new PdfStructElem(pdfDocument, PdfName.TD);
        cellElem1.getPdfObject().put(PdfName.ActualText, new PdfString("Column 1 Data"));
        rowElem.addKid(cellElem1); // Attach CELL inside ROW

        PdfStructElem cellElem2 = new PdfStructElem(pdfDocument, PdfName.TD);
        cellElem2.getPdfObject().put(PdfName.ActualText, new PdfString("Column 2 Data"));
        rowElem.addKid(cellElem2); // Attach CELL inside ROW

        // Step 6: Close the document
        document.close();
        pdfDocument.close();
        writer.close();

        byte[] result = taggedPdfOutputStream.toByteArray();
        verifyPdfBytes(result); // Verify the integrity of the generated PDF bytes
        return result;
    }

    private void verifyPdfBytes(byte[] pdfBytes) throws IOException {
        try (PdfReader reader = new PdfReader(new ByteArrayInputStream(pdfBytes))) {
            PdfDocument pdfDocument = new PdfDocument(reader);
            if (pdfDocument.getNumberOfPages() == 0) {
                throw new IOException("Generated PDF has no pages");
            }
        }
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
