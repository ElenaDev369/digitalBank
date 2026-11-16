package com.digitalbank.cuentas_service.domain.model;

import java.util.Objects;
import java.util.UUID;


public record CuentaId(UUID valor) {
    public CuentaId{
        Objects.requireNonNull(valor, "El valor es oblogatorio");
    }

    public static CuentaId generar(){
        return new CuentaId(UUID.randomUUID());
    }
}
