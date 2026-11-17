package com.digitalbank.cuentas_service.application.port.out;

import com.digitalbank.cuentas_service.domain.model.Cuenta;
import com.digitalbank.cuentas_service.domain.model.CuentaId;

import java.util.Optional;

public interface CuentaRepository {
    Cuenta guardar(Cuenta cuenta);

    Optional<Cuenta> buscarPorId(CuentaId id);
}
