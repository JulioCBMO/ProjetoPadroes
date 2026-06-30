package main.java.FactoryMethod;

public interface UsuarioFactory {
    Usuario criar(String email, String senha, String instituicao);
}
