package main.java.Command;

/**
 * Invoker do padrão Command.
 * Responsável por armazenar e disparar os comandos (Desacopla a interface da lógica).
 */
public class PainelControle {
    
    private Command slot;

    public void setCommand(Command command) {
        this.slot = command;
    }

    public void pressionarBotao() {
        if (slot != null) {
            slot.executar();
        } else {
            System.out.println("[Erro] Nenhum comando configurado no painel.");
        }
    }
}