package Strategy;

import Util.Veredito;
import java.util.List;

public class ConsolidacaoHierarquica implements ConsolidacaoStrategy {

    @Override
    public Veredito consolidar(List<Veredito> vereditos) {
        if (vereditos.size() != 2) {
            throw new IllegalArgumentException("Esperado exatamente 2 vereditos para consolidação hierárquica.");
        }

        Veredito v1 = vereditos.get(0);
        Veredito v2 = vereditos.get(1);

        // Caso de conflito direto: ACEITO vs REJEITADO
        if ((v1 == Veredito.ACEITO && v2 == Veredito.REJEITADO) ||
            (v1 == Veredito.REJEITADO && v2 == Veredito.ACEITO)) {
            throw new IllegalStateException("CONFLITO: um revisor disse ACEITO e outro REJEITADO. É necessário consenso externo.");
        }

        // Caso contrário, prevalece o de maior peso
        return v1.getPeso() >= v2.getPeso() ? v1 : v2;
    }
}