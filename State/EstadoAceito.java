package State;

/**
 * Estado terminal de sucesso (RF09).
 * O artigo foi aprovado para publicação. Nenhuma modificação futura é permitida.
 */
public class EstadoAceito implements EstadoArtigo {

    @Override
    public void submeter(Artigo artigo) {
        throw new IllegalStateException("Artigo finalizado. Alterações não são permitidas.");
    }

    @Override
    public void distribuir(Artigo artigo) {
        throw new IllegalStateException("Artigo aprovado não pode ser redistribuído.");
    }

    @Override
    public void concluirRevisao(Artigo artigo, boolean aprovado) {
        throw new IllegalStateException("A revisão deste artigo já foi encerrada como ACEITO.");
    }

    @Override
    public String getStatus() {
        return "ACEITO";
    }
}