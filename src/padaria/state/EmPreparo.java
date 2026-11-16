package padaria.state;

import padaria.model.Pedido;

public class EmPreparo implements EstadoPedido {
    @Override
    public String getNome() {
        return "Em preparo";
    }

    @Override
    public boolean permiteAlteracao() {
        return false;
    }

    @Override
    public void avancar(Pedido pedido) {
        pedido.setEstado(new Pronto());
    }

    @Override
    public void cancelar(Pedido pedido) {
        pedido.setEstado(new Cancelado());
    }
}
