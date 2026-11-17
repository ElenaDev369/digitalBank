package com.digitalbank.cuentas_service.domain.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public record Dinero(BigDecimal importe, Currency moneda) {

    public Dinero {
        Objects.requireNonNull(importe, "El importe es obligatorio");
        Objects.requireNonNull(moneda, "La moneda es obligatoria");
        if (importe.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException("El importe admite como máximo 2 decimales");
        }
        importe = importe.setScale(2);
    }

    public static Dinero cero(Currency moneda) {
        return new Dinero(BigDecimal.ZERO, moneda);
    }

    public Dinero sumar(Dinero otro) {
        exigirMismaMoneda(otro);
        return new Dinero(this.importe.add(otro.importe), this.moneda);
    }

    public Dinero restar(Dinero otro) {
        exigirMismaMoneda(otro);
        return new Dinero(this.importe.subtract(otro.importe), this.moneda);
    }

    public boolean esMayorQue(Dinero otro) {
        exigirMismaMoneda(otro);
        return this.importe.compareTo(otro.importe) > 0;
    }

    public boolean esPositivo() {
        return this.importe.signum() > 0;
    }

    public boolean esCero() {
        return this.importe.signum() == 0;
    }

    private void exigirMismaMoneda(Dinero otro) {
        if (!this.moneda.equals(otro.moneda)) {
            throw new IllegalArgumentException("No se pueden operar monedas distintas");
        }
    }
}