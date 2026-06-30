package main.java.Notificacao;

import main.java.StateArtigo.Artigo;

/**
 * Padrão Template Method aplicado ao envio de notificações por e-mail (RF09).
 * Define o esqueleto fixo do envio; cada subclasse decide o conteúdo.
 */
public abstract class EmailNotificacao {

    protected Artigo artigo;
    protected EmailService emailService;

    public EmailNotificacao(Artigo artigo, EmailService emailService) {
        this.artigo = artigo;
        this.emailService = emailService;
    }

    // Template Method — passos fixos, ordem garantida
    public final void enviar() {
        String destinatario = artigo.getAutorPrincipal().getEmail();
        String assunto = montarAssunto();
        String corpo = montarCorpo();
        emailService.enviar(destinatario, assunto, corpo);
    }

    protected abstract String montarAssunto();
    protected abstract String montarCorpo();

    // Reaproveitado pelas duas subclasses
    protected String formatarPareceres() {
        if (artigo.getPareceres().isEmpty()) {
            return "Nenhum parecer disponível.";
        }
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (String parecer : artigo.getPareceres()) {
            sb.append("[Revisor ").append(i++).append("]\n");
            sb.append(parecer).append("\n\n");
        }
        return sb.toString();
    }
}
