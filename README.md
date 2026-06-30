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

O projeto utiliza **Maven** para gerenciamento de dependências (necessário para o envio real de e-mail do RF09, via biblioteca Jakarta Mail).

### Pré-requisitos
- JDK 17 ou superior
- Maven 3.6+ (`mvn -version` para conferir)

### Passo a passo

1. Clone o repositório ou extraia o arquivo ZIP.
2. Na raiz do projeto (onde está o `pom.xml`), configure as variáveis de ambiente para o envio real de e-mail (RF09):

   **Linux/Mac:**
   ```bash
   export EMAIL_REMETENTE="seuemail@gmail.com"
   export EMAIL_SENHA="sua-senha-de-app-aqui"
   ```

   **Windows (PowerShell):**
   ```powershell
   $env:EMAIL_REMETENTE="seuemail@gmail.com"
   $env:EMAIL_SENHA="sua-senha-de-app-aqui"
   ```

   > A "Senha de app" é gerada em [myaccount.google.com/apppasswords](https://myaccount.google.com/apppasswords) (não é a senha normal da conta Gmail). Se essas variáveis não forem configuradas, o sistema continua rodando normalmente e apenas avisa no console que o envio de e-mail falhou — todos os outros padrões funcionam sem essa configuração.

3. Compile e execute com Maven:

   ```bash
   mvn compile exec:java
   ```

   Alternativamente, gere um `.jar` executável com todas as dependências embutidas:

   ```bash
   mvn package
   java -jar target/ProjetoPadroes.jar
   ```

4. O sistema realiza o povoamento inicial automático lendo `usuarios.csv` (atendendo à exigência **E1**) e imprime no console todo o fluxo de orquestração simulado, cobrindo os 10 requisitos funcionais.

### Estrutura do projeto

```
ProjetoPadroes/
├── pom.xml
├── usuarios.csv              <- também copiado para src/main/resources/
├── src/main/java/
│   ├── Main.java
│   ├── Command/
│   ├── Decorator/
│   ├── FacadeDashboard/
│   ├── FactoryMethod/
│   ├── Notificacao/
│   ├── Observer/
│   ├── StateArtigo/
│   ├── StateConvite/
│   ├── Strategy/
│   ├── TemplateMethod/
│   └── Util/
```

---

## 🧩 Mapeamento de Padrões de Projeto Aplicados

A tabela abaixo serve como guia arquitetural, demonstrando onde cada Padrão de Projeto foi (ou será) aplicado para solucionar os Requisitos Funcionais (RFs) exigidos no planejamento do sistema.

| Padrão de Projeto | Requisito Alvo | Objetivo da Aplicação |
| :--- | :--- | :--- |
| **Command** | **RF01** (Inicialização) | Encapsular o método `start()` que reseta o estado do sistema como um objeto Command, permitindo desfazer, logar e reutilizar a operação facilmente. |
| **Factory Method** | **RF02** (Cadastro) | Instanciar tipos de usuário (Autor, Revisor, Chair) com comportamentos distintos sem acoplar o código cliente, decidindo dinamicamente a criação via Registry. |
| **Strategy** | **RF03** (Distribuição) | Encapsular o algoritmo de compatibilidade (artigo × revisor) como uma interface `CompatibilidadeStrategy` trocável (ex: palavras-chave exatas, pontuação ponderada). |
| **State** | **RF04** (Convite), **RF05** (Artigo) | Controlar os ciclos de estado do convite (`ConvitePendente -> ConviteAceito/ConviteRecusado`) e do artigo (`EstadoSubmetido -> EstadoEmRevisao -> EstadoAceito/EstadoRejeitado`), eliminando estruturas condicionais (`ifs`) espalhadas pelo código. |
| **Observer** | **RF06, RF09** (Notificações) | `NotificadorEmail` e `LogAuditoria` reagem automaticamente às mudanças de estado do artigo (`Artigo.notificarObservers()`), disparando e-mail real (RF09) e log de auditoria sem que `Artigo` precise conhecer os detalhes de cada ação. |
| **Template Method** | **RF07, RF09** (Pareceres e E-mails) | **(RF07)** `RelatorioRevisaoTemplate` define o esqueleto fixo do relatório (cabeçalho + corpo + rodapé), com `RelatorioSimples` e `RelatorioDetalhado` variando o corpo. **(RF09)** `EmailNotificacao` reutiliza a mesma lógica para os e-mails de `EmailAceitacao`/`EmailRejeicao`, que têm estrutura idêntica mas conteúdo variável. |
| **Facade** | **RF08** (Dashboard) | `DashboardFacade` oferece uma interface única para agregar dados de `Artigo` (State) e `Pesquisador` (Factory Method) — total de artigos, revisores ativos, avaliados, pendentes e quem é responsável por cada pendência — sem que o `Main` precise conhecer a relação interna entre essas classes. |
| **Decorator** | **RF10** (Selos de destaque) | `SeloMelhorArtigo` e `SeloMencaoHonrosa` adicionam dinamicamente marcações de destaque a um artigo aceito (combináveis entre si), sem alterar a classe `Artigo` nem exigir uma subclasse para cada combinação possível. |

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