package uk.gov.hmcts.probate.functional;

import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;

@RunWith(SpringIntegrationSerenityRunner.class)
@ContextConfiguration(classes = TestContextConfiguration.class)
public abstract class IntegrationTestBase {

    @Rule
    public SpringIntegrationMethodRule springIntegration;
    @Autowired
    protected TestUtils utils;
    protected String documentManagementUrl;
    String businessServiceUrl;
    String persistenceServiceUrl;
    String pdfServiceUrl;
    String mobileNumber;

    IntegrationTestBase() {
        this.springIntegration = new SpringIntegrationMethodRule();
    }

    @Autowired
    public void businessServiceConfiguration(@Value("${probate.business.url}") String businessServiceUrl,
                                             @Value("${probate.persistence.url}") String persistenceServiceUrl,
                                             @Value("${probate.pdfservice.url}") String pdfServiceUrl,
                                             @Value("${probate.notify.mobile}") String mobileNumber,
                                             @Value("${probate.document_management.url}")
                                                 String documentManagementUrl) {
        this.businessServiceUrl = businessServiceUrl;
        this.persistenceServiceUrl = persistenceServiceUrl;
        this.pdfServiceUrl = pdfServiceUrl;
        this.mobileNumber = mobileNumber;
        this.documentManagementUrl = documentManagementUrl;
    }
}
