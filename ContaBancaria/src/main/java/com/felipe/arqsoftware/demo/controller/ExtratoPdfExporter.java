package com.felipe.arqsoftware.demo.controller;

import com.felipe.arqsoftware.demo.model.Extrato;
import com.felipe.arqsoftware.demo.util.AbstractExplorer;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Classe responsável por exportar uma lista de extratos para um arquivo PDF.
 */
public class ExtratoPdfExporter extends AbstractExplorer {

    /**
     * Exporta uma lista de extratos para um arquivo PDF.
     *
     * @param listExtratos A lista de extratos a ser exportada.
     * @param response     O objeto {@link HttpServletResponse} para configurar a resposta HTTP.
     * @throws IOException Se ocorrer algum erro durante a geração do PDF.
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
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10f);
        table.setWidths(new float[]{1.5f, 2.5f, 2.5f, 2.5f, 2.5f});

        // Escreve o cabeçalho e os dados da tabela
        writeTableHeader(table);
        writeTableData(table, listExtratos);

        document.add(table);

        document.close();
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.gray);
        cell.setPadding(5);

        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.COURIER);
        font.setSize(10);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("#", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Nome", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("CPF", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Conta Corrente", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Saldo Anterior", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Novo Saldo", font));
        table.addCell(cell);
    }

    /**
     * Escreve os cabeçalhos da tabela no PDF.
     *
     * @param table A tabela onde os cabeçalhos serão adicionados.
     */

    /**
     * Escreve os dados da tabela no PDF.
     *
     * @param table       A tabela onde os dados serão adicionados.
     * @param listExtratos A lista de extratos cujos dados serão escritos.
     */
    private void writeTableData(PdfPTable table, List<Extrato> listExtratos) {

        int cont = 0;



        for (Extrato extrato : listExtratos) {

            cont++;

            table.addCell(String.valueOf(cont));
            table.addCell(String.valueOf(extrato.getId()));
            table.addCell(String.format("%.2f", extrato.getContaCorrente().getCliente().getName()));
            table.addCell(String.format("%.2f", extrato.getContaCorrente().getCliente().getCpf()));
            table.addCell(String.format("%.2f", extrato.getContaCorrente().getId()));
            table.addCell(String.format("%.2f", extrato.getSaldoAnterior()));
            table.addCell(String.format("%.2f", extrato.getNovoSaldo()));
        }
    }
}
