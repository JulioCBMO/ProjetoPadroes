# 📚 Paper Submission 

**Disciplina:** Padrões de Projeto de Software  
**Curso:** Sistemas para Internet (5º Período)  
**Professor:** Alex Sandro da Cunha Rêgo  
**Instituição:** IFPB - Instituto Federal da Paraíba  

### 👥 Equipe Desenvolvedora
| Integrante | Função |
| :--- | :--- |
| **FELIPE ANTONIO** | Arquitetura e Desenvolvimento |
| **JULIO CÉSAR** | Arquitetura e Desenvolvimento |

---

## 🎯 Visão Geral do Projeto

O **Paper Submission Management System** é um sistema projetado para gerenciar o ciclo de vida completo de submissão e avaliação de artigos científicos para eventos acadêmicos. 

O foco central desta implementação foi aplicar as melhores práticas de Engenharia de Software, utilizando **Padrões de Projeto (GoF)** e princípios **SOLID** (especialmente *Open/Closed* e *Single Responsibility*) para resolver problemas complexos como: distribuição inteligente de artigos, blind-review, transições de estado e emissão padronizada de relatórios.

---

## 🚀 Como Executar o Sistema (Exigências E1 e E2)

O sistema foi construído em **Java puro**, sem dependências externas complexas, garantindo portabilidade e fácil execução na máquina do avaliador.

1. Clone o repositório ou extraia o arquivo ZIP.
2. Certifique-se de que o arquivo `usuarios.csv` está localizado na **raiz do projeto** (fora da pasta `src`).
3. Abra o projeto na sua IDE de preferência (IntelliJ, Eclipse, VS Code).
4. Compile e execute a classe `src/Main.java`.
5. **Observação:** O sistema realizará o povoamento inicial automático lendo os dados do CSV (Atendendo à restrição **E1**) e imprimirá no console todo o fluxo de orquestração simulado.

---

## 🧩 Mapeamento de Padrões de Projeto Aplicados

A tabela abaixo serve como guia arquitetural, demonstrando onde cada Padrão de Projeto foi (ou será) aplicado para solucionar os Requisitos Funcionais (RFs) exigidos no planejamento do sistema.

| Padrão de Projeto | Requisito Alvo | Objetivo da Aplicação |
| :--- | :--- | :--- |
| **Command** | **RF01** (Inicialização) | Encapsular o método `start()` que reseta o estado do sistema como um objeto Command, permitindo desfazer, logar e reutilizar a operação facilmente. |
| **Factory Method** | **RF02** (Cadastro) | Instanciar tipos de usuário (Autor, Revisor, Chair) com comportamentos distintos sem acoplar o código cliente, decidindo dinamicamente a criação via Registry. |
| **Strategy** | **RF03** (Distribuição) | Encapsular o algoritmo de compatibilidade (artigo × revisor) como uma interface `CompatibilidadeStrategy` trocável (ex: palavras-chave exatas, pontuação ponderada). |
| **State** | **RF04, RF05** (Ciclos) | Controlar os ciclos de estado do convite e do artigo (`Submetido -> Revisão -> Aceito/Rejeitado`), eliminando estruturas condicionais (`ifs`) espalhadas pelo código. |
| **Observer** | **RF06** (Notificações) | Fazer o revisor atuar como um Observer que reage ao evento de distribuição, sendo notificado por e-mail automaticamente quando um artigo é alocado a ele. |
| **Template Method** | **RF07, RF09** (Pareceres e E-mails) | **(RF07)** Definir o esqueleto estrutural fixo do parecer (contribuições + críticas + veredito), deixando as subclasses preencherem a seção. **(RF09)** Reutilizar a mesma lógica para os e-mails de aceite/rejeição, que possuem estrutura idêntica mas campos variáveis. |
| **Facade** | **RF08** (Dashboard) | Prover uma `DashboardFacade` que oferece uma interface simples para agregar dados de submissões, revisores e prazos, escondendo a complexidade de consultar múltiplos subsistemas. |
| **Decorator** | **RF10** (Selos/Tags) | Adicionar "selos de qualidade" dinâmicos ao artigo (ex: `ArtigoComRevisaoRapida`, `ArtigoDestaque`) sem a necessidade de alterar a classe base. |

---

## ⚙️ Detalhamento dos Requisitos Funcionais

Abaixo estão as regras de negócio implementadas e blindadas pela nossa arquitetura:

* **RF01 - Inicialização:** Sistema preparado para inicializar e gerenciar as instâncias do evento e prazos.
* **RF02 - Cadastro de Usuários (Pesquisadores):** Sistema modelado com *Composição* para permitir que um Pesquisador atue como Autor e seja promovido a Revisor dinamicamente (resolvendo a limitação de herança do Java).
* **RF03 - Áreas Temáticas:** Cadastro de especialidades para o cálculo inteligente do *Strategy*.
* **RF04 - Comitê Técnico:** Fluxo de aceite de convite de revisão integrado ao perfil do usuário.
* **RF05 - Submissão de Artigos:** Entrada inicial controlada pelo estado `EstadoSubmetido` (*State*).
* **RF06 - Distribuição Automática:** Motor (*Strategy*) que avalia afinidade temática, garante o *Blind-Review* (não avalia o próprio artigo) e respeita uma cota máxima por revisor para balanceamento de carga.
* **RF07 - Avaliação e Relatórios:** Emissão de pareceres (contribuições/críticas) orquestrados pelo *Template Method*, alterando o *State* do artigo ao final do processo.
* **RF09 - Notificações:** Avisos automáticos disparados pelo *Observer* via e-mail simulado no console quando o status do artigo é atualizado.