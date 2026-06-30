package main.java.Strategy;

import main.java.FactoryMethod.Pesquisador;
import main.java.StateArtigo.Artigo;

public class CompatibilidadePonderada implements CompatibilidadeStrategy {

    @Override
    public double calcular(Artigo artigo, Pesquisador revisor) {
        long comuns = artigo.getTemas().stream()
            .filter(revisor.getEspecialidades()::contains)
            .count();
        return (double) comuns / artigo.getTemas().size();
    }
}
