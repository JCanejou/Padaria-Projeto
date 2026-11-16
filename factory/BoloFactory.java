package padaria.factory;

import padaria.model.Bolo;
import padaria.model.Produto;

public class BoloFactory extends ProdutoFactory {
    @Override
    protected Produto criarProduto(String nome, double preco) {
        return new Bolo(nome, preco);
    }
}
