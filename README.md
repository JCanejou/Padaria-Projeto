# Padaria — Controle de Pedidos

Sistema em Java (console) para **cadastrar, listar, consultar e alterar pedidos** de uma padaria.

Projeto da 2ª VA — Programação III — Campus Surubim.

## Como executar

Requisito: JDK 17 ou superior.

**IntelliJ IDEA:** `File → Open` → selecione a pasta do projeto → clique no ▶ da configuração **Padaria**. Se aparecer o aviso de SDK, clique em *Setup SDK* e escolha um JDK.

**VS Code** (com o *Extension Pack for Java*): `File → Open Folder` → selecione a pasta do projeto → `Run and Debug` → **Padaria**.

**Terminal** (dentro da pasta do projeto):

```bash
javac -d out $(find src -name "*.java")
java -cp out padaria.Main
```

No Windows (PowerShell):

```powershell
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out padaria.Main
```

Se os acentos aparecerem quebrados no terminal do Windows, rode `chcp 65001` antes.

## Funcionalidades

| Menu | O que faz |
|---|---|
| 1 - Cadastrar pedido | Nome do cliente, itens do cardápio com quantidade e forma de pagamento |
| 2 - Listar pedidos | Tabela com número, cliente, quantidade de itens, total e status |
| 3 - Consultar pedido | Detalhes completos de um pedido pelo número |
| 4 - Alterar pedido | Cliente, itens, quantidades, pagamento, avançar status e cancelar |
| 5 - Ver cardápio | Produtos disponíveis |
| 6 - Cadastrar produto | Novo produto no cardápio escolhendo a categoria |

## Regras de negócio

- Todo pedido tem pelo menos 1 item; o último item não pode ser removido (para desistir, cancela-se o pedido).
- A quantidade de cada item vai de 1 a 999.
- Produtos precisam de nome e preço maior que zero.
- Só pedidos com status **Recebido** podem ser alterados.

## Estrutura

```
src/padaria
├── Main.java
├── factory      → Factory Method
├── state        → State
├── strategy     → Strategy
├── repository   → Singleton
├── model        → Produto, Pao, Bolo, Salgado, Bebida, ItemPedido, Pedido
├── service      → Cardapio, PedidoService
├── view         → Menu (interface no console)
└── util         → Moeda
```

## Padrões de projeto aplicados

| Padrão | Classe | Onde |
|---|---|---|
| Factory Method | A | `factory/` |
| State | A | `state/` |
| Strategy | A | `strategy/` |
| Singleton | B | `repository/RepositorioPedidos.java` |

### 1. Factory Method — criação dos produtos

**Problema:** o cardápio tem várias categorias de produto (pão, bolo, salgado, bebida) e o código que cadastra produtos não deveria depender de cada classe concreta.

**Solução:**
- `ProdutoFactory` é o **criador abstrato**. Ele tem o método `cadastrarProduto(nome, preco)`, que valida os dados e chama o **método fábrica** `criarProduto(...)`.
- `PaoFactory`, `BoloFactory`, `SalgadoFactory` e `BebidaFactory` são os **criadores concretos**: cada um sobrescreve `criarProduto` e devolve seu produto.
- `Produto` é o **produto abstrato**; `Pao`, `Bolo`, `Salgado` e `Bebida` são os **produtos concretos**.

**Onde é usado:** `Cardapio` (carrega o cardápio inicial) e `Menu.cadastrarProduto()` (o usuário escolhe a categoria e a fábrica correspondente cria o produto).

**Vantagem:** para criar uma nova categoria (ex.: `Doce`), basta criar `Doce` e `DoceFactory`, sem mexer na validação nem no restante do sistema.

### 2. State — status do pedido

**Problema:** o pedido muda de comportamento conforme o status. Um pedido recebido pode ser editado; em preparo, não; pronto não pode ser cancelado; entregue não muda mais. Fazer isso com vários `if/switch` espalhados ficaria difícil de manter.

**Solução:**
- `EstadoPedido` é a interface do **estado**: `getNome()`, `permiteAlteracao()`, `avancar(pedido)` e `cancelar(pedido)`.
- `Recebido`, `EmPreparo`, `Pronto`, `Entregue` e `Cancelado` são os **estados concretos**. Cada um decide para qual estado o pedido vai.
- `Pedido` é o **contexto**: guarda o estado atual e delega para ele (`avancarStatus()` chama `estado.avancar(this)`).

**Fluxo:**

```
Recebido → Em preparo → Pronto → Entregue
    ↓           ↓
 Cancelado   Cancelado
```

| Estado | Pode alterar? | Avançar | Cancelar |
|---|---|---|---|
| Recebido | Sim | Em preparo (se tiver itens) | Sim |
| Em preparo | Não | Pronto | Sim |
| Pronto | Não | Entregue | Não |
| Entregue | Não | Não | Não |
| Cancelado | Não | Não | Não |

### 3. Strategy — forma de pagamento

**Problema:** cada forma de pagamento calcula o total de um jeito (desconto, acréscimo ou valor normal).

**Solução:**
- `FormaPagamento` é a interface da **estratégia**, com `calcularTotal(subtotal)`.
- `PagamentoDinheiro` (sem desconto), `PagamentoPix` (5% de desconto), `PagamentoDebito` (sem desconto) e `PagamentoCredito` (acréscimo de 3%) são as **estratégias concretas**.
- `Pedido` é o **contexto**: `getTotal()` chama `formaPagamento.calcularTotal(getSubtotal())` sem saber qual estratégia está usando.

**Vantagem:** a forma de pagamento pode ser trocada na alteração do pedido e o total é recalculado automaticamente. Uma nova forma (ex.: vale-refeição) é só mais uma classe.

### 4. Singleton — repositório de pedidos

**Problema:** todos os pedidos precisam ficar em um único lugar e a numeração não pode se repetir.

**Solução:** `RepositorioPedidos` tem:
- construtor `private` (ninguém consegue dar `new`);
- atributo `static` com a única instância;
- método `getInstancia()` (`synchronized`) que cria a instância na primeira chamada e devolve sempre a mesma.

Ele guarda os pedidos em memória (`LinkedHashMap`) e gera o número sequencial de cada pedido. `PedidoService` acessa o repositório por `RepositorioPedidos.getInstancia()`.

## Roteiro sugerido para a apresentação

1. Cadastrar um produto novo (opção 6) → mostra o **Factory Method**.
2. Cadastrar um pedido pagando com Pix (opção 1) → mostra o **Strategy** (5% de desconto no total).
3. Alterar o pedido trocando para cartão de crédito (opção 4 → 5) → total recalculado pela outra estratégia.
4. Avançar o status e tentar adicionar um item (opção 4 → 6 e depois 2) → o **State** bloqueia a alteração.
5. Tentar cancelar um pedido pronto → erro definido pelo estado `Pronto`.
6. Listar pedidos (opção 2) → todos vêm do mesmo **Singleton**.
