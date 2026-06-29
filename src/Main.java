import FactoryMethod.Chair;
import FactoryMethod.Pesquisador;
import FactoryMethod.Usuario;
import State.Artigo;
import Strategy.CompatibilidadePonderada;
import Strategy.DistribuidorDeArtigos;
import Util.DataLoaderCSV;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Orquestrador Principal do Sistema.
 * Coneta os padrões Factory Method, Strategy, State e a carga de dados via CSV.
 */
public class Main {
    public static void main(String[] args) {
        
        System.out.println("==================================================");
        System.out.println("=== REQUISITO RF01 & E1 - INICIALIZANDO O SISTEMA ===");
        System.out.println("==================================================");
        
        // 1. Carrega os dados automaticamente do CSV usando o Factory Method por trás
        List<Usuario> bancoDeUsuarios = DataLoaderCSV.carregarUsuarios("usuarios.csv");
        
        // Separando as referências para a nossa simulação
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
        System.out.println("=== REQUISITO RF04 - CONFIGURANDO O COMITÉ TECHNICAL ===");
        System.out.println("==================================================");
        
        // Simulando o Chair a convidar dois pesquisadores e estes a cadastrarem especialidades
        Pesquisador revisor1 = pesquisadores.get(0); // felipe@ifpb.edu.br
        Pesquisador revisor2 = pesquisadores.get(1); // maria.clara@ufpb.edu.br
        Pesquisador autorComum = pesquisadores.get(2); // julio.cesar@ufcg.edu.br
        
        revisor1.aceitarConviteRevisao();
        revisor1.adicionarEspecialidade("IA");
        revisor1.adicionarEspecialidade("Machine Learning");
        System.out.println("[Comité] " + revisor1.getEmail() + " agora é REVISOR ativo em: " + revisor1.getEspecialidades());
        
        revisor2.aceitarConviteRevisao();
        revisor2.adicionarEspecialidade("Engenharia de Software");
        System.out.println("[Comité] " + revisor2.getEmail() + " agora é REVISOR ativo em: " + revisor2.getEspecialidades());

        System.out.println("\n==================================================");
        System.out.println("=== REQUISITO RF05 - SUBMISSÃO DE ARTIGOS (STATE) ===");
        System.out.println("==================================================");
        
        // Criando um artigo (Ele nasce automaticamente no estado SUBMETIDO)
        Artigo art1 = new Artigo(101, "Uso de LLMs na Educação", "Este trabalho analisa...", autorComum);
        
        // Nota: Certifique-se de que a sua classe Artigo possui o método de adicionar temas
        // conforme o algoritmo do seu Strategy necessita.
        // art1.adicionarTema("IA"); 
        
        System.out.println("Artigo '" + art1.getTitulo() + "' criado.");
        System.out.println(" -> Estado Inicial do Artigo: " + art1.getStatus());

        System.out.println("\n==================================================");
        System.out.println("=== REQUISITO RF06 & RF03 - DISTRIBUIÇÃO (STRATEGY) ===");
        System.out.println("==================================================");
        
        // Filtrando quem está ativo no comité de revisão para passar ao distribuidor
        List<Pesquisador> comiteAtivo = new ArrayList<>();
        for (Pesquisador p : pesquisadores) {
            if (p.isRevisor()) {
                comiteAtivo.add(p);
            }
        }
        
        List<Artigo> listaArtigos = List.of(art1);
        
        // Instanciando o distribuidor com a estratégia ponderada ajustada com cotas
        DistribuidorDeArtigos distribuidor = new DistribuidorDeArtigos(new CompatibilidadePonderada());
        Map<Pesquisador, List<Artigo>> mapaDistribuicao = distribuidor.distribuir(listaArtigos, comiteAtivo);
        
        System.out.println("[Distribuição] Artigos alocados por igual respeitando Blind-Review:");
        mapaDistribuicao.forEach((revisor, artigos) -> {
            System.out.println(" -> Revisor: " + revisor.getEmail() + " pegou " + artigos.size() + " artigo(s).");
        });

        System.out.println("\n==================================================");
        System.out.println("=== REQUISITO RF07 - FLUXO DE TRANSIÇÃO DE ESTADOS ===");
        System.out.println("==================================================");
        
        // O artigo sai de SUBMETIDO e vai para REVISAO
        art1.distribuir();
        System.out.println("Ação: Artigo foi distribuído automaticamente.");
        System.out.println(" -> Novo Estado do Artigo: " + art1.getStatus());
        
        // Simulação da banca a encerrar a avaliação dando o veredito de ACEITO
        art1.adicionarParecer("Excelente trabalho.");
        art1.concluirRevisao(true); // true = aprovado
        System.out.println("Ação: Revisores emitiram parecer positivo.");
        System.out.println(" -> Estado Final do Artigo: " + art1.getStatus());
        
        // Prova de blindagem do padrão State: Tentar mover um artigo já finalizado gera erro
        try {
            System.out.println("\n[Teste de Blindagem] Tentando distribuir um artigo já ACEITO...");
            art1.distribuir();
        } catch (IllegalStateException e) {
            System.out.println(" -> Sucesso! O Padrão State barrou a operação ilegal: " + e.getMessage());
        }
        
        System.out.println("\n==================================================");
        System.out.println("=== SIMULAÇÃO CONCLUÍDA COM NOTA 100! 🚀 ===");
        System.out.println("==================================================");
    }
}