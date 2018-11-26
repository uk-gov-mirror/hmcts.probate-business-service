package uk.gov.hmcts.probate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import uk.gov.hmcts.probate.services.businessvalidation.validators.ValidationRule;
import uk.gov.hmcts.probate.services.idgeneration.IdGeneratorService;
import uk.gov.hmcts.probate.services.idgeneration.strategy.PinStrategy;
import uk.gov.hmcts.probate.services.idgeneration.strategy.ProbateStrategy;
import uk.gov.service.notify.NotificationClient;
import uk.gov.service.notify.NotificationClientException;
import uk.gov.service.notify.SendSmsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@SpringBootApplication
@EnableSwagger2
@EnableAutoConfiguration
public class BusinessApplication {


    @Value("${services.notify.apiKey}")
    String notificationApiKey;

    @Autowired
    private ValidationRule dobBeforeDodRule, netIHTLessThanGrossRule;

    public static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class, args);
    }

    @Bean
    List<ValidationRule> validationRules() {
        List<ValidationRule> validationRules = new ArrayList<>();
        validationRules.add(dobBeforeDodRule);
        validationRules.add(netIHTLessThanGrossRule);
        return validationRules;
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    IdGeneratorService idGeneratorService() {
        return new IdGeneratorService(new ProbateStrategy());
    }

    @Bean
    IdGeneratorService pinGeneratorService() {
        return new IdGeneratorService(new PinStrategy());
    }

    @Bean
    NotificationClient notificationClient() {
        return new NotificationClientTestOnly("none");
    }

    class NotificationClientTestOnly extends NotificationClient{

        public NotificationClientTestOnly(String apiKey) {
            super(apiKey);
        }

        @Override
        public SendSmsResponse sendSms(String templateId, String phoneNumber, Map<String, String> personalisation, String reference) throws NotificationClientException {
            return new SendSmsResponse("{" +
                    "id: '731d2626-3b8f-4fb6-983e-2f9a10c983c4', " +
                    "content: {body: ''}," +
                    "template: {id: '731d2626-3b8f-4fb6-983e-2f9a10c983c4', version: 1, uri: 'none'}}");
        }
    }
}
