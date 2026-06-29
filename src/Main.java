import FactoryMethod.Chair;
import FactoryMethod.Pesquisador;
import FactoryMethod.Usuario;
import State.Artigo;
import Strategy.CompatibilidadePonderada;
import Strategy.DistribuidorDeArtigos;
import Util.DataLoaderCSV;
import Observer.NotificadorEmail;
import Observer.LogAuditoria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Orquestrador Principal do Sistema.
 * Interconecta os padrões Factory Method, Strategy, State e Observer.
 */
public class Main {
    public static void main(String[] args) {
        
        System.out.println("==================================================");
        System.out.println("=== REQUISITO RF01 & E1 - INICIALIZANDO O SISTEMA ===");
        System.out.println("==================================================");
        
        // 1. Carga automática de dados via CSV (Factory Method integrado)
        List<Usuario> bancoDeUsuarios = DataLoaderCSV.carregarUsuarios("usuarios.csv");
        
        Chair coordenador = null;
        List<Pesquisador> pesquisadores = new ArrayList<>();
        
        for (Usuario u : bancoDeUsuarios) {
            if (u instanceof Chair) {
                coordenador = (Chair) u;
            } else if (u instanceof Pesquisador) {
                pesquisadores.add((Pesquisador) u);
            }
        }

        System.out.println("\n==================================================");
        System.out.println("=== REQUISITO RF04 - CONFIGURANDO O COMITÉ TÉCNICO ===");
        System.out.println("==================================================");
        
        // Simulando a ativação de revisores e cadastro de especialidades
        Pesquisador revisor1 = pesquisadores.get(0); // felipe@ifpb.edu.br
        Pesquisador revisor2 = pesquisadores.get(1); // maria.clara@ufpb.edu.br
        Pesquisador autorComum = pesquisadores.get(2); // julio.cesar@ufcg.edu.br
        
        revisor1.aceitarConviteRevisao();
        revisor1.adicionarEspecialidade("IA");
        revisor1.adicionarEspecialidade("Machine Learning");
        System.out.println("[Comité] " + revisor1.getEmail() + " ativado como REVISOR.");
        
        revisor2.aceitarConviteRevisao();
        revisor2.adicionarEspecialidade("Engenharia de Software");
        System.out.println("[Comité] " + revisor2.getEmail() + " ativado como REVISOR.");

        System.out.println("\n==================================================");
        System.out.println("=== REQUISITO RF05 & OBSERVER - CRIANDO O ARTIGO ===");
        System.out.println("==================================================");
        
        // Criando o artigo (Nasce automaticamente como SUBMETIDO via State)
        Artigo art1 = new Artigo(101, "Uso de LLMs na Educação", "Este trabalho analisa...", autorComum);
        
        // PLUGANDO OS OBSERVERS: Vincula os disparadores automáticos de e-mail e log
        art1.anexarObserver(new NotificadorEmail());
        art1.anexarObserver(new LogAuditoria());
        
        System.out.println("Artigo '" + art1.getTitulo() + "' inicializado.");

        System.out.println("\n==================================================");
        System.out.println("=== REQUISITO RF06 & RF03 - DISTRIBUIÇÃO (STRATEGY) ===");
        System.out.println("==================================================");
        
        // Filtra o comité ativo para passar ao motor de distribuição
        List<Pesquisador> comiteAtivo = new ArrayList<>();
        for (Pesquisador p : pesquisadores) {
            if (p.isRevisor()) {
                comiteAtivo.add(p);
            }
        }
        
        List<Artigo> listaArtigos = List.of(art1);
        
        // Executa a estratégia ponderada com balanceamento de cotas
        DistribuidorDeArtigos distribuidor = new DistribuidorDeArtigos(new CompatibilidadePonderada());
        Map<Pesquisador, List<Artigo>> mapaDistribuicao = distribuidor.distribuir(listaArtigos, comiteAtivo);
        
        System.out.println("[Distribuição] Alocação finalizada:");
        mapaDistribuicao.forEach((revisor, artigos) -> {
            System.out.println(" -> Revisor: " + revisor.getEmail() + " recebeu " + artigos.size() + " artigo(s).");
        });

        System.out.println("\n==================================================");
        System.out.println("=== REQUISITO RF07 & RF09 - TRANSIÇÕES E ALERTAS ===");
        System.out.println("==================================================");
        
        // O disparo das ações altera o State e aciona o Observer automaticamente
        System.out.println("Ação: Disparando distribuição do artigo...");
        art1.distribuir(); 
        
        System.out.println("\nAção: Emitindo parecer final da banca...");
        art1.adicionarParecer("Excelente fundamentação teórica.");
        art1.concluirRevisao(true); // true = Aceito / false = Rejeitado

        System.out.println("\n==================================================");
        System.out.println("=== TESTE DE BLINDAGEM DO FLUXO (STATE) ===");
        System.out.println("==================================================");
        
        // Prova de consistência: Tentar mover um artigo já finalizado gera exceção
        try {
            System.out.println("Tentando redistribuir um artigo que já foi ACEITO...");
            art1.distribuir();
        } catch (IllegalStateException e) {
            System.out.println(" -> Sucesso! O Padrão State barrou a operação: " + e.getMessage());
        }
        
        System.out.println("\n==================================================");
        System.out.println("=== SIMULAÇÃO CONCLUÍDA COM SUCESSO! 🚀 ===");
        System.out.println("==================================================");
    }
}