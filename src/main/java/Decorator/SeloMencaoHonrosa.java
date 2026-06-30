package main.java.Decorator;

public class SeloMencaoHonrosa extends SeloDecorator {

    public SeloMencaoHonrosa(ArtigoExibivel artigoDecorado) {
        super(artigoDecorado);
    }

    @Override
    public String exibir() {
        return super.exibir() + " ⭐ Menção Honrosa";
    }
}