package main.java.FactoryMethod;

public class Chair extends Usuario {

    public Chair(String email, String senha, String instituicao) {
        super(email, senha, instituicao);
    }

    @Override
    public String getTipo() { return "CHAIR"; }
}
