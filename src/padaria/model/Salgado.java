package padaria.model;

public class Salgado extends Produto {
    public Salgado(String nome, double preco) {
        super(nome, preco);
    }

    @Override
    public String getCategoria() {
        return "Salgado";
    }
}
