public class CompatibilidadePonderada implements CompatibilidadeStrategy {

    @Override
    public double calcular(Artigo artigo, Revisor revisor) {
        long comuns = artigo.getTemas().stream()
            .filter(revisor.getEspecialidades()::contains)
            .count();
        return (double) comuns / artigo.getTemas().size();
    }
}
