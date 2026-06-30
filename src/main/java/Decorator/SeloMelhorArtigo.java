package main.java.Decorator;

public class SeloMelhorArtigo extends SeloDecorator {

    public SeloMelhorArtigo(ArtigoExibivel artigoDecorado) {
        super(artigoDecorado);
    }

    @Override
    public String exibir() {
        return "🏆 [Melhor Artigo da Trilha] " + super.exibir();
    }
}