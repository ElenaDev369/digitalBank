package com.digitalbank.cuentas_service.application.port.in;

import com.digitalbank.cuentas_service.domain.model.Cuenta;

import java.util.Currency;

public interface AbrirCuentaUseCase {
    Cuenta abrir(String titular, Currency moneda);
}
