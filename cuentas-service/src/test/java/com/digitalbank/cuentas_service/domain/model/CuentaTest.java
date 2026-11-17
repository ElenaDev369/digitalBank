package com.digitalbank.cuentas_service.domain.model;

import com.digitalbank.cuentas_service.domain.exception.CuentaCerradaException;
import com.digitalbank.cuentas_service.domain.exception.CuentaConSaldoException;
import com.digitalbank.cuentas_service.domain.exception.SaldoInsuficienteException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CuentaTest {

    private static final Currency EUR = Currency.getInstance("EUR");

    private static Dinero euros(String importe) {
        return new Dinero(new BigDecimal(importe), EUR);
    }

    private static Cuenta cuentaConSaldo(String saldo) {
        Cuenta cuenta = Cuenta.abrir("Ana García", EUR);
        cuenta.depositar(euros(saldo));
        return cuenta;
    }

    // ---------- abrir ----------

    @Test
    @DisplayName("Una cuenta nueva está activa y con saldo cero")
    void abrirCreaCuentaActivaConSaldoCero() {
        Cuenta cuenta = Cuenta.abrir("Ana García", EUR);

        assertEquals(EstadoCuenta.ACTIVA, cuenta.estado());
        assertEquals(Dinero.cero(EUR), cuenta.saldo());
        assertEquals("Ana García", cuenta.titular());
    }

    @Test
    @DisplayName("El titular no puede ser nulo ni estar vacío")
    void abrirConTitularInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> Cuenta.abrir(null, EUR));
        assertThrows(IllegalArgumentException.class, () -> Cuenta.abrir("   ", EUR));
    }

    // ---------- depositar ----------

    @Test
    @DisplayName("Depositar aumenta el saldo")
    void depositarAumentaElSaldo() {
        Cuenta cuenta = cuentaConSaldo("100.00");

        cuenta.depositar(euros("50.00"));

        assertEquals(euros("150.00"), cuenta.saldo());
    }

    @Test
    @DisplayName("No se puede depositar un importe cero o negativo")
    void depositarImporteNoPositivoLanzaExcepcion() {
        Cuenta cuenta = cuentaConSaldo("100.00");

        assertThrows(IllegalArgumentException.class, () -> cuenta.depositar(euros("0.00")));
        assertThrows(IllegalArgumentException.class, () -> cuenta.depositar(euros("-5.00")));
        assertEquals(euros("100.00"), cuenta.saldo());
    }

    // ---------- retirar ----------

    @Test
    @DisplayName("Retirar disminuye el saldo")
    void retirarDisminuyeElSaldo() {
        Cuenta cuenta = cuentaConSaldo("100.00");

        cuenta.retirar(euros("30.00"));

        assertEquals(euros("70.00"), cuenta.saldo());
    }

    @Test
    @DisplayName("Se puede retirar exactamente todo el saldo (caso límite)")
    void retirarTodoElSaldoDejaLaCuentaACero() {
        Cuenta cuenta = cuentaConSaldo("100.00");

        cuenta.retirar(euros("100.00"));

        assertEquals(Dinero.cero(EUR), cuenta.saldo());
    }

    @Test
    @DisplayName("No se puede retirar más de lo que hay y el saldo no cambia")
    void retirarMasQueElSaldoLanzaExcepcion() {
        Cuenta cuenta = cuentaConSaldo("100.00");

        assertThrows(SaldoInsuficienteException.class, () -> cuenta.retirar(euros("100.01")));
        assertEquals(euros("100.00"), cuenta.saldo());
    }

    @Test
    @DisplayName("No se puede retirar un importe cero o negativo")
    void retirarImporteNoPositivoLanzaExcepcion() {
        Cuenta cuenta = cuentaConSaldo("100.00");

        assertThrows(IllegalArgumentException.class, () -> cuenta.retirar(euros("0.00")));
        assertThrows(IllegalArgumentException.class, () -> cuenta.retirar(euros("-5.00")));
    }

    // ---------- cerrar ----------

    @Test
    @DisplayName("Una cuenta con saldo cero se puede cerrar")
    void cerrarConSaldoCeroCambiaElEstado() {
        Cuenta cuenta = Cuenta.abrir("Ana García", EUR);

        cuenta.cerrar();

        assertEquals(EstadoCuenta.CERRADA, cuenta.estado());
    }

    @Test
    @DisplayName("No se puede cerrar una cuenta con saldo y sigue activa")
    void cerrarConSaldoLanzaExcepcion() {
        Cuenta cuenta = cuentaConSaldo("10.00");

        assertThrows(CuentaConSaldoException.class, cuenta::cerrar);
        assertEquals(EstadoCuenta.ACTIVA, cuenta.estado());
    }

    // ---------- cuenta cerrada ----------

    @Test
    @DisplayName("Una cuenta cerrada no admite ninguna operación")
    void cuentaCerradaNoAdmiteOperaciones() {
        Cuenta cuenta = Cuenta.abrir("Ana García", EUR);
        cuenta.cerrar();

        assertThrows(CuentaCerradaException.class, () -> cuenta.depositar(euros("5.00")));
        assertThrows(CuentaCerradaException.class, () -> cuenta.retirar(euros("5.00")));
        assertThrows(CuentaCerradaException.class, cuenta::cerrar);
    }

    // ---------- identidad ----------

    @Test
    @DisplayName("Dos cuentas distintas no son iguales aunque tengan los mismos datos")
    void cuentasConDistintoIdNoSonIguales() {
        Cuenta una = Cuenta.abrir("Ana García", EUR);
        Cuenta otra = Cuenta.abrir("Ana García", EUR);

        assertNotEquals(una, otra);
        assertEquals(una, una);
    }
}
