package com.myflx.validation;

import com.myflx.validation.processor.ValidationFailFastPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author LuoShangLin
 */
@Configuration
@Import(ValidationFailFastPostProcessor.class)
public class ValidFailFastConfiguration {
}
