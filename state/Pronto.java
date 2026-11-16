package padaria.state;

import padaria.model.Pedido;

public class Pronto implements EstadoPedido {
    @Override
    public String getNome() {
        return "Pronto";
    }

    @Override
    public boolean permiteAlteracao() {
        return false;
    }

    @Override
    public void avancar(Pedido pedido) {
        pedido.setEstado(new Entregue());
    }

    @Override
    public void cancelar(Pedido pedido) {
        throw new IllegalStateException("Um pedido pronto não pode mais ser cancelado.");
    }
}
