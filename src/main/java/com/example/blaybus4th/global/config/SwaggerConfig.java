package com.example.blaybus4th.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .version("v1.0.0") // 현재 문서 버전
                        .description("blaybus 해커톤에 사용하는 API 목록 문서화") // 부제목
                        .title("blaybus API 목록")
                        .contact(new Contact()
                                .name("BE") // 연락처 이름
                                .email("tklr0731@naver.com")
                        ))

                .servers(List.of(
                        new Server()
                                .description("개발용 서버") // 해당 url서버에 대해 해당 설명제공
                                .url("http://localhost:8080"),
                        new Server()
                                .description("배포용 서버")
                                .url("http://43.200.52.62:8080")
                ))
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP) // http 기반 타입
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER) // JWT 위치는 header
                                .name("Authorization")) // header 키값이름
                ).addSecurityItem(new SecurityRequirement().addList("JWT"));

    }

}
