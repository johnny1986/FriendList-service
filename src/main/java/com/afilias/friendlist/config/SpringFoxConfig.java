package com.afilias.friendlist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SpringFoxConfig {

    private String AUTH_SERVER = "http://localhost:8080/swagger-ui/";

    private String CLIENT_ID = "127ae992";

    private String CLIENT_SECRET = "50f8d32c-2e19-4f03-b3d7-8578b98d42d3";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.afilias.friendlist.controller"))
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo())/*.securitySchemes(Arrays.asList(securityScheme()))
                .securityContexts(Arrays.asList(securityContext()))*/;
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .scopeSeparator(" ")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    private SecurityScheme securityScheme() {
        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(new TokenEndpoint(AUTH_SERVER + "/token", "oauthtoken"))
                .tokenRequestEndpoint(
                        new TokenRequestEndpoint(AUTH_SERVER + "/authorize", CLIENT_ID, CLIENT_SECRET))
                .build();

        SecurityScheme oauth = new OAuthBuilder().name("spring_oauth")
                .grantTypes(Arrays.asList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
        return oauth;
    }

    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {
                new AuthorizationScope("read", "for read operations"),
                new AuthorizationScope("write", "for write operations"),
                new AuthorizationScope("foo", "Access app API")};
        return scopes;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(
                        Arrays.asList(new SecurityReference("spring_oauth", scopes())))
                .forPaths(PathSelectors.regex("/app.*"))
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfo(
                "FRIEND LIST REST API",
                "service providing resources to build a user friend.",
                "API TOS",
                "Terms of service",
                new Contact("Juan Fuster", "", "juafusba@gmail.com"),
                "License of API",
                "API license URL",
                Collections.emptyList());
    }

}
