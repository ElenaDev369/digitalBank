package com.digitalbank.cuentas_service.domain.exception;

import com.digitalbank.cuentas_service.domain.model.CuentaId;

public class CuentaCerradaException extends DominioException {
    public CuentaCerradaException(CuentaId id) {
        super("La cuenta " + id.valor() + " está cerrada");
    }
}
