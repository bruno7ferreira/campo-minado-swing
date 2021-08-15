package br.com.bruno.campo_minado.modelo;

@FunctionalInterface
public interface CampoObservador {

    public void eventoOcorreu(Campo c, CampoEvento e);

}
