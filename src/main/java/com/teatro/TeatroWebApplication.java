package com.teatro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Classe principal da aplicação Spring Boot para o Sistema de Teatro
 * 
 * Esta aplicação fornece uma API REST completa para gerenciamento de:
 * - Eventos teatrais
 * - Sessões com horários dinâmicos
 * - Áreas e poltronas
 * - Venda de ingressos
 * - Autenticação e autorização
 * - Estatísticas e relatórios
 * 
 * @author Equipe de Desenvolvimento
 * @version 1.0.0
 */
@SpringBootApplication
// @EnableJpaAuditing
public class TeatroWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeatroWebApplication.class, args);
        
        System.out.println("🎭 Sistema de Teatro Web API iniciado com sucesso!");
        System.out.println("📚 Swagger UI: http://localhost:8080/api/swagger-ui.html");
        System.out.println("🔗 API Base: http://localhost:8080/api");
        System.out.println("📊 Health Check: http://localhost:8080/api/actuator/health");
    }
} 