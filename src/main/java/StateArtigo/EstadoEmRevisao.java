package StateArtigo;

/**
 * Estado ativo de avaliação (RF06).
 * O artigo foi distribuído aos revisores e está coletando pareceres técnicos.
 */
public class EstadoEmRevisao implements EstadoArtigo {

    @Override
    public void submeter(Artigo artigo) {
        throw new IllegalStateException("Não é possível alterar um artigo em fase de revisão.");
    }

    @Override
    public void distribuir(Artigo artigo) {
        throw new IllegalStateException("Este artigo já foi distribuído para os revisores.");
    }

    @Override
    public void concluirRevisao(Artigo artigo, boolean aprovado) {
        // Define o veredito final com base na decisão do comitê (RF07)
        if (aprovado) {
            artigo.setEstado(new EstadoAceito());
        } else {
            artigo.setEstado(new EstadoRejeitado());
        }
    }

    @Override
    public String getStatus() {
        return "REVISAO";
    }
}
