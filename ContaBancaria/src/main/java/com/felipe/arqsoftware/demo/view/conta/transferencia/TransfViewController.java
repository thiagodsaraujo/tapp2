package com.felipe.arqsoftware.demo.view.conta.transferencia;

import com.felipe.arqsoftware.demo.model.Cliente;
import com.felipe.arqsoftware.demo.model.ContaCorrente;
import com.felipe.arqsoftware.demo.service.ClienteService;
import com.felipe.arqsoftware.demo.service.ContaCorrenteService;
import com.felipe.arqsoftware.demo.service.exceptions.ContaNotFoundException;
import com.felipe.arqsoftware.demo.service.exceptions.SaldoInsuficienteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/conta-painel/transfer")
public class TransfViewController {

    @Autowired
    private ContaCorrenteService service;

    @Autowired
    private ClienteService clienteService;

    // Exibe o formulário de transferência
    @GetMapping("/{clienteId}")
    public String exibirFormularioTransferencia(@PathVariable Long clienteId, Model model) {
        // Busca o cliente pelo ID
        Cliente cliente = clienteService.findById(clienteId);

        // Obtém a conta corrente do cliente (supondo que ele tenha uma única conta)
        ContaCorrente contaOrigem = cliente.getContaCorrente().isEmpty() ? null : cliente.getContaCorrente().get(0);

        // Adiciona o cliente e a contaOrigem ao modelo
        model.addAttribute("cliente", cliente);
        model.addAttribute("contaOrigem", contaOrigem);

        return "transferencia-form"; // Nome do template Thymeleaf
    }

    @PostMapping("/transferir")
    public String operacaoTransferencia(@RequestParam int numContaOrigem,
                                        @RequestParam Double valor,
                                        @RequestParam int numContaDestino,
                                        @RequestParam Long clienteId,
                                        Model model) {
        try {
            // Executa a transferência usando o serviço
            service.transferir(numContaOrigem, valor, numContaDestino);

            // Redireciona para o painel do cliente após a transferência
            return "redirect:/conta-painel/cliente/" + clienteId;
        } catch (SaldoInsuficienteException e) {
            model.addAttribute("error", "Saldo insuficiente para realizar a transferência.");
            return "transferencia-form";
        } catch (ContaNotFoundException e) {
            model.addAttribute("error", "Conta de origem ou destino não encontrada.");
            return "transferencia-form";
        }
    }


//    // Processa a transferência a partir do formulário
//    @PostMapping("/transferir")
//    public String operacaoTransferencia(@RequestParam int numContaOrigem,
//                                        @RequestParam Double valor,
//                                        @RequestParam int numContaDestino,
//                                        Model model) {
//        try {
//            service.transferir(numContaOrigem, valor, numContaDestino);
//            return "redirect:/conta-painel/cliente/1"; // Redireciona para o painel da conta origem
//        } catch (SaldoInsuficienteException e) {
//            model.addAttribute("error", "Saldo insuficiente para realizar a transferência.");
//            return "transferencia-form"; // Retorna para o formulário com mensagem de erro
//        } catch (ContaNotFoundException e) {
//            model.addAttribute("error", "Conta de origem ou destino não encontrada.");
//            return "transferencia-form"; // Retorna para o formulário com mensagem de erro
//        }
//    }


}
