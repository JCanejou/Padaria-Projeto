package padaria.strategy;

public interface FormaPagamento {
    String getNome();

    String getRegra();

    double calcularTotal(double subtotal);
}
