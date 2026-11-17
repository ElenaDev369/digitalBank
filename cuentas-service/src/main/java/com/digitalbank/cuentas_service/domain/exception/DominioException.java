package com.digitalbank.cuentas_service.domain.exception;

public abstract class DominioException extends RuntimeException {
    protected DominioException(String mensaje) {
        super(mensaje);
    }
}
