public class CompatibilidadeExata implements CompatibilidadeStrategy {

    @Override
    public double calcular(Artigo artigo, Revisor revisor) {
        long temas = artigo.getTemas().stream()
            .filter(revisor.getEspecialidades()::contains)
            .count();
        return temas > 0 ? 1.0 : 0.0;
    }
}