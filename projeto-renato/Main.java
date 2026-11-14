package com.mycompany.main;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);

    // Listas que guardam os cadastros em memoria
    static ArrayList<Cliente> listaClientes = new ArrayList<>();
    static ArrayList<TipoObjeto> listaTipos = new ArrayList<>();
    static ArrayList<Objeto> listaObjetos = new ArrayList<>();
    static ArrayList<Manutencao> listaManutencoes = new ArrayList<>();

    // Contadores para gerar os codigos automaticamente
    static int proxIdCliente = 1;
    static int proxIdTipo = 1;
    static int proxIdObjeto = 1;
    static int proxIdManutencao = 1;

    public static void main(String[] args) {
        menu();
    }

    // ---------- Metodos auxiliares de leitura ----------
    static int lerInt(String mensagem) {
        System.out.print(mensagem);
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static String lerTexto(String mensagem) {
        String texto;
        do {
            System.out.print(mensagem);
            texto = sc.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("O campo nao pode ficar vazio!");
            }
        } while (texto.isEmpty());
        return texto;
    }

    // ---------- Metodos auxiliares de busca ----------
    static TipoObjeto buscarTipo(int id) {
        for (TipoObjeto t : listaTipos) {
            if (t.getIdTipo() == id) {
                return t;
            }
        }
        return null;
    }

    static Objeto buscarObjeto(int id) {
        for (Objeto o : listaObjetos) {
            if (o.getIdObjeto() == id) {
                return o;
            }
        }
        return null;
    }

    // ---------- Menu principal ----------
    static void menu() {
        int opcao;
        do {
            System.out.println("\n### SISTEMA DE EMPRESTIMOS DE OBJETOS ###");
            System.out.println("==========================================");
            System.out.println("|  1 - Cadastro de Clientes              |");
            System.out.println("|  2 - Cadastro de Tipos de Objetos      |");
            System.out.println("|  3 - Cadastro de Objetos               |");
            System.out.println("|  4 - Cadastro de Manutencoes           |");
            System.out.println("|  0 - Sair                              |");
            System.out.println("==========================================");

            opcao = lerInt("Opcao -> ");

            switch (opcao) {
                case 1 -> clientes();
                case 2 -> tipos();
                case 3 -> objetos();
                case 4 -> manutencoes();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }

    static void submenu(String titulo) {
        System.out.println("\n--- " + titulo + " ---");
        System.out.println("1 - Incluir");
        System.out.println("2 - Consultar");
        System.out.println("0 - Voltar");
    }

    // ---------- Clientes ----------
    static void clientes() {
        int opcao;
        do {
            submenu("CADASTRO DE CLIENTES");
            opcao = lerInt("Opcao -> ");
            switch (opcao) {
                case 1 -> incluirCliente();
                case 2 -> consultarClientes();
                case 0 -> {
                }
                default -> System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }

    static void incluirCliente() {
        String nome = lerTexto("Nome: ");
        String email = lerTexto("E-mail: ");
        String cidade = lerTexto("Cidade: ");
        listaClientes.add(new Cliente(proxIdCliente, nome, email, cidade));
        System.out.println("Cliente incluido com sucesso! Codigo: " + proxIdCliente);
        proxIdCliente++;
    }

    static void consultarClientes() {
        if (listaClientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        System.out.println("\nCOD | NOME | E-MAIL | CIDADE");
        for (Cliente c : listaClientes) {
            System.out.println(c.getIdCliente() + " | " + c.getNome() + " | "
                    + c.getEmail() + " | " + c.getCidade());
        }
    }

    // ---------- Tipos de Objetos ----------
    static void tipos() {
        int opcao;
        do {
            submenu("CADASTRO DE TIPOS DE OBJETOS");
            opcao = lerInt("Opcao -> ");
            switch (opcao) {
                case 1 -> incluirTipo();
                case 2 -> consultarTipos();
                case 0 -> {
                }
                default -> System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }

    static void incluirTipo() {
        String nome = lerTexto("Nome do tipo: ");
        listaTipos.add(new TipoObjeto(proxIdTipo, nome));
        System.out.println("Tipo incluido com sucesso! Codigo: " + proxIdTipo);
        proxIdTipo++;
    }

    static void consultarTipos() {
        if (listaTipos.isEmpty()) {
            System.out.println("Nenhum tipo de objeto cadastrado.");
            return;
        }
        System.out.println("\nCOD | NOME");
        for (TipoObjeto t : listaTipos) {
            System.out.println(t.getIdTipo() + " | " + t.getNome());
        }
    }

    // ---------- Objetos ----------
    static void objetos() {
        int opcao;
        do {
            submenu("CADASTRO DE OBJETOS");
            opcao = lerInt("Opcao -> ");
            switch (opcao) {
                case 1 -> incluirObjeto();
                case 2 -> consultarObjetos();
                case 0 -> {
                }
                default -> System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }

    static void incluirObjeto() {
        if (listaTipos.isEmpty()) {
            System.out.println("Cadastre ao menos um tipo de objeto antes de incluir objetos.");
            return;
        }
        String nome = lerTexto("Nome do objeto: ");
        String situacao = lerTexto("Situacao (ex: DISPONIVEL, EMPRESTADO): ");

        consultarTipos();
        int idTipo = lerInt("Codigo do tipo -> ");
        if (buscarTipo(idTipo) == null) {
            System.out.println("Tipo nao encontrado! Objeto nao incluido.");
            return;
        }

        listaObjetos.add(new Objeto(proxIdObjeto, nome, situacao, idTipo));
        System.out.println("Objeto incluido com sucesso! Codigo: " + proxIdObjeto);
        proxIdObjeto++;
    }

    static void consultarObjetos() {
        if (listaObjetos.isEmpty()) {
            System.out.println("Nenhum objeto cadastrado.");
            return;
        }
        System.out.println("\nCOD | NOME | SITUACAO | TIPO");
        for (Objeto o : listaObjetos) {
            TipoObjeto t = buscarTipo(o.getIdTipo());
            String nomeTipo = (t != null) ? t.getNome() : "?";
            System.out.println(o.getIdObjeto() + " | " + o.getNome() + " | "
                    + o.getSituacao() + " | " + nomeTipo);
        }
    }

    // ---------- Manutencoes ----------
    static void manutencoes() {
        int opcao;
        do {
            submenu("CADASTRO DE MANUTENCOES");
            opcao = lerInt("Opcao -> ");
            switch (opcao) {
                case 1 -> incluirManutencao();
                case 2 -> consultarManutencoes();
                case 0 -> {
                }
                default -> System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }

    static void incluirManutencao() {
        if (listaObjetos.isEmpty()) {
            System.out.println("Cadastre ao menos um objeto antes de incluir manutencoes.");
            return;
        }
        consultarObjetos();
        int idObjeto = lerInt("Codigo do objeto -> ");
        if (buscarObjeto(idObjeto) == null) {
            System.out.println("Objeto nao encontrado! Manutencao nao incluida.");
            return;
        }
        String descricao = lerTexto("Descricao: ");
        String status = lerTexto("Status (ex: EM ANDAMENTO, CONCLUIDA): ");

        listaManutencoes.add(new Manutencao(proxIdManutencao, idObjeto, descricao, status));
        System.out.println("Manutencao incluida com sucesso! Codigo: " + proxIdManutencao);
        proxIdManutencao++;
    }

    static void consultarManutencoes() {
        if (listaManutencoes.isEmpty()) {
            System.out.println("Nenhuma manutencao cadastrada.");
            return;
        }
        System.out.println("\nCOD | OBJETO | DESCRICAO | STATUS");
        for (Manutencao m : listaManutencoes) {
            Objeto o = buscarObjeto(m.getIdObjeto());
            String nomeObjeto = (o != null) ? o.getNome() : "?";
            System.out.println(m.getIdManutencao() + " | " + nomeObjeto + " | "
                    + m.getDescricao() + " | " + m.getStatus());
        }
    }
}