package br.com.bruno.campo_minado.visao;

import br.com.bruno.campo_minado.modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("all")
public class PainelTabuleiro extends JPanel {

    public PainelTabuleiro(Tabuleiro tabuleiro) {

        setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));

        int total = tabuleiro.getLinhas() * tabuleiro.getColunas();
        for (int i = 0; i < total; i++) {
            add(new BotaoCampo(null));
        }

    }
}
