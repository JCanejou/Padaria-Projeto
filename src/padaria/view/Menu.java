package padaria.view;

import padaria.factory.BebidaFactory;
import padaria.factory.BoloFactory;
import padaria.factory.PaoFactory;
import padaria.factory.ProdutoFactory;
import padaria.factory.SalgadoFactory;
import padaria.model.ItemPedido;
import padaria.model.Pedido;
import padaria.model.Produto;
import padaria.service.Cardapio;
import padaria.service.PedidoService;
import padaria.strategy.FormaPagamento;
import padaria.strategy.PagamentoCredito;
import padaria.strategy.PagamentoDebito;
import padaria.strategy.PagamentoDinheiro;
import padaria.strategy.PagamentoPix;
import padaria.util.Moeda;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Scanner scanner = new Scanner(System.in);
    private final PedidoService pedidoService = new PedidoService();
    private final Cardapio cardapio = new Cardapio();
    private final List<FormaPagamento> formasPagamento = List.of(
            new PagamentoDinheiro(),
            new PagamentoPix(),
            new PagamentoDebito(),
            new PagamentoCredito()
    );

    public void iniciar() {
        System.out.println("=================================");
        System.out.println("   PADARIA - CONTROLE DE PEDIDOS  ");
        System.out.println("=================================");

        int opcao;
        do {
            System.out.println();
            System.out.println("1 - Cadastrar pedido");
            System.out.println("2 - Listar pedidos");
            System.out.println("3 - Consultar pedido");
            System.out.println("4 - Alterar pedido");
            System.out.println("5 - Ver cardápio");
            System.out.println("6 - Cadastrar produto no cardápio");
            System.out.println("0 - Sair");
            opcao = lerInteiro("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1 -> cadastrarPedido();
                    case 2 -> listarPedidos();
                    case 3 -> consultarPedido();
                    case 4 -> alterarPedido();
                    case 5 -> mostrarCardapio();
                    case 6 -> cadastrarProduto();
                    case 0 -> System.out.println("Encerrando o sistema...");
                    default -> System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void cadastrarPedido() {
        System.out.println("\n--- NOVO PEDIDO ---");
        String cliente = lerTexto("Nome do cliente: ");
        List<ItemPedido> itens = new ArrayList<>();

        while (true) {
            mostrarCardapio();
            int numero = lerInteiro("Número do produto (0 para finalizar): ");
            if (numero == 0) {
                break;
            }
            Produto produto = escolherProduto(numero);
            if (produto == null) {
                continue;
            }
            int quantidade = lerInteiro("Quantidade: ");
            try {
                adicionarNaLista(itens, produto, quantidade);
                System.out.println("Adicionado: " + quantidade + "x " + produto.getNome());
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }

        if (itens.isEmpty()) {
            System.out.println("Pedido não cadastrado: nenhum item foi adicionado.");
            return;
        }

        FormaPagamento formaPagamento = escolherFormaPagamento();
        Pedido pedido = pedidoService.cadastrar(cliente, itens, formaPagamento);
        System.out.println("\nPedido #" + pedido.getId() + " cadastrado com sucesso!");
        imprimirDetalhes(pedido);
    }

    private void adicionarNaLista(List<ItemPedido> itens, Produto produto, int quantidade) {
        ItemPedido.validarQuantidade(quantidade);
        for (int i = 0; i < itens.size(); i++) {
            ItemPedido item = itens.get(i);
            if (item.getProduto() == produto) {
                int total = item.getQuantidade() + quantidade;
                ItemPedido.validarQuantidade(total);
                itens.set(i, new ItemPedido(produto, total));
                return;
            }
        }
        itens.add(new ItemPedido(produto, quantidade));
    }

    private void listarPedidos() {
        List<Pedido> pedidos = pedidoService.listar();
        System.out.println("\n--- PEDIDOS ---");
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado.");
            return;
        }
        System.out.printf("%-5s %-20s %-6s %-12s %-12s%n", "Nº", "Cliente", "Itens", "Total", "Status");
        for (Pedido pedido : pedidos) {
            System.out.printf("%-5s %-20s %-6d %-12s %-12s%n",
                    "#" + pedido.getId(),
                    resumir(pedido.getCliente(), 20),
                    pedido.getItens().size(),
                    Moeda.formatar(pedido.getTotal()),
                    pedido.getEstado().getNome());
        }
    }

    private void consultarPedido() {
        int id = lerInteiro("Número do pedido: ");
        imprimirDetalhes(pedidoService.consultar(id));
    }

    private void alterarPedido() {
        int id = lerInteiro("Número do pedido: ");
        Pedido pedido = pedidoService.consultar(id);

        int opcao;
        do {
            imprimirDetalhes(pedido);
            System.out.println("\n--- ALTERAR PEDIDO #" + pedido.getId() + " ---");
            System.out.println("1 - Alterar nome do cliente");
            System.out.println("2 - Adicionar item");
            System.out.println("3 - Remover item");
            System.out.println("4 - Alterar quantidade de um item");
            System.out.println("5 - Alterar forma de pagamento");
            System.out.println("6 - Avançar status");
            System.out.println("7 - Cancelar pedido");
            System.out.println("0 - Voltar");
            opcao = lerInteiro("Escolha uma opção: ");

            try {
                if (opcao >= 1 && opcao <= 5) {
                    pedido.verificarSePodeAlterar();
                }
                switch (opcao) {
                    case 1 -> pedido.setCliente(lerTexto("Novo nome do cliente: "));
                    case 2 -> adicionarItem(pedido);
                    case 3 -> pedido.removerItem(escolherItem(pedido));
                    case 4 -> {
                        int indice = escolherItem(pedido);
                        pedido.alterarQuantidade(indice, lerInteiro("Nova quantidade: "));
                    }
                    case 5 -> pedido.setFormaPagamento(escolherFormaPagamento());
                    case 6 -> {
                        pedido.avancarStatus();
                        System.out.println("Status atualizado para: " + pedido.getEstado().getNome());
                    }
                    case 7 -> {
                        pedido.cancelar();
                        System.out.println("Pedido cancelado.");
                    }
                    case 0 -> {
                    }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void adicionarItem(Pedido pedido) {
        mostrarCardapio();
        Produto produto = escolherProduto(lerInteiro("Número do produto: "));
        if (produto != null) {
            pedido.adicionarItem(produto, lerInteiro("Quantidade: "));
        }
    }

    private void mostrarCardapio() {
        System.out.println("\n--- CARDÁPIO ---");
        List<Produto> produtos = cardapio.getProdutos();
        for (int i = 0; i < produtos.size(); i++) {
            System.out.println((i + 1) + " - " + produtos.get(i));
        }
    }

    private void cadastrarProduto() {
        System.out.println("\n--- NOVO PRODUTO ---");
        System.out.println("1 - Pão");
        System.out.println("2 - Bolo");
        System.out.println("3 - Salgado");
        System.out.println("4 - Bebida");
        ProdutoFactory factory = switch (lerInteiro("Categoria: ")) {
            case 1 -> new PaoFactory();
            case 2 -> new BoloFactory();
            case 3 -> new SalgadoFactory();
            case 4 -> new BebidaFactory();
            default -> throw new IllegalArgumentException("Categoria inválida.");
        };
        String nome = lerTexto("Nome do produto: ");
        double preco = lerDecimal("Preço: ");
        Produto produto = factory.cadastrarProduto(nome, preco);
        cardapio.adicionar(produto);
        System.out.println("Produto cadastrado: " + produto);
    }

    private Produto escolherProduto(int numero) {
        List<Produto> produtos = cardapio.getProdutos();
        if (numero < 1 || numero > produtos.size()) {
            System.out.println("Produto inválido.");
            return null;
        }
        return produtos.get(numero - 1);
    }

    private int escolherItem(Pedido pedido) {
        List<ItemPedido> itens = pedido.getItens();
        for (int i = 0; i < itens.size(); i++) {
            System.out.println((i + 1) + " - " + itens.get(i));
        }
        return lerInteiro("Número do item: ") - 1;
    }

    private FormaPagamento escolherFormaPagamento() {
        while (true) {
            System.out.println("\n--- FORMA DE PAGAMENTO ---");
            for (int i = 0; i < formasPagamento.size(); i++) {
                FormaPagamento forma = formasPagamento.get(i);
                System.out.println((i + 1) + " - " + forma.getNome() + " (" + forma.getRegra() + ")");
            }
            int opcao = lerInteiro("Escolha: ");
            if (opcao >= 1 && opcao <= formasPagamento.size()) {
                return formasPagamento.get(opcao - 1);
            }
            System.out.println("Opção inválida.");
        }
    }

    private void imprimirDetalhes(Pedido pedido) {
        System.out.println("\n========== PEDIDO #" + pedido.getId() + " ==========");
        System.out.println("Cliente:   " + pedido.getCliente());
        System.out.println("Data:      " + pedido.getCriadoEm().format(DATA));
        System.out.println("Status:    " + pedido.getEstado().getNome());
        System.out.println("Itens:");
        for (ItemPedido item : pedido.getItens()) {
            System.out.println("  " + item);
        }
        FormaPagamento forma = pedido.getFormaPagamento();
        System.out.println("Subtotal:  " + Moeda.formatar(pedido.getSubtotal()));
        System.out.println("Pagamento: " + forma.getNome() + " (" + forma.getRegra() + ")");
        System.out.println("Total:     " + Moeda.formatar(pedido.getTotal()));
        System.out.println("=====================================");
    }

    private String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("Campo obrigatório.");
        }
    }

    private int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private double lerDecimal(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Digite um valor válido (ex: 4,50).");
            }
        }
    }

    private String resumir(String texto, int limite) {
        return texto.length() <= limite ? texto : texto.substring(0, limite - 3) + "...";
    }
}
