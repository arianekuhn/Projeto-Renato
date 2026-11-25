

public class Manutencao {
    private int idManutencao;
    private int idObjeto;
    private String descricao;
    private String status;

    public Manutencao(int idManutencao, int idObjeto, String descricao, String status) {
        this.idManutencao = idManutencao;
        this.idObjeto = idObjeto;
        this.descricao = descricao.toUpperCase();
        this.status = status.toUpperCase();
    }

    public int getIdManutencao() {
        return idManutencao;
    }

    public void setIdManutencao(int idManutencao) {
        this.idManutencao = idManutencao;
    }

    public int getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(int idObjeto) {
        this.idObjeto = idObjeto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao.toUpperCase();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status.toUpperCase();
    }
}
