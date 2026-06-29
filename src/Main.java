import Command.Command;
import Command.PainelControle;
import Command.SistemaEvento;
import Command.StartCommand;

import FactoryMethod.Chair;
import FactoryMethod.Pesquisador;
import FactoryMethod.Usuario;

import State.Artigo;

import Strategy.CompatibilidadePonderada;
import Strategy.DistribuidorDeArtigos;

import Util.DataLoaderCSV;

import Observer.NotificadorEmail;
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

        System.out.println("\n==================================================");
        System.out.println("=== CONFIGURANDO O COMITÊ TÉCNICO (RF04) ===");
        System.out.println("==================================================");
        
        // Simulando a ativação de revisores e cadastro de especialidades
        Pesquisador revisor1 = pesquisadores.get(0); // felipe@ifpb.edu.br
        Pesquisador revisor2 = pesquisadores.get(1); // maria.clara@ufpb.edu.br
        Pesquisador autorComum = pesquisadores.get(2); // julio.cesar@ufcg.edu.br
        
        revisor1.aceitarConviteRevisao();
        revisor1.adicionarEspecialidade("IA");
        revisor1.adicionarEspecialidade("Machine Learning");
        System.out.println("[Comitê] " + revisor1.getEmail() + " ativado como REVISOR.");
        
        revisor2.aceitarConviteRevisao();
        revisor2.adicionarEspecialidade("Engenharia de Software");
        System.out.println("[Comitê] " + revisor2.getEmail() + " ativado como REVISOR.");

        System.out.println("\n==================================================");
        System.out.println("=== SUBMISSÃO DO ARTIGO (STATE & OBSERVER) ===");
        System.out.println("==================================================");
        
        // Criando o artigo (Nasce automaticamente como SUBMETIDO via State)
        Artigo art1 = new Artigo(101, "Uso de LLMs na Educação", "Este trabalho analisa...", autorComum);
        
        // PLUGANDO OS OBSERVERS: Vincula os disparadores automáticos de e-mail e log de auditoria
        art1.anexarObserver(new NotificadorEmail());
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
        
        System.out.println("[Distribuição] Alocação finalizada com sucesso pelo algoritmo.");

        System.out.println("\n==================================================");
        System.out.println("=== TRANSIÇÕES DE ESTADO INTERNAS (STATE) ===");
        System.out.println("==================================================");
        
        // O disparo das ações altera o State e aciona os Logs/E-mails do Observer na hora!
        System.out.println("-> Executando comando de distribuição do artigo...");
        art1.distribuir(); 
        
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

        System.out.println("==================================================");
        System.out.println("=== SIMULAÇÃO CONCLUÍDA COM TODOS OS PADRÕES! 🚀 ===");
        System.out.println("==================================================");
    }
}