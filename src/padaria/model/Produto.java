package padaria.model;

import padaria.util.Moeda;

public abstract class Produto {
    private final String nome;
    private final double preco;

    protected Produto(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public abstract String getCategoria();

    @Override
    public String toString() {
        return nome + " (" + getCategoria() + ") - " + Moeda.formatar(preco);
    }
}
