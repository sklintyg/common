package se.inera.intyg.common.ag114.v1.model.validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import se.inera.intyg.common.ag114.v1.validator.InternalDraftValidatorImpl;
import se.inera.intyg.common.ag114.v1.validator.ValidatorUtilSKL;
import se.inera.intyg.common.support.validate.InternalDraftValidator;

@Configuration
public class TestConfiguration {

    @Bean
    public ValidatorUtilSKL validatorUtilSKL() {
        return new ValidatorUtilSKL();
    }

    @Bean
    public InternalDraftValidator internalDraftValidator() {
        return new InternalDraftValidatorImpl();
    }
}
