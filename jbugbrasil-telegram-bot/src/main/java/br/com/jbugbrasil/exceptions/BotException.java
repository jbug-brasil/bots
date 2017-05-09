package br.com.jbugbrasil.exceptions;

public class BotException extends RuntimeException {

  public BotException() {
  }

  public BotException(String message) {
    super(message);
  }

  public BotException(String message, Throwable cause) {
    super(message, cause);
  }

  public BotException(Throwable cause) {
    super(cause);
  }

  public BotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
