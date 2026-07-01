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
   .\mvnw.cmd compile exec:java
   ```

   Alternativamente, gere um `.jar` executável com todas as dependências embutidas:

   ```bash
   mvn package
   java -jar target/ProjetoPadroes.jar
   ```
   se as acentuações ficarem estranhas utilize esse codigo
   
   ```
   $OutputEncoding = [Console]::OutputEncoding = [Text.Encoding]::UTF8
   ```

4. O sistema realiza o povoamento inicial automático lendo `usuarios.csv` (atendendo à exigência **E1**) e imprime no console todo o fluxo de orquestração simulado, cobrindo os 10 requisitos funcionais.

### Estrutura do projeto

```
ProjetoPadroes/
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── mvnw
├── mvnw.cmd
├── pom.xml
├── usuarios.csv              
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
│   │   ├── CompatibilidadeStrategy.java
│   │   ├── CompatibilidadeExata.java
│   │   ├── CompatibilidadePonderada.java
│   │   ├── DistribuidorDeArtigos.java
│   │   ├── EstrategiaConsolidacao.java
│   │   ├── ConsolidacaoHierarquica.java
│   │   └── ConsolidacaoOtimista.java
│   ├── TemplateMethod/
│   └── Util/
│       ├── DataLoaderCSV.java
│       └── Veredito.java
```

---

## 🧩 Mapeamento de Padrões de Projeto Aplicados

| Padrão de Projeto | Requisito Alvo | Objetivo da Aplicação |
| :--- | :--- | :--- |
| **Command** | **RF01** (Inicialização) | Encapsular o método `start()` que reseta o estado do sistema como um objeto Command, permitindo desfazer, logar e reutilizar a operação facilmente. |
| **Factory Method** | **RF02** (Cadastro) | Instanciar tipos de usuário (Autor, Revisor, Chair) com comportamentos distintos sem acoplar o código cliente, decidindo dinamicamente a criação via Registry. |
| **State** | **RF04** (Convite), **RF05** (Artigo) | Controlar os ciclos de estado do convite (`ConvitePendente -> ConviteAceito/ConviteRecusado`) e do artigo (`EstadoSubmetido -> EstadoEmRevisao -> EstadoAceito/EstadoRejeitado`), eliminando estruturas condicionais (`ifs`) espalhadas pelo código. |
| **Strategy** | **RF03** (Distribuição) | Encapsular o algoritmo de compatibilidade (artigo × revisor) como uma interface `CompatibilidadeStrategy` trocável (ex: palavras-chave exatas, pontuação ponderada). |
| **Strategy** | **RF11** (Consolidação) | Encapsular a lógica de consolidação de vereditos em duas estratégias alternativas: `ConsolidacaoHierarquica` (com conflito) e `ConsolidacaoOtimista` (sempre mais positivo), permitindo ao coordenador escolher a política de decisão em tempo de execução. |
| **Observer** | **RF06, RF09** (Notificações) | `NotificadorEmail` e `LogAuditoria` reagem automaticamente às mudanças de estado do artigo (`Artigo.notificarObservers()`), disparando e-mail real (RF09) e log de auditoria sem que `Artigo` precise conhecer os detalhes de cada ação. |
| **Template Method** | **RF07, RF09** (Pareceres e E-mails) | **(RF07)** `RelatorioRevisaoTemplate` define o esqueleto fixo do relatório (cabeçalho + corpo + rodapé), com `RelatorioSimples` e `RelatorioDetalhado` variando o corpo. **(RF09)** `EmailNotificacao` reutiliza a mesma lógica para os e-mails de `EmailAceitacao`/`EmailRejeicao`, que têm estrutura idêntica mas conteúdo variável. |
| **Facade** | **RF08** (Dashboard) | `DashboardFacade` oferece uma interface única para agregar dados de `Artigo` (State) e `Pesquisador` (Factory Method) — total de artigos, revisores ativos, avaliados, pendentes e quem é responsável por cada pendência — sem que o `Main` precise conhecer a relação interna entre essas classes. Além disso, o dashboard agora exibe os artigos aceitos com seus selos aplicados via Decorator. |
| **Decorator** | **RF10** (Selos de destaque) | `SeloMelhorArtigo` e `SeloMencaoHonrosa` adicionam dinamicamente marcações de destaque a um artigo aceito (combináveis entre si), sem alterar a classe `Artigo` nem exigir uma subclasse para cada combinação possível. A exibição dos selos é integrada ao e-mail de aceitação e ao dashboard. |

---

## ⚙️ Detalhamento dos Requisitos Funcionais

Abaixo estão as regras de negócio implementadas e blindadas pela nossa arquitetura:

* **RF01 - Inicialização:** Sistema preparado para inicializar e gerenciar as instâncias do evento e prazos de submissão (com data limite configurável). Utiliza o padrão **Command** para encapsular o reset do sistema.

* **RF02 - Cadastro de Usuários (Pesquisadores):** Sistema modelado com *Composição* para permitir que um Pesquisador atue como Autor e seja promovido a Revisor dinamicamente (resolvendo a limitação de herança do Java). Usuários são carregados automaticamente via CSV utilizando **Factory Method**.

* **RF03 - Áreas Temáticas:** Cadastro central de áreas temáticas feito pelo coordenador, com validação na submissão e no convite de revisores. As áreas são armazenadas em uma classe estática e podem ser limpas a cada novo evento.

* **RF04 - Comitê Técnico:** Fluxo de convite e aceite de revisão, com estado (Pendente, Aceito, Recusado) utilizando o padrão **State**. O revisor informa suas especialidades no momento do aceite.

* **RF05 - Submissão de Artigos:** Entrada de artigos com autor principal, coautores (validados contra o cadastro), resumo e temas (apenas áreas previamente cadastradas). Prazo de submissão é verificado (RF01). O artigo inicia no estado `SUBMETIDO`.

* **RF06 - Distribuição Automática:** Motor (*Strategy*) que avalia afinidade temática (compatibilidade ponderada), garante o *Blind-Review* (não avalia o próprio artigo), respeita balanceamento de carga e registra os revisores designados em cada artigo. A distribuição só ocorre se houver revisores elegíveis.

* **RF07 - Avaliação e Relatórios:** Emissão de pareceres (contribuições/críticas) orquestrados pelo **Template Method**. Os vereditos são armazenados individualmente por revisor. Quando todos os revisores designados avaliarem, a consolidação ocorre de acordo com a estratégia escolhida (RF11). O estado do artigo é atualizado para `ACEITO` ou `REJEITADO` após a consolidação bem-sucedida.

* **RF08 - Dashboard:** Exibe métricas consolidadas (total de artigos, revisores, avaliados, pendentes) e lista de pendências por revisor, utilizando o padrão **Facade**. Agora também mostra os artigos aceitos com seus selos aplicados (Decorator), proporcionando uma visão rápida dos destaques do evento.

* **RF09 - Notificações:** Envio automático de e-mail real (via Jakarta Mail) aos autores quando o artigo é aceito ou rejeitado, disparado pelo **Observer**. O e-mail inclui os pareceres dos revisores e, em caso de aceitação, exibe os selos aplicados (Decorator). Além disso, há uma opção no menu para reenviar notificações em massa (reenvio forçado) caso necessário.

* **RF10 - Selos de Destaque:** O coordenador pode aplicar selos ("Melhor Artigo da Trilha" e "Menção Honrosa") a artigos aceitos, utilizando o padrão **Decorator** para compor a exibição. Os selos são exibidos no dashboard, no e-mail de aceitação e na listagem do menu.

* **RF11 - Estratégia de Consolidação:** O coordenador pode escolher em tempo de execução a política de consolidação dos vereditos, utilizando o padrão **Strategy**. Duas estratégias estão disponíveis:
  - **Hierárquica com conflito:** Em caso de empate extremo (ACEITO vs REJEITADO), o sistema exige consenso entre os revisores, mantendo o artigo em estado `REVISAO` até que um dos revisores altere seu voto.
  - **Otimista:** Sempre prevalece o veredito mais positivo, favorecendo o autor. Não há conflitos, e o artigo é consolidado automaticamente.