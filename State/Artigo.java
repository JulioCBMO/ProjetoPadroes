package State;

import FactoryMethod.Pesquisador;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de Contexto do padrão State.
 * Mantém os dados do artigo científico e delega o controle do ciclo de vida
 * para as classes de estado concreto (RF05, RF06, RF07).
 */
public class Artigo {

    private final int id;
    private final String titulo;
    private final String resumo;
    private final Pesquisador autorPrincipal;
    private final List<Pesquisador> coautores = new ArrayList<>();
    private final List<String> pareceres = new ArrayList<>();
    
    // Referência ao estado atual do ciclo de vida
    private EstadoArtigo estadoAtual;

    public Artigo(int id, String titulo, String resumo, Pesquisador autorPrincipal) {
        this.id = id;
        this.titulo = titulo;
        this.resumo = resumo;
        this.autorPrincipal = autorPrincipal;
        // Todo artigo nasce obrigatoriamente no estado Submetido (RF05)
        this.estadoAtual = new EstadoSubmetido();
    }

    // --- Delegação de Comportamento para o Padrão State ---
    
    public void submeter() { estadoAtual.submeter(this); }
    public void distribuir() { estadoAtual.distribuir(this); }
    public void concluirRevisao(boolean aprovado) { estadoAtual.concluirRevisao(this, aprovado); }

    /** Altera o estado atual (Usado internamente pelas classes de Estado Concreto). */
    protected void setEstado(EstadoArtigo novoEstado) {
        this.estadoAtual = novoEstado;
    }

    public String getStatus() {
        return estadoAtual.getStatus();
    }

    // --- Métodos Auxiliares de Negócio ---

    /** Validação de blind-review: impede que autores revisem o próprio trabalho (RF06). */
    public boolean isAutorOuCoautor(Pesquisador p) {
        if (autorPrincipal.getEmail().equals(p.getEmail())) return true;
        return coautores.stream().anyMatch(c -> c.getEmail().equals(p.getEmail()));
    }

    public void adicionarCoautor(Pesquisador p) {
        this.coautores.add(p);
    }

    /** Armazena as críticas e contribuições enviadas pelos revisores (RF07). */
    public void adicionarParecer(String parecer) {
        this.pareceres.add(parecer);
    }

    // --- Getters simples ---
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getResumo() { return resumo; }
    public Pesquisador getAutorPrincipal() { return autorPrincipal; }
    public List<Pesquisador> getCoautores() { return coautores; }
    public List<String> getPareceres() { return pareceres; }
}