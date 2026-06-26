import java.util.List;
import java.util.Map;

public class DistribuidorDeArtigos {

    private CompatibilidadeStrategy strategy;

    public DistribuidorDeArtigos(CompatibilidadeStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(CompatibilidadeStrategy strategy) {
        this.strategy = strategy;
    }

    public Map<Revisor, List<Artigo>> distribuir(List<Artigo> artigos, List<Revisor> revisores) {
        Map<Revisor, List<Artigo>> distribuicao = new HashMap<>();

        for (Artigo artigo : artigos) {
            Revisor melhor = revisores.stream()
                .filter(r -> !artigo.isAutorOuCoautor(r)) // blind-review
                .max(Comparator.comparingDouble(r -> strategy.calcular(artigo, r)))
                .orElseThrow(() -> new RuntimeException("Nenhum revisor disponível"));

            distribuicao.computeIfAbsent(melhor, k -> new ArrayList<>()).add(artigo);
        }

        return distribuicao;
    }
}
