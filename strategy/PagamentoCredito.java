package padaria.strategy;

public class PagamentoCredito implements FormaPagamento {
    @Override
    public String getNome() {
        return "Cartão de crédito";
    }

    @Override
    public String getRegra() {
        return "acréscimo de 3%";
    }

    @Override
    public double calcularTotal(double subtotal) {
        return subtotal * 1.03;
    }
}
