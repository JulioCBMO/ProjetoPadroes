package Strategy;

import FactoryMethod.Pesquisador;
import StateArtigo.Artigo;

public class CompatibilidadeExata implements CompatibilidadeStrategy {

    @Override
    public double calcular(Artigo artigo, Pesquisador revisor) {
        long temas = artigo.getTemas().stream()
            .filter(revisor.getEspecialidades()::contains)
            .count();
        return temas > 0 ? 1.0 : 0.0;
    }
}
