package com.teatro.exception;

/**
 * Exceção lançada quando há problemas de autenticação
 */
public class AutenticacaoException extends RuntimeException {

  public AutenticacaoException(String message) {
    super(message);
  }

  public AutenticacaoException(String message, Throwable cause) {
    super(message, cause);
  }
}
