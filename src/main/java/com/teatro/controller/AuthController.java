package com.teatro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.teatro.dto.LoginRequest;
import com.teatro.dto.LoginResponse;
import com.teatro.dto.UsuarioDTO;
import com.teatro.model.Usuario;
import com.teatro.security.JwtTokenProvider;
import com.teatro.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;
  private final UsuarioService usuarioService;

  public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider,
      UsuarioService usuarioService) {
    this.authenticationManager = authenticationManager;
    this.tokenProvider = tokenProvider;
    this.usuarioService = usuarioService;
  }

  @PostMapping("/login")
  @Operation(summary = "Fazer login", description = "Autentica um usuário e retorna um token JWT")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.generateToken(authentication);

    Usuario usuario = usuarioService.buscarPorEmail(loginRequest.getEmail());

    LoginResponse response = new LoginResponse();
    response.setToken(jwt);
    response.setTipo("Bearer");
    response.setUsuarioId(usuario.getId());
    response.setUsuarioNome(usuario.getNome());
    response.setUsuarioEmail(usuario.getEmail());
    response.setTipoUsuario(usuario.getTipoUsuario());
    response.setPontosFidelidade(usuario.getTotalPontosFidelidade());
    response.setNivelFidelidade(calcularNivelFidelidade(usuario.getTotalPontosFidelidade()));

    return ResponseEntity.ok(response);
  }

  @PostMapping("/cadastro")
  @Operation(summary = "Cadastrar usuário", description = "Cadastra um novo usuário no sistema")
  public ResponseEntity<UsuarioDTO> cadastrar(@Valid @RequestBody UsuarioDTO usuarioDTO) {
    Usuario usuario = usuarioService.cadastrar(usuarioDTO);
    return ResponseEntity.ok(UsuarioDTO.fromEntity(usuario));
  }

  @GetMapping("/me")
  @Operation(summary = "Obter usuário atual",
      description = "Retorna informações do usuário autenticado")
  public ResponseEntity<UsuarioDTO> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    Usuario usuario = usuarioService.buscarPorEmail(email);
    return ResponseEntity.ok(UsuarioDTO.fromEntity(usuario));
  }

  private String calcularNivelFidelidade(int pontos) {
    if (pontos >= 1000)
      return "DIAMANTE";
    if (pontos >= 500)
      return "OURO";
    if (pontos >= 200)
      return "PRATA";
    if (pontos >= 50)
      return "BRONZE";
    return "INICIANTE";
  }
}
