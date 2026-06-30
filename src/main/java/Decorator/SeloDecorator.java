package Decorator;

public abstract class SeloDecorator implements ArtigoExibivel {

    protected final ArtigoExibivel artigoDecorado;

    public SeloDecorator(ArtigoExibivel artigoDecorado) {
        this.artigoDecorado = artigoDecorado;
    }

    @Override
    public String exibir() {
        return artigoDecorado.exibir();
    }
}
