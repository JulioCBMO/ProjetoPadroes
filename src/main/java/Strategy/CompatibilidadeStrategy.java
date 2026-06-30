package Strategy;

import FactoryMethod.Pesquisador;
import StateArtigo.Artigo;

public interface CompatibilidadeStrategy {
    double calcular(Artigo artigo, Pesquisador revisor);
}

