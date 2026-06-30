package main.java.Command;

/**
 * Comando concreto para inicializar o sistema (RF01).
 * Registra o log da operação e delega a execução para o Receiver.
 */
public class StartCommand implements Command {
    
    private final SistemaEvento sistema;

    public StartCommand(SistemaEvento sistema) {
        this.sistema = sistema;
    }

    @Override
    public void executar() {
        System.out.println("📝 [Log Auditoria Command] Disparando rotina de inicialização segura...");
        sistema.start();
    }
}