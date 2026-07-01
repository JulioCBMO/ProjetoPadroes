package FacadeDashboard;

import java.util.List;

public class DashboardDTO {

    private final int totalArtigos;
    private final int totalRevisores;
    private final int artigosAvaliados;
    private final int artigosPendentes;
    private final List<PendenciaDTO> pendencias;
    private final List<String> artigosAceitosComSelos;

    public DashboardDTO(int totalArtigos, int totalRevisores, int artigosAvaliados,
                         int artigosPendentes, List<PendenciaDTO> pendencias,
                         List<String> artigosAceitosComSelos) {
        this.totalArtigos = totalArtigos;
        this.totalRevisores = totalRevisores;
        this.artigosAvaliados = artigosAvaliados;
        this.artigosPendentes = artigosPendentes;
        this.pendencias = pendencias;
        this.artigosAceitosComSelos = artigosAceitosComSelos;
    }

    public int getTotalArtigos() { return totalArtigos; }
    public int getTotalRevisores() { return totalRevisores; }
    public int getArtigosAvaliados() { return artigosAvaliados; }
    public int getArtigosPendentes() { return artigosPendentes; }
    public List<PendenciaDTO> getPendencias() { return pendencias; }
    public List<String> getArtigosAceitosComSelos() { return artigosAceitosComSelos; }

    public void imprimir() {
        System.out.println("\n========== DASHBOARD DO EVENTO ==========");
        System.out.println("Artigos submetidos:   " + totalArtigos);
        System.out.println("Revisores ativos:     " + totalRevisores);
        System.out.println("Artigos avaliados:    " + artigosAvaliados);
        System.out.println("Artigos pendentes:    " + artigosPendentes);
        System.out.println("------------------------------------------");

        if (artigosAceitosComSelos != null && !artigosAceitosComSelos.isEmpty()) {
            System.out.println("Artigos ACEITOS com selos:");
            for (String linha : artigosAceitosComSelos) {
                System.out.println("  " + linha);
            }
        } else {
            System.out.println("Nenhum artigo aceito com selo ainda.");
        }

        System.out.println("------------------------------------------");
        if (pendencias.isEmpty()) {
            System.out.println("Nenhuma pendência no momento.");
        } else {
            System.out.println("Pendências por revisor:");
            for (PendenciaDTO p : pendencias) {
                System.out.println(" -> " + p.artigoTitulo() + "  (revisor: " + p.revisorEmail() + ")");
            }
        }
        System.out.println("==========================================\n");
    }
}