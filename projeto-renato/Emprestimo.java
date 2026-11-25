import java.time.LocalDate;


public class Emprestimo {

    private int idEmprestimo;
    private int idCliente;
    private int idObjeto;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao; 

    public Emprestimo(int idEmprestimo, int idCliente, int idObjeto, LocalDate dataEmprestimo) {
        this.idEmprestimo = idEmprestimo;
        this.idCliente = idCliente;
        this.idObjeto = idObjeto;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = null;
    }

    public int getIdEmprestimo() {
        return idEmprestimo;
    }

    public void setIdEmprestimo(int idEmprestimo) {
        this.idEmprestimo = idEmprestimo;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(int idObjeto) {
        this.idObjeto = idObjeto;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    // O emprestimo esta em aberto enquanto nao tiver data de devolucao
    public boolean isEmAberto() {
        return dataDevolucao == null;
    }

    public String getStatus() {
        return isEmAberto() ? "EMPRESTADO" : "DEVOLVIDO";
    }
}

