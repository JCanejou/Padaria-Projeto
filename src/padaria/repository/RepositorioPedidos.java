package padaria.repository;

import padaria.model.Pedido;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class RepositorioPedidos {
    private static RepositorioPedidos instancia;

    private final Map<Integer, Pedido> pedidos = new LinkedHashMap<>();
    private int proximoId = 1;

    private RepositorioPedidos() {
    }

    public static synchronized RepositorioPedidos getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioPedidos();
        }
        return instancia;
    }

    public int gerarId() {
        return proximoId++;
    }

    public void salvar(Pedido pedido) {
        pedidos.put(pedido.getId(), pedido);
    }

    public Optional<Pedido> buscarPorId(int id) {
        return Optional.ofNullable(pedidos.get(id));
    }

    public List<Pedido> listarTodos() {
        return new ArrayList<>(pedidos.values());
    }
}
