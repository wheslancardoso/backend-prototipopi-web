package com.teatro.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teatro.model.Usuario;
import com.teatro.model.Usuario.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para transferência de dados de usuário
 */
public class UsuarioDTO {

  private Long id;

  @NotBlank(message = "Nome é obrigatório")
  @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
  private String nome;

  @NotBlank(message = "CPF é obrigatório")
  @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
      message = "CPF deve estar no formato 000.000.000-00")
  private String cpf;

  @NotBlank(message = "Endereço é obrigatório")
  @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
  private String endereco;

  @NotBlank(message = "Telefone é obrigatório")
  @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}",
      message = "Telefone deve estar no formato (00) 00000-0000")
  private String telefone;

  @NotBlank(message = "Email é obrigatório")
  @Email(message = "Email deve ser válido")
  @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
  private String email;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @NotBlank(message = "Senha é obrigatória")
  @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
  private String senha;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private TipoUsuario tipoUsuario;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Integer totalPontosFidelidade;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String nivelFidelidade;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Boolean ativo;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dataCadastro;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dataAtualizacao;

  // Construtores
  public UsuarioDTO() {}

  public UsuarioDTO(Usuario usuario) {
    this.id = usuario.getId();
    this.nome = usuario.getNome();
    this.cpf = usuario.getCpf();
    this.endereco = usuario.getEndereco();
    this.telefone = usuario.getTelefone();
    this.email = usuario.getEmail();
    this.tipoUsuario = usuario.getTipoUsuario();
    this.totalPontosFidelidade = usuario.getTotalPontosFidelidade();
    this.nivelFidelidade = calcularNivelFidelidade(usuario.getTotalPontosFidelidade());
    this.ativo = usuario.getAtivo();
    this.dataCadastro = usuario.getDataCadastro();
    this.dataAtualizacao = usuario.getDataAtualizacao();
  }

  // Método estático para converter entidade para DTO
  public static UsuarioDTO fromEntity(Usuario usuario) {
    return new UsuarioDTO(usuario);
  }

  // Método para converter DTO para entidade (sem senha)
  public Usuario toEntity() {
    Usuario usuario = new Usuario();
    usuario.setId(this.id);
    usuario.setNome(this.nome);
    usuario.setCpf(this.cpf);
    usuario.setEndereco(this.endereco);
    usuario.setTelefone(this.telefone);
    usuario.setEmail(this.email);
    usuario.setTipoUsuario(this.tipoUsuario != null ? this.tipoUsuario : TipoUsuario.COMUM);
    usuario.setAtivo(this.ativo != null ? this.ativo : true);
    return usuario;
  }

  // Método para calcular nível de fidelidade baseado nos pontos
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

  // Getters e Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getEndereco() {
    return endereco;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public TipoUsuario getTipoUsuario() {
    return tipoUsuario;
  }

  public void setTipoUsuario(TipoUsuario tipoUsuario) {
    this.tipoUsuario = tipoUsuario;
  }

  public Integer getTotalPontosFidelidade() {
    return totalPontosFidelidade;
  }

  public void setTotalPontosFidelidade(Integer totalPontosFidelidade) {
    this.totalPontosFidelidade = totalPontosFidelidade;
  }

  public String getNivelFidelidade() {
    return nivelFidelidade;
  }

  public void setNivelFidelidade(String nivelFidelidade) {
    this.nivelFidelidade = nivelFidelidade;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
  }

  public LocalDateTime getDataCadastro() {
    return dataCadastro;
  }

  public void setDataCadastro(LocalDateTime dataCadastro) {
    this.dataCadastro = dataCadastro;
  }

  public LocalDateTime getDataAtualizacao() {
    return dataAtualizacao;
  }

  public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
    this.dataAtualizacao = dataAtualizacao;
  }
}
