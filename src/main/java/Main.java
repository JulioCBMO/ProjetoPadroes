import Command.Command;
import Command.PainelControle;
import Command.SistemaEvento;
import Command.StartCommand;
import Decorator.FabricaExibicao;
import FacadeDashboard.DashboardFacade;
import FactoryMethod.AreaTematica;
import FactoryMethod.Chair;
import FactoryMethod.Pesquisador;
import FactoryMethod.Usuario;
import Notificacao.EmailService;
import Observer.LogAuditoria;
import Observer.NotificadorEmail;
import StateArtigo.Artigo;
import StateConvite.ComiteTecnico;
import StateConvite.Convite;
import Strategy.CompatibilidadePonderada;
import Strategy.ConsolidacaoHierarquica;
import Strategy.ConsolidacaoOtimista;
import Strategy.ConsolidacaoStrategy;
import Strategy.DistribuidorDeArtigos;
import TemplateMethod.RelatorioDetalhado;
import TemplateMethod.RelatorioRevisaoTemplate;
import TemplateMethod.RelatorioSimples;
import Util.DataLoaderCSV;
import Util.Veredito;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Orquestrador Principal do Sistema — Menu Interativo.
 * Permite ao usuário navegar pelos requisitos funcionais via console.
 */
public class Main {

    // --- Estado global da sessão ---
    
    static SistemaEvento evento = null;
    static Chair coordenador = null;
    static List<Pesquisador> pesquisadores = new ArrayList<>();
    static ComiteTecnico comite = new ComiteTecnico();
    static List<Artigo> listaArtigos = new ArrayList<>();
    static EmailService emailService = null;
    static boolean distribuido = false;
    static int proximoIdArtigo = 101;
    static LocalDate dataLimiteEvento = null;
    static ConsolidacaoStrategy estrategiaConsolidacao = new ConsolidacaoHierarquica(); // padrão


    public static void main(String[] args) {
        // Encoding UTF-8 para o console
        System.out.println("EMAIL_REMETENTE = " + System.getenv("EMAIL_REMETENTE"));
        System.out.println("EMAIL_SENHA = " + (System.getenv("EMAIL_SENHA") != null ? "****" : "null"));
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        Scanner sc = new Scanner(System.in, java.nio.charset.StandardCharsets.UTF_8);
       
        // Carga automática do CSV na inicialização (E1)
        carregarCSV();

        // Configura e-mail (usa variáveis de ambiente se disponíveis)
        String emailRemetente = System.getenv("EMAIL_REMETENTE");
        String emailSenha     = System.getenv("EMAIL_SENHA");
        emailService = new EmailService(emailRemetente, emailSenha);

        exibirBoasVindas();

        boolean rodando = true;
        while (rodando) {
            exibirMenu();
            System.out.print("Opcao: ");
            String opcao = sc.nextLine().trim();

            switch (opcao) {
            case "1"  -> rf01InicializarEvento(sc);
            case "2"  -> rf03CadastrarAreas(sc);      
            case "3"  -> rf02ListarUsuarios();
            case "4"  -> rf04ConvidarRevisor(sc);
            case "5"  -> rf05SubmeterArtigo(sc);
            case "6"  -> rf06DistribuirArtigos();
            case "7"  -> rf07ConcluirRevisao(sc);
            case "8"  -> rf08Dashboard();
            case "9"  -> rf09NotificarAutores();
            case "10" -> rf10AplicarSelo(sc);
            case "11" -> rf11EscolherEstrategia(sc);
            case "0"  -> rodando = false;
            default   -> System.out.println("[!] Opcao invalida.");
}
        }

        System.out.println("\nSistema encerrado. Ate logo!");
        sc.close();
    }

