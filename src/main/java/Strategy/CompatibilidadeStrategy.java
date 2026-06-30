package main.java.Strategy;

import main.java.FactoryMethod.Pesquisador;
import main.java.StateArtigo.Artigo;

public interface CompatibilidadeStrategy {
    double calcular(Artigo artigo, Pesquisador revisor);
}

