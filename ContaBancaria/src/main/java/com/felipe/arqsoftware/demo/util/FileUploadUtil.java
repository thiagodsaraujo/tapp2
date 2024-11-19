package com.felipe.arqsoftware.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

/**
 * Classe utilitária para operações de manipulação de arquivos.
 *
 * <p>
 * Esta classe fornece métodos para salvar arquivos em diretórios específicos,
 * limpar diretórios (removendo todos os arquivos), e excluir diretórios inteiros.
 * </p>
 *
 * <h3>Funcionalidades:</h3>
 * <ul>
 *   <li><b>Salvar Arquivos:</b> Salva um arquivo recebido (por exemplo, via upload) em um diretório especificado.</li>
 *   <li><b>Limpar Diretório:</b> Remove todos os arquivos de um diretório.</li>
 *   <li><b>Remover Diretório:</b> Remove todos os arquivos de um diretório e, em seguida, o próprio diretório.</li>
 * </ul>
 *
 * <h3>Exemplo de uso:</h3>
 * <pre>{@code
 * String uploadDir = "uploads";
 * String fileName = "imagem.jpg";
 * MultipartFile file = ...; // Objeto MultipartFile vindo de uma requisição
 *
 * FileUploadUtil.saveFile(uploadDir, fileName, file);
 * }</pre>
 *
 * @author Seu Nome
 * @version 1.0
 */
public class FileUploadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

    /**
     * Salva um arquivo em um diretório especificado.
     *
     * <p>
     * Se o diretório não existir, ele será criado automaticamente. O arquivo será
     * salvo com o nome especificado e substituirá qualquer arquivo existente com o
     * mesmo nome.
     * </p>
     *
     * @param uploadDir   O diretório onde o arquivo será salvo.
     * @param fileName    O nome do arquivo a ser salvo.
     * @param multipartFile O arquivo a ser salvo, representado como um objeto {@link MultipartFile}.
     * @throws IOException Se ocorrer um erro ao salvar o arquivo.
     */
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        // Cria o diretório caso ele não exista
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Salva o arquivo no diretório especificado
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Não foi possível salvar o arquivo: " + fileName, e);
        }
    }

    /**
     * Limpa um diretório, removendo todos os arquivos dentro dele.
     *
     * <p>
     * Se o diretório contiver subdiretórios, eles não serão removidos.
     * Apenas arquivos regulares serão excluídos.
     * </p>
     *
     * @param dir O caminho do diretório a ser limpo.
     */
    public static void cleanDir(String dir) {
        Path dirPath = Paths.get(dir);
        try {
            Files.list(dirPath).forEach(file -> {
                if (!Files.isDirectory(file)) {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        LOGGER.error("Não foi possível excluir o arquivo: " + file, e);
                        System.out.println("Não foi possível excluir este arquivo: " + file);
                    }
                }
            });
        } catch (IOException e2) {
            LOGGER.error("Não foi possível listar o diretório: " + dirPath, e2);
            System.out.println("Não foi possível listar o diretório: " + dirPath);
        }
    }

    /**
     * Remove um diretório, incluindo todos os seus arquivos.
     *
     * <p>
     * Este método chama o método {@link #cleanDir(String)} para limpar o diretório,
     * e em seguida tenta excluir o próprio diretório.
     * </p>
     *
     * @param dir O caminho do diretório a ser removido.
     */
    public static void removeDir(String dir) {
        cleanDir(dir);

        try {
            Files.delete(Paths.get(dir));
        } catch (IOException e) {
            LOGGER.error("Não foi possível remover o diretório: " + dir, e);
            System.out.println("Não foi possível remover o diretório: " + dir);
        }
    }
}
