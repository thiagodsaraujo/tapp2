package com.felipe.arqsoftware.demo.service;

import com.felipe.arqsoftware.demo.model.Cliente;
import com.felipe.arqsoftware.demo.repository.ClienteRepository;
import com.felipe.arqsoftware.demo.service.exceptions.ClientNotFoundException;
import com.felipe.arqsoftware.demo.service.exceptions.CpfCadastradoException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository repository;


    @Transactional
    public List<Cliente> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Cliente findById(Long id) throws ClientNotFoundException {
        Optional<Cliente> clientObj = repository.findById(id);
        Cliente cliente = clientObj.orElseThrow(() -> new ClientNotFoundException("Operation failed. No account valid for the id on the output."));
        return clientObj.get();
    }

    @Transactional
    public Cliente createCliente(Cliente cliente) {
        String cpfCliente = cliente.getCpf();
        List<Cliente> clientes = repository.findAll();
        for (Cliente clienteCadastrado : clientes) {
            if (cpfCliente.equals(clienteCadastrado.getCpf())) {
                throw new CpfCadastradoException("CPF informado encontra-se em uso");
            }
        }
        return repository.save(cliente);
    }

    public void deleteClient(Long id) {
        repository.deleteById(id);
    }

    @Autowired
    private ClienteRepository clienteRepository;

    @CircuitBreaker(name = "clienteService", fallbackMethod = "fallbackBuscarCliente")
    public Cliente buscarCliente(Long clienteId) {
        // Simula uma falha aleatória para demonstrar o Circuit Breaker
        if (new Random().nextInt(10) < 5) { // 50% de chance de falha
            throw new RuntimeException("Falha ao buscar cliente");
        }
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
    }

    public Cliente fallbackBuscarCliente(Long clienteId, Throwable t) {
        // Retorna um cliente com dados básicos ou uma mensagem indicando falha
        Cliente fallbackCliente = new Cliente();
        fallbackCliente.setId(clienteId);
        fallbackCliente.setName("Cliente não disponível");
        fallbackCliente.setContaCorrente(Collections.emptyList()); // Sem contas disponíveis
        return fallbackCliente;
    }
}
