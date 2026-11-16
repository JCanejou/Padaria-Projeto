package padaria.state;

import padaria.model.Pedido;

public class Recebido implements EstadoPedido {
    @Override
    public String getNome() {
        return "Recebido";
    }

    @Override
    public boolean permiteAlteracao() {
        return true;
    }

    @Override
    public void avancar(Pedido pedido) {
        if (pedido.getItens().isEmpty()) {
            throw new IllegalStateException("Não é possível preparar um pedido sem itens.");
        }
        pedido.setEstado(new EmPreparo());
    }

    @Override
    public void cancelar(Pedido pedido) {
        pedido.setEstado(new Cancelado());
    }
}
