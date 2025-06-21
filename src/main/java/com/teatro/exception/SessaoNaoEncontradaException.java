package com.teatro.exception;

/**
 * Exceção lançada quando uma sessão não é encontrada no sistema
 */
public class SessaoNaoEncontradaException extends RuntimeException {

  public SessaoNaoEncontradaException(String message) {
    super(message);
  }

  public SessaoNaoEncontradaException(String message, Throwable cause) {
    super(message, cause);
  }
}
