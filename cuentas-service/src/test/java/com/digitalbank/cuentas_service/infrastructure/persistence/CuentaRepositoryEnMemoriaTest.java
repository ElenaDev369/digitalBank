package com.digitalbank.cuentas_service.infrastructure.persistence;

import com.digitalbank.cuentas_service.domain.model.Cuenta;
import com.digitalbank.cuentas_service.domain.model.CuentaId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CuentaRepositoryEnMemoriaTest {

    private final CuentaRepositoryEnMemoria repositorio = new CuentaRepositoryEnMemoria();

    @Test
    @DisplayName("Una cuenta guardada se puede recuperar por su id")
    void guardarYBuscarDevuelveLaCuenta() {
        Cuenta cuenta = Cuenta.abrir("Ana García", Currency.getInstance("EUR"));

        repositorio.guardar(cuenta);

        assertEquals(Optional.of(cuenta), repositorio.buscarPorId(cuenta.id()));
    }

    @Test
    @DisplayName("Buscar un id que no existe devuelve vacío")
    void buscarInexistenteDevuelveVacio() {
        assertEquals(Optional.empty(), repositorio.buscarPorId(CuentaId.generar()));
    }
}
