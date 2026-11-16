package padaria.model;

public class Bebida extends Produto {
    public Bebida(String nome, double preco) {
        super(nome, preco);
    }

    @Override
    public String getCategoria() {
        return "Bebida";
    }
}
