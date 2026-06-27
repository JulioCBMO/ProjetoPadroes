package FactoryMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa o usuário comum do sistema (Pesquisador).
 * Utiliza Composição para unificar os papéis de Autor e Revisor, 
 * evitando as limitações da herança estática no Java.
 */
public class Pesquisador extends Usuario {

    // --- Atributos de Autor (RF05) ---
    private final List<Artigo> artigosSubmetidos = new ArrayList<>();

    // --- Atributos de Revisor (RF04 / RF06) ---
    private boolean isRevisor = false;
    private final List<String> especialidades = new ArrayList<>();
    private final List<Artigo> artigosParaRevisar = new ArrayList<>();

    public Pesquisador(String email, String senha, String instituicao) {
        super(email, senha, instituicao);
    }

    // --- Métodos de Autor ---
    
    /** Submete um artigo ao evento (RF05). */
    public void submeterArtigo(Artigo artigo) {
        artigosSubmetidos.add(artigo);
    }

    public List<Artigo> getArtigosSubmetidos() {
        return artigosSubmetidos;
    }

    // --- Métodos de Revisor ---
    
    /** Ativa o papel de revisor no sistema (RF04). */
    public void aceitarConviteRevisao() {
        this.isRevisor = true;
    }

    public boolean isRevisor() {
        return isRevisor;
    }

    /** Cadastra palavras-chave para o algoritmo de compatibilidade (RF03). */
    public void adicionarEspecialidade(String tema) {
        especialidades.add(tema);
    }

    public List<String> getEspecialidades() {
        return especialidades;
    }

    /** Recebe um artigo distribuído automaticamente (RF06). */
    public void adicionarArtigoParaRevisar(Artigo artigo) {
        if (!isRevisor) {
            throw new IllegalStateException("Pesquisador não é um Revisor ativo.");
        }
        artigosParaRevisar.add(artigo);
    }

    public List<Artigo> getArtigosParaRevisar() {
        return artigosParaRevisar;
    }

    /** Retorna dinamicamente o papel atual do usuário. */
    @Override
    public String getTipo() { 
        return isRevisor ? "REVISOR" : "AUTOR"; 
    }
}