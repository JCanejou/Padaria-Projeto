package padaria.model;

import padaria.util.Moeda;

public class ItemPedido {
    private final Produto produto;
    private int quantidade;

    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        setQuantidade(quantidade);
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return produto.getPreco() * quantidade;
    }

    @Override
    public String toString() {
        return quantidade + "x " + produto.getNome() + " (" + Moeda.formatar(produto.getPreco()) + ") = " + Moeda.formatar(getSubtotal());
    }
}
