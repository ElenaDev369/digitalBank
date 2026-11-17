package com.digitalbank.cuentas_service.application.port.in;

import com.digitalbank.cuentas_service.domain.model.CuentaId;

public interface CerrarCuentaUseCase {
    void cerrar(CuentaId id);
}
