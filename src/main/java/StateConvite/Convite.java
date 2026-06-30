package StateConvite;

import FactoryMethod.Pesquisador;
import java.util.List;

public class Convite {

    private Pesquisador pesquisador;
    private EstadoConvite estado = new ConvitePendente();

    public Convite(Pesquisador pesquisador) {
        this.pesquisador = pesquisador;
    }

    public void aceitar(List<String> especialidades) {
        estado.aceitar(this, especialidades);
    }

    public void recusar() {
        estado.recusar(this);
    }

    public void setEstado(EstadoConvite estado) { this.estado = estado; }
    public Pesquisador getPesquisador() { return pesquisador; }
    public String getStatus() { return estado.getDescricao(); }
}
