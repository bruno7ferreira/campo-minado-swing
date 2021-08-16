package br.com.bruno.campo_minado.visao;

import br.com.bruno.campo_minado.modelo.Tabuleiro;

import javax.swing.*;

@SuppressWarnings("ALL")
public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        Tabuleiro tabuleiro = new Tabuleiro(16, 30, 50);
        add(new PainelTabuleiro(tabuleiro));//instanciando um painel tabuleiro e adicionando o mesmo

        setTitle("Campo Minado");//titulo do jogo
        setSize(690, 438); // tamanho da tela do jogo
        setLocationRelativeTo(null); // tela centralizada ao monitor
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);// para finalizar o programa quando a janela for fechada
        setVisible(true);
    }

    public static void main(String[] args) {

        new TelaPrincipal();

    }
}
