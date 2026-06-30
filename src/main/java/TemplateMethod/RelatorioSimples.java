package TemplateMethod;

import StateArtigo.Artigo;

/**
 * Implementação concreta do Template Method.
 * Gera um relatório curto e objetivo apenas com o status final.
 */
public class RelatorioSimples extends RelatorioRevisaoTemplate {

    @Override
    protected void imprimirCorpo(Artigo artigo) {
        System.out.println("Título: " + artigo.getTitulo());
        System.out.println("Veredito Final: " + artigo.getStatus());
    }
}
