package Strategy;

import Util.Veredito;
import java.util.List;

public interface ConsolidacaoStrategy {
    /**
     * Dada uma lista de vereditos (um por revisor), retorna o veredito consolidado.
     * Pode lançar exceção se não for possível consolidar.
     */
    Veredito consolidar(List<Veredito> vereditos);
}
