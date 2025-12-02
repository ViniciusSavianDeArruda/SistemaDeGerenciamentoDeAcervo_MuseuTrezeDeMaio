
# Dicionário de Dados - Sistema de Biblioteca e Acervo Histórico do Museu Treze de Maio

## Sumário de Tabelas
1.  [Usuario](#tabela-usuario)
2.  [Doador](#tabela-doador)
3.  [ItemAcervo](#tabela-itemacervo)
4.  [LogOperacao](#tabela-logoperacao)
5.  [Publicacao](#tabela-publicacao)
6.  [Livro](#tabela-livro)
7.  [Autor](#tabela-autor)
8.  [LivroAutor](#tabela-livroautor)
9.  [Assunto](#tabela-assunto)
10. [LivroAssunto](#tabela-livroassunto)
11. [Serie](#tabela-serie)
12. [Periodico](#tabela-periodico)
13. [Exemplar](#tabela-exemplar)
14. [ItemHistorico](#tabela-itemhistorico)

---

<div style="page-break-after: always;"></div>

### **Tabela: `Usuario`**
**Descrição:** Armazena as informações dos usuários que podem interagir com o sistema.

| Atributo | Tipo de Dado | Restrições | Descrição |
| :--- | :--- | :--- | :--- |
| `id_usuario` | INT | PK, AUTO_INCREMENT | Chave primária autoincremental para identificar unicamente cada usuário. |
| `login_usuario` | VARCHAR(30) | NN, UN | Login único do usuário. Não pode ser nulo. |
| `senha` | VARCHAR(150) | NN | Senha do usuário, armazenada como hash. Não pode ser nulo. |
| `tipo_usuario` | VARCHAR(20) | NN, CHECK | Perfil de permissão do usuário (ex: 'Administrador', 'Visitante'). Não Nulo. |

### **Tabela: `Doador`**
**Descrição:** Armazena informações sobre as pessoas ou instituições que doaram itens para o acervo.

| Atributo | Tipo de Dado | Restrições | Descrição |
| :--- | :--- | :--- | :--- |
| `id_doador` | INT | PK, AUTO_INCREMENT | Chave primária autoincremental para identificar unicamente cada doador. |
| `nome` | VARCHAR(200) | NN, UN | Nome do doador. Não pode ser nulo ou repetido. |

### **Tabela: `ItemAcervo`**
**Descrição:** Tabela pai que contém os dados comuns a todos os itens catalogados no acervo do museu.

| Atributo | Tipo de Dado | Restrições | Descrição |
| :--- | :--- | :--- | :--- |
| `id_item_acervo` | INT | PK, AUTO_INCREMENT | Chave primária. Identificador único para qualquer item que entra no acervo. |
| `titulo` | VARCHAR(400) | NN | Título principal da obra. Tamanho estendido para acomodar várias informações. |
| `data_aquisicao` | DATE | NN | Data em que o item foi adquirido pela biblioteca. |
| `tipo_item` | VARCHAR(30) | NN, CHECK | Campo que define o tipo do item (ex: 'Livro', 'Periodico', 'ItemHistorico'). |
| `link_midia` | VARCHAR(2048) | | Caminho para a versão digitalizada do item (PDF, imagem, etc.). Não é obrigatório. |
| `id_doador` | INT | FK | Chave estrangeira que aponta para o doador do item. Não obrigatório. |

<div style="page-break-after: always;"></div>

### **Tabela: `LogOperacao`**
**Descrição:** Tabela que registra todas as operações de criação e alteração nos itens do acervo.

| Atributo | Tipo de Dado | Restrições | Descrição |
| :--- | :--- | :--- | :--- |
| `id_log` | INT | PK, AUTO_INCREMENT | Identificador único para cada registro de log. |
| `operacao` | VARCHAR(100) | NN | Descrição da ação realizada (ex: "Criação de Item", "Alteração de Título"). |
| `data_operacao` | DATETIME | NN | Data e hora em que a operação foi realizada. |
| `id_usuario` | INT | FK, NN | Chave estrangeira que aponta para o usuário que realizou a operação. |
| `id_item_acervo` | INT | FK, NN | Chave estrangeira que aponta para o item do acervo que sofreu a operação. |

### **Tabela: `Publicacao`**
**Descrição:** Armazena os diferentes registros de publicação de um item do acervo (edições, reimpressões).

| Atributo | Tipo de Dado | Restrições | Descrição |
| :--- | :--- | :--- | :--- |
| `id_publicacao` | INT | PK, AUTO_INCREMENT | Identificador único para cada registro de publicação. |
| `local_publicacao` | VARCHAR(255) | | Local de publicação (cidade, país). |
| `editora` | VARCHAR(100) | | Nome da editora responsável por esta publicação. |
| `ano` | INT | | Ano em que esta publicação foi feita. |
| `id_item_acervo` | INT | FK, NN | Chave estrangeira que liga este registro de publicação ao item do acervo correspondente. |

<div style="page-break-after: always;"></div>

### **Tabela: `Livro`**
**Descrição:** Tabela filha (especialização) para itens do tipo 'Livro'.

| Atributo | Tipo de Dado | Restrições | Descrição |
| :--- | :--- | :--- | :--- |
| `id_item_acervo` | INT | PK, FK, NN | Chave primária e estrangeira que liga o livro ao registro geral em `ItemAcervo`. |
| `local_chamada` | VARCHAR(50) | NN | Código de localização do livro na estante física (etiqueta). Ex: `B869.1-3 F981a`. |
| `edicao` | VARCHAR(100) | | Descrição da edição (ex: "3ª Edição"). |
| `desc_fisica` | VARCHAR(300) | | Descrição física do livro (ex: "342 p. ; 23 cm"). |
| `notas` | VARCHAR(MAX) | | Campo de texto livre para anotações gerais sobre a obra. |
| `serie_descricao` | VARCHAR(100) | | Descreve a posição do livro na série (ex: "Volume 3", "Livro 1"). |
| `titulo_original` | VARCHAR(200) | | Título da obra no idioma original, caso seja uma tradução. |
| `isbn` | VARCHAR(100) | UN | ISBN (International Standard Book Number). Identificador único para a edição. |
| `id_serie` | INT | FK | Chave estrangeira que aponta para a tabela `Serie`. Não obrigatória. |

<div style="page-break-after: always;"></div>

### **Tabela: `Autor`**
**Descrição:** Armazena os nomes dos autores das obras.

| Atributo | Tipo de Dado | Restrições | Descrição |
| :--- | :--- | :--- | :--- |
| `id_autor` | INT | PK, AUTO_INCREMENT | Identificador único para cada autor. |
| `nome` | VARCHAR(200) | NN | Nome completo do autor. |

### **Tabela: `LivroAutor`**
**Descrição:** Tabela associativa que implementa o relacionamento N:N entre `Livro` e `Autor`.

| Atributo | Tipo de Dado | Restrições | Descrição |
| :--- | :--- | :--- | :--- |
| `id_livro` | INT | PK, FK, NN | Parte da chave primária composta. Aponta para o livro. |
| `id_autor` | INT | PK, FK, NN | Parte da chave primária composta. Aponta para o autor. |

<div style="page-break-after: always;"></div>

### **Tabela: `Assunto`**
**Descrição:** Armazena os assuntos (temas, palavras-chave) para classificação das obras.

| Atributo | Tipo de Dado | Restrições | Descrição |
| :--- | :--- | :--- | :--- |
| `id_assunto` | INT | PK, AUTO_INCREMENT | Identificador único para cada assunto. |
| `descricao` | VARCHAR(100) | NN, UN | Descrição do assunto (ex: "História do Brasil - Período Colonial"). Não pode ser nulo ou repetido. |

### **Tabela: `LivroAssunto`**
**Descrição:** Tabela associativa que implementa o relacionamento N:N entre `Livro` e `Assunto`.

| Atributo | Tipo de Dado | Restrições | Descrição |
| :--- | :--- | :--- | :--- |
| `id_livro` | INT | PK, FK, NN | Parte da chave primária composta. Aponta para o livro. |
| `id_assunto` | INT | PK, FK, NN | Parte da chave primária composta. Aponta para o assunto. |

<div style="page-break-after: always;"></div>

### **Tabela: `Serie`**
**Descrição:** Armazena as coleções ou séries às quais os livros podem pertencer.

| Atributo | Tipo de Dado | Restrições | Descrição |
| :--- | :--- | :--- | :--- |
| `id_serie` | INT | PK, AUTO_INCREMENT | Identificador único para cada série. |
| `nome` | VARCHAR(100) | NN, UN | Nome único da série (ex: "História da População Negra No RS"). |

### **Tabela: `Periodico`**
**Descrição:** Tabela filha (especialização) para itens do tipo 'Periodico' (revistas, jornais).

| Atributo | Tipo de Dado | Restrições | Descrição |
| :--- | :--- | :--- | :--- |
| `id_item_acervo` | INT | PK, FK, NN | Chave primária e estrangeira que liga o periódico ao registro geral em `ItemAcervo`. |
| `issn` | VARCHAR(100) | UN | ISSN (International Standard Serial Number). Identificador único para o título do periódico. |
| `periodicidade` | VARCHAR(100) | NN | Frequência de publicação (ex: "Trimestral"). |
| `data_inicio_publicacao` | DATE | | Data em que o periódico começou a ser publicado. |
| `data_fim_publicacao` | DATE | | Data em que o periódico cessou a publicação. Nulo se ainda estiver ativo. |
| `titulo_original` | VARCHAR(200) | | Título do periódico no idioma original, se aplicável. |

<div style="page-break-after: always;"></div>

### **Tabela: `Exemplar`**
**Descrição:** Armazena as cópias físicas individuais de cada edição de um periódico.

| Atributo | Tipo de Dado | Restrições | Descrição |
| :--- | :--- | :--- | :--- |
| `id_exemplar` | INT | PK, AUTO_INCREMENT | Identificador único para cada cópia física de um periódico. |
| `volume` | VARCHAR(100) | | Número do volume do exemplar. |
| `numero_exemplar` | VARCHAR(100) | | Número da edição do exemplar. |
| `ano_publicacao` | INT | | Ano em que este exemplar específico foi publicado. |
| `local_chamada` | VARCHAR(50) | NN | Código de localização do exemplar físico na estante. |
| `id_periodico` | INT | FK, NN | Chave estrangeira que liga o exemplar ao periódico que ele pertence. |

<div style="page-break-after: always;"></div>

### **Tabela: `ItemHistorico`**
**Descrição:** Tabela filha (especialização) para itens que não são publicações, como cartas, relatos, fotos.

| Atributo | Tipo de Dado | Restrições | Descrição |
| :--- | :--- | :--- | :--- |
| `id_item_acervo` | INT | PK, FK, NN | Chave primária e estrangeira que liga o item histórico ao registro geral em `ItemAcervo`. |
| `data_criacao` | DATE | | Data de criação do objeto. |
| `descricao` | VARCHAR(MAX) | | Descrição detalhada do item. |
| `dimensoes` | VARCHAR(50) | | Dimensões físicas do objeto (ex: "30cm x 50cm"). |
| `material` | VARCHAR(100) | | Material do qual o objeto é feito (ex: "Papel Jornal", "Papiro"). |
| `local_armazenado` | VARCHAR(100) | NN | Local específico de armazenamento do item dentro do acervo. |
