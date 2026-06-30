package main.java.StateConvite;

import java.util.List;

public class ConviteAceito implements EstadoConvite {

    @Override
    public void aceitar(Convite convite, List<String> especialidades) {
        throw new IllegalStateException("Convite já foi aceito.");
    }

    @Override
    public void recusar(Convite convite) {
        throw new IllegalStateException("Convite já foi aceito, não pode ser recusado.");
    }

    @Override
    public String getDescricao() { return "ACEITO"; }
}
