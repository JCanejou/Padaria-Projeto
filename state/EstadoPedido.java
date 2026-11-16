package padaria.state;

import padaria.model.Pedido;

public interface EstadoPedido {
    String getNome();

    boolean permiteAlteracao();

    void avancar(Pedido pedido);

    void cancelar(Pedido pedido);
}
