package main.java.FactoryMethod;

public class ChairFactory implements UsuarioFactory {
    @Override
    public Usuario criar(String email, String senha, String instituicao) {
        return new Chair(email, senha, instituicao);
    }
}