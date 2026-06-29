package Observer;

import State.Artigo;

/**
 * Observer concreto responsável por disparar notificações por e-mail 
 */
public class NotificadorEmail implements ArtigoObserver {

    @Override
    public void onEstadoAlterado(Artigo artigo) {
        String emailAutor = artigo.getAutorPrincipal().getEmail();
        String status = artigo.getStatus();
        
        System.out.println(" 📧 [E-mail disparado para " + emailAutor + "] " +
                           "O status do seu artigo '" + artigo.getTitulo() + 
                           "' foi atualizado para: " + status);
    }
}