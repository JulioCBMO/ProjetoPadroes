package StateConvite;

import java.util.List;

public class ConviteRecusado implements EstadoConvite {

    @Override
    public void aceitar(Convite convite, List<String> especialidades) {
        throw new IllegalStateException("Convite já foi recusado.");
    }

    @Override
    public void recusar(Convite convite) {
        throw new IllegalStateException("Convite já foi recusado.");
    }

    @Override
    public String getDescricao() { return "RECUSADO"; }
}
