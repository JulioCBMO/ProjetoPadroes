package FactoryMethod;

import java.util.ArrayList;
import java.util.List;

public class Autor extends Usuario {

    private List<Artigo> artigosSubmetidos = new ArrayList<>();

    public Autor(String email, String senha, String instituicao) {
        super(email, senha, instituicao);
    }

    public void submeterArtigo(Artigo artigo) {
        artigosSubmetidos.add(artigo);
    }

    @Override
    public String getTipo() { return "AUTOR"; }
}