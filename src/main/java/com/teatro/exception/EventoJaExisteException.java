package com.teatro.exception;

/**
 * Exceção lançada quando um evento já existe no sistema
 */
public class EventoJaExisteException extends RuntimeException {

  public EventoJaExisteException(String message) {
    super(message);
  }

  public EventoJaExisteException(String message, Throwable cause) {
    super(message, cause);
  }
}
