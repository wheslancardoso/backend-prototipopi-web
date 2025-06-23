package com.teatro.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teatro.config.TestSecurityConfig;
import com.teatro.dto.SessaoDTO;
import com.teatro.model.Sessao.TipoSessao;
import com.teatro.service.SessaoService;

@WebMvcTest(SessaoController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class SessaoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SessaoService sessaoService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("Deve listar sessões ativas com sucesso")
  void deveListarSessoes() throws Exception {
    Mockito.when(sessaoService.listarSessoesAtivas()).thenReturn(Collections.emptyList());
    mockMvc.perform(get("/api/sessoes")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Deve buscar sessão por ID com sucesso")
  void deveBuscarSessaoPorId() throws Exception {
    SessaoDTO sessao = new SessaoDTO();
    Mockito.when(sessaoService.buscarPorId(1L)).thenReturn(sessao);
    mockMvc.perform(get("/api/sessoes/1")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve retornar 404 ao buscar sessão inexistente")
  void deveRetornar404AoBuscarSessaoInexistente() throws Exception {
    Mockito.when(sessaoService.buscarPorId(99L))
        .thenThrow(new com.teatro.exception.SessaoNaoEncontradaException("Sessão não encontrada"));
    mockMvc.perform(get("/api/sessoes/99")).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Deve criar sessão com sucesso")
  void deveCriarSessao() throws Exception {
    SessaoDTO sessaoRequest = new SessaoDTO();
    sessaoRequest.setNome("Sessão Teste");
    sessaoRequest.setTipoSessao(TipoSessao.MANHA);
    sessaoRequest.setDataSessao(LocalDate.of(2024, 1, 15));
    sessaoRequest.setHorario(java.time.LocalTime.of(14, 30));
    sessaoRequest.setEventoId(1L);
    
    SessaoDTO sessaoResponse = new SessaoDTO();
    Mockito.when(sessaoService.cadastrarSessao(any(SessaoDTO.class))).thenReturn(sessaoResponse);

    mockMvc
        .perform(post("/api/sessoes").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(sessaoRequest)))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("Deve atualizar sessão com sucesso")
  void deveAtualizarSessao() throws Exception {
    SessaoDTO sessaoRequest = new SessaoDTO();
    sessaoRequest.setNome("Sessão Atualizada");
    sessaoRequest.setTipoSessao(TipoSessao.TARDE);
    sessaoRequest.setDataSessao(LocalDate.of(2024, 1, 16));
    sessaoRequest.setHorario(java.time.LocalTime.of(16, 0));
    sessaoRequest.setEventoId(1L);
    
    SessaoDTO sessaoResponse = new SessaoDTO();
    Mockito.when(sessaoService.atualizarSessao(eq(1L), any(SessaoDTO.class)))
        .thenReturn(sessaoResponse);

    mockMvc.perform(put("/api/sessoes/1").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(sessaoRequest))).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve deletar sessão com sucesso")
  void deveDeletarSessao() throws Exception {
    Mockito.doNothing().when(sessaoService).removerSessao(1L);
    mockMvc.perform(delete("/api/sessoes/1")).andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Deve listar sessões por tipo")
  void deveListarSessoesPorTipo() throws Exception {
    Mockito.when(sessaoService.listarSessoesPorTipo(TipoSessao.MANHA))
        .thenReturn(Collections.emptyList());
    mockMvc.perform(get("/api/sessoes/tipo/MANHA")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve listar sessões por data")
  void deveListarSessoesPorData() throws Exception {
    LocalDate data = LocalDate.of(2024, 1, 15);
    Mockito.when(sessaoService.listarSessoesPorData(data)).thenReturn(Collections.emptyList());
    mockMvc.perform(get("/api/sessoes/data?data=2024-01-15")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve listar sessões futuras")
  void deveListarSessoesFuturas() throws Exception {
    Mockito.when(sessaoService.listarSessoesFuturas()).thenReturn(Collections.emptyList());
    mockMvc.perform(get("/api/sessoes/futuras")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve listar sessões disponíveis para compra")
  void deveListarSessoesDisponiveisParaCompra() throws Exception {
    Mockito.when(sessaoService.listarSessoesDisponiveisParaCompra())
        .thenReturn(Collections.emptyList());
    mockMvc.perform(get("/api/sessoes/disponiveis")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve listar sessões disponíveis por evento")
  void deveListarSessoesDisponiveisPorEvento() throws Exception {
    Mockito.when(sessaoService.listarSessoesDisponiveisParaCompraPorEvento(1L))
        .thenReturn(Collections.emptyList());
    mockMvc.perform(get("/api/sessoes/disponiveis/evento/1")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Deve gerar horários dinâmicos")
  void deveGerarHorariosDinamicos() throws Exception {
    LocalDate data = LocalDate.of(2024, 1, 15);
    List<String> horarios = Collections.emptyList();
    Mockito.when(sessaoService.listarHorariosDisponiveisPorData(data)).thenReturn(horarios);
    mockMvc.perform(get("/api/sessoes/horarios-disponiveis?data=2024-01-15"))
        .andExpect(status().isOk());
  }
}
