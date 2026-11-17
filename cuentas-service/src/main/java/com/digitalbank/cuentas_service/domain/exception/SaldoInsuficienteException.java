package com.digitalbank.cuentas_service.domain.exception;

import com.digitalbank.cuentas_service.domain.model.Cuenta;
import com.digitalbank.cuentas_service.domain.model.CuentaId;
import com.digitalbank.cuentas_service.domain.model.Dinero;

public class SaldoInsuficienteException extends DominioException {
    public SaldoInsuficienteException(Dinero saldo, Dinero importe) {
        super("Saldo insuficiente: tienes " + saldo.importe() + " " + saldo.moneda()
                + " e intentas retirar " + importe.importe() + " " + importe.moneda());
    }
}
