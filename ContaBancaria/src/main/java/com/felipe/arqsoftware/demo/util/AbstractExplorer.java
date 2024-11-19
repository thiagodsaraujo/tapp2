package com.felipe.arqsoftware.demo.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe utilitária para configurar os cabeçalhos da resposta HTTP para exportação de arquivos.
 *
 * <p>
 * Esta classe fornece um método para configurar a resposta com o tipo de conteúdo (Content-Type),
 * extensão do arquivo e um nome de arquivo apropriado, incluindo um carimbo de data/hora.
 * </p>
 *
 * <h3>Exemplo de uso:</h3>
 * <pre>{@code
 * AbstractExporter exporter = new AbstractExporter();
 * exporter.setResponseHeader(response, "application/pdf", ".pdf", "relatorio_");
 * }</pre>
 *
 * <p>
 * O exemplo acima configura a resposta HTTP para exportar um arquivo PDF chamado
 * `relatorio_yyyy-MM-dd_HH-mm-ss.pdf`.
 * </p>
 *
 * <h3>Vantagens:</h3>
 * <ul>
 *   <li>Centraliza a lógica de configuração de cabeçalhos HTTP para exportação.</li>
 *   <li>Permite reutilização para diferentes formatos de arquivos, como PDF, CSV ou Excel.</li>
 *   <li>Segue as boas práticas de HTTP, garantindo que os arquivos sejam baixados corretamente.</li>
 * </ul>
 *
 * @author Seu Nome
 * @version 1.0
 */
public class AbstractExplorer {

    /**
     * Configura os cabeçalhos da resposta HTTP para exportação de arquivos.
     *
     * <p>
     * Este método define o tipo de conteúdo, a extensão do arquivo e o cabeçalho
     * "Content-Disposition" para forçar o download do arquivo com um nome único.
     * O nome do arquivo inclui um prefixo personalizado, um carimbo de data/hora
     * e a extensão fornecida.
     * </p>
     *
     * @param response   O objeto {@link HttpServletResponse} que será configurado.
     * @param contentType O tipo de conteúdo (Content-Type), por exemplo, "application/pdf".
     * @param extension   A extensão do arquivo, por exemplo, ".pdf" ou ".csv".
     * @param prefix      O prefixo do nome do arquivo, por exemplo, "relatorio_".
     * @throws IOException Se ocorrer um erro ao configurar o cabeçalho da resposta.
     */
    public void setResponseHeader(HttpServletResponse response, String contentType,
                                  String extension, String prefix) throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormatter.format(new Date());
        String fileName = prefix + timestamp + extension;

        // Define o tipo de conteúdo
        response.setContentType(contentType);

        // Configura o cabeçalho "Content-Disposition" para forçar o download
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
    }
}

