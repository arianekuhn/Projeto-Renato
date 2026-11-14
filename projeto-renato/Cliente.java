
package com.mycompany.main;

public class Cliente {
    
    private int idCliente;
    private String nome;
    private String email;
    private String cidade;

    public Cliente(int idCliente, String nome, String email, String cidade) {
        this.idCliente = idCliente;
        this.nome = nome.toUpperCase();
        this.email = email.toUpperCase();
        this.cidade = cidade.toUpperCase();
    }
    
    public int getCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome.toUpperCase();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toUpperCase();
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade.toUpperCase();
    }
}