    // =========================================================
    // RF01 — Command: inicializar evento
    // =========================================================
    static void rf01InicializarEvento(Scanner sc) {
    separador("RF01 - INICIALIZAR EVENTO (Command)");
    System.out.print("Nome do evento: ");
    String nome = sc.nextLine().trim();

    System.out.print("Data limite para submissão (formato AAAA-MM-DD, ou Enter para ignorar): ");
    String dataStr = sc.nextLine().trim();
    LocalDate dataLimite = null;
    if (!dataStr.isEmpty()) {
        try {
            dataLimite = LocalDate.parse(dataStr);
        } catch (DateTimeParseException e) {
            System.out.println("[!] Data inválida. Usando sem prazo.");
        }
    }

    evento = new SistemaEvento(nome);
    if (dataLimite != null) {
        evento.setDataLimite(dataLimite);
    }
    Command startCmd = new StartCommand(evento);
    PainelControle painel = new PainelControle();
    painel.setCommand(startCmd);
    painel.pressionarBotao();

    // Limpa estado anterior
    listaArtigos.clear();
    comite = new ComiteTecnico();
    distribuido = false;
    proximoIdArtigo = 101;
    AreaTematica.limparAreas(); // limpa áreas do evento anterior

}

    // =========================================================
    // RF02 — Factory Method: listar usuários carregados do CSV
    // =========================================================
    static void rf02ListarUsuarios() {
        separador("RF02 - USUARIOS CADASTRADOS (Factory Method)");
        if (coordenador != null) {
            System.out.println("[CHAIR]      " + coordenador.getEmail()
                + " | " + coordenador.getInstituicao());
        }
        if (pesquisadores.isEmpty()) {
            System.out.println("Nenhum pesquisador carregado.");
            return;
        }
        for (Pesquisador p : pesquisadores) {
            String papel = p.isRevisor() ? "REVISOR" : "AUTOR  ";
            System.out.println("[" + papel + "]  " + p.getEmail()
                + " | " + p.getInstituicao()
                + (p.isRevisor() ? " | temas: " + p.getEspecialidades() : ""));
        }
    }

    static void rf03CadastrarAreas(Scanner sc) {
    separador("RF03 - CADASTRO DE ÁREAS TEMÁTICAS");
    System.out.println("Áreas já cadastradas: " + AreaTematica.getAreasCadastradas());
    System.out.print("Digite as áreas separadas por vírgula (ex: IA, Machine Learning, Visão Computacional): ");
    String[] areas = sc.nextLine().trim().split(",");
    for (String a : areas) {
        AreaTematica.cadastrarArea(a.trim());
    }
    System.out.println("Áreas cadastradas com sucesso: " + AreaTematica.getAreasCadastradas());
}

    // =========================================================
    // RF04 — State (Convite): convidar revisor
    // =========================================================
    static void rf04ConvidarRevisor(Scanner sc) {
        separador("RF04 - CONVIDAR REVISOR (State - Convite)");

        List<Pesquisador> naoRevisores = pesquisadores.stream()
            .filter(p -> !p.isRevisor()).toList();

        if (naoRevisores.isEmpty()) {
            System.out.println("Todos os pesquisadores ja sao revisores.");
            return;
        }

        System.out.println("Pesquisadores disponiveis para convite:");
        for (int i = 0; i < naoRevisores.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + naoRevisores.get(i).getEmail());
        }

        System.out.print("Escolha o numero: ");
        int idx = lerInt(sc) - 1;
        if (idx < 0 || idx >= naoRevisores.size()) {
            System.out.println("[!] Numero invalido.");
            return;
        }

        Pesquisador escolhido = naoRevisores.get(idx);
        System.out.print("Informe as especialidades separadas por virgula: ");
        String[] temas = sc.nextLine().trim().split(",");
        List<String> especialidades = new ArrayList<>();
        for (String t : temas) especialidades.add(t.trim());

