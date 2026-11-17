package com.digitalbank.cuentas_service.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CuentaIdTest {

    @Test
    @DisplayName("Generar crea identificadores distintos")
    void generarCreaIdentificadoresDistintos() {
        CuentaId primero = CuentaId.generar();
        CuentaId segundo = CuentaId.generar();

        assertNotNull(primero.valor());
        assertNotEquals(primero, segundo);
    }

    @Test
    @DisplayName("El valor es obligatorio")
    void elValorEsObligatorio() {
        assertThrows(NullPointerException.class, () -> new CuentaId(null));
    }
}
