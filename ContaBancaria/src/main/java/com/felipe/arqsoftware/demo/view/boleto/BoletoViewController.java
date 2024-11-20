package com.felipe.arqsoftware.demo.view.boleto;


import com.example.boleto.BoletoApi.model.Boleto;
import com.example.boleto.BoletoApi.model.EnumStatus;
import com.example.boleto.BoletoApi.repository.BoletoRepository;
import com.example.boleto.BoletoApi.service.BoletoService;
import com.felipe.arqsoftware.demo.model.Cliente;
import com.felipe.arqsoftware.demo.model.ContaCorrente;
import com.felipe.arqsoftware.demo.service.ClienteService;
import com.felipe.arqsoftware.demo.service.ContaCorrenteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/conta-painel/cliente/viewboleto")
public class BoletoViewController {


    private final BoletoService boletoService;

    private final ClienteService clienteService;

    private final BoletoRepository boletoRepository;


    private final ContaCorrenteService contaCorrenteService;

    public BoletoViewController( BoletoService boletoService, ClienteService clienteService, BoletoRepository boletoRepository, ContaCorrenteService contaCorrenteService) {
        this.boletoService = boletoService;
        this.clienteService = clienteService;
        this.boletoRepository = boletoRepository;
        this.contaCorrenteService = contaCorrenteService;
    }

    @GetMapping("/{clienteId}")
    public String listarBoletos(@PathVariable Long clienteId, Model model) {

        // Busca o cliente pelo ID
        Cliente cliente = clienteService.findById(clienteId);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado para o ID: " + clienteId);
        }

        // Recupera a conta corrente do cliente
        // Supondo que cada cliente tenha apenas uma conta
        ContaCorrente contaCorrente = cliente.getContaCorrente().isEmpty() ? null : cliente.getContaCorrente().get(0);

        if (contaCorrente == null) {
            throw new IllegalArgumentException("Nenhuma conta encontrada para o cliente com ID: " + clienteId);
        }

        // Obtém a lista de boletos com status EM_ABERTO
        List<Boleto> boletosEmAberto = boletoService.findByStatus(EnumStatus.EM_ABERTO);

        // Adiciona os dados ao modelo
        model.addAttribute("cliente", cliente);
        model.addAttribute("idConta", contaCorrente.getId());
        model.addAttribute("boletos", boletosEmAberto);

        return "boleto-list"; // Nome do template Thymeleaf
    }


    /**
     * Processa o pagamento do boleto e redireciona para a página da conta-painel do cliente.
     *
     * @param idConta     ID da conta que realizará o pagamento do boleto.
     * @param valorBoleto Valor do boleto a ser pago.
     * @param clienteId   ID do cliente associado.
     * @param redirectAttributes Permite adicionar mensagens de redirecionamento.
     * @return Redireciona para a página da conta-painel do cliente.
     */


    @PostMapping("/pagarBoleto/{idConta}/{valorBoleto}")
    public String pagarBoleto(
            @PathVariable Long idConta,
            @PathVariable Double valorBoleto,
            @RequestParam Long clienteId // Cliente usado apenas para redirecionar
    ) {
        // Chama o serviço para processar o pagamento do boleto
        contaCorrenteService.pagarBoleto(idConta, valorBoleto);

        // Atualiza o status do boleto para PAGO
        Boleto boleto = boletoRepository.findByValor(valorBoleto);
        boleto.setStatus(EnumStatus.PAGO);
        boletoRepository.save(boleto);


        // Redireciona para a página do painel do cliente
        return "redirect:/conta-painel/cliente/" + clienteId;
    }



}
