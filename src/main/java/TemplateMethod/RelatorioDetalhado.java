package TemplateMethod;

import StateArtigo.Artigo;

/**
 * Implementação concreta do Template Method.
 * Gera um relatório longo expondo todos os pareceres dos revisores.
 */
public class RelatorioDetalhado extends RelatorioRevisaoTemplate {

    @Override
    protected void imprimirCorpo(Artigo artigo) {
        System.out.println("Título: " + artigo.getTitulo());
        System.out.println("Autor Principal: " + artigo.getAutorPrincipal().getEmail());
        System.out.println("Status Atual: " + artigo.getStatus());
        
        System.out.println("\n--- Histórico de Pareceres da Banca ---");
        if (artigo.getPareceres().isEmpty()) {
            System.out.println("Nenhum parecer emitido até o momento.");
        } else {
            for (String parecer : artigo.getPareceres()) {
                System.out.println(" -> " + parecer);
            }
        }
    }
}
