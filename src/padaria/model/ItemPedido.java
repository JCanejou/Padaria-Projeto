package padaria.model;

import padaria.util.Moeda;

public final class ItemPedido {
    public static final int QUANTIDADE_MAXIMA = 999;

    private final Produto produto;
    private int quantidade;

    public ItemPedido(Produto produto, int quantidade) {
        validarQuantidade(quantidade);
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    void setQuantidade(int quantidade) {
        validarQuantidade(quantidade);
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return produto.getPreco() * quantidade;
    }

    public static void validarQuantidade(int quantidade) {
        if (quantidade < 1 || quantidade > QUANTIDADE_MAXIMA) {
            throw new IllegalArgumentException("A quantidade deve ser entre 1 e " + QUANTIDADE_MAXIMA + ".");
        }
    }

    @Override
    public String toString() {
        return quantidade + "x " + produto.getNome() + " (" + Moeda.formatar(produto.getPreco()) + ") = " + Moeda.formatar(getSubtotal());
    }
}
