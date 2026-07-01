package Strategy;

import FactoryMethod.Pesquisador;
import StateArtigo.Artigo;
import java.util.*;

public class DistribuidorDeArtigos {

    private CompatibilidadeStrategy strategy;

    public DistribuidorDeArtigos(CompatibilidadeStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(CompatibilidadeStrategy strategy) {
        this.strategy = strategy;
    }

    public Map<Pesquisador, List<Artigo>> distribuir(List<Artigo> artigos, List<Pesquisador> todosPesquisadores) {
        Map<Pesquisador, List<Artigo>> distribuicao = new HashMap<>();

        for (Artigo artigo : artigos) {
            Pesquisador escolhido = escolherRevisor(artigo, todosPesquisadores);

            escolhido.adicionarArtigoParaRevisar(artigo);
            artigo.adicionarRevisorDesignado(escolhido);
            artigo.distribuir(); // State: SUBMETIDO → EM_REVISAO

            distribuicao.computeIfAbsent(escolhido, k -> new ArrayList<>()).add(artigo);
        }

        return distribuicao;
    }

    private Pesquisador escolherRevisor(Artigo artigo, List<Pesquisador> todosPesquisadores) {
        // 1. Elegíveis: revisor ativo + respeita blind-review
        List<Pesquisador> elegiveis = todosPesquisadores.stream()
            .filter(Pesquisador::isRevisor)
            .filter(r -> !artigo.isAutorOuCoautor(r))
            .toList();

        if (elegiveis.isEmpty()) {
            throw new IllegalStateException(
                "Nenhum revisor elegível para o artigo: " + artigo.getTitulo());
        }

        // 2. Separa quem tem afinidade temática real (score > 0)
        List<Pesquisador> comAfinidade = elegiveis.stream()
            .filter(r -> strategy.calcular(artigo, r) > 0.0)
            .toList();

        // 3. Prioriza o grupo com afinidade; usa o resto só se necessário
        List<Pesquisador> candidatos = comAfinidade.isEmpty() ? elegiveis : comAfinidade;

        // 4. Dentro do grupo escolhido, balanceia por carga atual (menor fila primeiro)
        return candidatos.stream()
            .min(Comparator.comparingInt(r -> r.getArtigosParaRevisar().size()))
            .orElseThrow();
    }
}
