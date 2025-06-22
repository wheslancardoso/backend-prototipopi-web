package com.teatro.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.teatro.dto.LoginRequest;
import com.teatro.dto.LoginResponse;
import com.teatro.dto.UsuarioDTO;
import com.teatro.exception.AutenticacaoException;
import com.teatro.exception.UsuarioJaExisteException;
import com.teatro.exception.UsuarioNaoEncontradoException;
import com.teatro.model.Usuario;
import com.teatro.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller para operações relacionadas a usuários
 * 
 * Endpoints: - POST /api/usuarios/login - Autenticação - POST /api/usuarios/cadastro - Cadastro de
 * usuário - GET /api/usuarios - Listar usuários (admin) - GET /api/usuarios/{id} - Buscar usuário
 * por ID - PUT /api/usuarios/{id} - Atualizar usuário - DELETE /api/usuarios/{id} - Remover usuário
 * (admin) - POST /api/usuarios/{id}/alterar-senha - Alterar senha - POST
 * /api/usuarios/recuperar-senha - Recuperar senha
 */
@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*") // Configurar CORS adequadamente em produção
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários e autenticação")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;

  /**
   * Autenticação de usuário
   * 
   * @param loginRequest Dados de login
   * @return Token JWT e dados do usuário
   */
  @PostMapping("/login")
  @Operation(summary = "Autenticar usuário",
      description = "Realiza autenticação do usuário usando CPF/email e senha")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
          content = @Content(schema = @Schema(implementation = LoginResponse.class))),
      @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")})
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      Usuario usuario = usuarioService.autenticarUsuario(loginRequest.getIdentificador(),
          loginRequest.getSenha());

      // TODO: Gerar token JWT
      String token = "jwt-token-placeholder"; // Implementar JWT

      LoginResponse response =
          new LoginResponse(token, converterParaDTO(usuario), usuario.getTipoUsuario().name());

      return ResponseEntity.ok(response);
    } catch (AutenticacaoException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  /**
   * Cadastro de novo usuário
   * 
   * @param usuarioDTO Dados do usuário
   * @return Usuário cadastrado
   */
  @PostMapping("/cadastro")
  @Operation(summary = "Cadastrar usuário", description = "Cria um novo usuário no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
          content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
      @ApiResponse(responseCode = "409", description = "CPF ou email já cadastrado"),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")})
  public ResponseEntity<UsuarioDTO> cadastrar(@Valid @RequestBody UsuarioDTO usuarioDTO) {
    try {
      Usuario usuario = converterParaModel(usuarioDTO);
      Usuario usuarioSalvo = usuarioService.cadastrarUsuario(usuario);
      return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(usuarioSalvo));
    } catch (UsuarioJaExisteException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  /**
   * Lista todos os usuários ativos
   * 
   * @return Lista de usuários ativos
   */
  @GetMapping
  @Operation(summary = "Listar usuários",
      description = "Retorna lista de todos os usuários ativos no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
          content = @Content(schema = @Schema(implementation = UsuarioDTO.class)))})
  public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
    List<Usuario> usuarios = usuarioService.listarUsuariosAtivos();
    List<UsuarioDTO> usuariosDTO =
        usuarios.stream().map(this::converterParaDTO).collect(Collectors.toList());
    return ResponseEntity.ok(usuariosDTO);
  }

  /**
   * Busca usuário por ID
   * 
   * @param id ID do usuário
   * @return Usuário encontrado
   */
  @GetMapping("/{id}")
  @Operation(summary = "Buscar usuário por ID",
      description = "Retorna dados de um usuário específico pelo ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuário encontrado",
          content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
      @ApiResponse(responseCode = "404", description = "Usuário não encontrado")})
  public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
    try {
      Usuario usuario = usuarioService.buscarPorId(id);
      return ResponseEntity.ok(converterParaDTO(usuario));
    } catch (UsuarioNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Atualiza dados de um usuário
   * 
   * @param id ID do usuário
   * @param usuarioDTO Dados atualizados
   * @return Usuário atualizado
   */
  @PutMapping("/{id}")
  @Operation(summary = "Atualizar usuário", description = "Atualiza dados de um usuário existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
          content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
      @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
      @ApiResponse(responseCode = "409", description = "CPF ou email já cadastrado"),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")})
  public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id,
      @Valid @RequestBody UsuarioDTO usuarioDTO) {
    try {
      Usuario usuario = converterParaModel(usuarioDTO);
      Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuario);
      return ResponseEntity.ok(converterParaDTO(usuarioAtualizado));
    } catch (UsuarioNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    } catch (UsuarioJaExisteException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  /**
   * Remove um usuário (apenas admin)
   * 
   * @param id ID do usuário
   * @return Status da operação
   */
  @DeleteMapping("/{id}")
  @Operation(summary = "Remover usuário",
      description = "Desativa um usuário no sistema (soft delete)")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
          @ApiResponse(responseCode = "404", description = "Usuário não encontrado")})
  public ResponseEntity<Void> remover(@PathVariable Long id) {
    try {
      // Desativa o usuário em vez de remover
      usuarioService.alterarStatusUsuario(id, false);
      return ResponseEntity.noContent().build();
    } catch (UsuarioNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Altera senha de um usuário
   * 
   * @param id ID do usuário
   * @param request Dados da alteração de senha
   * @return Status da operação
   */
  @PostMapping("/{id}/alterar-senha")
  @Operation(summary = "Alterar senha", description = "Altera a senha de um usuário")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
          @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
          @ApiResponse(responseCode = "401", description = "Senha atual incorreta")})
  public ResponseEntity<Void> alterarSenha(@PathVariable Long id,
      @RequestBody AlterarSenhaRequest request) {
    try {
      usuarioService.alterarSenha(id, request.getSenhaAtual(), request.getNovaSenha());
      return ResponseEntity.ok().build();
    } catch (UsuarioNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    } catch (AutenticacaoException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  /**
   * Recupera senha de um usuário
   * 
   * @param request Dados da recuperação de senha
   * @return Status da operação
   */
  @PostMapping("/recuperar-senha")
  @Operation(summary = "Recuperar senha",
      description = "Recupera senha de um usuário usando CPF e email")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "200", description = "Senha recuperada com sucesso"),
          @ApiResponse(responseCode = "404", description = "Usuário não encontrado")})
  public ResponseEntity<Void> recuperarSenha(@RequestBody RecuperarSenhaRequest request) {
    try {
      usuarioService.recuperarSenha(request.getCpf(), request.getEmail(), request.getNovaSenha());
      return ResponseEntity.ok().build();
    } catch (UsuarioNaoEncontradoException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Converte Usuario para UsuarioDTO
   */
  private UsuarioDTO converterParaDTO(Usuario usuario) {
    return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getCpf(),
        usuario.getEndereco(), usuario.getTelefone(), usuario.getEmail(),
        usuario.getTipoUsuario().name(), null // Não incluir senha no DTO
    );
  }

  /**
   * Converte UsuarioDTO para Usuario
   */
  private Usuario converterParaModel(UsuarioDTO usuarioDTO) {
    Usuario usuario = new Usuario();
    usuario.setId(usuarioDTO.getId());
    usuario.setNome(usuarioDTO.getNome());
    usuario.setCpf(usuarioDTO.getCpf());
    usuario.setEndereco(usuarioDTO.getEndereco());
    usuario.setTelefone(usuarioDTO.getTelefone());
    usuario.setEmail(usuarioDTO.getEmail());
    usuario.setSenha(usuarioDTO.getSenha());

    // Converte String para enum TipoUsuario
    if (usuarioDTO.getTipoUsuario() != null) {
      usuario.setTipoUsuario(Usuario.TipoUsuario.valueOf(usuarioDTO.getTipoUsuario()));
    }

    return usuario;
  }

  /**
   * DTO interno para alteração de senha
   */
  public static class AlterarSenhaRequest {
    private String senhaAtual;
    private String novaSenha;

    // Getters e Setters
    public String getSenhaAtual() {
      return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
      this.senhaAtual = senhaAtual;
    }

    public String getNovaSenha() {
      return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
      this.novaSenha = novaSenha;
    }
  }

  /**
   * DTO interno para recuperação de senha
   */
  public static class RecuperarSenhaRequest {
    private String cpf;
    private String email;
    private String novaSenha;

    // Getters e Setters
    public String getCpf() {
      return cpf;
    }

    public void setCpf(String cpf) {
      this.cpf = cpf;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getNovaSenha() {
      return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
      this.novaSenha = novaSenha;
    }
  }
}
