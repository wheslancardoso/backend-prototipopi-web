package com.teatro.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teatro.config.TestSecurityConfig;
import com.teatro.dto.LoginRequest;
import com.teatro.dto.UsuarioDTO;
import com.teatro.model.Usuario;
import com.teatro.model.Usuario.TipoUsuario;
import com.teatro.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
@Import(TestSecurityConfig.class)
@ContextConfiguration(classes = {UsuarioController.class, TestSecurityConfig.class})
class UsuarioControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UsuarioService usuarioService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("Deve fazer login com sucesso")
  void deveFazerLogin() throws Exception {
    // Arrange
    LoginRequest request = new LoginRequest();
    request.setIdentificador("joao@email.com");
    request.setSenha("senha123");

    Usuario usuario = new Usuario();
    usuario.setId(1L);
    usuario.setNome("João Silva");
    usuario.setEmail("joao@email.com");
    usuario.setTipoUsuario(TipoUsuario.COMUM);

    Mockito.when(usuarioService.autenticarUsuario(anyString(), anyString())).thenReturn(usuario);

    // Act & Assert
    mockMvc
        .perform(post("/api/usuarios/login").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Deve cadastrar usuário com sucesso")
  void deveCadastrarUsuario() throws Exception {
    // Arrange
    UsuarioDTO usuarioDTO = new UsuarioDTO();
    usuarioDTO.setNome("Maria Silva");
    usuarioDTO.setEmail("maria@email.com");
    usuarioDTO.setCpf("123.456.789-00");
    usuarioDTO.setSenha("senha123");

    Usuario usuario = new Usuario();
    usuario.setId(2L);
    usuario.setNome("Maria Silva");
    usuario.setEmail("maria@email.com");

    Mockito.when(usuarioService.cadastrarUsuario(any(UsuarioDTO.class)))
        .thenReturn(new UsuarioDTO(usuario));

    // Act & Assert
    mockMvc
        .perform(post("/api/usuarios/cadastro").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuarioDTO)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Deve listar todos os usuários")
  void deveListarUsuarios() throws Exception {
    // Arrange
    Usuario usuario = new Usuario();
    usuario.setId(1L);
    usuario.setNome("João Silva");
    usuario.setEmail("joao@email.com");

    Mockito.when(usuarioService.listarUsuariosAtivos())
        .thenReturn(Collections.singletonList(new UsuarioDTO(usuario)));

    // Act & Assert
    mockMvc.perform(get("/api/usuarios")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Deve buscar usuário por ID")
  void deveBuscarUsuarioPorId() throws Exception {
    // Arrange
    Usuario usuario = new Usuario();
    usuario.setId(1L);
    usuario.setNome("João Silva");
    usuario.setEmail("joao@email.com");

    Mockito.when(usuarioService.buscarPorId(eq(1L))).thenReturn(new UsuarioDTO(usuario));

    // Act & Assert
    mockMvc.perform(get("/api/usuarios/1")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Deve atualizar usuário")
  void deveAtualizarUsuario() throws Exception {
    // Arrange
    UsuarioDTO usuarioDTO = new UsuarioDTO();
    usuarioDTO.setNome("João Silva Atualizado");
    usuarioDTO.setEmail("joao@email.com");

    Usuario usuario = new Usuario();
    usuario.setId(1L);
    usuario.setNome("João Silva Atualizado");
    usuario.setEmail("joao@email.com");

    Mockito.when(usuarioService.atualizarUsuario(eq(1L), any(UsuarioDTO.class)))
        .thenReturn(new UsuarioDTO(usuario));

    // Act & Assert
    mockMvc
        .perform(put("/api/usuarios/1").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuarioDTO)))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Deve remover usuário")
  void deveRemoverUsuario() throws Exception {
    // Arrange
    Mockito.doNothing().when(usuarioService).removerUsuario(eq(1L));

    // Act & Assert
    mockMvc.perform(delete("/api/usuarios/1")).andExpect(status().isNoContent());
  }
}
