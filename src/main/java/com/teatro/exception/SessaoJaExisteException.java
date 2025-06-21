package com.teatro.exception;

/**
 * Exceção lançada quando uma sessão já existe no sistema
 */
public class SessaoJaExisteException extends RuntimeException {

  public SessaoJaExisteException(String message) {
    super(message);
  }

  public SessaoJaExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}
