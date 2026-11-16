package padaria.factory;

import padaria.model.Pao;
import padaria.model.Produto;

public class PaoFactory extends ProdutoFactory {
    @Override
    protected Produto criarProduto(String nome, double preco) {
        return new Pao(nome, preco);
    }
}
