package com.snippie.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private final String API_TITLE = "Snippie Backend API";
    private final String API_VERSION = "v1.0.0";
    private final String API_DESCRIPTION = "Snippie 프로젝트 Backend API 명세서입니다.";

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title(API_TITLE)
                .version(API_VERSION)
                .description(API_DESCRIPTION);

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
