package ru.nsu.sberlab.configuration.openapi;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OpenApiConfiguration {
    @Bean
    public GroupedOpenApi usersGroup(@Value("${springdoc.version}") String appVersion) {
        return GroupedOpenApi.builder().group("taggypet")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new Info().title("Taggy Pet API").version(appVersion));
                })
                .build();
    }
}
