package main.java.Notificacao;

import main.java.StateArtigo.Artigo;

public class EmailRejeicao extends EmailNotificacao {

    public EmailRejeicao(Artigo artigo, EmailService emailService) {
        super(artigo, emailService);
    }

    @Override
    protected String montarAssunto() {
        return "Resultado da submissão - Artigo nº " + artigo.getId();
    }

    @Override
    protected String montarCorpo() {
        return String.format("""
            Prezado(a) Sr(a). %s:

            Lamentamos informar que seu artigo de nº %d intitulado "%s" \
            não pôde ser aceito para publicação.

            Ao final do email, seguem os pareceres dos revisores, que esperamos \
            que possam auxiliá-lo em futuras submissões.

            Agradecemos sua submissão.

            Atenciosamente,
            Coordenação do Comitê de Programa

            %s
            """,
            artigo.getAutorPrincipal().getEmail(),
            artigo.getId(),
            artigo.getTitulo(),
            formatarPareceres()
        );
    }
}