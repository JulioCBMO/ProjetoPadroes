package Strategy;

import Util.Veredito;
import java.util.Comparator;
import java.util.List;

public class ConsolidacaoOtimista implements ConsolidacaoStrategy {

    @Override
    public Veredito consolidar(List<Veredito> vereditos) {
        if (vereditos.isEmpty()) {
            throw new IllegalArgumentException("Lista de vereditos vazia.");
        }
        // Escolhe o de maior peso (mais positivo)
        return vereditos.stream()
            .max(Comparator.comparingInt(Veredito::getPeso))
            .orElseThrow();
    }
}
