package padaria.service;

import padaria.model.ItemPedido;
import padaria.model.Pedido;
import padaria.repository.RepositorioPedidos;
import padaria.strategy.FormaPagamento;

import java.util.List;

public class PedidoService {
    private final RepositorioPedidos repositorio = RepositorioPedidos.getInstancia();

    public Pedido cadastrar(String cliente, List<ItemPedido> itens, FormaPagamento formaPagamento) {
        if (cliente == null || cliente.isBlank()) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório.");
        }
        if (formaPagamento == null) {
            throw new IllegalArgumentException("A forma de pagamento é obrigatória.");
        }
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("O pedido precisa ter pelo menos um item.");
        }
        Pedido pedido = new Pedido(repositorio.gerarId(), cliente, formaPagamento);
        for (ItemPedido item : itens) {
            pedido.adicionarItem(item.getProduto(), item.getQuantidade());
        }
        repositorio.salvar(pedido);
        return pedido;
    }

    public List<Pedido> listar() {
        return repositorio.listarTodos();
    }

    public Pedido consultar(int id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido #" + id + " não encontrado."));
    }
}
