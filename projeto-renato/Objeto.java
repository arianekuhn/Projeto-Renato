
package com.mycompany.main;

public class Objeto {
    private int idObjeto;
    private String nome;
    private String situacao;
    private int idTipo;

    public Objeto(int idObjeto, String nome, String situacao, int idTipo) {
        this.idObjeto = idObjeto;
        this.nome = nome.toUpperCase();
        this.situacao = situacao.toUpperCase();
        this.idTipo = idTipo;
    }

    public int getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(int idObjeto) {
        this.idObjeto = idObjeto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome.toUpperCase();
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao.toUpperCase();
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }
}
    