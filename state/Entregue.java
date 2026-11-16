package padaria.state;

import padaria.model.Pedido;

public class Entregue implements EstadoPedido {
    @Override
    public String getNome() {
        return "Entregue";
    }

    @Override
    public boolean permiteAlteracao() {
        return false;
    }

    @Override
    public void avancar(Pedido pedido) {
        throw new IllegalStateException("O pedido já foi entregue.");
    }

    @Override
    public void cancelar(Pedido pedido) {
        throw new IllegalStateException("Um pedido entregue não pode ser cancelado.");
    }
}
