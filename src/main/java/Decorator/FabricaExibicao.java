package main.java.Decorator;


import main.java.StateArtigo.Artigo;

public class FabricaExibicao {

    public static ArtigoExibivel montarExibicao(Artigo artigo) {
        ArtigoExibivel exibicao = new ArtigoBase(artigo);

        if (artigo.getSelos().contains("MELHOR_ARTIGO")) {
            exibicao = new SeloMelhorArtigo(exibicao);
        }
        if (artigo.getSelos().contains("MENCAO_HONROSA")) {
            exibicao = new SeloMencaoHonrosa(exibicao);
        }
        return exibicao;
    }
}
