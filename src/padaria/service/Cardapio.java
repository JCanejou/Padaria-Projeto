package padaria.service;

import padaria.factory.BebidaFactory;
import padaria.factory.BoloFactory;
import padaria.factory.PaoFactory;
import padaria.factory.ProdutoFactory;
import padaria.factory.SalgadoFactory;
import padaria.model.Produto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Cardapio {
    private final List<Produto> produtos = new ArrayList<>();

    public Cardapio() {
        ProdutoFactory pao = new PaoFactory();
        ProdutoFactory bolo = new BoloFactory();
        ProdutoFactory salgado = new SalgadoFactory();
        ProdutoFactory bebida = new BebidaFactory();

        adicionar(pao.cadastrarProduto("Pão francês", 0.80));
        adicionar(pao.cadastrarProduto("Pão de queijo", 1.50));
        adicionar(pao.cadastrarProduto("Pão doce", 2.50));
        adicionar(bolo.cadastrarProduto("Bolo de chocolate (fatia)", 7.00));
        adicionar(bolo.cadastrarProduto("Bolo de milho (fatia)", 6.00));
        adicionar(salgado.cadastrarProduto("Coxinha", 5.50));
        adicionar(salgado.cadastrarProduto("Pastel de carne", 6.00));
        adicionar(bebida.cadastrarProduto("Café com leite", 4.00));
        adicionar(bebida.cadastrarProduto("Suco de laranja", 6.50));
    }

    public void adicionar(Produto produto) {
        for (Produto existente : produtos) {
            if (existente.getNome().equalsIgnoreCase(produto.getNome())) {
                throw new IllegalArgumentException("Já existe um produto com esse nome no cardápio.");
            }
        }
        produtos.add(produto);
    }

    public List<Produto> getProdutos() {
        return Collections.unmodifiableList(produtos);
    }
}
