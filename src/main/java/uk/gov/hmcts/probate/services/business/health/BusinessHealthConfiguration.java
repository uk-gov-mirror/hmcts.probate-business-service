package uk.gov.hmcts.probate.services.business.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BusinessHealthConfiguration {

    @Value("${services.persistence.baseUrl}")
    private String servicePersistenceBaseUrl;

    @Value("${services.pdf.service.url}")
    private String pdfServiceBaseUrl;

    @Value("${services.auth.provider.baseUrl}")
    private String authServiceBaseUrl;


    @Bean
    public BusinessHealthIndicator persistenceServiceHealthIndicator(@Autowired RestTemplate restTemplate) {
        return new BusinessHealthIndicator(servicePersistenceBaseUrl, restTemplate);
    }

    @Bean
    public BusinessHealthIndicator pdfServiceHealthIndicator(@Autowired RestTemplate restTemplate) {
        return new BusinessHealthIndicator(pdfServiceBaseUrl, restTemplate);
    }

    @Bean
    public BusinessHealthIndicator authServiceHealthIndicator(@Autowired RestTemplate restTemplate) {
        return new BusinessHealthIndicator(authServiceBaseUrl, restTemplate);
    }
}
