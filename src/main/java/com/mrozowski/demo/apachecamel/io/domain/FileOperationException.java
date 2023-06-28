package com.mrozowski.demo.apachecamel.io.domain;

public class FileOperationException extends RuntimeException {
  public FileOperationException(String message, Throwable cause) {
    super(message, cause);
  }
}
