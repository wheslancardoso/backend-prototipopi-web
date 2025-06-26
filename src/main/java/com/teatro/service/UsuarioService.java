package com.teatro.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teatro.dto.LoginRequest;
import com.teatro.dto.LoginResponse;
import com.teatro.dto.UsuarioDTO;
import com.teatro.exception.AutenticacaoException;
import com.teatro.exception.UsuarioJaExisteException;
import com.teatro.exception.UsuarioNaoEncontradoException;
import com.teatro.model.Usuario;
import com.teatro.model.Usuario.TipoUsuario;
import com.teatro.repository.UsuarioRepository;

/**
 * Service para operações de negócio relacionadas a usuários
 * 
 * Responsabilidades: - Autenticação de usuários - Cadastro e validação de dados - Gerenciamento de
 * perfis (ADMIN/COMUM) - Sistema de fidelidade - Validações de negócio
 */
@Service
@Transactional
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * Autentica um usuário e retorna resposta com token
   * 
   * @param loginRequest Dados de login
   * @return LoginResponse com token e informações do usuário
   * @throws AutenticacaoException se credenciais inválidas
   */
  public LoginResponse autenticarUsuario(LoginRequest loginRequest) {
    Usuario usuario = autenticarUsuario(loginRequest.getIdentificador(), loginRequest.getSenha());

    // TODO: Implementar geração de JWT token
    String token = "jwt-token-placeholder";
    String refreshToken = "refresh-token-placeholder";
    LocalDateTime dataExpiracao = LocalDateTime.now().plusHours(24);

    return new LoginResponse(token, refreshToken, usuario.getId(), usuario.getNome(),
        usuario.getEmail(), usuario.getTipoUsuario(), usuario.getTotalPontosFidelidade(),
        calcularNivelFidelidade(usuario.getTotalPontosFidelidade()), dataExpiracao);
  }

  /**
   * Autentica um usuário por CPF ou email
   * 
   * @param identificador CPF ou email do usuário
   * @param senha Senha em texto plano
   * @return Usuário autenticado
   * @throws AutenticacaoException se credenciais inválidas
   */
  public Usuario autenticarUsuario(String identificador, String senha) {
    // Busca usuário por CPF ou email
    Optional<Usuario> usuarioOpt = usuarioRepository.findByCpfOrEmail(identificador);

    if (usuarioOpt.isEmpty()) {
      throw new AutenticacaoException("CPF/Email ou senha incorretos");
    }

    Usuario usuario = usuarioOpt.get();

    // Verifica se a senha está correta
    if (!passwordEncoder.matches(senha, usuario.getSenha())) {
      throw new AutenticacaoException("CPF/Email ou senha incorretos");
    }

    // Verifica se o usuário está ativo
    if (!usuario.isAtivo()) {
      throw new AutenticacaoException("Usuário inativo. Entre em contato com o administrador.");
    }

    return usuario;
  }

  /**
   * Cadastra um novo usuário
   * 
   * @param usuarioDTO Dados do usuário
   * @return UsuarioDTO do usuário cadastrado
   * @throws UsuarioJaExisteException se CPF ou email já existem
   */
  public UsuarioDTO cadastrarUsuario(UsuarioDTO usuarioDTO) {
    Usuario usuario = usuarioDTO.toEntity();

    // Validações de negócio
    validarDadosUsuario(usuario);

    // Verifica se CPF já existe
    if (usuarioRepository.existsByCpf(usuario.getCpf())) {
      throw new UsuarioJaExisteException("CPF já cadastrado: " + usuario.getCpf());
    }

    // Verifica se email já existe
    if (usuarioRepository.existsByEmail(usuario.getEmail())) {
      throw new UsuarioJaExisteException("Email já cadastrado: " + usuario.getEmail());
    }

    // Define tipo de usuário padrão como COMUM
    if (usuario.getTipoUsuario() == null) {
      usuario.setTipoUsuario(TipoUsuario.COMUM);
    }

    // Define usuário como ativo
    usuario.setAtivo(true);

    // Criptografa a senha
    usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

    Usuario usuarioSalvo = usuarioRepository.save(usuario);
    return new UsuarioDTO(usuarioSalvo);
  }

  /**
   * Busca usuário por ID
   * 
   * @param id ID do usuário
   * @return UsuarioDTO do usuário encontrado
   * @throws UsuarioNaoEncontradoException se usuário não existe
   */
  @Transactional(readOnly = true)
  public UsuarioDTO buscarPorId(Long id) {
    Usuario usuario = usuarioRepository.findById(id).orElseThrow(
        () -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID: " + id));
    return new UsuarioDTO(usuario);
  }

  /**
   * Busca usuário por CPF
   * 
   * @param cpf CPF do usuário
   * @return UsuarioDTO do usuário encontrado
   * @throws UsuarioNaoEncontradoException se usuário não existe
   */
  @Transactional(readOnly = true)
  public UsuarioDTO buscarPorCpf(String cpf) {
    Usuario usuario = usuarioRepository.findByCpf(cpf).orElseThrow(
        () -> new UsuarioNaoEncontradoException("Usuário não encontrado com CPF: " + cpf));
    return new UsuarioDTO(usuario);
  }

  /**
   * Busca usuário por email
   * 
   * @param email Email do usuário
   * @return Usuario encontrado
   * @throws UsuarioNaoEncontradoException se usuário não existe
   */
  @Transactional(readOnly = true)
  public Usuario buscarPorEmail(String email) {
    return usuarioRepository.findByEmail(email).orElseThrow(
        () -> new UsuarioNaoEncontradoException("Usuário não encontrado com email: " + email));
  }

  /**
   * Lista todos os usuários ativos
   * 
   * @return Lista de UsuarioDTO dos usuários ativos
   */
  @Transactional(readOnly = true)
  public List<UsuarioDTO> listarUsuariosAtivos() {
    return usuarioRepository.findByAtivoTrue().stream().map(UsuarioDTO::new)
        .collect(Collectors.toList());
  }

  /**
   * Atualiza dados de um usuário
   * 
   * @param id ID do usuário
   * @param usuarioDTO Dados atualizados
   * @return UsuarioDTO do usuário atualizado
   * @throws UsuarioNaoEncontradoException se usuário não existe
   */
  public UsuarioDTO atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
    Usuario usuarioExistente = usuarioRepository.findById(id).orElseThrow(
        () -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID: " + id));

    Usuario usuario = usuarioDTO.toEntity();

    // Validações de negócio
    validarDadosUsuario(usuario);

    // Verifica se CPF foi alterado e se já existe
    if (!usuarioExistente.getCpf().equals(usuario.getCpf())
        && usuarioRepository.existsByCpf(usuario.getCpf())) {
      throw new UsuarioJaExisteException("CPF já cadastrado: " + usuario.getCpf());
    }

    // Verifica se email foi alterado e se já existe
    if (!usuarioExistente.getEmail().equals(usuario.getEmail())
        && usuarioRepository.existsByEmail(usuario.getEmail())) {
      throw new UsuarioJaExisteException("Email já cadastrado: " + usuario.getEmail());
    }

    // Atualiza os dados
    usuarioExistente.setNome(usuario.getNome());
    usuarioExistente.setCpf(usuario.getCpf());
    usuarioExistente.setEmail(usuario.getEmail());
    usuarioExistente.setEndereco(usuario.getEndereco());
    usuarioExistente.setTelefone(usuario.getTelefone());
    usuarioExistente.setTipoUsuario(usuario.getTipoUsuario());

    // Se a senha foi alterada, criptografa
    if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
      usuarioExistente.setSenha(passwordEncoder.encode(usuario.getSenha()));
    }

    Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
    return new UsuarioDTO(usuarioAtualizado);
  }

  /**
   * Ativa/desativa um usuário
   * 
   * @param id ID do usuário
   * @param ativo Status desejado
   * @return UsuarioDTO do usuário atualizado
   * @throws UsuarioNaoEncontradoException se usuário não existe
   */
  public UsuarioDTO alterarStatusUsuario(Long id, boolean ativo) {
    Usuario usuario = usuarioRepository.findById(id).orElseThrow(
        () -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID: " + id));
    usuario.setAtivo(ativo);
    Usuario usuarioAtualizado = usuarioRepository.save(usuario);
    return new UsuarioDTO(usuarioAtualizado);
  }

  /**
   * Altera a senha de um usuário
   * 
   * @param id ID do usuário
   * @param senhaAtual Senha atual
   * @param novaSenha Nova senha
   * @return true se alteração foi bem-sucedida
   * @throws AutenticacaoException se senha atual está incorreta
   */
  public boolean alterarSenha(Long id, String senhaAtual, String novaSenha) {
    Usuario usuario = usuarioRepository.findById(id).orElseThrow(
        () -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID: " + id));

    // Verifica se a senha atual está correta
    if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
      throw new AutenticacaoException("Senha atual incorreta");
    }

    // Criptografa e salva a nova senha
    usuario.setSenha(passwordEncoder.encode(novaSenha));
    usuarioRepository.save(usuario);

    return true;
  }

  /**
   * Recupera senha de um usuário
   * 
   * @param cpf CPF do usuário
   * @param email Email do usuário
   * @param novaSenha Nova senha
   * @return true se recuperação foi bem-sucedida
   * @throws UsuarioNaoEncontradoException se usuário não existe
   */
  public boolean recuperarSenha(String cpf, String email, String novaSenha) {
    Usuario usuario = usuarioRepository.findByCpf(cpf).orElseThrow(
        () -> new UsuarioNaoEncontradoException("Usuário não encontrado com CPF: " + cpf));

    // Verifica se o email corresponde ao CPF
    if (!usuario.getEmail().equals(email)) {
      throw new UsuarioNaoEncontradoException("Email não corresponde ao CPF informado");
    }

    // Criptografa e salva a nova senha
    usuario.setSenha(passwordEncoder.encode(novaSenha));
    usuarioRepository.save(usuario);

    return true;
  }

  /**
   * Adiciona pontos de fidelidade a um usuário
   * 
   * @param usuarioId ID do usuário
   * @param pontos Pontos a serem adicionados
   * @return UsuarioDTO do usuário atualizado
   */
  public UsuarioDTO adicionarPontosFidelidade(Long usuarioId, int pontos) {
    Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(
        () -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID: " + usuarioId));

    // TODO: Implementar lógica de pontos de fidelidade
    // Por enquanto, apenas retorna o usuário
    return new UsuarioDTO(usuario);
  }

  /**
   * Calcula o nível de fidelidade baseado nos pontos
   * 
   * @param pontos Total de pontos do usuário
   * @return Nível de fidelidade
   */
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

  /**
   * Valida dados de um usuário
   * 
   * @param usuario Usuário a ser validado
   * @throws IllegalArgumentException se dados inválidos
   */
  private void validarDadosUsuario(Usuario usuario) {
    if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
      throw new IllegalArgumentException("Nome é obrigatório");
    }

    if (usuario.getCpf() == null || usuario.getCpf().trim().isEmpty()) {
      throw new IllegalArgumentException("CPF é obrigatório");
    }

    if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
      throw new IllegalArgumentException("Email é obrigatório");
    }

    if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
      throw new IllegalArgumentException("Senha é obrigatória");
    }

    // Validação básica de CPF (formato)
    if (!usuario.getCpf().matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
      throw new IllegalArgumentException("CPF deve estar no formato 000.000.000-00");
    }

    // Validação básica de email
    if (!usuario.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
      throw new IllegalArgumentException("Email deve ter formato válido");
    }
  }

  /**
   * Verifica se um usuário é administrador
   * 
   * @param usuario Usuário a ser verificado
   * @return true se for administrador
   */
  public boolean isAdministrador(Usuario usuario) {
    return usuario != null && TipoUsuario.ADMIN.equals(usuario.getTipoUsuario());
  }

  /**
   * Conta usuários ativos
   * 
   * @return Número de usuários ativos
   */
  @Transactional(readOnly = true)
  public long contarUsuariosAtivos() {
    return usuarioRepository.countByAtivoTrue();
  }

  /**
   * Remove um usuário (soft delete)
   * 
   * @param id ID do usuário
   * @throws UsuarioNaoEncontradoException se usuário não existe
   */
  public void removerUsuario(Long id) {
    Usuario usuario = usuarioRepository.findById(id).orElseThrow(
        () -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID: " + id));
    usuario.setAtivo(false);
    usuarioRepository.save(usuario);
  }

  /**
   * Cadastra um novo usuário (método simplificado para compatibilidade)
   * 
   * @param usuarioDTO Dados do usuário
   * @return Usuario cadastrado
   * @throws UsuarioJaExisteException se CPF ou email já existem
   */
  public Usuario cadastrar(UsuarioDTO usuarioDTO) {
    UsuarioDTO usuarioSalvo = cadastrarUsuario(usuarioDTO);
    return buscarPorEmail(usuarioSalvo.getEmail());
  }
}
