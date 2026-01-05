package org.example.microTech.exceptions;

public class BusinessException extends RuntimeException {
  public BusinessException(String message) {
    super(message);
  }
}
