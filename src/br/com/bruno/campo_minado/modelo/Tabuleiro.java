package br.com.bruno.campo_minado.modelo;

import br.com.bruno.campo_minado.excecao.ExplosaoException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Tabuleiro {

    //atributos 
    private int linhas;
    private int colunas;
    private int minas;

    private final List<Campo> campos = new ArrayList<>();

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
    public void abrir(int linha, int coluna) {
        try {
            campos.parallelStream()
                    .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                    .findFirst()
                    .ifPresent(c -> c.abrir());
        } catch (ExplosaoException e) {
            campos.forEach(c -> c.setAberto(true));
            throw e;

        }

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
                campos.add(new Campo(contadorLinha, contadorColuna));
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

    //ToString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int cont = 0; cont < colunas; cont++) {
            sb.append(" ");

            sb.append(cont);
            sb.append(" ");
        }
        sb.append("\n");

        int i = 0;
        for (int l = 0; l < linhas; l++) {
            sb.append(l);
            sb.append(" ");
            for (int c = 0; c < colunas; c++) {
                sb.append(" ");
                sb.append(campos.get(i));
                sb.append(" ");
                i++;
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
