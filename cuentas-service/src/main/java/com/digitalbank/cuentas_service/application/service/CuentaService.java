package com.digitalbank.cuentas_service.application.service;

import com.digitalbank.cuentas_service.application.port.in.AbrirCuentaUseCase;
import com.digitalbank.cuentas_service.application.port.in.CerrarCuentaUseCase;
import com.digitalbank.cuentas_service.application.port.in.ConsultarCuentaUseCase;
import com.digitalbank.cuentas_service.application.port.in.DepositarUseCase;
import com.digitalbank.cuentas_service.application.port.in.RetirarUseCase;
import com.digitalbank.cuentas_service.application.port.out.CuentaRepository;
import com.digitalbank.cuentas_service.domain.exception.CuentaNoEncontradaException;
import com.digitalbank.cuentas_service.domain.model.Cuenta;
import com.digitalbank.cuentas_service.domain.model.CuentaId;
import com.digitalbank.cuentas_service.domain.model.Dinero;
import org.springframework.stereotype.Service;

import java.util.Currency;

@Service
public class CuentaService implements AbrirCuentaUseCase, ConsultarCuentaUseCase,
        DepositarUseCase, RetirarUseCase, CerrarCuentaUseCase {

    private final CuentaRepository repositorio;

    public CuentaService(CuentaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Cuenta abrir(String titular, Currency moneda) {
        return repositorio.guardar(Cuenta.abrir(titular, moneda));
    }

    @Override
    public Cuenta consultar(CuentaId id) {
        return buscar(id);
    }

    @Override
    public Cuenta depositar(CuentaId id, Dinero importe) {
        Cuenta cuenta = buscar(id);
        cuenta.depositar(importe);
        return repositorio.guardar(cuenta);
    }

    @Override
    public Cuenta retirar(CuentaId id, Dinero importe) {
        Cuenta cuenta = buscar(id);
        cuenta.retirar(importe);
        return repositorio.guardar(cuenta);
    }

    @Override
    public void cerrar(CuentaId id) {
        Cuenta cuenta = buscar(id);
        cuenta.cerrar();
        repositorio.guardar(cuenta);
    }

    private Cuenta buscar(CuentaId id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new CuentaNoEncontradaException(id));
    }
}