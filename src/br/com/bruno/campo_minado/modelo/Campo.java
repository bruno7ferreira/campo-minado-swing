package br.com.bruno.campo_minado.modelo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class Campo {

    //atributos
    private final int linha;
    private final int coluna;

    private boolean minado;
    private boolean aberto;
    private boolean marcado;

    //campos vizinhos
    private List<Campo> vizinhos = new ArrayList<>();

    private Set<CampoObservador> observadores = new LinkedHashSet<>(); //Usando o set para evitar duplicação
    private List<BiConsumer<Campo, CampoEvento>> observadores2 = new ArrayList<>();
    // BiConsumer = não retorna nada e tem dois tipos como parametro


    //construtor
    Campo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    //métodos

    public void registrarObservador(CampoObservador observador) {
        observadores.add(observador);
    }

    private void notificarObservadores(CampoEvento evento) {
        observadores.stream()
                .forEach(observador -> observador.eventoOcorreu(this, evento));
    }

    boolean adicionarVizinho(Campo vizinho) {
        boolean linhaDiferente = this.linha != vizinho.linha;
        boolean colunaDiferente = this.coluna != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente;

        int deltaLinha = Math.abs(this.linha - vizinho.linha); // retorna o número absoluto
        int deltaColuna = Math.abs(this.coluna - vizinho.coluna); // retorna o número absoluto
        int deltaGeral = deltaColuna + deltaLinha;

        if (deltaColuna == 1 && !diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else if (deltaGeral == 2 & diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else {
            return false;
        }
    }

    public void alterarMarcacao() {
        if (!aberto) {
            this.marcado = !marcado;

            if (marcado) {
                notificarObservadores(CampoEvento.MARCAR);
            } else {
                notificarObservadores(CampoEvento.DESMARCAR);
            }
        }
    }

    public boolean abrir() {
        if (!aberto && !marcado) { // abrindo um campo do jogo

            if (minado) { //abrir um campo minado, fim de jogo
                notificarObservadores(CampoEvento.EXPLODIR);
                return true;
            }

            setAberto(true);


            if (vizinhancaSegura()) {
                vizinhos.forEach(v -> abrir()); // abre o campo, caso a vizinhaça esteja segura
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean vizinhancaSegura() {
        return vizinhos.stream()
                .noneMatch(v -> v.minado); // verifica se algum vizinho está minado
    }

    void minar() {
        if (!minado) {
            this.minado = true; //deixa o campo minado
        }
    }

    boolean objetivoAlcancado() {
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }

    public int minasNaVizinhanca() {//retorna a quantidade de vizinhos minadas
        return (int) vizinhos.stream()
                .filter(v -> v.minado)
                .count();
    }

    void reiniciar() { // Reinicia o jogo
        aberto = false;
        minado = false;
        marcado = false;
    }


    //get and set
    void setAberto(boolean aberto) {
        this.aberto = aberto;

        if (aberto) {
            notificarObservadores(CampoEvento.ABRIR);
        }


    }

    public boolean isMarcado() {
        return marcado;
    }

    public boolean isAberto() {
        return aberto;
    }

    public boolean isMinado() {
        return minado;
    }

    public boolean isFechado() {
        return !isMinado();
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }
}