        Convite convite = comite.convidar(escolhido);
        convite.aceitar(especialidades);
        System.out.println("[Comite] " + escolhido.getEmail()
            + " aceitou o convite! Especialidades: " + especialidades);
    }

    // =========================================================
    // RF05 — State (Artigo): submeter artigo
    // =========================================================
    static void rf05SubmeterArtigo(Scanner sc) {
    separador("RF05 - SUBMETER ARTIGO (State - Artigo)");

    // Verifica se o sistema está aberto e dentro do prazo (RF01)
    if (evento == null || !evento.isAberto()) {
        System.out.println("[!] O sistema não está aberto para submissões. Inicialize o evento primeiro.");
        return;
    }
    if (!evento.isDentroDoPrazo()) {
        System.out.println("[!] O prazo de submissão já expirou. Data limite: " + evento.getDataLimite());
        return;
    }

    if (pesquisadores.isEmpty()) {
        System.out.println("[!] Nenhum pesquisador cadastrado.");
        return;
    }

    System.out.print("Titulo do artigo: ");
    String titulo = sc.nextLine().trim();

    System.out.print("Resumo: ");
    String resumo = sc.nextLine().trim();

    System.out.println("Autor principal:");
    for (int i = 0; i < pesquisadores.size(); i++) {
        System.out.println("  " + (i + 1) + ". " + pesquisadores.get(i).getEmail());
    }
    System.out.print("Escolha o numero: ");
    int idxAutor = lerInt(sc) - 1;
    if (idxAutor < 0 || idxAutor >= pesquisadores.size()) {
        System.out.println("[!] Numero invalido.");
        return;
    }
    Pesquisador autor = pesquisadores.get(idxAutor);

    // --- Coautores (RF05) ---
    List<Pesquisador> coautores = new ArrayList<>();
    System.out.println("Informe os e-mails dos coautores (separados por vírgula, ou Enter para nenhum):");
    String coautoresStr = sc.nextLine().trim();
    if (!coautoresStr.isEmpty()) {
        String[] emails = coautoresStr.split(",");
        for (String email : emails) {
            Pesquisador coautor = pesquisadores.stream()
                .filter(p -> p.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst()
                .orElse(null);
            if (coautor == null) {
                System.out.println("[!] Coautor " + email.trim() + " não encontrado. Ignorado.");
            } else {
                coautores.add(coautor);
            }
        }
    }

    // --- Temas (validando com áreas cadastradas) ---
    System.out.print("Temas separados por virgula (ex: IA,Machine Learning). Devem estar nas áreas cadastradas: ");
    String[] temasStr = sc.nextLine().trim().split(",");
    List<String> temasValidos = new ArrayList<>();
    for (String t : temasStr) {
        String area = t.trim();
        if (AreaTematica.areaExiste(area)) {
            temasValidos.add(area);
        } else {
            System.out.println("[!] Área '" + area + "' não cadastrada. Será ignorada.");
        }
    }
    if (temasValidos.isEmpty()) {
        System.out.println("[!] Nenhum tema válido. A submissão não pode prosseguir.");
        return;
    }

    int id = proximoIdArtigo++;
    Artigo artigo = new Artigo(id, titulo, resumo, autor);
    for (String t : temasValidos) {
        artigo.adicionarTema(t);
    }
    for (Pesquisador c : coautores) {
        artigo.adicionarCoautor(c);
    }

    artigo.anexarObserver(new NotificadorEmail(emailService));
    artigo.anexarObserver(new LogAuditoria());

    autor.submeterArtigo(artigo);
    listaArtigos.add(artigo);
    distribuido = false;

    System.out.println("[RF05] Artigo #" + id + " \"" + titulo
        + "\" submetido! Status: " + artigo.getStatus() + " Coautores: " + coautores.size());
}

    // =========================================================
    // RF06 — Strategy + Observer: distribuir artigos
    // =========================================================
    static void rf06DistribuirArtigos() {
        separador("RF06 - DISTRIBUICAO AUTOMATICA (Strategy + Observer)");

        if (listaArtigos.isEmpty()) {
            System.out.println("[!] Nenhum artigo submetido ainda.");
            return;
        }

        List<Pesquisador> revisores = pesquisadores.stream()
            .filter(Pesquisador::isRevisor).toList();

        if (revisores.isEmpty()) {
            System.out.println("[!] Nenhum revisor no comite. Use a opcao 3 primeiro.");
            return;
        }

        DistribuidorDeArtigos distribuidor =
            new DistribuidorDeArtigos(new CompatibilidadePonderada());

        List<Artigo> pendentes = listaArtigos.stream()
            .filter(a -> a.getStatus().equals("SUBMETIDO")).toList();

        if (pendentes.isEmpty()) {
            System.out.println("[!] Todos os artigos ja foram distribuidos.");
            return;
        }

        try {
            Map<Pesquisador, List<Artigo>> mapa =
            distribuidor.distribuir(pendentes, pesquisadores);

        mapa.forEach((revisor, artigos) ->
            artigos.forEach(a ->
                System.out.println("[Distribuicao] \"" + a.getTitulo()
                    + "\" -> " + revisor.getEmail())
            )
        );
        distribuido = true;
        
    } catch (IllegalStateException e) {
        System.out.println("[!] " + e.getMessage());
        System.out.println("   Possíveis causas:");
        System.out.println("   - Não há revisores no comitê (convide pela opção 4)");
        System.out.println("   - Todos os revisores são autores/coautores do artigo");
        System.out.println("   - Nenhum revisor com afinidade temática suficiente");
    }
    }

    // =========================================================
    // RF07 — Template Method: concluir revisão
    // =========================================================
    static void rf07ConcluirRevisao(Scanner sc) {
    separador("RF07 - CONCLUIR REVISAO (Template Method)");

    List<Artigo> emRevisao = listaArtigos.stream()
        .filter(a -> a.getStatus().equals("REVISAO")).toList();

    if (emRevisao.isEmpty()) {
        System.out.println("[!] Nenhum artigo em revisao no momento.");
        return;
    }

    System.out.println("Artigos em revisao:");
    for (int i = 0; i < emRevisao.size(); i++) {
        System.out.println("  " + (i + 1) + ". #"
            + emRevisao.get(i).getId() + " - " + emRevisao.get(i).getTitulo());
    }
    System.out.print("Escolha o numero: ");
    int idx = lerInt(sc) - 1;
    if (idx < 0 || idx >= emRevisao.size()) {
        System.out.println("[!] Numero invalido.");
        return;
    }

    Artigo artigo = emRevisao.get(idx);

    System.out.print("Contribuicoes / pontos positivos: ");
    String contribuicoes = sc.nextLine().trim();

    System.out.print("Pontos negativos / criticas: ");
    String criticas = sc.nextLine().trim();

    System.out.println("Veredito:");
    System.out.println("  1. Aceito");
    System.out.println("  2. Fracamente aceito");
    System.out.println("  3. Fracamente recusado");
    System.out.println("  4. Recusado");
    System.out.print("Escolha: ");
    int v = lerInt(sc);

    String vereditoStr = switch (v) {
        case 1 -> "Aceito";
        case 2 -> "Fracamente aceito";
        case 3 -> "Fracamente rejeitado";
        case 4 -> "Rejeitado";
        default -> "Rejeitado";
    };

    Veredito veredito = Veredito.fromString(vereditoStr);

    // Pergunta qual revisor está avaliando (simulação de login)
    System.out.print("Informe seu email (revisor): ");
    String emailRevisor = sc.nextLine().trim();

    // Valida se o revisor está designado para este artigo
    boolean designado = artigo.getRevisoresDesignados().stream()
        .anyMatch(r -> r.getEmail().equals(emailRevisor));
    if (!designado) {
        System.out.println("[!] Este revisor não está designado para avaliar este artigo.");
        return;
    }

    // Armazena o veredito
    try {
        artigo.adicionarVeredito(emailRevisor, veredito);
    } catch (IllegalStateException e) {
        System.out.println("[!] " + e.getMessage());
        return;
    }

    // Adiciona o parecer textual (armazenamento histórico)
    artigo.adicionarParecer(
        "Contribuicoes: " + contribuicoes
        + "\nCriticas: " + criticas
        + "\nVeredito: " + vereditoStr
    );

    // Verifica se todos os revisores designados já avaliaram
    if (artigo.todosRevisoresAvaliaram()) {
        try {
            // Pega os vereditos armazenados (ordem não importa)
            List<Veredito> listaVereditos = new ArrayList<>(artigo.getVereditos().values());
            Veredito consolidado = estrategiaConsolidacao.consolidar(listaVereditos);
            boolean aprovado = consolidado.isAprovado();
            artigo.concluirRevisao(aprovado); // muda estado para ACEITO ou REJEITADO
            System.out.println("[Sistema] Artigo consolidado com veredito: " + consolidado);
            // Exibe relatórios (Template Method)
            new RelatorioDetalhado().gerarRelatorio(artigo);
            new RelatorioSimples().gerarRelatorio(artigo);
        } catch (IllegalStateException e) {
            // Conflito detectado (Hierárquica)
            System.out.println("[Sistema] " + e.getMessage());
            System.out.println("O artigo permanece em REVISAO até que os revisores entrem em consenso.");
            // Não altera o estado
        }
    } else {
        int pendentes = artigo.getRevisoresDesignados().size() - artigo.getVereditos().size();
        System.out.println("[Sistema] Aguardando avaliação dos demais revisores (" + pendentes + " pendente(s)).");
    }
}

    // =========================================================
    // RF08 — Facade: dashboard
    // =========================================================
    static void rf08Dashboard() {
        separador("RF08 - DASHBOARD (Facade)");
        DashboardFacade dashboard = new DashboardFacade(listaArtigos, pesquisadores);
        dashboard.gerarResumo().imprimir();
    }

    // =========================================================
    // RF09 — Template Method + Observer: notificar autores
    // =========================================================
    static void rf09NotificarAutores() {
    separador("RF09 - NOTIFICACAO EM MASSA (reenvio forçado)");
    List<Artigo> finalizados = listaArtigos.stream()
        .filter(a -> a.getStatus().equals("ACEITO") || a.getStatus().equals("REJEITADO"))
        .toList();

    if (finalizados.isEmpty()) {
        System.out.println("[!] Nenhum artigo finalizado ainda.");
        return;
    }

    System.out.println("Reenviando notificações para " + finalizados.size() + " artigo(s)...");
    for (Artigo a : finalizados) {
        // Chama o observer novamente (garantia de envio)
        NotificadorEmail notificador = new NotificadorEmail(emailService);
        notificador.onEstadoAlterado(a);
        System.out.println("  - Enviado para " + a.getAutorPrincipal().getEmail());
    }
    System.out.println("Notificações reenviadas com sucesso.");
}

    // =========================================================
    // RF10 — Decorator: aplicar selo ao artigo
    // =========================================================
    static void rf10AplicarSelo(Scanner sc) {
        separador("RF10 - APLICAR SELO DE DESTAQUE (Decorator)");

        List<Artigo> aceitos = listaArtigos.stream()
            .filter(a -> a.getStatus().equals("ACEITO")).toList();

        if (aceitos.isEmpty()) {
            System.out.println("[!] Nenhum artigo aceito ainda.");
            return;
        }

        System.out.println("Artigos aceitos:");
        for (int i = 0; i < aceitos.size(); i++) {
            System.out.println("  " + (i + 1) + ". \"" + aceitos.get(i).getTitulo()
                + "\" | Selos: " + aceitos.get(i).getSelos());
        }
        System.out.print("Escolha o numero: ");
        int idx = lerInt(sc) - 1;
        if (idx < 0 || idx >= aceitos.size()) {
            System.out.println("[!] Numero invalido.");
            return;
        }

        Artigo artigo = aceitos.get(idx);

        System.out.println("Selos disponiveis:");
        System.out.println("  1. Melhor Artigo da Trilha");
        System.out.println("  2. Mencao Honrosa");
        System.out.print("Escolha: ");
        int selo = lerInt(sc);

        if (selo == 1) {
            artigo.adicionarSelo("MELHOR_ARTIGO");
            System.out.println("[RF10] Selo 'Melhor Artigo da Trilha' aplicado!");
        } else if (selo == 2) {
            artigo.adicionarSelo("MENCAO_HONROSA");
            System.out.println("[RF10] Selo 'Mencao Honrosa' aplicado!");
        } else {
            System.out.println("[!] Opcao invalida.");
            return;
        }

        System.out.println("[RF10] Exibicao com Decorator: "
            + FabricaExibicao.montarExibicao(artigo).exibir());
    }

    static void rf11EscolherEstrategia(Scanner sc) {
        separador("ESCOLHER ESTRATÉGIA DE CONSOLIDAÇÃO");
        System.out.println("1. Hierárquica (com conflito)");
        System.out.println("2. Otimista (sempre mais positivo)");
        System.out.print("Escolha: ");
        int op = lerInt(sc);
        if (op == 1) {
            estrategiaConsolidacao = new ConsolidacaoHierarquica();
            System.out.println("Estratégia definida: Hierárquica.");
        }   else if (op == 2) {
            estrategiaConsolidacao = new ConsolidacaoOtimista();
            System.out.println("Estratégia definida: Otimista.");
        }   else {
            System.out.println("Opção inválida.");
        }
    }


    // =========================================================
    // Utilitários
    // =========================================================
    static void carregarCSV() {
        List<Usuario> banco = DataLoaderCSV.carregarUsuarios("usuarios.csv");
        for (Usuario u : banco) {
            if (u instanceof Chair c) coordenador = c;
            else if (u instanceof Pesquisador p) pesquisadores.add(p);
        }
        System.out.println("[E1] " + banco.size()
            + " usuarios carregados do CSV automaticamente.");
    }

    static void exibirBoasVindas() {
        System.out.println("==================================================");
        System.out.println("  SISTEMA DE SUBMISSAO DE ARTIGOS CIENTIFICOS");
        System.out.println("  Disciplina: Padroes de Projeto de Software");
        System.out.println("==================================================");
    }

    static void exibirMenu() {
        System.out.println("\n--------------------------------------------------");
        System.out.println("  MENU PRINCIPAL");
        System.out.println("--------------------------------------------------");
        System.out.println("  1. Inicializar novo evento           (RF01 - Command)");
        System.out.println("  2. Cadastrar áreas temáticas         (RF03 - Factory Method)"); 
        System.out.println("  3. Listar usuários cadastrados       (RF02 - Factory Method)");
        System.out.println("  4. Convidar revisor para o comite    (RF04 - State Convite)");
        System.out.println("  5. Submeter artigo                   (RF05 - State Artigo)");
        System.out.println("  6. Distribuir artigos automaticamente(RF06 - Strategy)");
        System.out.println("  7. Concluir revisao de artigo        (RF07 - Template Method)");
        System.out.println("  8. Exibir dashboard                  (RF08 - Facade)");
        System.out.println("  9. Notificar autores por e-mail      (RF09 - Observer)");
        System.out.println(" 10. Aplicar selo ao artigo            (RF10 - Decorator)");
        System.out.println(" 11. Escolher estratégia de consolidação (RF11 - Strategy)");
        System.out.println("  0. Sair");
        System.out.println("--------------------------------------------------");
    }

    static void separador(String titulo) {
        System.out.println("\n==================================================");
        System.out.println("  " + titulo);
        System.out.println("==================================================");
    }

    static int lerInt(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}