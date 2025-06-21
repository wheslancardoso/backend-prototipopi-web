package com.teatro.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teatro.exception.AutenticacaoException;
import com.teatro.exception.UsuarioJaExisteException;
import com.teatro.exception.UsuarioNaoEncontradoException;
import com.teatro.model.Usuario;
import com.teatro.repository.UsuarioRepository;

/**
 * Service para operações de negócio relacionadas a usuários
 * 
 * Responsabilidades: - Autenticação de usuários - Cadastro e validação de dados - Gerenciamento de
 * perfis (ADMIN/COMUM) - Validações de negócio
 */
@Service
@Transactional
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

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
   * @param usuario Dados do usuário
   * @return Usuário cadastrado
   * @throws UsuarioJaExisteException se CPF ou email já existem
   */
  public Usuario cadastrarUsuario(Usuario usuario) {
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
      usuario.setTipoUsuario(Usuario.TipoUsuario.COMUM);
    }

    // Define usuário como ativo
    usuario.setAtivo(true);

    // Criptografa a senha
    usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

    return usuarioRepository.save(usuario);
  }

  /**
   * Busca usuário por ID
   * 
   * @param id ID do usuário
   * @return Usuário encontrado
   * @throws UsuarioNaoEncontradoException se usuário não existe
   */
  @Transactional(readOnly = true)
  public Usuario buscarPorId(Long id) {
    return usuarioRepository.findById(id).orElseThrow(
        () -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID: " + id));
  }

  /**
   * Busca usuário por CPF
   * 
   * @param cpf CPF do usuário
   * @return Usuário encontrado
   * @throws UsuarioNaoEncontradoException se usuário não existe
   */
  @Transactional(readOnly = true)
  public Usuario buscarPorCpf(String cpf) {
    return usuarioRepository.findByCpf(cpf).orElseThrow(
        () -> new UsuarioNaoEncontradoException("Usuário não encontrado com CPF: " + cpf));
  }

  /**
   * Lista todos os usuários ativos
   * 
   * @return Lista de usuários ativos
   */
  @Transactional(readOnly = true)
  public List<Usuario> listarUsuariosAtivos() {
    return usuarioRepository.findByAtivoTrue();
  }

  /**
   * Atualiza dados de um usuário
   * 
   * @param id ID do usuário
   * @param usuario Dados atualizados
   * @return Usuário atualizado
   * @throws UsuarioNaoEncontradoException se usuário não existe
   */
  public Usuario atualizarUsuario(Long id, Usuario usuario) {
    Usuario usuarioExistente = buscarPorId(id);

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

    return usuarioRepository.save(usuarioExistente);
  }

  /**
   * Ativa/desativa um usuário
   * 
   * @param id ID do usuário
   * @param ativo Status desejado
   * @return Usuário atualizado
   * @throws UsuarioNaoEncontradoException se usuário não existe
   */
  public Usuario alterarStatusUsuario(Long id, boolean ativo) {
    Usuario usuario = buscarPorId(id);
    usuario.setAtivo(ativo);
    return usuarioRepository.save(usuario);
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
    Usuario usuario = buscarPorId(id);

    // Verifica se a senha atual está correta
    if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
      throw new AutenticacaoException("Senha atual incorreta");
    }

    // Valida a nova senha
    if (novaSenha == null || novaSenha.trim().isEmpty()) {
      throw new IllegalArgumentException("Nova senha não pode ser vazia");
    }

    if (novaSenha.length() < 6) {
      throw new IllegalArgumentException("Nova senha deve ter pelo menos 6 caracteres");
    }

    // Criptografa e salva a nova senha
    usuario.setSenha(passwordEncoder.encode(novaSenha));
    usuarioRepository.save(usuario);

    return true;
  }

  /**
   * Recupera senha por CPF e email
   * 
   * @param cpf CPF do usuário
   * @param email Email do usuário
   * @param novaSenha Nova senha
   * @return true se recuperação foi bem-sucedida
   * @throws UsuarioNaoEncontradoException se CPF e email não correspondem
   */
  public boolean recuperarSenha(String cpf, String email, String novaSenha) {
    // Busca usuário por CPF
    Optional<Usuario> usuarioOpt = usuarioRepository.findByCpf(cpf);

    if (usuarioOpt.isEmpty()) {
      throw new UsuarioNaoEncontradoException("CPF não encontrado: " + cpf);
    }

    Usuario usuario = usuarioOpt.get();

    // Verifica se o email corresponde
    if (!usuario.getEmail().equals(email)) {
      throw new UsuarioNaoEncontradoException("CPF e email não correspondem");
    }

    // Valida a nova senha
    if (novaSenha == null || novaSenha.trim().isEmpty()) {
      throw new IllegalArgumentException("Nova senha não pode ser vazia");
    }

    if (novaSenha.length() < 6) {
      throw new IllegalArgumentException("Nova senha deve ter pelo menos 6 caracteres");
    }

    // Criptografa e salva a nova senha
    usuario.setSenha(passwordEncoder.encode(novaSenha));
    usuarioRepository.save(usuario);

    return true;
  }

  /**
   * Valida dados de um usuário
   * 
   * @param usuario Usuário a ser validado
   * @throws IllegalArgumentException se dados são inválidos
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

    if (usuario.getSenha().length() < 6) {
      throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
    }

    // Validação básica de email
    if (!usuario.getEmail().contains("@")) {
      throw new IllegalArgumentException("Email inválido");
    }

    // Validação básica de CPF (formato)
    String cpf = usuario.getCpf().replaceAll("[^0-9]", "");
    if (cpf.length() != 11) {
      throw new IllegalArgumentException("CPF deve ter 11 dígitos");
    }
  }

  /**
   * Verifica se um usuário é administrador
   * 
   * @param usuario Usuário a ser verificado
   * @return true se é administrador
   */
  public boolean isAdministrador(Usuario usuario) {
    return Usuario.TipoUsuario.ADMIN.equals(usuario.getTipoUsuario());
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
}
