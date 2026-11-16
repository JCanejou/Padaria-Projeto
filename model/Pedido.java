package padaria.model;

import padaria.state.EstadoPedido;
import padaria.state.Recebido;
import padaria.strategy.FormaPagamento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {
    private final int id;
    private final LocalDateTime criadoEm;
    private final List<ItemPedido> itens = new ArrayList<>();
    private String cliente;
    private FormaPagamento formaPagamento;
    private EstadoPedido estado;

    public Pedido(int id, String cliente, FormaPagamento formaPagamento) {
        this.id = id;
        this.criadoEm = LocalDateTime.now();
        this.estado = new Recebido();
        setCliente(cliente);
        setFormaPagamento(formaPagamento);
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        verificarSePodeAlterar();
        if (cliente == null || cliente.isBlank()) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório.");
        }
        this.cliente = cliente.trim();
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        verificarSePodeAlterar();
        if (formaPagamento == null) {
            throw new IllegalArgumentException("A forma de pagamento é obrigatória.");
        }
        this.formaPagamento = formaPagamento;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public void adicionarItem(Produto produto, int quantidade) {
        verificarSePodeAlterar();
        for (ItemPedido item : itens) {
            if (item.getProduto().getNome().equalsIgnoreCase(produto.getNome())) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                return;
            }
        }
        itens.add(new ItemPedido(produto, quantidade));
    }

    public void removerItem(int indice) {
        verificarSePodeAlterar();
        validarIndice(indice);
        itens.remove(indice);
    }

    public void alterarQuantidade(int indice, int quantidade) {
        verificarSePodeAlterar();
        validarIndice(indice);
        itens.get(indice).setQuantidade(quantidade);
    }

    public void avancarStatus() {
        estado.avancar(this);
    }

    public void cancelar() {
        estado.cancelar(this);
    }

    public double getSubtotal() {
        double subtotal = 0;
        for (ItemPedido item : itens) {
            subtotal += item.getSubtotal();
        }
        return subtotal;
    }

    public double getTotal() {
        return formaPagamento.calcularTotal(getSubtotal());
    }

    private void verificarSePodeAlterar() {
        if (estado != null && !estado.permiteAlteracao()) {
            throw new IllegalStateException("Pedido com status \"" + estado.getNome() + "\" não pode ser alterado.");
        }
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= itens.size()) {
            throw new IllegalArgumentException("Item inválido.");
        }
    }
}
