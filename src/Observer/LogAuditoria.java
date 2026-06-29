package Observer;

import State.Artigo;

/**
 * Observer concreto que registra logs de auditoria no sistema.
 * Demonstra a capacidade de plugar múltiplos observadores sem alterar o Artigo.
 */
public class LogAuditoria implements ArtigoObserver {

    @Override
    public void onEstadoAlterado(Artigo artigo) {
        System.out.println(" 📝 [LOG DE SISTEMA] O Artigo ID " + artigo.getId() + 
                           " mudou o estado interno para " + artigo.getStatus() + ".");
    }
}