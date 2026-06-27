import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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
        
        revisores.forEach(r -> distribuicao.put(r, new ArrayList<>()));

        int limitePorRevisor = (int) Math.ceil((double) artigos.size() / revisores.size());

        for (Artigo artigo : artigos) {
            Revisor melhor = revisores.stream()
                .filter(r -> !artigo.isAutorOuCoautor(r)) // blind-review
                .filter(r -> distribuicao.get(r).size() < limitePorRevisor) 
                .max(Comparator.comparingDouble(r -> strategy.calcular(artigo, r)))
                .orElseThrow(() -> new RuntimeException("Não foi possível distribuir mantendo as regras de limite."));

            distribuicao.get(melhor).add(artigo);
        }

        return distribuicao;
    }
}