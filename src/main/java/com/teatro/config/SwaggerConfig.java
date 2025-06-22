package com.teatro.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Configuração do Swagger/OpenAPI para documentação da API
 * 
 * Acesse a documentação em: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("Sistema de Teatro - API REST").description(
            "API REST para gerenciamento de eventos teatrais, sessões, áreas e venda de ingressos. "
                + "Sistema desenvolvido com Spring Boot, JPA/Hibernate e MySQL.")
            .version("1.0.0")
            .contact(new Contact().name("Equipe de Desenvolvimento").email("contato@teatro.com")
                .url("https://github.com/seu-usuario/teatro-web-api"))
            .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT")))
        .servers(List.of(
            new Server().url("http://localhost:8080").description("Servidor de Desenvolvimento"),
            new Server().url("https://api.teatro.com").description("Servidor de Produção")));
  }
}
