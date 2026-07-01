import Command.Command;
import Command.PainelControle;
import Command.SistemaEvento;
import Command.StartCommand;
import FacadeDashboard.DashboardFacade;
import FactoryMethod.Chair;
import FactoryMethod.Pesquisador;
import FactoryMethod.Usuario;
import Strategy.CompatibilidadePonderada;
import Strategy.DistribuidorDeArtigos;
import StateConvite.ComiteTecnico;
import StateConvite.Convite;

import Util.DataLoaderCSV;

import Notificacao.EmailService;
import Observer.NotificadorEmail;
import StateArtigo.Artigo;
import Observer.LogAuditoria;

import TemplateMethod.RelatorioRevisaoTemplate;
import TemplateMethod.RelatorioDetalhado;
import TemplateMethod.RelatorioSimples;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Orquestrador Principal do Sistema.
 * Integra e executa de forma harmoniosa os padrões Command, Factory Method, 
 * Strategy, State, Observer e Template Method.
 */
public class Main {
    public static void main(String[] args) {
        
        System.out.println("==================================================");
        System.out.println("=== REQUISITO RF01 - INICIALIZAÇÃO (COMMAND) ===");
        System.out.println("==================================================");
        
        // 1. Instancia o núcleo do sistema (Receiver)
        SistemaEvento meuEvento = new SistemaEvento("Tech Simpósio 2026");
        
        // 2. Encapsula a ação de start em um Comando
        Command startCmd = new StartCommand(meuEvento);
        
        // 3. Configura o botão do coordenador (Invoker) e dispara
        PainelControle painelChair = new PainelControle();
        painelChair.setCommand(startCmd);
        painelChair.pressionarBotao();

        System.out.println("\n==================================================");
        System.out.println("=== INICIALIZANDO USUÁRIOS (E1 - CARGA CSV) ===");
        System.out.println("==================================================");
        
        // Carga automática de dados via CSV com Factory Method integrado nos bastidores
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

        if (coordenador != null) {
            System.out.println("[Sistema] Chair responsável pelo evento: " + coordenador.getEmail());
        }

        System.out.println("\n==================================================");
        System.out.println("=== CONFIGURANDO O COMITÊ TÉCNICO (RF04) ===");
        System.out.println("==================================================");
        
        Pesquisador revisor1 = pesquisadores.get(0); // felipe@ifpb.edu.br
        Pesquisador revisor2 = pesquisadores.get(1); // maria.clara@ufpb.edu.br
        Pesquisador autorComum = pesquisadores.get(2); // julio.cesar@ufcg.edu.br

        // O Chair convida pesquisadores já cadastrados para o comitê (State: ConvitePendente)
        ComiteTecnico comite = new ComiteTecnico();

        Convite convite1 = comite.convidar(revisor1);
        convite1.aceitar(List.of("IA", "Machine Learning")); // ConvitePendente -> ConviteAceito
        System.out.println("[Comitê] " + revisor1.getEmail() + " aceitou o convite e está REVISOR.");

        Convite convite2 = comite.convidar(revisor2);
        convite2.aceitar(List.of("Engenharia de Software")); // ConvitePendente -> ConviteAceito
        System.out.println("[Comitê] " + revisor2.getEmail() + " aceitou o convite e está REVISOR.");

        System.out.println("\n==================================================");
        System.out.println("=== SUBMISSÃO DO ARTIGO (STATE & OBSERVER) ===");
        System.out.println("==================================================");
        
        // Criando o artigo (Nasce automaticamente como SUBMETIDO via State)
        Artigo art1 = new Artigo(101, "Uso de LLMs na Educação", "Este trabalho analisa...", autorComum);

        // Áreas temáticas do artigo (RF03), necessárias para o cálculo de compatibilidade
        art1.adicionarTema("IA");
        art1.adicionarTema("Machine Learning");

        // Configuração do serviço de e-mail real (RF09)
        // IMPORTANTE: defina as variáveis de ambiente EMAIL_REMETENTE e EMAIL_SENHA
        // antes de rodar (veja instruções no README).
        String emailRemetente = System.getenv("EMAIL_REMETENTE");
        String emailSenha = System.getenv("EMAIL_SENHA");

        if (emailRemetente == null || emailSenha == null) {
            System.out.println("[Aviso] Variáveis EMAIL_REMETENTE/EMAIL_SENHA não configuradas.");
            System.out.println("        O envio real de e-mail (RF09) vai falhar. Veja o README.");
        }

        EmailService emailService = new EmailService(emailRemetente, emailSenha);

        // PLUGANDO OS OBSERVERS: Vincula os disparadores automáticos de e-mail e log de auditoria
        art1.anexarObserver(new NotificadorEmail(emailService));
        art1.anexarObserver(new LogAuditoria());
        
        System.out.println("Artigo '" + art1.getTitulo() + "' inicializado com os Observers acoplados.");

        System.out.println("\n==================================================");
        System.out.println("=== DISTRIBUIÇÃO DOS ARTIGOS (STRATEGY) ===");
        System.out.println("==================================================");
        
        // Filtra o comitê ativo para passar ao motor de distribuição do Strategy
        List<Pesquisador> comiteAtivo = new ArrayList<>();
        for (Pesquisador p : pesquisadores) {
            if (p.isRevisor()) {
                comiteAtivo.add(p);
            }
        }
        
        List<Artigo> listaArtigos = List.of(art1);
        
        // Executa a estratégia ponderada com o nosso algoritmo de balanceamento de cotas
        DistribuidorDeArtigos distribuidor = new DistribuidorDeArtigos(new CompatibilidadePonderada());
        Map<Pesquisador, List<Artigo>> mapaDistribuicao = distribuidor.distribuir(listaArtigos, comiteAtivo);

        mapaDistribuicao.forEach((revisor, artigos) ->
            artigos.forEach(a ->
                System.out.println("[Distribuição] \"" + a.getTitulo() + "\" → " + revisor.getEmail())
            )
        );

        System.out.println("\n==================================================");
        System.out.println("=== TRANSIÇÕES DE ESTADO INTERNAS (STATE) ===");
        System.out.println("==================================================");
        
        // A transição de estado SUBMETIDO -> EM_REVISAO já ocorre dentro de
        // DistribuidorDeArtigos.distribuir() (chamado acima), então não repetimos aqui.
        System.out.println("-> Artigo já distribuído. Estado atual: " + art1.getStatus());
        
        System.out.println("\n-> Emitindo parecer final da banca avaliadora...");
        art1.adicionarParecer("Excelente fundamentação teórica.");
        art1.adicionarParecer("Resultados práticos bem demonstrados e validados.");
        art1.concluirRevisao(true); // true = Aceito / false = Rejeitado

        System.out.println("\n==================================================");
        System.out.println("=== GERAÇÃO DE RELATÓRIOS (TEMPLATE METHOD) ===");
        System.out.println("==================================================");
        
        // Executando as variações do esqueleto de impressão padronizado
        System.out.println("Emitindo Relatório Simples (Painel Geral do Chair):");
        RelatorioRevisaoTemplate relSimple = new RelatorioSimples();
        relSimple.gerarRelatorio(art1);
        
        System.out.println("Emitindo Relatório Detalhado (Feedback Completo para o Autor):");
        RelatorioRevisaoTemplate relDetail = new RelatorioDetalhado();
        relDetail.gerarRelatorio(art1);

        DashboardFacade dashboard = new DashboardFacade(listaArtigos, pesquisadores);
        dashboard.gerarResumo().imprimir();

        System.out.println("==================================================");
        System.out.println("=== SIMULAÇÃO CONCLUÍDA COM TODOS OS PADRÕES! 🚀 ===");
        System.out.println("==================================================");
    }
}