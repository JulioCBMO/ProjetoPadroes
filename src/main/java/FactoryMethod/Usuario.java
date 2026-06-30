package main.java.FactoryMethod;

public abstract class Usuario {
    protected String email;
    protected String senha;
    protected String instituicao;

    public Usuario(String email, String senha, String instituicao) {
        this.email = email;
        this.senha = senha;
        this.instituicao = instituicao;
    }

    public String getEmail() { return email; }

    // Cada tipo sobrescreve conforme sua responsabilidade
    public abstract String getTipo();
}