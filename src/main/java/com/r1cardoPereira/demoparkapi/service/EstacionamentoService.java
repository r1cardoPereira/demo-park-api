package com.r1cardoPereira.demoparkapi.service;


import com.r1cardoPereira.demoparkapi.entity.Cliente;
import com.r1cardoPereira.demoparkapi.entity.ClienteVaga;
import com.r1cardoPereira.demoparkapi.entity.Vaga;
import com.r1cardoPereira.demoparkapi.util.EstacionamentoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Service class for parking operations.
 * This class is responsible for managing the check-in process of a parking lot.
 * It uses the ClienteVagaService, ClienteService, and VagaService to perform its operations.
 */
@Service
@RequiredArgsConstructor
public class EstacionamentoService {

    /**
     * Service for managing ClienteVaga entities.
     */
    private final ClienteVagaService clienteVagaService;

    /**
     * Service for managing Cliente entities.
     */
    private final ClienteService clienteService;

    /**
     * Service for managing Vaga entities.
     */
    private final VagaService vagaService;

    /**
     * Performs the check-in process for a ClienteVaga entity.
     * This method will update the Cliente and Vaga entities associated with the ClienteVaga,
     * set the check-in time, generate a receipt, and save the updated ClienteVaga entity.
     *
     * @param clienteVaga the ClienteVaga entity to check in.
     * @return the updated ClienteVaga entity.
     */
    @Transactional
    public ClienteVaga checkIn(ClienteVaga clienteVaga){
       Cliente cliente = clienteService.buscarPorCpf(clienteVaga.getCliente().getCpf());
       clienteVaga.setCliente(cliente);

       Vaga vaga = vagaService.buscarPorVagaLivre();
       vaga.setStatus(Vaga.StatusVaga.OCUPADA);
       clienteVaga.setVaga(vaga);

       clienteVaga.setDataEntrada(LocalDateTime.now());
       clienteVaga.setRecibo(EstacionamentoUtils.gerarRecibo());

       return clienteVagaService.salvar(clienteVaga);
    }

    @Transactional
    public ClienteVaga checkOut(String recibo) {

    ClienteVaga clienteVaga = clienteVagaService.buscarPorRecibo(recibo);

    LocalDateTime dataSaida = LocalDateTime.now();
    BigDecimal valor = EstacionamentoUtils.calcularCusto(clienteVaga.getDataEntrada(), dataSaida);

    clienteVaga.setValor(valor);

    long totalDeVezes = clienteVagaService.getTotalDeVezesEstacionamentoCompleto(clienteVaga.getCliente().getCpf());

    BigDecimal desconto =EstacionamentoUtils.calcularDesconto(valor, totalDeVezes);
    clienteVaga.setDesconto(desconto);

    clienteVaga.setDataSaida(dataSaida);
    clienteVaga.getVaga().setStatus(Vaga.StatusVaga.LIVRE);

     return clienteVagaService.salvar(clienteVaga);


    }
}



