package br.com.bruno.campo_minado.visao;

import br.com.bruno.campo_minado.modelo.Campo;
import br.com.bruno.campo_minado.modelo.CampoEvento;
import br.com.bruno.campo_minado.modelo.CampoObservador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


@SuppressWarnings("all")
public class BotaoCampo extends JButton implements CampoObservador, MouseListener {

    //atributos
    private final Color BG_PADRAO = new Color(184, 184, 184);
    private final Color BG_MARCAR = new Color(8, 179, 247);
    private final Color BG_EXPLODIR = new Color(189, 66, 68);
    private final Color TEXTO_VERDE = new Color(0, 100, 0);

    private Campo campo;

    //Construtor
    public BotaoCampo(Campo campo) {

        this.campo = campo;
        setBackground(BG_PADRAO); // definindo a cor do plano de fundo
        setOpaque(true);
        setBorder(BorderFactory.createBevelBorder(0)); // definido a cor da borda do botão

        addMouseListener(this); // evento do mouse registrado
        campo.registrarObservador(this);
    }

    //métodos
    @Override
    public void eventoOcorreu(Campo campo, CampoEvento evento) {
        switch (evento) {
            case ABRIR:
                aplicarEstiloAbrir();
                break;
            case MARCAR:
                aplicarEstiloMarcar();
                break;
            case EXPLODIR:
                aplicarEstiloExplodir();
                break;
            default:
                aplicarEstiloPadrao();
        }
    }

    private void aplicarEstiloPadrao() {
        setBackground(BG_PADRAO);
        setText("");
    }

    private void aplicarEstiloExplodir() {
        setBackground(BG_EXPLODIR);
        setForeground(Color.WHITE);
        setText("X");
    }

    private void aplicarEstiloMarcar() {
        setBackground(BG_MARCAR);
        setForeground(Color.BLACK);
        setText("<|");

    }

    private void aplicarEstiloAbrir() {

        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        if (campo.isMinado()) {
            setBackground(BG_EXPLODIR);
            return;
        }

        setBackground(BG_PADRAO);

        switch (campo.minasNaVizinhanca()) {
            case 1:
                setForeground(TEXTO_VERDE);
                break;
            case 2:
                setForeground(Color.BLUE);
                break;
            case 3:
                setForeground(Color.YELLOW);
                break;
            case 4:
            case 5:
            case 6:
                setForeground(Color.RED);
                break;
            default:
                setForeground(Color.PINK);
        }
        String valor = !campo.vizinhancaSegura() ? campo.minasNaVizinhanca() + "" : "";
        setText(valor);
    }

    //Interface MouseListener = eventos do mouse
    @Override
    public void mousePressed(MouseEvent e) { //mouse pressionado
        if (e.getButton() == 1) {
            campo.abrir();
        } else {
            campo.alterarMarcacao();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    } // mouse clicado

    @Override
    public void mouseReleased(MouseEvent e) {
    } // mouse lançado

    @Override
    public void mouseEntered(MouseEvent e) {
    } // inscrito

    @Override
    public void mouseExited(MouseEvent e) {
    } // mouse sair


}
