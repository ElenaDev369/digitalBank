package com.digitalbank.cuentas_service.application.service;

import com.digitalbank.cuentas_service.application.port.out.CuentaRepository;
import com.digitalbank.cuentas_service.domain.exception.CuentaNoEncontradaException;
import com.digitalbank.cuentas_service.domain.exception.SaldoInsuficienteException;
import com.digitalbank.cuentas_service.domain.model.Cuenta;
import com.digitalbank.cuentas_service.domain.model.CuentaId;
import com.digitalbank.cuentas_service.domain.model.Dinero;
import com.digitalbank.cuentas_service.domain.model.EstadoCuenta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    private static final Currency EUR = Currency.getInstance("EUR");

    @Mock
    private CuentaRepository repositorio;

    @InjectMocks
    private CuentaService servicio;

    private static Dinero euros(String importe) {
        return new Dinero(new BigDecimal(importe), EUR);
    }

    @Test
    @DisplayName("Abrir una cuenta la guarda y la devuelve activa")
    void abrirGuardaLaCuenta() {
        when(repositorio.guardar(any(Cuenta.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Cuenta cuenta = servicio.abrir("Ana García", EUR);

        assertEquals(EstadoCuenta.ACTIVA, cuenta.estado());
        verify(repositorio).guardar(cuenta);
    }

    @Test
    @DisplayName("Consultar una cuenta inexistente lanza CuentaNoEncontradaException")
    void consultarCuentaInexistenteLanzaExcepcion() {
        CuentaId id = CuentaId.generar();
        when(repositorio.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(CuentaNoEncontradaException.class, () -> servicio.consultar(id));
    }

    @Test
    @DisplayName("Depositar actualiza el saldo y guarda la cuenta")
    void depositarActualizaElSaldoYGuarda() {
        Cuenta cuenta = Cuenta.abrir("Ana García", EUR);
        when(repositorio.buscarPorId(cuenta.id())).thenReturn(Optional.of(cuenta));
        when(repositorio.guardar(cuenta)).thenReturn(cuenta);

        Cuenta resultado = servicio.depositar(cuenta.id(), euros("50.00"));

        assertEquals(euros("50.00"), resultado.saldo());
        verify(repositorio).guardar(cuenta);
    }

    @Test
    @DisplayName("Depositar en una cuenta inexistente no guarda nada")
    void depositarEnCuentaInexistenteLanzaExcepcion() {
        CuentaId id = CuentaId.generar();
        when(repositorio.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(CuentaNoEncontradaException.class,
                () -> servicio.depositar(id, euros("50.00")));
        verify(repositorio, never()).guardar(any());
    }

    @Test
    @DisplayName("Retirar sin saldo suficiente lanza la excepción y no guarda")
    void retirarSinSaldoSuficienteNoGuarda() {
        Cuenta cuenta = Cuenta.abrir("Ana García", EUR);
        when(repositorio.buscarPorId(cuenta.id())).thenReturn(Optional.of(cuenta));

        assertThrows(SaldoInsuficienteException.class,
                () -> servicio.retirar(cuenta.id(), euros("10.00")));
        verify(repositorio, never()).guardar(any());
    }

    @Test
    @DisplayName("Cerrar una cuenta sin saldo la guarda como cerrada")
    void cerrarCuentaSinSaldoLaGuardaCerrada() {
        Cuenta cuenta = Cuenta.abrir("Ana García", EUR);
        when(repositorio.buscarPorId(cuenta.id())).thenReturn(Optional.of(cuenta));

        servicio.cerrar(cuenta.id());

        assertEquals(EstadoCuenta.CERRADA, cuenta.estado());
        verify(repositorio).guardar(cuenta);
    }
}