package main.java.Observer;

import main.java.Notificacao.EmailAceitacao;
import main.java.Notificacao.EmailRejeicao;
import main.java.Notificacao.EmailService;
import main.java.StateArtigo.Artigo;
/**
 * Observer concreto responsável por disparar notificações reais por e-mail (RF09).
 */
public class NotificadorEmail implements ArtigoObserver {

    private final EmailService emailService;

    public NotificadorEmail(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onEstadoAlterado(Artigo artigo) {
        String status = artigo.getStatus();

        try {
            if (status.equals("ACEITO")) {
                new EmailAceitacao(artigo, emailService).enviar();
            } else if (status.equals("REJEITADO")) {
                new EmailRejeicao(artigo, emailService).enviar();
            }
            // SUBMETIDO e EM_REVISAO não disparam e-mail
        } catch (RuntimeException e) {
            System.out.println("[Aviso] Falha ao enviar e-mail de notificação: " + e.getMessage());
        }
    }
}