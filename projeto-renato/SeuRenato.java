import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class SeuRenato {
    static Scanner sc = new Scanner(System.in);

    // Situacoes possiveis de um objeto (controladas pelo sistema)
    static final String ATIVO = "ATIVO";              
    static final String EMPRESTADO = "EMPRESTADO";
    static final String EM_MANUTENCAO = "EM MANUTENCAO";
    static final String BAIXADO = "BAIXADO";

    // Status possiveis de uma manutencao
    static final String RECEBIDO = "RECEBIDO";
    static final String EM_SERVICO = "EM SERVICO";
    static final String RESOLVIDO = "RESOLVIDO";

    static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    // Listas que guardam os cadastros em memoria
    static ArrayList<Cliente> listaClientes = new ArrayList<>();
    static ArrayList<TipoObjeto> listaTipos = new ArrayList<>();
    static ArrayList<Objeto> listaObjetos = new ArrayList<>();
    static ArrayList<Manutencao> listaManutencoes = new ArrayList<>(); 
    static ArrayList<Emprestimo> listaEmprestimos = new ArrayList<>();

    // Contadores para gerar os codigos automaticamente
    static int proxIdCliente = 1;
    static int proxIdTipo = 1;
    static int proxIdObjeto = 1;
    static int proxIdManutencao = 1;
    static int proxIdEmprestimo = 1;

    public static void main(String[] args) {
        menu();
    }

    
    // Metodos auxiliares de leitura
   
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

    // Na edicao: se o usuario apenas teclar ENTER, o valor atual e mantido
    static String lerTextoOpcional(String mensagem, String atual) {
        System.out.print(mensagem + " [" + atual + "]: ");
        String texto = sc.nextLine().trim();
        return texto.isEmpty() ? atual : texto;
    }

    // Retorna o valor atual se ENTER, -1 se invalido
    static int lerIntOpcional(String mensagem, int atual) {
        System.out.print(mensagem + " [" + atual + "]: ");
        String texto = sc.nextLine().trim();
        if (texto.isEmpty()) {
            return atual;
        }
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Le uma data no formato dd/MM/aaaa. Se padrao != null, ENTER usa o padrao.
    static LocalDate lerData(String mensagem, LocalDate padrao) {
        while (true) {
            String sufixo = (padrao != null) ? " [" + formatar(padrao) + "]" : "";
            System.out.print(mensagem + " (dd/mm/aaaa)" + sufixo + ": ");
            String texto = sc.nextLine().trim();
            if (texto.isEmpty() && padrao != null) {
                return padrao;
            }
            try {
                return LocalDate.parse(texto, FORMATO_DATA);
            } catch (DateTimeParseException e) {
                System.out.println("Data invalida! Use o formato dd/mm/aaaa.");
            }
        }
    }

    static boolean confirmar(String mensagem) {
        System.out.print(mensagem + " (S/N): ");
        return sc.nextLine().trim().equalsIgnoreCase("S");
    }

    static String formatar(LocalDate data) {
        return (data == null) ? "-" : data.format(FORMATO_DATA);
    }

    // Metodos auxiliares de busca

    static Cliente buscarCliente(int id) {
        for (Cliente c : listaClientes) {
            if (c.getIdCliente() == id) {
                return c;
            }
        }
        return null;
    }

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

    static Manutencao buscarManutencao(int id) {
        for (Manutencao m : listaManutencoes) {
            if (m.getIdManutencao() == id) {
                return m;
            }
        }
        return null;
    }

    static Manutencao buscarManutencaoDoObjeto(int idObjeto) {
        for (Manutencao m : listaManutencoes) {
            if (m.getIdObjeto() == idObjeto) {
                return m;
            }
        }
        return null;
    }

    static Emprestimo buscarEmprestimo(int id) {
        for (Emprestimo e : listaEmprestimos) {
            if (e.getIdEmprestimo() == id) {
                return e;
            }
        }
        return null;
    }

    // Emprestimo em aberto (nao devolvido) de um objeto
    static Emprestimo buscarEmprestimoAberto(int idObjeto) {
        for (Emprestimo e : listaEmprestimos) {
            if (e.getIdObjeto() == idObjeto && e.isEmAberto()) {
                return e;
            }
        }
        return null;
    }

    static String nomeCliente(int id) {
        Cliente c = buscarCliente(id);
        return (c != null) ? c.getNome() : "?";
    }

    static String nomeTipo(int id) {
        TipoObjeto t = buscarTipo(id);
        return (t != null) ? t.getNome() : "?";
    }

    static String nomeObjeto(int id) {
        Objeto o = buscarObjeto(id);
        return (o != null) ? o.getNome() : "?";
    }

    // Verificacoes de uso 
    static boolean clienteEmUso(int idCliente) {
        for (Emprestimo e : listaEmprestimos) {
            if (e.getIdCliente() == idCliente) {
                return true;
            }
        }
        return false;
    }

    static boolean tipoEmUso(int idTipo) {
        for (Objeto o : listaObjetos) {
            if (o.getIdTipo() == idTipo) {
                return true;
            }
        }
        return false;
    }

    static boolean objetoEmUso(int idObjeto) {
        for (Emprestimo e : listaEmprestimos) {
            if (e.getIdObjeto() == idObjeto) {
                return true;
            }
        }
        return buscarManutencaoDoObjeto(idObjeto) != null;
    }

    // Menus

    static void menu() {
        int opcao;
        do {
            System.out.println("\n### SISTEMA DE EMPRESTIMOS DE OBJETOS ###");
            System.out.println("==========================================");
            System.out.println("|  1 - Cadastro de Clientes              |");
            System.out.println("|  2 - Cadastro de Tipos de Objetos      |");
            System.out.println("|  3 - Cadastro de Objetos               |");
            System.out.println("|  4 - Cadastro de Manutencoes           |");
            System.out.println("|  5 - Emprestimos e Devolucoes          |");
            System.out.println("|  6 - Relatorios                        |");
            System.out.println("|  0 - Sair                              |");
            System.out.println("==========================================");

            opcao = lerInt("Opcao -> ");

            switch (opcao) {
                case 1 -> clientes();
                case 2 -> tipos();
                case 3 -> objetos();
                case 4 -> manutencoes();
                case 5 -> emprestimos();
                case 6 -> relatorios();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }

    static void submenu(String titulo) {
        System.out.println("\n--- " + titulo + " ---");
        System.out.println("1 - Incluir");
        System.out.println("2 - Consultar");
        System.out.println("3 - Alterar");
        System.out.println("4 - Excluir");
    }

    // Pede o codigo de um registro; 0 cancela
    static int pedirCodigo(String mensagem) {
        return lerInt(mensagem + " (0 para cancelar) -> ");
    }

    // Pede um criterio de busca (vazio lista tudo) e converte para maiusculo
    static String pedirFiltro() {
        System.out.print("Buscar por nome (ENTER lista todos): ");
        return sc.nextLine().trim().toUpperCase();
    }

    
    // RF001 - Clientes (pessoas)
    
    static void clientes() {
        int opcao;
        do {
            submenu("CADASTRO DE CLIENTES");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opcao -> ");
            switch (opcao) {
                case 1 -> incluirCliente();
                case 2 -> consultarClientes(pedirFiltro());
                case 3 -> alterarCliente();
                case 4 -> excluirCliente();
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

    static int consultarClientes(String filtro) {
        int encontrados = 0;
        for (Cliente c : listaClientes) {
            if (c.getNome().contains(filtro)) {
                if (encontrados == 0) {
                    System.out.println("\nCOD | NOME | E-MAIL | CIDADE");
                }
                System.out.println(c.getIdCliente() + " | " + c.getNome() + " | "
                        + c.getEmail() + " | " + c.getCidade());
                encontrados++;
            }
        }
        if (encontrados == 0) {
            System.out.println("Nenhum cliente encontrado.");
        }
        return encontrados;
    }

    static void alterarCliente() {
        if (consultarClientes("") == 0) {
            return;
        }
        int id = pedirCodigo("Codigo do cliente a alterar");
        if (id == 0) {
            return;
        }
        Cliente c = buscarCliente(id);
        if (c == null) {
            System.out.println("Cliente nao encontrado!");
            return;
        }
        System.out.println("(ENTER mantem o valor atual)");
        c.setNome(lerTextoOpcional("Nome", c.getNome()));
        c.setEmail(lerTextoOpcional("E-mail", c.getEmail()));
        c.setCidade(lerTextoOpcional("Cidade", c.getCidade()));
        System.out.println("Cliente alterado com sucesso!");
    }

    static void excluirCliente() {
        if (consultarClientes("") == 0) {
            return;
        }
        int id = pedirCodigo("Codigo do cliente a excluir");
        if (id == 0) {
            return;
        }
        Cliente c = buscarCliente(id);
        if (c == null) {
            System.out.println("Cliente nao encontrado!");
            return;
        }
        if (clienteEmUso(id)) {
            System.out.println("Erro: o cliente possui emprestimos registrados e nao pode ser excluido.");
            return;
        }
        if (confirmar("Confirma a exclusao de " + c.getNome() + "?")) {
            listaClientes.remove(c);
            System.out.println("Cliente excluido com sucesso!");
        } else {
            System.out.println("Exclusao cancelada.");
        }
    }


    // RF002 - Tipos de objetos

    static void tipos() {
        int opcao;
        do {
            submenu("CADASTRO DE TIPOS DE OBJETOS");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opcao -> ");
            switch (opcao) {
                case 1 -> incluirTipo();
                case 2 -> consultarTipos(pedirFiltro());
                case 3 -> alterarTipo();
                case 4 -> excluirTipo();
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

    static int consultarTipos(String filtro) {
        int encontrados = 0;
        for (TipoObjeto t : listaTipos) {
            if (t.getNome().contains(filtro)) {
                if (encontrados == 0) {
                    System.out.println("\nCOD | NOME");
                }
                System.out.println(t.getIdTipo() + " | " + t.getNome());
                encontrados++;
            }
        }
        if (encontrados == 0) {
            System.out.println("Nenhum tipo de objeto encontrado.");
        }
        return encontrados;
    }

    static void alterarTipo() {
        if (consultarTipos("") == 0) {
            return;
        }
        int id = pedirCodigo("Codigo do tipo a alterar");
        if (id == 0) {
            return;
        }
        TipoObjeto t = buscarTipo(id);
        if (t == null) {
            System.out.println("Tipo nao encontrado!");
            return;
        }
        System.out.println("(ENTER mantem o valor atual)");
        t.setNome(lerTextoOpcional("Nome do tipo", t.getNome()));
        System.out.println("Tipo alterado com sucesso!");
    }

    static void excluirTipo() {
        if (consultarTipos("") == 0) {
            return;
        }
        int id = pedirCodigo("Codigo do tipo a excluir");
        if (id == 0) {
            return;
        }
        TipoObjeto t = buscarTipo(id);
        if (t == null) {
            System.out.println("Tipo nao encontrado!");
            return;
        }
        if (tipoEmUso(id)) {
            System.out.println("Erro: existem objetos cadastrados com este tipo.");
            return;
        }
        if (confirmar("Confirma a exclusao de " + t.getNome() + "?")) {
            listaTipos.remove(t);
            System.out.println("Tipo excluido com sucesso!");
        } else {
            System.out.println("Exclusao cancelada.");
        }
    }

    // RF003 e RF006 - Objetos e situacao do objeto

    static void objetos() {
        int opcao;
        do {
            submenu("CADASTRO DE OBJETOS");
            System.out.println("5 - Dar baixa em objeto (roubo, extravio, descarte)");
            System.out.println("6 - Reativar objeto baixado");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opcao -> ");
            switch (opcao) {
                case 1 -> incluirObjeto();
                case 2 -> consultarObjetos(pedirFiltro());
                case 3 -> alterarObjeto();
                case 4 -> excluirObjeto();
                case 5 -> baixarObjeto();
                case 6 -> reativarObjeto();
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

        consultarTipos("");
        int idTipo = lerInt("Codigo do tipo -> ");
        if (buscarTipo(idTipo) == null) {
            System.out.println("Tipo nao encontrado! Objeto nao incluido.");
            return;
        }

        // Todo objeto novo entra como ATIVO 
        listaObjetos.add(new Objeto(proxIdObjeto, nome, ATIVO, idTipo));
        System.out.println("Objeto incluido com sucesso! Codigo: " + proxIdObjeto);
        proxIdObjeto++;
    }

    static int consultarObjetos(String filtro) {
        int encontrados = 0;
        for (Objeto o : listaObjetos) {
            if (o.getNome().contains(filtro)) {
                if (encontrados == 0) {
                    System.out.println("\nCOD | NOME | SITUACAO | TIPO");
                }
                System.out.println(o.getIdObjeto() + " | " + o.getNome() + " | "
                        + o.getSituacao() + " | " + nomeTipo(o.getIdTipo()));
                encontrados++;
            }
        }
        if (encontrados == 0) {
            System.out.println("Nenhum objeto encontrado.");
        }
        return encontrados;
    }

    static void alterarObjeto() {
        if (consultarObjetos("") == 0) {
            return;
        }
        int id = pedirCodigo("Codigo do objeto a alterar");
        if (id == 0) {
            return;
        }
        Objeto o = buscarObjeto(id);
        if (o == null) {
            System.out.println("Objeto nao encontrado!");
            return;
        }
        System.out.println("(ENTER mantem o valor atual)");
        String nome = lerTextoOpcional("Nome do objeto", o.getNome());
        consultarTipos("");
        int idTipo = lerIntOpcional("Codigo do tipo", o.getIdTipo());
        if (buscarTipo(idTipo) == null) {
            System.out.println("Tipo nao encontrado! Objeto nao alterado.");
            return;
        }
        o.setNome(nome);
        o.setIdTipo(idTipo);
        System.out.println("Objeto alterado com sucesso!");
    }

    static void excluirObjeto() {
        if (consultarObjetos("") == 0) {
            return;
        }
        int id = pedirCodigo("Codigo do objeto a excluir");
        if (id == 0) {
            return;
        }
        Objeto o = buscarObjeto(id);
        if (o == null) {
            System.out.println("Objeto nao encontrado!");
            return;
        }
        if (objetoEmUso(id)) {
            System.out.println("Erro: o objeto possui historico de emprestimos/manutencao. "
                    + "Use a opcao de dar baixa em vez de excluir.");
            return;
        }
        if (confirmar("Confirma a exclusao de " + o.getNome() + "?")) {
            listaObjetos.remove(o);
            System.out.println("Objeto excluido com sucesso!");
        } else {
            System.out.println("Exclusao cancelada.");
        }
    }

    // RF006 - baixa: o objeto nao pode mais ser emprestado
    static void baixarObjeto() {
        if (consultarObjetos("") == 0) {
            return;
        }
        int id = pedirCodigo("Codigo do objeto a dar baixa");
        if (id == 0) {
            return;
        }
        Objeto o = buscarObjeto(id);
        if (o == null) {
            System.out.println("Objeto nao encontrado!");
            return;
        }
        if (o.getSituacao().equals(BAIXADO)) {
            System.out.println("Erro: o objeto ja esta baixado.");
            return;
        }
        if (o.getSituacao().equals(EMPRESTADO)) {
            System.out.println("Erro: o objeto esta emprestado. Registre a devolucao antes de dar baixa.");
            return;
        }
        if (!confirmar("Confirma a baixa de " + o.getNome() + "?")) {
            System.out.println("Baixa cancelada.");
            return;
        }
        // Se estava em manutencao, a manutencao deixa de existir
        Manutencao m = buscarManutencaoDoObjeto(id);
        if (m != null) {
            listaManutencoes.remove(m);
        }
        o.setSituacao(BAIXADO);
        System.out.println("Baixa registrada com sucesso! O objeto nao podera mais ser emprestado.");
    }

    static void reativarObjeto() {
        if (consultarObjetos("") == 0) {
            return;
        }
        int id = pedirCodigo("Codigo do objeto a reativar");
        if (id == 0) {
            return;
        }
        Objeto o = buscarObjeto(id);
        if (o == null) {
            System.out.println("Objeto nao encontrado!");
            return;
        }
        if (!o.getSituacao().equals(BAIXADO)) {
            System.out.println("Erro: apenas objetos baixados podem ser reativados.");
            return;
        }
        o.setSituacao(ATIVO);
        System.out.println("Objeto reativado com sucesso!");
    }

    // RF005 - Manutencoes

    static void manutencoes() {
        int opcao;
        do {
            submenu("CADASTRO DE MANUTENCOES");
            System.out.println("5 - Encerrar manutencao (objeto volta a ficar disponivel)");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opcao -> ");
            switch (opcao) {
                case 1 -> incluirManutencao();
                case 2 -> consultarManutencoes(pedirFiltro());
                case 3 -> alterarManutencao();
                case 4 -> excluirManutencao();
                case 5 -> encerrarManutencao();
                case 0 -> {
                }
                default -> System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }

    static void incluirManutencao() {
        // Somente objetos ATIVOS 
        int disponiveis = 0;
        for (Objeto o : listaObjetos) {
            if (o.getSituacao().equals(ATIVO)) {
                if (disponiveis == 0) {
                    System.out.println("\nObjetos disponiveis para manutencao:");
                    System.out.println("COD | NOME | TIPO");
                }
                System.out.println(o.getIdObjeto() + " | " + o.getNome() + " | " + nomeTipo(o.getIdTipo()));
                disponiveis++;
            }
        }
        if (disponiveis == 0) {
            System.out.println("Nao ha objetos ativos disponiveis para enviar para manutencao.");
            return;
        }
        int idObjeto = pedirCodigo("Codigo do objeto");
        if (idObjeto == 0) {
            return;
        }
        Objeto o = buscarObjeto(idObjeto);
        if (o == null) {
            System.out.println("Objeto nao encontrado! Manutencao nao incluida.");
            return;
        }
        if (!o.getSituacao().equals(ATIVO)) {
            System.out.println("Erro: o objeto esta " + o.getSituacao()
                    + " e nao pode ser enviado para manutencao.");
            return;
        }
        String descricao = lerTexto("Descricao do que deve ser feito: ");

        listaManutencoes.add(new Manutencao(proxIdManutencao, idObjeto, descricao, RECEBIDO));
        o.setSituacao(EM_MANUTENCAO);
        System.out.println("Manutencao incluida com sucesso! Codigo: " + proxIdManutencao
                + " (status " + RECEBIDO + ")");
        proxIdManutencao++;
    }

    static int consultarManutencoes(String filtro) {
        int encontrados = 0;
        for (Manutencao m : listaManutencoes) {
            String nomeObj = nomeObjeto(m.getIdObjeto());
            if (nomeObj.contains(filtro)) {
                if (encontrados == 0) {
                    System.out.println("\nCOD | OBJETO | DESCRICAO | STATUS");
                }
                System.out.println(m.getIdManutencao() + " | " + nomeObj + " | "
                        + m.getDescricao() + " | " + m.getStatus());
                encontrados++;
            }
        }
        if (encontrados == 0) {
            System.out.println("Nenhuma manutencao em andamento.");
        }
        return encontrados;
    }

    static void alterarManutencao() {
        if (consultarManutencoes("") == 0) {
            return;
        }
        int id = pedirCodigo("Codigo da manutencao a alterar");
        if (id == 0) {
            return;
        }
        Manutencao m = buscarManutencao(id);
        if (m == null) {
            System.out.println("Manutencao nao encontrada!");
            return;
        }
        System.out.println("(ENTER mantem o valor atual)");
        m.setDescricao(lerTextoOpcional("Descricao", m.getDescricao()));

        System.out.println("Status: 1 - RECEBIDO | 2 - EM SERVICO | 3 - RESOLVIDO");
        int opStatus = lerIntOpcional("Novo status", 0);
        switch (opStatus) {
            case 1 -> m.setStatus(RECEBIDO);
            case 2 -> m.setStatus(EM_SERVICO);
            case 3 -> {
                finalizarManutencao(m);
                return;
            }
            case 0 -> {
            }
            default -> System.out.println("Status invalido, mantido o anterior.");
        }
        System.out.println("Manutencao alterada com sucesso!");
    }

    // Quando resolvida, a manutencao sai da lista e o objeto volta a ficar disponivel
    static void finalizarManutencao(Manutencao m) {
        Objeto o = buscarObjeto(m.getIdObjeto());
        if (o != null) {
            o.setSituacao(ATIVO);
        }
        m.setStatus(RESOLVIDO);
        listaManutencoes.remove(m);
        System.out.println("Manutencao encerrada! O objeto "
                + (o != null ? o.getNome() : "?") + " voltou a ficar disponivel.");
    }

    static void encerrarManutencao() {
        if (consultarManutencoes("") == 0) {
            return;
        }
        int id = pedirCodigo("Codigo da manutencao a encerrar");
        if (id == 0) {
            return;
        }
        Manutencao m = buscarManutencao(id);
        if (m == null) {
            System.out.println("Manutencao nao encontrada!");
            return;
        }
        finalizarManutencao(m);
    }

    static void excluirManutencao() {
        if (consultarManutencoes("") == 0) {
            return;
        }
        int id = pedirCodigo("Codigo da manutencao a excluir");
        if (id == 0) {
            return;
        }
        Manutencao m = buscarManutencao(id);
        if (m == null) {
            System.out.println("Manutencao nao encontrada!");
            return;
        }
        if (confirmar("Confirma a exclusao? O objeto voltara a ficar disponivel.")) {
            Objeto o = buscarObjeto(m.getIdObjeto());
            if (o != null && o.getSituacao().equals(EM_MANUTENCAO)) {
                o.setSituacao(ATIVO);
            }
            listaManutencoes.remove(m);
            System.out.println("Manutencao excluida com sucesso!");
        } else {
            System.out.println("Exclusao cancelada.");
        }
    }

    // RF004 - Emprestimos e devolucoes
    
    static void emprestimos() {
        int opcao;
        do {
            System.out.println("\n--- EMPRESTIMOS E DEVOLUCOES ---");
            System.out.println("1 - Realizar emprestimo");
            System.out.println("2 - Consultar emprestimos");
            System.out.println("3 - Alterar emprestimo");
            System.out.println("4 - Excluir emprestimo");
            System.out.println("5 - Registrar devolucao");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opcao -> ");
            switch (opcao) {
                case 1 -> incluirEmprestimo();
                case 2 -> consultarEmprestimos(pedirFiltro());
                case 3 -> alterarEmprestimo();
                case 4 -> excluirEmprestimo();
                case 5 -> devolverObjeto();
                case 0 -> {
                }
                default -> System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }

    static void incluirEmprestimo() {
        if (listaClientes.isEmpty()) {
            System.out.println("Cadastre ao menos um cliente antes de realizar emprestimos.");
            return;
        }
        // Lista apenas os objetos que podem ser emprestados
        int disponiveis = 0;
        for (Objeto o : listaObjetos) {
            if (o.getSituacao().equals(ATIVO)) {
                if (disponiveis == 0) {
                    System.out.println("\nObjetos disponiveis para emprestimo:");
                    System.out.println("COD | NOME | TIPO");
                }
                System.out.println(o.getIdObjeto() + " | " + o.getNome() + " | " + nomeTipo(o.getIdTipo()));
                disponiveis++;
            }
        }
        if (disponiveis == 0) {
            System.out.println("Nao ha objetos disponiveis para emprestimo.");
            return;
        }
        int idObjeto = pedirCodigo("Codigo do objeto");
        if (idObjeto == 0) {
            return;
        }
        Objeto o = buscarObjeto(idObjeto);
        if (o == null) {
            System.out.println("Objeto nao encontrado! Emprestimo nao realizado.");
            return;
        }
        // Restricao: impedir emprestimo de objetos em manutencao, ja emprestados ou baixados
        if (!o.getSituacao().equals(ATIVO)) {
            System.out.println("Erro: o objeto " + o.getNome() + " esta " + o.getSituacao()
                    + " e nao pode ser emprestado.");
            return;
        }

        consultarClientes("");
        int idCliente = lerInt("Codigo do cliente -> ");
        if (buscarCliente(idCliente) == null) {
            System.out.println("Cliente nao encontrado! Emprestimo nao realizado.");
            return;
        }
        LocalDate data = lerData("Data do emprestimo", LocalDate.now());

        listaEmprestimos.add(new Emprestimo(proxIdEmprestimo, idCliente, idObjeto, data));
        o.setSituacao(EMPRESTADO);
        System.out.println("Emprestimo realizado com sucesso! Codigo: " + proxIdEmprestimo);
        proxIdEmprestimo++;
    }

    // Filtro: nome do cliente ou do objeto
    static int consultarEmprestimos(String filtro) {
        int encontrados = 0;
        for (Emprestimo e : listaEmprestimos) {
            String cliente = nomeCliente(e.getIdCliente());
            String objeto = nomeObjeto(e.getIdObjeto());
            if (cliente.contains(filtro) || objeto.contains(filtro)) {
                if (encontrados == 0) {
                    System.out.println("\nCOD | CLIENTE | OBJETO | EMPRESTIMO | DEVOLUCAO | SITUACAO");
                }
                System.out.println(e.getIdEmprestimo() + " | " + cliente + " | " + objeto + " | "
                        + formatar(e.getDataEmprestimo()) + " | " + formatar(e.getDataDevolucao())
                        + " | " + e.getStatus());
                encontrados++;
            }
        }
        if (encontrados == 0) {
            System.out.println("Nenhum emprestimo encontrado.");
        }
        return encontrados;
    }

    static void alterarEmprestimo() {
        if (consultarEmprestimos("") == 0) {
            return;
        }
        int id = pedirCodigo("Codigo do emprestimo a alterar");
        if (id == 0) {
            return;
        }
        Emprestimo e = buscarEmprestimo(id);
        if (e == null) {
            System.out.println("Emprestimo nao encontrado!");
            return;
        }
        System.out.println("(ENTER mantem o valor atual. Para trocar o objeto, exclua e refaca o emprestimo)");
        consultarClientes("");
        int idCliente = lerIntOpcional("Codigo do cliente", e.getIdCliente());
        if (buscarCliente(idCliente) == null) {
            System.out.println("Cliente nao encontrado! Emprestimo nao alterado.");
            return;
        }
        LocalDate dataEmp = lerData("Data do emprestimo", e.getDataEmprestimo());
        LocalDate dataDev = e.getDataDevolucao();
        if (dataDev != null) {
            dataDev = lerData("Data da devolucao", dataDev);
            if (dataDev.isBefore(dataEmp)) {
                System.out.println("Erro: a devolucao nao pode ser anterior ao emprestimo. Nada foi alterado.");
                return;
            }
        }
        e.setIdCliente(idCliente);
        e.setDataEmprestimo(dataEmp);
        e.setDataDevolucao(dataDev);
        System.out.println("Emprestimo alterado com sucesso!");
    }

    static void excluirEmprestimo() {
        if (consultarEmprestimos("") == 0) {
            return;
        }
        int id = pedirCodigo("Codigo do emprestimo a excluir");
        if (id == 0) {
            return;
        }
        Emprestimo e = buscarEmprestimo(id);
        if (e == null) {
            System.out.println("Emprestimo nao encontrado!");
            return;
        }
        if (confirmar("Confirma a exclusao do emprestimo " + id + "?")) {
            // Se ainda estava em aberto, o objeto volta a ficar disponivel
            if (e.isEmAberto()) {
                Objeto o = buscarObjeto(e.getIdObjeto());
                if (o != null && o.getSituacao().equals(EMPRESTADO)) {
                    o.setSituacao(ATIVO);
                }
            }
            listaEmprestimos.remove(e);
            System.out.println("Emprestimo excluido com sucesso!");
        } else {
            System.out.println("Exclusao cancelada.");
        }
    }

    static void devolverObjeto() {
        int abertos = 0;
        for (Emprestimo e : listaEmprestimos) {
            if (e.isEmAberto()) {
                if (abertos == 0) {
                    System.out.println("\nEmprestimos em aberto:");
                    System.out.println("COD | CLIENTE | OBJETO | EMPRESTIMO");
                }
                System.out.println(e.getIdEmprestimo() + " | " + nomeCliente(e.getIdCliente()) + " | "
                        + nomeObjeto(e.getIdObjeto()) + " | " + formatar(e.getDataEmprestimo()));
                abertos++;
            }
        }
        if (abertos == 0) {
            System.out.println("Nao ha emprestimos em aberto.");
            return;
        }
        int id = pedirCodigo("Codigo do emprestimo a devolver");
        if (id == 0) {
            return;
        }
        Emprestimo e = buscarEmprestimo(id);
        if (e == null) {
            System.out.println("Emprestimo nao encontrado!");
            return;
        }
        if (!e.isEmAberto()) {
            System.out.println("Erro: este emprestimo ja foi devolvido.");
            return;
        }
        LocalDate data = lerData("Data da devolucao", LocalDate.now());
        if (data.isBefore(e.getDataEmprestimo())) {
            System.out.println("Erro: a devolucao nao pode ser anterior a data do emprestimo ("
                    + formatar(e.getDataEmprestimo()) + ").");
            return;
        }
        e.setDataDevolucao(data);
        Objeto o = buscarObjeto(e.getIdObjeto());
        if (o != null) {
            o.setSituacao(ATIVO);
        }
        System.out.println("Devolucao registrada com sucesso!");
    }

    // =====================================================================
    // Relatorios
    // =====================================================================
    static void relatorios() {
        int opcao;
        do {
            System.out.println("\n--- RELATORIOS ---");
            System.out.println("1 - Emprestimos por periodo");
            System.out.println("2 - Objetos por situacao");
            System.out.println("0 - Voltar");
            opcao = lerInt("Opcao -> ");
            switch (opcao) {
                case 1 -> relatorioEmprestimosPeriodo();
                case 2 -> relatorioObjetosPorSituacao();
                case 0 -> {
                }
                default -> System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
    }

    // RF007 - emprestimos em um periodo de datas
    static void relatorioEmprestimosPeriodo() {
        LocalDate inicio = lerData("Data inicial", null);
        LocalDate fim = lerData("Data final", null);
        if (fim.isBefore(inicio)) {
            System.out.println("Erro: a data final nao pode ser anterior a data inicial.");
            return;
        }

        ArrayList<Emprestimo> resultado = new ArrayList<>();
        for (Emprestimo e : listaEmprestimos) {
            LocalDate d = e.getDataEmprestimo();
            if (!d.isBefore(inicio) && !d.isAfter(fim)) {
                resultado.add(e);
            }
        }
        resultado.sort(Comparator.comparing(Emprestimo::getDataEmprestimo)
                .thenComparingInt(Emprestimo::getIdEmprestimo));

        System.out.println("\n===== EMPRESTIMOS DE " + formatar(inicio) + " A " + formatar(fim) + " =====");
        if (resultado.isEmpty()) {
            System.out.println("Nenhum emprestimo no periodo informado.");
            return;
        }
        System.out.println("COD | DATA | PESSOA | OBJETO | TIPO | SITUACAO");
        int emprestados = 0;
        for (Emprestimo e : resultado) {
            Objeto o = buscarObjeto(e.getIdObjeto());
            String tipo = (o != null) ? nomeTipo(o.getIdTipo()) : "?";
            String situacao = e.getStatus();
            if (!e.isEmAberto()) {
                situacao += " EM " + formatar(e.getDataDevolucao());
            } else {
                emprestados++;
            }
            System.out.println(e.getIdEmprestimo() + " | " + formatar(e.getDataEmprestimo()) + " | "
                    + nomeCliente(e.getIdCliente()) + " | " + nomeObjeto(e.getIdObjeto()) + " | "
                    + tipo + " | " + situacao);
        }
        System.out.println("Total: " + resultado.size() + " emprestimo(s), "
                + emprestados + " ainda em aberto.");
    }

    // RF008 - objetos por situacao e tipo, em ordem alfabetica, em dois blocos
    static void relatorioObjetosPorSituacao() {
        System.out.println("\nSituacao: 1 - Todas | 2 - Emprestado | 3 - Disponivel (ativo) "
                + "| 4 - Baixado | 5 - Em manutencao");
        int op = lerInt("Opcao -> ");
        String situacao;
        switch (op) {
            case 1 -> situacao = null;
            case 2 -> situacao = EMPRESTADO;
            case 3 -> situacao = ATIVO;
            case 4 -> situacao = BAIXADO;
            case 5 -> situacao = EM_MANUTENCAO;
            default -> {
                System.out.println("Opcao invalida!");
                return;
            }
        }

        int idTipo = 0;
        if (!listaTipos.isEmpty()) {
            consultarTipos("");
            idTipo = lerInt("Codigo do tipo para filtrar (0 para todos) -> ");
            if (idTipo != 0 && buscarTipo(idTipo) == null) {
                System.out.println("Tipo nao encontrado!");
                return;
            }
        }

        // Filtra e ordena de A a Z pelo nome
        ArrayList<Objeto> emprestados = new ArrayList<>();
        ArrayList<Objeto> naoEmprestados = new ArrayList<>();
        for (Objeto o : listaObjetos) {
            if (situacao != null && !o.getSituacao().equals(situacao)) {
                continue;
            }
            if (idTipo != 0 && o.getIdTipo() != idTipo) {
                continue;
            }
            if (o.getSituacao().equals(EMPRESTADO)) {
                emprestados.add(o);
            } else {
                naoEmprestados.add(o);
            }
        }
        emprestados.sort(Comparator.comparing(Objeto::getNome));
        naoEmprestados.sort(Comparator.comparing(Objeto::getNome));

        System.out.println("\n===== 1) ITENS EMPRESTADOS =====");
        if (emprestados.isEmpty()) {
            System.out.println("Nenhum item.");
        } else {
            System.out.println("COD | NOME | TIPO | SITUACAO | PESSOA");
            for (Objeto o : emprestados) {
                Emprestimo e = buscarEmprestimoAberto(o.getIdObjeto());
                String pessoa = (e != null) ? nomeCliente(e.getIdCliente()) : "?";
                System.out.println(o.getIdObjeto() + " | " + o.getNome() + " | " + nomeTipo(o.getIdTipo())
                        + " | " + o.getSituacao() + " | " + pessoa);
            }
        }

        System.out.println("\n===== 2) ITENS NAO EMPRESTADOS =====");
        if (naoEmprestados.isEmpty()) {
            System.out.println("Nenhum item.");
        } else {
            System.out.println("COD | NOME | TIPO | SITUACAO");
            for (Objeto o : naoEmprestados) {
                System.out.println(o.getIdObjeto() + " | " + o.getNome() + " | "
                        + nomeTipo(o.getIdTipo()) + " | " + o.getSituacao());
            }
        }
    }
}
