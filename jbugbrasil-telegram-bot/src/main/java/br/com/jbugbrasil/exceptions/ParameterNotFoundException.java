package br.com.jbugbrasil.exceptions;

/**
 * Created by fspolti on 8/31/16.
 */
public class ParameterNotFoundException extends RuntimeException {

    public ParameterNotFoundException() {}

    public ParameterNotFoundException(String message) {
        super(message);
    }

    public ParameterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterNotFoundException(Throwable cause) {
        super(cause);
    }

    public ParameterNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}