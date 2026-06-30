package Strategy;

import FactoryMethod.Pesquisador;
import StateArtigo.Artigo;

public class CompatibilidadePonderada implements CompatibilidadeStrategy {

    @Override
    public double calcular(Artigo artigo, Pesquisador revisor) {
        long comuns = artigo.getTemas().stream()
            .filter(revisor.getEspecialidades()::contains)
            .count();
        return (double) comuns / artigo.getTemas().size();
    }
}
