package padaria.strategy;

public class PagamentoDinheiro implements FormaPagamento {
    @Override
    public String getNome() {
        return "Dinheiro";
    }

    @Override
    public String getRegra() {
        return "sem desconto";
    }

    @Override
    public double calcularTotal(double subtotal) {
        return subtotal;
    }
}
