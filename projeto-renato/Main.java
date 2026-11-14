package com.mycompany.main;

import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        menu();
    }

    static int lerInt(String mensagem) {
        System.out.print(mensagem);
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

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
                case 1:
                    clientes();
                    break;
                case 2:
                    tipos();
                    break;
                case 3:
                    objetos();
                    break;
                case 4:
                    manutencoes();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }

    // Os cadastros serao implementados na proxima atividade.
    static void clientes() {
        System.out.println("Cadastro de Clientes - em desenvolvimento.");
    }

    static void tipos() {
        System.out.println("Cadastro de Tipos de Objetos - em desenvolvimento.");
    }

    static void objetos() {
        System.out.println("Cadastro de Objetos - em desenvolvimento.");
    }

    static void manutencoes() {
        System.out.println("Cadastro de Manutencoes - em desenvolvimento.");
    }
}