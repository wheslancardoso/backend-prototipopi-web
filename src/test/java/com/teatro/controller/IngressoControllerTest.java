package com.teatro.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teatro.dto.IngressoDTO;
import com.teatro.model.Ingresso;
import com.teatro.service.IngressoService;

@WebMvcTest(IngressoController.class)
class IngressoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private IngressoService ingressoService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("Deve listar ingressos com sucesso")
  void deveListarIngressos() throws Exception {
    Mockito.when(ingressoService.listarTodosIngressos()).thenReturn(Collections.emptyList());
    mockMvc.perform(get("/api/ingressos")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Deve retornar 404 ao buscar ingresso inexistente")
  void deveRetornar404AoBuscarIngressoInexistente() throws Exception {
    Mockito.when(ingressoService.buscarPorId(99L))
        .thenThrow(new com.teatro.exception.IngressoNaoEncontradoException("Não encontrado"));
    mockMvc.perform(get("/api/ingressos/99")).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Deve retornar erro 409 ao tentar comprar ingresso com poltrona ocupada")
  void deveRetornarErroPoltronaOcupada() throws Exception {
    IngressoDTO request = new IngressoDTO();
    Mockito.when(ingressoService.comprarIngresso(any(Ingresso.class)))
        .thenThrow(new com.teatro.exception.PoltronaOcupadaException("Poltrona ocupada"));
    mockMvc.perform(post("/api/ingressos").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))).andExpect(status().isConflict());
  }

  @Test
  @DisplayName("Deve validar ingresso por código com sucesso")
  void deveValidarIngressoPorCodigo() throws Exception {
    Ingresso ingresso = new Ingresso();
    Mockito.when(ingressoService.validarPorCodigo(eq("ABC123"))).thenReturn(ingresso);
    mockMvc.perform(get("/api/ingressos/validar/ABC123")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve cancelar ingresso com sucesso")
  void deveCancelarIngresso() throws Exception {
    Mockito.doNothing().when(ingressoService).cancelarIngresso(1L);
    mockMvc.perform(delete("/api/ingressos/1")).andExpect(status().isNoContent());
  }
}
