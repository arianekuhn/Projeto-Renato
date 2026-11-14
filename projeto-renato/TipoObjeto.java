
package com.mycompany.main;

public class TipoObjeto {
    
    private int idTipo;
    private String nome;

    public TipoObjeto(int idTipo, String nome) {
        this.idTipo = idTipo;
        this.nome = nome.toUpperCase();
        
    }
    
    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }
    
     public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome.toUpperCase();
    }
}


