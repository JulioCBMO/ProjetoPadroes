package StateConvite;

import FactoryMethod.Pesquisador;
import java.util.List;

public class ConvitePendente implements EstadoConvite {

    @Override
    public void aceitar(Convite convite, List<String> especialidades) {
        Pesquisador pesquisador = convite.getPesquisador();
        especialidades.forEach(pesquisador::adicionarEspecialidade);
        pesquisador.aceitarConviteRevisao(); 
        convite.setEstado(new ConviteAceito());
    }

    @Override
    public void recusar(Convite convite) {
        convite.setEstado(new ConviteRecusado());
    }

    @Override
    public String getDescricao() { return "PENDENTE"; }
}
