package br.com.bruno.campo_minado.visao;

import br.com.bruno.campo_minado.modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;


@SuppressWarnings("SpellCheckingInspection")
public class PainelTabuleiro extends JPanel {

    public PainelTabuleiro(Tabuleiro tabuleiro) {

        setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));

        tabuleiro.paraCadaCampo(campo -> add(new BotaoCampo(campo)));

        tabuleiro.registrarObservador(e -> {
            if (e.isGanhou()) {
                JOptionPane.showMessageDialog(this, "Você ganhou! :D");
            } else {
                JOptionPane.showMessageDialog(this, "Você perdeu! :(");
            }


        });


    }
}
