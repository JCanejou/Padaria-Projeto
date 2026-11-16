package padaria.model;

public class Bolo extends Produto {
    public Bolo(String nome, double preco) {
        super(nome, preco);
    }

    @Override
    public String getCategoria() {
        return "Bolo";
    }
}
