package com.digitalbank.cuentas_service.infrastructure.persistence;

import com.digitalbank.cuentas_service.application.port.out.CuentaRepository;
import com.digitalbank.cuentas_service.domain.model.Cuenta;
import com.digitalbank.cuentas_service.domain.model.CuentaId;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CuentaRepositoryEnMemoria implements CuentaRepository {

    private final Map<CuentaId, Cuenta> cuentas = new ConcurrentHashMap<>();

    @Override
    public Cuenta guardar(Cuenta cuenta) {
        cuentas.put(cuenta.id(), cuenta);
        return cuenta;
    }

    @Override
    public Optional<Cuenta> buscarPorId(CuentaId id) {
        return Optional.ofNullable(cuentas.get(id));
    }
}
