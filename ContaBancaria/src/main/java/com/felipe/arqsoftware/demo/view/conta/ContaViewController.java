package com.felipe.arqsoftware.demo.view.conta;

import com.felipe.arqsoftware.demo.model.Cliente;
import com.felipe.arqsoftware.demo.model.ContaCorrente;
import com.felipe.arqsoftware.demo.model.Extrato;
import com.felipe.arqsoftware.demo.service.ClienteService;
import com.felipe.arqsoftware.demo.service.ContaCorrenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/conta-painel")
public class ContaViewController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ContaCorrenteService contaCorrenteService;

//    http://localhost:8081/extratos/cliente/123/export/csv
    @GetMapping("/cliente/{clienteId}")
    public String exibirPainelCliente(@PathVariable Long clienteId,
                                      @RequestParam(defaultValue = "0") int page,
                                      Model model) {
        // Busca o cliente pelo ID
        Cliente cliente = clienteService.findById(clienteId);

        // Para cada conta, obtemos as movimentações para a página atual e verificamos se há uma próxima página
        cliente.getContaCorrente().forEach(conta -> {
            int start = page * 5;
            int end = Math.min(start + 5, conta.getExtratos().size());

            // Extratos limitados para a página atual
            List<Extrato> extratosPaginados = conta.getExtratos().subList(start, end);
            conta.setExtratos(extratosPaginados);
        });

        // Adiciona o cliente com as contas e extratos paginados ao modelo
        model.addAttribute("cliente", cliente);
        model.addAttribute("page", page);

        // Verifica se há uma próxima página para qualquer conta (apenas um botão global)
        boolean hasNextPage = cliente.getContaCorrente().stream()
                .anyMatch(conta -> (page + 1) * 5 < conta.getExtratos().size());
        model.addAttribute("hasNextPage", hasNextPage);

        return "conta-painel";
    }



}

