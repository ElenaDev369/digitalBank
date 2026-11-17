package com.digitalbank.cuentas_service.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DineroTest {

    private static final Currency EUR = Currency.getInstance("EUR");
    private static final Currency USD = Currency.getInstance("USD");

    private static Dinero euros(String importe) {
        return new Dinero(new BigDecimal(importe), EUR);
    }

    @Test
    @DisplayName("Sumar dos importes devuelve la suma")
    void sumarDosImportesDevuelveLaSuma() {
        Dinero resultado = euros("10.00").sumar(euros("5.50"));

        assertEquals(euros("15.50"), resultado);
    }

    @Test
    @DisplayName("Restar dos importes devuelve la diferencia")
    void restarDosImportesDevuelveLaDiferencia() {
        Dinero resultado = euros("10.00").restar(euros("4.00"));

        assertEquals(euros("6.00"), resultado);
    }

    @Test
    @DisplayName("Dinero permite resultados negativos: esa regla es de Cuenta, no de Dinero")
    void restarPuedeDarUnResultadoNegativo() {
        Dinero resultado = euros("5.00").restar(euros("8.00"));

        assertEquals(euros("-3.00"), resultado);
    }

    @Test
    @DisplayName("Sumar y restar no modifican los objetos originales (inmutabilidad)")
    void lasOperacionesNoModificanElOriginal() {
        Dinero original = euros("10.00");

        original.sumar(euros("5.00"));

        assertEquals(euros("10.00"), original);
    }

    @Test
    @DisplayName("No se pueden operar monedas distintas")
    void sumarMonedasDistintasLanzaExcepcion() {
        Dinero dolares = new Dinero(new BigDecimal("5.00"), USD);

        assertThrows(IllegalArgumentException.class, () -> euros("5.00").sumar(dolares));
    }

    @Test
    @DisplayName("Más de dos decimales no está permitido")
    void masDeDosDecimalesLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> euros("1.005"));
    }

    @Test
    @DisplayName("El importe se normaliza siempre a dos decimales")
    void elImporteSeNormalizaADosDecimales() {
        assertEquals(new BigDecimal("10.00"), euros("10").importe());
    }

    @Test
    @DisplayName("Dos Dinero con el mismo valor son iguales (Value Object)")
    void dosDineroConElMismoValorSonIguales() {
        assertEquals(euros("10"), euros("10.00"));
    }

    @Test
    @DisplayName("El importe y la moneda son obligatorios")
    void importeYMonedaSonObligatorios() {
        assertThrows(NullPointerException.class, () -> new Dinero(null, EUR));
        assertThrows(NullPointerException.class, () -> new Dinero(BigDecimal.TEN, null));
    }

    @Test
    @DisplayName("esMayorQue es estricto: con valores iguales devuelve false")
    void esMayorQueEsEstricto() {
        assertTrue(euros("10.00").esMayorQue(euros("5.00")));
        assertFalse(euros("10.00").esMayorQue(euros("10.00")));
        assertFalse(euros("5.00").esMayorQue(euros("10.00")));
    }

    @Test
    @DisplayName("esPositivo y esCero")
    void esPositivoYEsCero() {
        assertTrue(euros("0.01").esPositivo());
        assertFalse(euros("0.00").esPositivo());
        assertFalse(euros("-1.00").esPositivo());
        assertTrue(Dinero.cero(EUR).esCero());
        assertFalse(euros("0.01").esCero());
    }
}