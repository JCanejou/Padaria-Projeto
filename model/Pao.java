package padaria.model;

public class Pao extends Produto {
    public Pao(String nome, double preco) {
        super(nome, preco);
    }

    @Override
    public String getCategoria() {
        return "Pão";
    }
}
