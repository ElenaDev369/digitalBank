package com.digitalbank.cuentas_service.domain.exception;

import com.digitalbank.cuentas_service.domain.model.CuentaId;
import com.digitalbank.cuentas_service.domain.model.Dinero;

public class CuentaConSaldoException extends DominioException {
    public CuentaConSaldoException(CuentaId id, Dinero saldo) {
        super("No se puede cerrar la cuenta " + id.valor() + ": aún tiene un saldo de "
                + saldo.importe() + " " + saldo.moneda());
    }
}
