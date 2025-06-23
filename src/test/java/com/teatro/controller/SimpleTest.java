package com.teatro.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Teste simples para verificar se o contexto Spring carrega corretamente
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SimpleTest {

    @Test
    @DisplayName("Deve carregar o contexto Spring")
    void contextLoads() {
        // Se chegou aqui, o contexto carregou com sucesso
        System.out.println("Contexto Spring carregado com sucesso!");
    }
} 