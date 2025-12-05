# Sistema de Gerenciamento de Biblioteca e Acervo Histórico - Museu Treze de Maio

## DESCRIÇÃO DO PROJETO
Esse projeto foi realizado com o intuito de gerenciar o acervo bibliográfico e histórico do Museu Treze de Maio, respeitando as particularidades de cada tipo de item dentro do acervo. 

## Sumário
- [Funcionalidades](#funcionalidades)
- [Modelagem do Banco de Dados](#modelagem-do-banco-de-dados)
- [Ferramentas Utilizadas](#ferramentas-utilizadas)
- [Como Executar o Projeto](#como-executar-o-projeto)

---

## Funcionalidades
O diagrama de Casos de Uso abaixo ilustra as principais interações que os usuários (Administrador e Público) podem ter com o sistema.

*Casos De Uso:![Casos de Uso](./Imagens-Diagramas/Modelo%20caso%20de%20usos.png)*

---

## Modelagem do Banco de Dados
A modelagem foi dividida em duas etapas: conceitual e lógica, garantindo que os requisitos do negócio fossem traduzidos corretamente para a estrutura do banco de dados.

### Modelo Conceitual (DER)
Representa as entidades, seus atributos e os relacionamentos entre elas.

*Modelo Conceitual: ![Modelo Conceitual](./Imagens-Diagramas/Modelo_Conceitual.png)*

### Modelo Lógico
Mapeia o modelo conceitual para uma estrutura de tabelas, com chaves primárias e estrangeiras definidas, assim como o tipo de dado de cada atributo.

*Modelo Lógico: ![Modelo Lógico](./Imagens-Diagramas/Modelo_Logico.png)*

---


## Interface do Sistema
<img width="801" height="626" alt="image" src="https://github.com/user-attachments/assets/c3c71234-a7c5-4053-b227-f3193f3cbe5b" />

---

## Ferramentas Utilizadas
*   **SGBD:** Microsoft SQL Server
*   **IDE:** SQL Server Management Studio (SSMS)
*   **Funcionalidades (Casos de Uso): Astah**
*   **Modelagem:** brModelo
*   **Interface do Sistema:** Java with Maven, JAVA FX
*   **Controle de Versão:** Git / GitHub (Opcional)

---

## Como Executar o Projeto

### Pré-requisitos
*   Microsoft SQL Server instalado.
*   SQL Server Management Studio (SSMS) ou outra ferramenta de sua preferência para gerenciar o banco.
*   VERSÃO DO JAVA

### Passos para Instalação
1.  Abra o **SQL Server Management Studio (SSMS)** e conecte-se à sua instância do SQL Server.
2.  Abra o arquivo `DDL_db_Museu_Treze_Maio.sql`.
3.  Execute o Script DDL. Este script irá:
    *   Criar o banco de dados `db_museu_treze_maio`.
    *   Criar todas as tabelas com suas respectivas constraints (PK, FK, CHECK, UNIQUE).
    *   Criar os triggers de auditoria.
4.  Abra o arquivo `DML(inserts)_db_Museu_Treze_Maio.sql`.
5.  Execute o script completo para popular o banco de dados com dados de exemplo.
6.  (Opcional) Para verificar se tudo está funcionando, abra e execute o arquivo `Consultas_db_Museu_Treze_Maio.sql`.

## Desenvolvimento da Aplicação (Java)


### Atividades de Cada Integrante
| Integrante | Atividades/Responsabilidades |
| :--- | :--- |
| **Wilson Rocha** | Estrutura da Modelagem Lógica do Banco, Auxílio na escrita e validação dos Scripts SQL (DDL/DML). |
| **Vinicius Arruda** | Estrutura da Modelagem Lógica do Banco, Auxílio na escrita e validação dos Scripts SQL (DDL/DML). |
| **Felipe Isaia** | Configuração da Conexão com o banco, Realização dos Testes de Qualidade e Produção dos Vídeos de Demonstração. |
| **Gabriel Reis** | **Desenvolvimento da Interface (JavaFX/FXML)**, Arquitetura do Código (MVC/Classes) e Integração Final do Sistema. |

### Conclusões e Dificuldades
As principais dificuldades e aprendizados foram:

1.  **Persistência da Herança:** O maior desafio foi garantir que os dados fossem salvos nas duas tabelas (`ITEM_ACERVO` e `LIVRO`) simultaneamente. Isso foi resolvido com a implementação de **Transações Atômicas** (`commit`/`rollback`) no DAO, garantindo a integridade dos dados.
2.  **Conectividade:** Dificuldades iniciais na configuração da conexão  com o SQL Server (instância nomeada, necessidade de `\\` na URL de conexão).
3.  **Módulos JavaFX:** Ajustes finos no arquivo `module-info.java` para permitir que o JavaFX acesse as classes Model (reflexão da `TableView`), o que demonstrou o domínio do sistema modular Java.
4.  **Integridade Referencial na Exclusão:** Tivemos que ajustar o banco de dados (excluir o FK de `LOG_OPERACAO`) e a ordem dos `DELETE`s no DAO para evitar travamento do sistema.

---

### Tecnologias e Ferramentas
O sistema foi desenvolvido utilizando a linguagem **Java (JDK 21)**, escolhida pela sua robustez e portabilidade. Para a interface gráfica, utilizou-se o framework **JavaFX**, que permite a criação de telas modernas e modulares.

* **Gerenciamento de Dependências:** Maven (para automação de bibliotecas como JavaFX e Driver SQL).
* **IDE:** IntelliJ IDEA.
* **Conectividade:** Driver JDBC da Microsoft para comunicação com o SQL Server.

### Arquitetura do Sistema
O projeto segue estritamente o padrão de arquitetura **MVC (Model-View-Controller)**, complementado pelo padrão **DAO (Data Access Object)** para isolar as regras de negócio do acesso a dados.

A estrutura de pacotes foi organizada da seguinte forma:

* **`br.com.museu.model` (Model):** Contém as classes POJO que representam as entidades do mundo real (ex: `Livro`, `Usuario`, `ItemHistorico`). Aplicamos conceitos de **Herança** (classe pai `ItemAcervo`) para evitar duplicação de código.
* **`br.com.museu.view` (View):** Arquivos `.fxml` que definem o layout visual das telas (XML), separando completamente o design da lógica de programação.
* **`br.com.museu.controller` (Controller):** Classes responsáveis por "dar vida" às telas. Elas capturam os eventos de clique, preenchem as tabelas e validam os dados antes de enviar para o banco. Implementam também a lógica de segurança (ex: bloquear botões para usuários "Visitantes").
* **`br.com.museu.dao` (DAO):** Camada de persistência. Aqui ficam os comandos SQL (`INSERT`, `UPDATE`, `SELECT`, `DELETE`). O DAO recebe objetos do Controller e os transforma em comandos para o banco.
* **`br.com.museu.util`:** Utilitários como a classe de Conexão (Singleton) e a Sessão do Usuário.

### Soluções Técnicas Implementadas

1.  **Controle de Transações (Atomicidade):**
    Para salvar um livro, o sistema precisa inserir dados em duas tabelas diferentes (`ITEM_ACERVO` e `LIVRO`). Implementamos o controle de transação no Java (`conn.setAutoCommit(false)`), garantindo que, se ocorrer um erro na segunda tabela, a primeira operação é desfeita automaticamente (`rollback`), mantendo o banco íntegro.

2.  **Controle de Acesso (Singleton):**
    Desenvolvemos uma classe `SessaoUsuario` que armazena o usuário logado na memória. Ao abrir qualquer tela, o sistema verifica o perfil armazenado. Se for "Visitante", os componentes de edição e exclusão são ocultados ou desabilitados via código.

3.  **Interface Dinâmica:**
    Utilizamos `ObservableList` do JavaFX para garantir que as tabelas de listagem (Grids) sejam atualizadas automaticamente assim que um item é cadastrado ou editado, sem necessidade de reiniciar a tela.

### Como Executar a Aplicação

1.  **Pré-requisitos:** Ter o Java JDK 21 e o Maven instalados.
2.  **Configuração:** O arquivo `pom.xml` gerencia todas as bibliotecas necessárias. Ao abrir o projeto na IDE, execute o comando `mvn clean install` para baixar as dependências.
3.  **Execução:** Localize a classe principal `br.com.museu.museutrezemaio.App` e execute o método `main`.
4.  **Login:** A tela inicial solicitará usuário e senha (previamente cadastrados no banco de dados).
