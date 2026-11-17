package com.digitalbank.cuentas_service.application.port.in;

import com.digitalbank.cuentas_service.domain.model.Cuenta;
import com.digitalbank.cuentas_service.domain.model.CuentaId;
import com.digitalbank.cuentas_service.domain.model.Dinero;

public interface DepositarUseCase {
    Cuenta depositar(CuentaId id, Dinero importe);
}
