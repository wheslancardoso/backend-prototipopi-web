package com.teatro.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teatro.config.TestConfig;
import com.teatro.dto.EventoDTO;
import com.teatro.service.EventoService;

@WebMvcTest(EventoController.class)
@Import(TestConfig.class)
class EventoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private EventoService eventoService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("Deve listar eventos com sucesso")
  void deveListarEventos() throws Exception {
    Mockito.when(eventoService.listarEventosAtivos()).thenReturn(Collections.emptyList());
    mockMvc.perform(get("/api/eventos")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Deve buscar evento por ID com sucesso")
  void deveBuscarEventoPorId() throws Exception {
    EventoDTO evento = new EventoDTO();
    Mockito.when(eventoService.buscarPorId(1L)).thenReturn(evento);
    mockMvc.perform(get("/api/eventos/1")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve retornar 404 ao buscar evento inexistente")
  void deveRetornar404AoBuscarEventoInexistente() throws Exception {
    Mockito.when(eventoService.buscarPorId(99L))
        .thenThrow(new com.teatro.exception.EventoNaoEncontradoException("Evento não encontrado"));
    mockMvc.perform(get("/api/eventos/99")).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Deve criar evento com sucesso")
  void deveCriarEvento() throws Exception {
    EventoDTO eventoRequest = new EventoDTO();
    EventoDTO eventoResponse = new EventoDTO();
    Mockito.when(eventoService.cadastrarEvento(any(EventoDTO.class))).thenReturn(eventoResponse);
    mockMvc
        .perform(post("/api/eventos").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(eventoRequest)))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("Deve atualizar evento com sucesso")
  void deveAtualizarEvento() throws Exception {
    EventoDTO eventoRequest = new EventoDTO();
    EventoDTO eventoResponse = new EventoDTO();
    Mockito.when(eventoService.atualizarEvento(eq(1L), any(EventoDTO.class)))
        .thenReturn(eventoResponse);
    mockMvc.perform(put("/api/eventos/1").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(eventoRequest))).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve deletar evento com sucesso")
  void deveDeletarEvento() throws Exception {
    Mockito.doNothing().when(eventoService).removerEvento(1L);
    mockMvc.perform(delete("/api/eventos/1")).andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Deve listar eventos ativos")
  void deveListarEventosAtivos() throws Exception {
    Mockito.when(eventoService.listarEventosAtivos()).thenReturn(Collections.emptyList());
    mockMvc.perform(get("/api/eventos/ativos")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve alterar status do evento")
  void deveAlterarStatusEvento() throws Exception {
    EventoDTO evento = new EventoDTO();
    Mockito.when(eventoService.alterarStatusEvento(1L, true)).thenReturn(evento);
    mockMvc.perform(put("/api/eventos/1/status?ativo=true")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve listar eventos com sessões futuras")
  void deveListarEventosComSessoesFuturas() throws Exception {
    Mockito.when(eventoService.listarEventosComSessoesFuturas())
        .thenReturn(Collections.emptyList());
    mockMvc.perform(get("/api/eventos/com-sessoes-futuras")).andExpect(status().isOk());
  }
}
