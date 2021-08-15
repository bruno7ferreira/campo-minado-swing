package br.com.bruno.campo_minado.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador {

    //atributos 
    private final int linhas;
    private final int colunas;
    private final int minas;

    private final List<Campo> campos = new ArrayList<>();
    private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();
    //consumer = recebe um tipo e não retorna nada


    //Construtor
    public Tabuleiro(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;

        gerarCampo();
        associarVizinhos();
        sortearMinas();
    }

    //métodos
    public void registrarObservador(Consumer<ResultadoEvento> observador) {
        observadores.add(observador);
    }

    private void notificarObservadores(boolean resultado) {
        observadores.stream()
                .forEach(o -> o.accept(new ResultadoEvento(resultado)));
    }

    public void abrir(int linha, int coluna) {
        campos.parallelStream()
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                .findFirst()
                .ifPresent(c -> c.abrir());
    }

    public void alterarMarcacao(int linha, int coluna) {
        campos.parallelStream()
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                .findFirst()
                .ifPresent(c -> c.alterarMarcacao());
    }

    private void gerarCampo() {
        for (int contadorLinha = 0; contadorLinha < linhas; contadorLinha++) {
            for (int contadorColuna = 0; contadorColuna < colunas; contadorColuna++) {
                Campo campo = new Campo(linhas, colunas);
                campo.registrarObservador(this);
                campos.add(campo);
            }
        }
    }

    private void associarVizinhos() {
        for (Campo c1 : campos) {
            for (Campo c2 : campos) {
                c1.adicionarVizinho(c2);
            }
        }
    }

    private void sortearMinas() {
        long minasArmadas = 0;
        Predicate<Campo> minado = c -> c.isMinado(); //criando a lambda para retornar quais campos estão minados
        do {
            int aleatorio = (int) (Math.random() * campos.size()); //criando um número aleatorio
            campos.get(aleatorio).minar();//retornando a quantidade de campos e minando eles
            minasArmadas = campos.stream()
                    .filter(minado)//filtrando quantos campos estão minados
                    .count();
        } while (minasArmadas < minas);
    }

    public boolean objetivoAlcancado() {
        return campos.stream()
                .allMatch(c -> c.objetivoAlcancado());
    }

    public void reiniciar() {
        campos.stream()
                .forEach(c -> c.reiniciar());
        sortearMinas();
    }

    @Override
    public void eventoOcorreu(Campo c, CampoEvento e) {
        if (e == CampoEvento.EXPLODIR) {
            mostrarMinas();
            notificarObservadores(false);
        } else if (objetivoAlcancado()) {
            notificarObservadores(true);
        }
    }

    private void mostrarMinas() {
        campos.stream()
                .filter(c -> c.isMinado()) //filtrando os campos minados
                .forEach(c -> c.setAberto(true));
    }

    //get and set
    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

}
