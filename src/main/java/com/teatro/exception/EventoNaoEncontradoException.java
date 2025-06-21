package com.teatro.exception;

/**
 * Exceção lançada quando um evento não é encontrado no sistema
 */
public class EventoNaoEncontradoException extends RuntimeException {

  public EventoNaoEncontradoException(String message) {
    super(message);
  }

  public EventoNaoEncontradoException(String message, Throwable cause) {
    super(message, cause);
  }
}
