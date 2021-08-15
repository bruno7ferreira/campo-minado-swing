package br.com.bruno.campo_minado.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador {

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
        } catch (Exception e) {
            // FIXME Ajustar a implementação do método abrir
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
            System.out.println("Perdeu...");
        } else if (objetivoAlcancado()) {
            System.out.println("Você ganhou!");
        }
    }
}
