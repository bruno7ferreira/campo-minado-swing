package br.com.bruno.campo_minado.visao;

import br.com.bruno.campo_minado.modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;

//@author Bruno Rogério
@SuppressWarnings("all")
public class PainelTabuleiro extends JPanel {

    public PainelTabuleiro(Tabuleiro tabuleiro) {

        setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));

        tabuleiro.paraCadaCampo(c -> add(new BotaoCampo(c)));

        tabuleiro.registrarObservador(e -> {
            SwingUtilities.invokeLater(() -> {
                if (e.isGanhou()) {
                    JOptionPane.showMessageDialog(this, "Você ganhou! :D");
                } else {
                    JOptionPane.showMessageDialog(this, "Você perdeu! :(");
                }
                tabuleiro.reiniciar();
            });
        });
    }
}
