package padaria.strategy;

public class PagamentoDebito implements FormaPagamento {
    @Override
    public String getNome() {
        return "Cartão de débito";
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
