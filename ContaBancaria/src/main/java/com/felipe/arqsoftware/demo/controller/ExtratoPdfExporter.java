package com.felipe.arqsoftware.demo.controller;

import com.felipe.arqsoftware.demo.model.Extrato;
import com.felipe.arqsoftware.demo.util.AbstractExplorer;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;


/**
 * Classe responsável por exportar uma lista de extratos bancários para um arquivo PDF.
 * Esta classe utiliza a biblioteca **iText** para gerar o documento PDF com os dados dos extratos.
 */
public class ExtratoPdfExporter extends AbstractExplorer {

    /**
     * Exporta uma lista de extratos para um arquivo PDF e retorna ao cliente como resposta HTTP.
     * O documento PDF contém os dados dos extratos formatados em uma tabela, incluindo informações como
     * ID do extrato, ID da conta corrente, movimentação, saldo anterior e novo saldo.
     *
     * @param listExtratos A lista de extratos a ser exportada para o arquivo PDF.
     * @param response     O objeto {@link HttpServletResponse} usado para configurar a resposta HTTP.
     * @throws IOException Se ocorrer algum erro durante a escrita do arquivo PDF.
     */
    public void export(List<Extrato> listExtratos, HttpServletResponse response) throws IOException {
        // Configura os cabeçalhos da resposta para o arquivo PDF
        super.setResponseHeader(response, "application/pdf", ".pdf", "extratos_");

        // Cria um documento PDF
        Document document = new Document(PageSize.A4);

        // Inicializa o escritor de PDF
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Adiciona um título ao PDF
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        titleFont.setSize(18);
        titleFont.setColor(Color.BLUE);

        Paragraph title = new Paragraph("Extratos Bancários", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" ")); // Adiciona uma linha em branco

        // Cria a tabela para os dados dos extratos
        PdfPTable table = new PdfPTable(5); // Número de colunas ajustado para os campos do modelo
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10f);
        table.setWidths(new float[]{1.0f, 2.5f, 2.5f, 2.5f, 2.5f});

        // Escreve o cabeçalho e os dados da tabela
        writeTableHeader(table);
        writeTableData(table, listExtratos);

        document.add(table);

        document.close();
    }

    /**
     * Escreve o cabeçalho da tabela no documento PDF.
     * O cabeçalho contém as colunas: número sequencial, ID do extrato, ID da conta corrente, movimentação e novo saldo.
     *
     * @param table A tabela onde os cabeçalhos serão escritos.
     */
    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.GRAY);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(10);
        font.setColor(Color.WHITE);

        // Escreve os cabeçalhos das colunas
        cell.setPhrase(new Phrase("#", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("ID Extrato", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("ID Conta Corrente", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Movimentação", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Novo Saldo", font));
        table.addCell(cell);
    }

    /**
     * Escreve os dados dos extratos na tabela do documento PDF.
     * Cada linha da tabela é preenchida com os dados de cada extrato, incluindo ID, ID da conta corrente,
     * movimentação e saldo final, com cores condicionais para a movimentação (verde para saldo positivo e
     * vermelho para saldo negativo).
     *
     * @param table         A tabela onde os dados dos extratos serão escritos.
     * @param listExtratos  A lista de extratos a ser exportada para o PDF.
     */
    private void writeTableData(PdfPTable table, List<Extrato> listExtratos) {
        int cont = 0;

        // Itera sobre os extratos e escreve cada um na tabela
        for (Extrato extrato : listExtratos) {
            cont++;

            table.addCell(String.valueOf(cont)); // Número sequencial
            table.addCell(String.valueOf(extrato.getId())); // ID do extrato
            table.addCell(String.valueOf(extrato.getContaCorrente().getId())); // ID da conta corrente

            // Determina a cor da movimentação com base no impacto no saldo
            PdfPCell movimentacaoCell = new PdfPCell(new Phrase(String.format("%.2f", extrato.getMovimentacao())));
            movimentacaoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            if (extrato.getNovoSaldo() > extrato.getSaldoAnterior()) {
                movimentacaoCell.setBackgroundColor(new Color(198, 239, 206)); // Verde claro
            } else {
                movimentacaoCell.setBackgroundColor(new Color(255, 205, 210)); // Vermelho claro
            }
            table.addCell(movimentacaoCell);

            // Novo saldo
            PdfPCell novoSaldoCell = new PdfPCell(new Phrase(String.format("%.2f", extrato.getNovoSaldo())));
            novoSaldoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(novoSaldoCell);
        }

        // Adiciona uma última linha para exibir o saldo final
        PdfPCell totalCell = new PdfPCell(new Phrase("Saldo Final"));
        totalCell.setColspan(4); // Combina as colunas até a última
        totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalCell.setBackgroundColor(Color.LIGHT_GRAY);
        table.addCell(totalCell);

        double saldoFinal = listExtratos.get(listExtratos.size() - 1).getNovoSaldo();
        PdfPCell saldoFinalCell = new PdfPCell(new Phrase(String.format("%.2f", saldoFinal)));
        saldoFinalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        saldoFinalCell.setBackgroundColor(Color.LIGHT_GRAY);
        table.addCell(saldoFinalCell);
    }
}



