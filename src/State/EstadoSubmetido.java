package State;

/**
 * Estado inicial do artigo (RF05).
 * O artigo foi recebido pelo sistema e está aguardando a distribuição automática.
 */
public class EstadoSubmetido implements EstadoArtigo {

    @Override
    public void submeter(Artigo artigo) {
        throw new IllegalStateException("O artigo já foi submetido.");
    }

    @Override
    public void distribuir(Artigo artigo) {
        // Transiciona o artigo para o próximo estado do fluxo (RF06)
        artigo.setEstado(new EstadoEmRevisao());
    }

    @Override
    public void concluirRevisao(Artigo artigo, boolean aprovado) {
        throw new IllegalStateException("Não é possível avaliar um artigo ainda não distribuído.");
    }

    @Override
    public String getStatus() {
        return "SUBMETIDO";
    }
}