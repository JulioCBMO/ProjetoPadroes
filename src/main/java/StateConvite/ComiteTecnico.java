package StateConvite;

import FactoryMethod.Pesquisador;
import java.util.ArrayList;
import java.util.List;

public class ComiteTecnico {

    private List<Convite> convites = new ArrayList<>();

    public Convite convidar(Pesquisador pesquisador) {
        if (pesquisador.isRevisor()) {
            throw new IllegalStateException("Pesquisador já é revisor.");
        }
        Convite convite = new Convite(pesquisador);
        convites.add(convite);
        return convite;
    }

    public List<Pesquisador> getRevisoresAtivos() {
        return convites.stream()
            .filter(c -> c.getStatus().equals("ACEITO"))
            .map(Convite::getPesquisador)
            .toList();
    }
}
