package com.teatro.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.teatro.model.Usuario.TipoUsuario;

/**
 * DTO para resposta de login
 */
public class LoginResponse {

  private String token;
  private String refreshToken;
  private Long usuarioId;
  private String usuarioNome;
  private String usuarioEmail;
  private TipoUsuario tipoUsuario;
  private Integer pontosFidelidade;
  private String nivelFidelidade;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dataExpiracao;

  // Construtores
  public LoginResponse() {}

  public LoginResponse(String token, String refreshToken, Long usuarioId, String usuarioNome,
      String usuarioEmail, TipoUsuario tipoUsuario, Integer pontosFidelidade,
      String nivelFidelidade, LocalDateTime dataExpiracao) {
    this.token = token;
    this.refreshToken = refreshToken;
    this.usuarioId = usuarioId;
    this.usuarioNome = usuarioNome;
    this.usuarioEmail = usuarioEmail;
    this.tipoUsuario = tipoUsuario;
    this.pontosFidelidade = pontosFidelidade;
    this.nivelFidelidade = nivelFidelidade;
    this.dataExpiracao = dataExpiracao;
  }

  // Getters e Setters
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }

  public String getUsuarioNome() {
    return usuarioNome;
  }

  public void setUsuarioNome(String usuarioNome) {
    this.usuarioNome = usuarioNome;
  }

  public String getUsuarioEmail() {
    return usuarioEmail;
  }

  public void setUsuarioEmail(String usuarioEmail) {
    this.usuarioEmail = usuarioEmail;
  }

  public TipoUsuario getTipoUsuario() {
    return tipoUsuario;
  }

  public void setTipoUsuario(TipoUsuario tipoUsuario) {
    this.tipoUsuario = tipoUsuario;
  }

  public Integer getPontosFidelidade() {
    return pontosFidelidade;
  }

  public void setPontosFidelidade(Integer pontosFidelidade) {
    this.pontosFidelidade = pontosFidelidade;
  }

  public String getNivelFidelidade() {
    return nivelFidelidade;
  }

  public void setNivelFidelidade(String nivelFidelidade) {
    this.nivelFidelidade = nivelFidelidade;
  }

  public LocalDateTime getDataExpiracao() {
    return dataExpiracao;
  }

  public void setDataExpiracao(LocalDateTime dataExpiracao) {
    this.dataExpiracao = dataExpiracao;
  }
}
