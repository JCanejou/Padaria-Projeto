package padaria.factory;

import padaria.model.Produto;

public abstract class ProdutoFactory {
    public Produto cadastrarProduto(String nome, double preco) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        if (preco <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }
        return criarProduto(nome.trim(), preco);
    }

    protected abstract Produto criarProduto(String nome, double preco);
}
