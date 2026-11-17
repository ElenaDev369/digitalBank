package com.digitalbank.cuentas_service.domain.model;

import com.digitalbank.cuentas_service.domain.exception.CuentaCerradaException;
import com.digitalbank.cuentas_service.domain.exception.CuentaConSaldoException;
import com.digitalbank.cuentas_service.domain.exception.SaldoInsuficienteException;

import java.util.Currency;
import java.util.Objects;

public class Cuenta {

    private final CuentaId id;
    private final String titular;
    private Dinero saldo;
    private EstadoCuenta estado;

    private Cuenta(CuentaId id, String titular, Dinero saldo, EstadoCuenta estado) {
        this.id = Objects.requireNonNull(id, "El id es obligatorio");
        this.titular = Objects.requireNonNull(titular, "El titular es obligatorio");
        this.saldo = Objects.requireNonNull(saldo, "El saldo es obligatorio");
        this.estado = Objects.requireNonNull(estado, "El estado es obligatorio");
    }

    public static Cuenta abrir(String titular, Currency moneda) {
        if (titular == null || titular.isBlank()) {
            throw new IllegalArgumentException("El titular no puede estar vacío");
        }
        return new Cuenta(CuentaId.generar(), titular.trim(),
                Dinero.cero(moneda), EstadoCuenta.ACTIVA);
    }

    public void depositar(Dinero importe) {
        exigirActiva();
        exigirImportePositivo(importe);
        this.saldo = this.saldo.sumar(importe);
    }

    public void retirar(Dinero importe) {
        exigirActiva();
        exigirImportePositivo(importe);
        exigirSaldoSuficiente(importe);
        this.saldo = this.saldo.restar(importe);
    }

    public void cerrar() {
        exigirActiva();
        exigirSaldoCero();
        this.estado = EstadoCuenta.CERRADA;
    }

    private void exigirActiva() {
        if (this.estado == EstadoCuenta.CERRADA) {
            throw new CuentaCerradaException(this.id);
        }
    }

    private void exigirImportePositivo(Dinero importe) {
        Objects.requireNonNull(importe, "El importe es obligatorio");
        if (!importe.esPositivo()) {
            throw new IllegalArgumentException("El importe debe ser mayor que cero");
        }
    }

    private void exigirSaldoSuficiente(Dinero importe) {
        if (importe.esMayorQue(this.saldo)) {
            throw new SaldoInsuficienteException(this.saldo, importe);
        }
    }

    private void exigirSaldoCero() {
        if (!this.saldo.esCero()) {
            throw new CuentaConSaldoException(this.id, this.saldo);
        }
    }

    public CuentaId id() { return id; }
    public String titular() { return titular; }
    public Dinero saldo() { return saldo; }
    public EstadoCuenta estado() { return estado; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cuenta cuenta = (Cuenta) o;
        return Objects.equals(id, cuenta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}