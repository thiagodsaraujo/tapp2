package com.felipe.arqsoftware.demo.view.conta;

import com.felipe.arqsoftware.demo.model.Cliente;
import com.felipe.arqsoftware.demo.model.ContaCorrente;
import com.felipe.arqsoftware.demo.model.Extrato;
import com.felipe.arqsoftware.demo.service.ClienteService;
import com.felipe.arqsoftware.demo.service.ContaCorrenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/conta-painel")
public class ContaViewController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ContaCorrenteService contaCorrenteService;

    @GetMapping("/cliente/{clienteId}")
    public String exibirPainelCliente(@PathVariable Long clienteId, Model model) {
        // Busca o cliente pelo ID
        Cliente cliente = clienteService.findById(clienteId);

        // Para cada conta, limitamos o número de extratos a 5
        cliente.getContaCorrente().forEach(conta -> {
            List<Extrato> extratosLimitados = conta.getExtratos().stream()
                    .limit(5)
                    .collect(Collectors.toList());
            conta.setExtratos(extratosLimitados); // Define a lista limitada
        });

        // Adiciona o cliente com as contas e extratos limitados ao modelo
        model.addAttribute("cliente", cliente);

        return "conta-painel";
    }
}

