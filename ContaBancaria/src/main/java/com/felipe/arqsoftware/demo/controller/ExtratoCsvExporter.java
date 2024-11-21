package com.felipe.arqsoftware.demo.controller;

import com.felipe.arqsoftware.demo.model.Extrato;
import com.felipe.arqsoftware.demo.util.AbstractExplorer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.PostMapping;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExtratoCsvExporter extends AbstractExplorer {

    /**
     * Exporta uma lista de extratos para um arquivo CSV e retorna ao cliente como resposta HTTP.
     * A operação é protegida com um Circuit Breaker para garantir que falhas no serviço sejam tratadas de forma resiliente.
     *
     * @param listExtratos A lista de extratos a ser exportada para o arquivo CSV.
     * @param response     O objeto {@link HttpServletResponse} usado para configurar a resposta HTTP.
     * @throws IOException Se ocorrer algum erro durante a escrita do arquivo CSV.
     */
    @PostMapping("/exportarCSV")
    @CircuitBreaker(name = "export-service", fallbackMethod = "fallbackExportarCSV")
    public void export(List<Extrato> listExtratos, HttpServletResponse response) throws IOException {
        // Configura os cabeçalhos para exportação de CSV
        super.setResponseHeader(response, "text/csv;charset=UTF-8", ".csv", "extratos_");

        // Define a codificação para UTF-8
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // Cria um escritor CSV
        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        // Define os cabeçalhos do arquivo CSV
        String[] csvHeader = {"ID do Extrato", "Conta Corrente ID", "Movimentação", "Saldo Anterior", "Novo Saldo"};
        String[] fieldMapping = {"id", "contaCorrenteId", "movimentacao", "saldoAnterior", "novoSaldo"};

        // Escreve os cabeçalhos no arquivo CSV
        csvBeanWriter.writeHeader(csvHeader);

        // Escreve os dados dos extratos no arquivo CSV
        for (Extrato extrato : listExtratos) {
            csvBeanWriter.write(extrato, fieldMapping);
        }

        // Fecha o escritor CSV
        csvBeanWriter.close();
    }

    /**
     * Fallback para o método {@link #export(List, HttpServletResponse)}.
     * Este método é chamado caso ocorra uma falha no Circuit Breaker, retornando uma resposta indicando erro no serviço.
     *
     * @param listExtratos A lista de extratos que seria exportada, mas não será processada devido à falha.
     * @param response     O objeto {@link HttpServletResponse} usado para configurar a resposta HTTP.
     * @param t            O erro que causou a falha no processo.
     * @throws IOException Se ocorrer algum erro ao enviar a resposta de erro.
     */
    public void fallbackExportarCSV(List<Extrato> listExtratos, HttpServletResponse response, Throwable t) throws IOException {
        // Configura a resposta de erro para o cliente
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        response.getWriter().write("Erro no serviço de exportação. Tente novamente mais tarde.");
    }
}
