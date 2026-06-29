public interface CompatibilidadeStrategy {
    double calcular(Artigo artigo, Revisor revisor);
}

/**Por que isso satisfaz os requisitos do professor
O RF03 exige que o sistema identifique "alta compatibilidade" entre temas do artigo e especialidades do revisor. Com o Strategy você consegue:
Trocar o algoritmo em tempo de execução sem alterar DistribuidorDeArtigos
Testar cada algoritmo isoladamente (princípio da responsabilidade única)
Adicionar novos critérios no futuro (ex: considerar histórico do revisor) sem quebrar o código existente */