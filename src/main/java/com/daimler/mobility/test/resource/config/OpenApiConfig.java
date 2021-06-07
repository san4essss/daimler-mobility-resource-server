package com.daimler.mobility.test.resource.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(components())
                .security(securityRequirements())
                .info(info());
    }

    private Info info() {
        return new Info()
                .version("0.0.1")
                .title("Resource server API")
                .description("This Server offers an API for downloading files and a UI to manage the files.");
    }

    private Components components() {
        Components components = new Components();
        components.setSecuritySchemes(securitySchemes());

        return components;
    }

    public static List<SecurityRequirement> securityRequirements()
    {
        return Arrays.asList(new SecurityRequirement().addList("ApiKeyAuth"));
    }

    private static Map<String, SecurityScheme> securitySchemes()
    {
        Map<String, SecurityScheme> securitySchemes = new HashMap<>();

        SecurityScheme apiKeyAuth = new SecurityScheme()
                .name("Authorization")
                .description(""
                        + "The value of the authentication header must be composed as follows:\n"
                        + "```\n"
                        + " - for UI app: Authorization api-key my-valid-api-key-admin"
                        + "\n"
                        + " - for Other systems: Authorization api-key my-valid-api-key-user")
                .in(SecurityScheme.In.HEADER)
                .type(SecurityScheme.Type.APIKEY);

        securitySchemes.put("ApiKeyAuth", apiKeyAuth);
        return securitySchemes;
    }
}
