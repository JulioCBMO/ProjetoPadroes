package Command;

import java.time.LocalDate;

/**
 * Receiver do padrão Command (RF01).
 * Contém a lógica real de negócio e o estado global do evento acadêmico.
 */
public class SistemaEvento {
    
    private final String nomeEvento;
    private LocalDate dataLimite;
    private boolean abertoParaSubmissoes;

    public SistemaEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
        this.abertoParaSubmissoes = false;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    /** Lógica real de reset e inicialização do sistema */
    public void start() {
        System.out.println("[SistemaEvento] Resetando cache e limpando registros temporários...");
        this.abertoParaSubmissoes = true;
        System.out.println("[SistemaEvento] O evento '" + nomeEvento + "' foi INICIALIZADO e está aceitando submissões!");
    }

    public boolean isAberto() {
        return abertoParaSubmissoes;
    }

    public boolean isDentroDoPrazo() {
        if (dataLimite == null) return true; // sem prazo definido => sempre aberto
        return LocalDate.now().isBefore(dataLimite) || LocalDate.now().isEqual(dataLimite);
    }
}
