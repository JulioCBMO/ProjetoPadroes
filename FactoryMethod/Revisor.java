package FactoryMethod;

import java.util.ArrayList;
import java.util.List;

public class Revisor extends Usuario {

    private List<String> especialidades = new ArrayList<>();
    private List<Artigo> artigosParaRevisar = new ArrayList<>();

    public Revisor(String email, String senha, String instituicao) {
        super(email, senha, instituicao);
    }

    public void adicionarEspecialidade(String tema) {
        especialidades.add(tema);
    }

    public List<String> getEspecialidades() { return especialidades; }

    @Override
    public String getTipo() { return "REVISOR"; }
}
