package main.java.FactoryMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry de Fábricas de Usuários.
 * Aplica o Princípio Aberto/Fechado (OCP) usando um Map estático,
 * eliminando a necessidade de if/else na criação de usuários.
 */
public class UsuarioFactoryRegistry {

    private static final Map<String, UsuarioFactory> factories = new HashMap<>();

    static {
        factories.put("PESQUISADOR", new PesquisadorFactory());
        factories.put("CHAIR",       new ChairFactory());
    }

    /**
     * Cria um usuário baseado na string de tipo (E1 - Leitura de CSV).
     */
    public static Usuario criar(String tipo, String email, String senha, String instituicao) {
        
        String tipoAjustado = tipo.toUpperCase();
        
        // Fallback para não quebrar a leitura de CSVs antigos
        if (tipoAjustado.equals("AUTOR") || tipoAjustado.equals("REVISOR")) {
            tipoAjustado = "PESQUISADOR";
        }
        
        UsuarioFactory factory = factories.get(tipoAjustado);
        if (factory == null) {
            throw new IllegalArgumentException("Tipo de usuário inválido: " + tipo);
        }
        
        return factory.criar(email, senha, instituicao);
    }
}