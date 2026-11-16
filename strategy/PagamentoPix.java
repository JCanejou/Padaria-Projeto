package padaria.strategy;

public class PagamentoPix implements FormaPagamento {
    @Override
    public String getNome() {
        return "Pix";
    }

    @Override
    public String getRegra() {
        return "5% de desconto";
    }

    @Override
    public double calcularTotal(double subtotal) {
        return subtotal * 0.95;
    }
}
