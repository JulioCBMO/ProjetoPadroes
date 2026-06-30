package StateConvite;

import java.util.List;
public interface EstadoConvite {
    void aceitar(Convite convite, List<String> especialidades);
    void recusar(Convite convite);
    String getDescricao();
}
