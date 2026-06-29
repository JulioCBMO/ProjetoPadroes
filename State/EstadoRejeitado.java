package State;

/**
 * Estado terminal de recusa (RF09).
 * O artigo não atingiu os critérios de publicação. O fluxo está encerrado.
 */
public class EstadoRejeitado implements EstadoArtigo {

    @Override
    public void submeter(Artigo artigo) {
        throw new IllegalStateException("Artigo finalizado. Alterações não são permitidas.");
    }

    @Override
    public void distribuir(Artigo artigo) {
        throw new IllegalStateException("Artigo rejeitado não pode ser redistribuído.");
    }

    @Override
    public void concluirRevisao(Artigo artigo, boolean aprovado) {
        throw new IllegalStateException("A revisão deste artigo já foi encerrada como REJEITADO.");
    }

    @Override
    public String getStatus() {
        return "REJEITADO";
    }
}