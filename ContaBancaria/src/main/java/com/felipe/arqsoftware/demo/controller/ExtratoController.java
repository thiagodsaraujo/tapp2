package com.felipe.arqsoftware.demo.controller;

import com.felipe.arqsoftware.demo.model.Cliente;
import com.felipe.arqsoftware.demo.model.ContaCorrente;
import com.felipe.arqsoftware.demo.model.Extrato;
import com.felipe.arqsoftware.demo.service.ClienteService;
import com.felipe.arqsoftware.demo.service.ContaCorrenteService;
import com.felipe.arqsoftware.demo.service.ExtratoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequestMapping("/extratos")
public class ExtratoController {

    @Autowired
    ExtratoService service;

    @Autowired
    ClienteService clienteService;

    @Autowired
    ContaCorrenteService contaCorrenteService;

    @GetMapping
    public ResponseEntity<List<Extrato>> findAll() {
        List<Extrato> extratos = service.findAll();
        return ResponseEntity.ok().body(extratos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Extrato> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }


    @GetMapping("/cliente/{clienteId}/export/csv")
    @CircuitBreaker(name = "export-service", fallbackMethod = "fallbackExportToCSV")
    public void exportToCSV(@PathVariable Long clienteId, HttpServletResponse response) throws IOException, InterruptedException {
        // Simula uma falha para teste (você pode adicionar condições específicas aqui)

        Thread.sleep(5000); // Simula um atraso de 5 segundos


        if (clienteId == 123) {
            throw new RuntimeException("Erro ao exportar CSV para o cliente com ID: " + clienteId);
        }

        // Configuração de cabeçalhos de resposta para exportar CSV
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=extratos_cliente_" + clienteId + ".csv");

        // Busca o cliente e suas contas
        Cliente cliente = clienteService.findById(clienteId);

        cliente.getContaCorrente().forEach(conta -> {
            System.out.println(conta.getExtratos().toString());
        });

        List<Extrato> listExtratos = cliente.getContaCorrente()

                .stream()
                .flatMap(conta -> conta.getExtratos().stream())
                .toList();

        ExtratoCsvExporter exporter = new ExtratoCsvExporter();

        exporter.export(listExtratos, response);
    }

    // Fallback para o Circuit Breaker
    public void fallbackExportToCSV(Long clienteId, HttpServletResponse response, Throwable t) throws IOException {
        // Configuração de cabeçalhos para o fallback
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "inline");

        // Mensagem informando a indisponibilidade
        try (PrintWriter writer = response.getWriter()) {
            writer.println("O serviço de exportação está temporariamente indisponível. Tente novamente mais tarde.");
        }
    }


    @GetMapping("/cliente/{clienteId}/export/v2/csv")
    public void exportToCSV2(@PathVariable Long clienteId, HttpServletResponse response) throws IOException {

        Cliente cliente = clienteService.findById(clienteId);

        cliente.getContaCorrente().forEach(conta -> {
            System.out.println(conta.getExtratos().toString());
        });



        List<Extrato> listExtratos = cliente.getContaCorrente()

                .stream()
                .flatMap(conta -> conta.getExtratos().stream())
                .toList();

        ExtratoCsvExporter exporter = new ExtratoCsvExporter();

        exporter.export(listExtratos, response);
    }

    @GetMapping("/cliente/{clienteId}/export/pdf")
    public void exportToPDF(@PathVariable Long clienteId, HttpServletResponse response) throws IOException {
        Cliente cliente = clienteService.findById(clienteId);

        cliente.getContaCorrente().forEach(conta -> {
            System.out.println(conta.getExtratos().toString());
        });

        List<Extrato> listExtratos = cliente.getContaCorrente()
                .stream()
                .flatMap(conta -> conta.getExtratos().stream())
                .toList();

        ExtratoPdfExporter exporter = new ExtratoPdfExporter();
        exporter.export(listExtratos, response);
    }



}
