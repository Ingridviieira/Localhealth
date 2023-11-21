package br.com.fiap.exceptions;

public record RestValidationError(String field, String message) {}