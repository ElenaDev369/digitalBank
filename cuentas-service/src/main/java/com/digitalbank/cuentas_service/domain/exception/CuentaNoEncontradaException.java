package com.digitalbank.cuentas_service.domain.exception;

import com.digitalbank.cuentas_service.domain.model.CuentaId;

public class CuentaNoEncontradaException extends DominioException {
    public CuentaNoEncontradaException(CuentaId id) {
        super("No existe la cuenta " + id.valor());
    }
}
