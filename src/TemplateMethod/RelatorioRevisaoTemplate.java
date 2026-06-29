package TemplateMethod;

import State.Artigo;

/**
 * Padrão Template Method aplicado à geração de pareceres 
 * Define o esqueleto do algoritmo de impressão, garantindo que todo relatório
 * tenha obrigatoriamente um cabeçalho, um corpo e um rodapé na ordem correta.
 */
public abstract class RelatorioRevisaoTemplate {

   
    public final void gerarRelatorio(Artigo artigo) {
        imprimirCabecalho();
        imprimirCorpo(artigo);
        imprimirRodape();
    }

    private void imprimirCabecalho() {
        System.out.println("\n========================================");
        System.out.println("   SISTEMA DE EVENTOS - PARECER OFICIAL   ");
        System.out.println("========================================");
    }

    /** Passo que as subclasses são obrigadas a implementar do jeito delas. */
    protected abstract void imprimirCorpo(Artigo artigo);

    private void imprimirRodape() {
        System.out.println("========================================");
        System.out.println("  Gerado automaticamente pelo sistema.  \n");
    }
}