package padaria.state;

import padaria.model.Pedido;

public class Cancelado implements EstadoPedido {
    @Override
    public String getNome() {
        return "Cancelado";
    }

    @Override
    public boolean permiteAlteracao() {
        return false;
    }

    @Override
    public void avancar(Pedido pedido) {
        throw new IllegalStateException("Um pedido cancelado não pode avançar.");
    }

    @Override
    public void cancelar(Pedido pedido) {
        throw new IllegalStateException("O pedido já está cancelado.");
    }
}
