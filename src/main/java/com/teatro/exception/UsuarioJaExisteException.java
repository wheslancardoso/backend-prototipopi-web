package com.teatro.exception;

/**
 * Exceção lançada quando um usuário já existe no sistema
 */
public class UsuarioJaExisteException extends RuntimeException {

  public UsuarioJaExisteException(String message) {
    super(message);
  }

  public UsuarioJaExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}
