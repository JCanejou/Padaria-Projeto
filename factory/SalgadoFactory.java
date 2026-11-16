package padaria.factory;

import padaria.model.Salgado;
import padaria.model.Produto;

public class SalgadoFactory extends ProdutoFactory {
    @Override
    protected Produto criarProduto(String nome, double preco) {
        return new Salgado(nome, preco);
    }
}
