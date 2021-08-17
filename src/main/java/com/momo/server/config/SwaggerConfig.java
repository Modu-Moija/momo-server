package com.momo.server.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
            .title("MoMo")
            .description("MoMo API")
            .build();
    }

    @Bean
    public Docket commonApi() {

        TypeResolver typeResolver = new TypeResolver();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
            .alternateTypeRules(AlternateTypeRules.newRule(
                typeResolver.resolve(ArrayList.class, LocalDate.class),
                typeResolver.resolve(ArrayList.class, Date.class), Ordered.HIGHEST_PRECEDENCE))
            .groupName("momo")
            .useDefaultResponseMessages(false)
            .apiInfo(this.apiInfo())
            .select()
            .apis(RequestHandlerSelectors
                .basePackage("com.momo.server.controller"))
            .paths(PathSelectors.ant("/api/**"))
            .build();

        return docket;
    }
}
