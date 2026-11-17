package com.digitalbank.cuentas_service.application.port.in;

import com.digitalbank.cuentas_service.domain.model.Cuenta;
import com.digitalbank.cuentas_service.domain.model.CuentaId;

public interface ConsultarCuentaUseCase {
    Cuenta consultar(CuentaId id);
}
