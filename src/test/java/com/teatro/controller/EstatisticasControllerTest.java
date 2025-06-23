package com.teatro.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.teatro.config.TestConfig;
import com.teatro.service.EstatisticasService;

@WebMvcTest(EstatisticasController.class)
@Import(TestConfig.class)
class EstatisticasControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private EstatisticasService estatisticasService;

  @Test
  @DisplayName("Deve retornar estatísticas gerais com sucesso")
  void deveRetornarEstatisticasGerais() throws Exception {
    Mockito.when(estatisticasService.obterEstatisticasGerais()).thenReturn(Collections.emptyMap());
    mockMvc.perform(get("/api/estatisticas/gerais")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Deve retornar estatísticas de eventos")
  void deveRetornarEstatisticasEventos() throws Exception {
    Mockito.when(estatisticasService.obterEstatisticasEventos()).thenReturn(Collections.emptyMap());
    mockMvc.perform(get("/api/estatisticas/eventos")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve retornar estatísticas de um evento específico")
  void deveRetornarEstatisticasEventoEspecifico() throws Exception {
    Mockito.when(estatisticasService.obterEstatisticasEvento(1L))
        .thenReturn(Collections.emptyMap());
    mockMvc.perform(get("/api/estatisticas/eventos/1")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve retornar estatísticas de sessões")
  void deveRetornarEstatisticasSessoes() throws Exception {
    Mockito.when(estatisticasService.obterEstatisticasSessoes()).thenReturn(Collections.emptyMap());
    mockMvc.perform(get("/api/estatisticas/sessoes")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve retornar estatísticas de vendas")
  void deveRetornarEstatisticasVendas() throws Exception {
    Mockito.when(estatisticasService.obterEstatisticasVendas()).thenReturn(Collections.emptyMap());
    mockMvc.perform(get("/api/estatisticas/vendas")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve retornar estatísticas de ocupação")
  void deveRetornarEstatisticasOcupacao() throws Exception {
    Mockito.when(estatisticasService.obterEstatisticasOcupacao())
        .thenReturn(Collections.emptyMap());
    mockMvc.perform(get("/api/estatisticas/ocupacao")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve retornar estatísticas de faturamento")
  void deveRetornarEstatisticasFaturamento() throws Exception {
    Mockito.when(estatisticasService.obterEstatisticasFaturamento())
        .thenReturn(Collections.emptyMap());
    mockMvc.perform(get("/api/estatisticas/faturamento")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve retornar estatísticas de fidelidade")
  void deveRetornarEstatisticasFidelidade() throws Exception {
    Mockito.when(estatisticasService.obterEstatisticasFidelidade())
        .thenReturn(Collections.emptyMap());
    mockMvc.perform(get("/api/estatisticas/fidelidade")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve retornar estatísticas por período")
  void deveRetornarEstatisticasPorPeriodo() throws Exception {
    Mockito.when(estatisticasService.obterEstatisticasPorPeriodo("2024-01-01", "2024-01-31"))
        .thenReturn(Collections.emptyMap());
    mockMvc.perform(get("/api/estatisticas/periodo?dataInicio=2024-01-01&dataFim=2024-01-31"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve retornar dashboard completo")
  void deveRetornarDashboard() throws Exception {
    Mockito.when(estatisticasService.obterDashboard()).thenReturn(Collections.emptyMap());
    mockMvc.perform(get("/api/estatisticas/dashboard")).andExpect(status().isOk());
  }
}
