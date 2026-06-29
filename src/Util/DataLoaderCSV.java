package Util;

import FactoryMethod.Usuario;
import FactoryMethod.UsuarioFactoryRegistry;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitário de leitura de arquivos para inicialização do sistema.
 * Atende à Exigência E1 (Povoar dados automaticamente) e E2 (Fácil execução).
 * Utiliza classes nativas de IO para evitar dependências externas.
 */
public class DataLoaderCSV {

    /**
     * Lê o CSV e delega a instanciação para o Factory Method (RF02).
     * * @param caminhoArquivo Caminho relativo do arquivo CSV (ex: "usuarios.csv").
     * @return Lista de Usuários instanciados e prontos para uso.
     */
    public static List<Usuario> carregarUsuarios(String caminhoArquivo) {
        List<Usuario> usuariosCarregados = new ArrayList<>();

        // Try-with-resources: garante que o arquivo será fechado da memória automaticamente
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                // Pula o cabeçalho do CSV
                if (primeiraLinha && linha.toLowerCase().contains("tipo")) {
                    primeiraLinha = false;
                    continue;
                }

                // Quebra a linha nas vírgulas
                String[] colunas = linha.split(",");
                
                // Valida se a linha tem os 4 campos necessários
                if (colunas.length >= 4) {
                    String tipo = colunas[0].trim();
                    String email = colunas[1].trim();
                    String senha = colunas[2].trim();
                    String instituicao = colunas[3].trim();

                    // MÁGICA AQUI: O CSV não dá o 'new'. Ele pede para o Registry fabricar!
                    Usuario u = UsuarioFactoryRegistry.criar(tipo, email, senha, instituicao);
                    usuariosCarregados.add(u);
                }
            }
            // Único print tolerado, pois é um feedback de carga do sistema para o console E1
            System.out.println("[Sistema] " + usuariosCarregados.size() + " usuários carregados com sucesso do CSV.");

        } catch (IOException e) {
            System.err.println("[Erro Fatal] Não foi possível ler o arquivo CSV. Verifique o caminho: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("[Erro de Validação] Ocorreu um problema ao processar o CSV: " + e.getMessage());
        }

        return usuariosCarregados;
    }
}