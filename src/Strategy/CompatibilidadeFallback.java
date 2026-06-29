public class CompatibilidadeFallback implements CompatibilidadeStrategy {

    @Override
    public double calcular(Artigo artigo, Revisor revisor) {
        return 0.1; // score mínimo, garante distribuição mesmo sem match
    }
}