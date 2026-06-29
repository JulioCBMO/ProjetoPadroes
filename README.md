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

A tabela abaixo serve como guia arquitetural, demonstrando onde cada Padrão de Projeto foi aplicado para solucionar os Requisitos Funcionais (RFs) exigidos.

| Padrão de Projeto | Requisito Alvo | Classes Principais | Objetivo da Aplicação |
| :--- | :--- | :--- | :--- |
| **Factory Method** | **RF02** (Cadastro) | `UsuarioFactoryRegistry`, `PesquisadorFactory`, `ChairFactory` | Centralizar a criação de usuários, eliminando `if/else` ao ler o CSV e permitindo plugar novos tipos de usuários facilmente (OCP). |
| **Strategy** | **RF03, RF06** (Distribuição) | `CompatibilidadeStrategy`, `DistribuidorDeArtigos`, `CompatibilidadePonderada` | Encapsular o algoritmo de compatibilidade temática (blind-review e cotas de balanceamento) permitindo trocar as regras no futuro sem alterar o motor de distribuição. |
| **State** | **RF05, RF07** (Ciclo do Artigo) | `Artigo`, `EstadoArtigo`, `EstadoSubmetido`, `EstadoAceito` | Controlar rigidamente o fluxo do artigo (Submetido -> Em Revisão -> Aceito/Rejeitado), impedindo ações ilegais (ex: avaliar um artigo já finalizado). |
| **Observer** | **RF06, RF09** (Notificações) | `ArtigoObserver`, `NotificadorEmail`, `LogAuditoria` | Desacoplar a classe `Artigo` da lógica de envio de alertas. Quando o artigo muda de estado, ele notifica automaticamente os "assinantes" para disparar e-mails e logs. |
| **Template Method** | **RF07** (Pareceres) | `RelatorioRevisaoTemplate`, `RelatorioSimples`, `RelatorioDetalhado` | Criar um esqueleto fixo (cabeçalho, corpo, rodapé) para a impressão dos relatórios de avaliação, delegando apenas o "recheio" do texto para as subclasses. |

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