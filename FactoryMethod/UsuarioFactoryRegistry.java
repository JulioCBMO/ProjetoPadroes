package FactoryMethod;

import java.util.HashMap;
import java.util.Map;

public class UsuarioFactoryRegistry {

    private static final Map<String, UsuarioFactory> factories = new HashMap<>();

    static {
        factories.put("AUTOR",   new AutorFactory());
        factories.put("REVISOR", new RevisorFactory());
        factories.put("CHAIR",   new ChairFactory());
    }

    public static Usuario criar(String tipo, String email,
                                 String senha, String instituicao) {
        UsuarioFactory factory = factories.get(tipo.toUpperCase());
        if (factory == null) {
            throw new IllegalArgumentException("Tipo de usuário inválido: " + tipo);
        }
        return factory.criar(email, senha, instituicao);
    }
}