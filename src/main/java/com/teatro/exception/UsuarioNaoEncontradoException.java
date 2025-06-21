package com.teatro.exception;

/**
 * Exceção lançada quando um usuário não é encontrado no sistema
 */
public class UsuarioNaoEncontradoException extends RuntimeException {

  public UsuarioNaoEncontradoException(String message) {
    super(message);
  }

  public UsuarioNaoEncontradoException(String message, Throwable cause) {
    super(message, cause);
  }
}
