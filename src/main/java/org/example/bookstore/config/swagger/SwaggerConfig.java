package org.example.bookstore.config.swagger;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .scheme("bearer").bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER).name("Authorization")))
                .info(new Info()
                        .title("A Simple Book Store Application").version("v0.0.1-SNAPSHOT")
                        .description("This documentation contains all the APIs exposed for the sample book store project."))
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearer-jwt", Arrays.asList("read", "write")));
    }

}
