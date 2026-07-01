package Notificacao;

import Decorator.ArtigoExibivel;
import Decorator.FabricaExibicao;
import StateArtigo.Artigo;

public class EmailAceitacao extends EmailNotificacao {

    public EmailAceitacao(Artigo artigo, EmailService emailService) {
        super(artigo, emailService);
    }

    @Override
    protected String montarAssunto() {
        return "Parabéns! Artigo aceito - nº " + artigo.getId();
    }

    @Override
    protected String montarCorpo() {
        ArtigoExibivel exibicao = FabricaExibicao.montarExibicao(artigo);
        return String.format("""
            Prezado(a) Sr(a). %s:

            Parabéns! Sua submissão de nº %d, intitulada "%s", foi aceita.

            As avaliações estão disponíveis ao final do e-mail.

            Atenciosamente,
            Coordenação do Comitê de Programa

            %s
            """,
            artigo.getAutorPrincipal().getEmail(),
            artigo.getId(),
            exibicao.exibir(),
            formatarPareceres()
        );
    }
}