package main.java.FactoryMethod;

/**
 * Fábrica Concreta (Factory Method) para criar Pesquisadores.
 * Isola a lógica de instanciação conforme o RF02.
 */
public class PesquisadorFactory implements UsuarioFactory {

    @Override
    public Usuario criar(String email, String senha, String instituicao) {
        return new Pesquisador(email, senha, instituicao);
    }
}