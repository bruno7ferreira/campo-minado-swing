package br.com.bruno.campo_minado.modelo;

//@author Bruno Rogério
@FunctionalInterface
public interface CampoObservador {

    void eventoOcorreu(Campo c, CampoEvento e);

}
