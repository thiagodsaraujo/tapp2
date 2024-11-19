package com.felipe.arqsoftware.demo.controller;

import com.felipe.arqsoftware.demo.model.Extrato;
import com.felipe.arqsoftware.demo.util.AbstractExplorer;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * Classe responsável por exportar uma lista de extratos para um arquivo CSV.
 */
public class ExtratoCsvExporter extends AbstractExplorer {

    /**
     * Exporta uma lista de extratos para um arquivo CSV.
     *
     * <p>
     * O método configura os cabeçalhos da resposta HTTP e escreve os dados dos extratos
     * no arquivo CSV gerado.
     * </p>
     *
     * @param listExtratos A lista de extratos a ser exportada.
     * @param response     O objeto {@link HttpServletResponse} usado para configurar a resposta HTTP.
     * @throws IOException Se ocorrer algum erro durante a escrita do arquivo CSV.
     */
    public void export(List<Extrato> listExtratos, HttpServletResponse response) throws IOException {
        // Configura os cabeçalhos para exportação de CSV
        super.setResponseHeader(response, "text/csv", ".csv", "extratos_");

        // Cria um escritor CSV
        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        // Define os cabeçalhos do arquivo CSV
        String[] csvHeader = {"ID do Extrato", "Conta Corrente ID", "Movimentação", "Saldo Anterior", "Novo Saldo"};
        String[] fieldMapping = {"id", "contaCorrente.id", "movimentacao", "saldoAnterior", "novoSaldo"};

        csvBeanWriter.writeHeader(csvHeader);

        // Escreve os dados dos extratos no arquivo CSV
        for (Extrato extrato : listExtratos) {
            csvBeanWriter.write(extrato, fieldMapping);
        }

        // Fecha o escritor CSV
        csvBeanWriter.close();
    }
}

