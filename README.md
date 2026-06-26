# ProjetoPadroes
Sistema de Submissão e Avaliação de Artigos Científicos

RF01 – Command: o start() reseta o estado do sistema. Encapsular isso como um objeto Command permite desfazer, logar e reutilizar a operação facilmente.

RF02 – Factory Method: você tem três tipos de usuário (Autor, Revisor, Chair) com comportamentos distintos. O Factory decide qual instanciar sem acoplar o código cliente.

RF03 – Strategy: o algoritmo de compatibilidade (artigo × revisor) pode variar — por palavras-chave exatas, por pontuação ponderada, etc. Encapsula isso como uma CompatibilidadeStrategy trocável.

RF04 e RF05 – State: tanto o convite do revisor quanto o artigo têm ciclos de estados bem definidos. Um objeto EstadoArtigo (Submetido → Revisão → Aceito/Rejeitado) elimina ifs espalhados pelo código.
RF06 – Observer: quando um artigo é distribuído, revisores devem ser notificados por e-mail automaticamente. O revisor é um Observer que reage ao evento de distribuição.

RF07 – Template Method: o parecer tem estrutura fixa (contribuições + críticas + veredito), mas o conteúdo varia. O método template define o esqueleto; subclasses preenchem cada seção.

RF08 – Facade: o dashboard agrega dados de submissões, revisores e prazos. Uma DashboardFacade oferece uma interface simples escondendo a complexidade de consultar múltiplos subsistemas.

RF09 – Template Method: os e-mails de aceite e rejeição têm estrutura idêntica com campos variáveis. Reutiliza o mesmo padrão do RF07, com templates distintos para cada caso.

RF10 – Decorator: adicionar "selos de qualidade" ao artigo (ex: ArtigoComRevisaoRapida, ArtigoDestaque) sem alterar a classe base.
