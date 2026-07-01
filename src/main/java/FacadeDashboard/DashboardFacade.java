package FacadeDashboard;

import Decorator.FabricaExibicao;
import FactoryMethod.Pesquisador;
import StateArtigo.Artigo;
import java.util.*;

public class DashboardFacade {

    private final List<Artigo> todosArtigos;
    private final List<Pesquisador> todosPesquisadores;

    public DashboardFacade(List<Artigo> todosArtigos, List<Pesquisador> todosPesquisadores) {
        this.todosArtigos = todosArtigos;
        this.todosPesquisadores = todosPesquisadores;
    }

    public DashboardDTO gerarResumo() {
        int totalArtigos = todosArtigos.size();

        int totalRevisores = (int) todosPesquisadores.stream()
            .filter(Pesquisador::isRevisor)
            .count();

        int artigosAvaliados = (int) todosArtigos.stream()
            .filter(a -> a.getStatus().equals("ACEITO") || a.getStatus().equals("REJEITADO"))
            .count();

        int artigosPendentes = (int) todosArtigos.stream()
            .filter(a -> a.getStatus().equals("REVISAO"))
            .count();

        List<PendenciaDTO> pendencias = montarPendencias();

        // Lista de artigos aceitos com selos (usando Decorator)
        List<String> artigosAceitosComSelos = todosArtigos.stream()
            .filter(a -> a.getStatus().equals("ACEITO"))
            .map(a -> FabricaExibicao.montarExibicao(a).exibir())
            .toList();

        return new DashboardDTO(totalArtigos, totalRevisores, artigosAvaliados,
                                 artigosPendentes, pendencias, artigosAceitosComSelos);
    }

    /**
     * Cruza cada artigo pendente com o revisor responsável (RF08).
     */
    private List<PendenciaDTO> montarPendencias() {
        List<PendenciaDTO> pendencias = new ArrayList<>();

        List<Artigo> emRevisao = todosArtigos.stream()
            .filter(a -> a.getStatus().equals("REVISAO"))
            .toList();

        for (Pesquisador revisor : todosPesquisadores) {
            if (!revisor.isRevisor()) continue;

            for (Artigo artigo : revisor.getArtigosParaRevisar()) {
                if (emRevisao.contains(artigo)) {
                    pendencias.add(new PendenciaDTO(artigo.getTitulo(), revisor.getEmail()));
                }
            }
        }

        return pendencias;
    }
}