package FactoryMethod;

public class AutorFactory implements UsuarioFactory {
    @Override
    public Usuario criar(String email, String senha, String instituicao) {
        return new Autor(email, senha, instituicao);
    }
}