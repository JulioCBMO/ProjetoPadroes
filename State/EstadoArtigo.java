package State;

/**
 * Interface base para o padrão State (RF05, RF06, RF07).
 * Define as operações cujo comportamento varia dependendo do ciclo de vida do artigo.
 */
public interface EstadoArtigo {
    void submeter(Artigo artigo);
    void distribuir(Artigo artigo);
    void concluirRevisao(Artigo artigo, boolean aprovado);
    String getStatus();
}