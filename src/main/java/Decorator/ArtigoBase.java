package main.java.Decorator;



import main.java.StateArtigo.Artigo;

public class ArtigoBase implements ArtigoExibivel {

    private final Artigo artigo;

    public ArtigoBase(Artigo artigo) {
        this.artigo = artigo;
    }

    @Override
    public String exibir() {
        return "Artigo nº " + artigo.getId() + " - \"" + artigo.getTitulo() + "\"";
    }
}
