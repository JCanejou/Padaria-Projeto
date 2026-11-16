package padaria.factory;

import padaria.model.Bebida;
import padaria.model.Produto;

public class BebidaFactory extends ProdutoFactory {
    @Override
    protected Produto criarProduto(String nome, double preco) {
        return new Bebida(nome, preco);
    }
}
