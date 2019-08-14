package ec.gob.dinardap.remanente.controller;

import ec.gob.dinardap.remanente.modelo.CatalogoTransaccion;
import ec.gob.dinardap.remanente.servicio.CatalogoTransaccionServicio;
import ec.gob.dinardap.seguridad.servicio.InstitucionServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "pruebaCtrl")
@ViewScoped
public class PruebaCtrl extends BaseCtrl implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3066578283525294119L;

    @EJB
    private CatalogoTransaccionServicio catalogoTransaccionServicio;
    @EJB
    private InstitucionServicio institucionServicio;

//    private PreguntaDto preguntaDto;
    private String respuestaCorrecta;
    private String fundamentoCorrecto;
    private boolean seleccionado;

    private String titulo;

    @PostConstruct
    protected void init() {
        titulo = "Hola Prueba";
        List<CatalogoTransaccion> list = new ArrayList<>();
        list = catalogoTransaccionServicio.getCatalogoTransaccionList();
        for (CatalogoTransaccion item : list) {
            System.out.println(item.getNombre());
        }
        /*List<Institucion> institucions = new ArrayList<Institucion>();
        institucions = institucionServicio.findAll();
        for (Institucion i : institucions) {
            System.out.println("Institucion: " + i.toString());
            System.out.println("Institucion: "+i.getNombre());
        }
        for (CatalogoTransaccion ct : list) {
            System.out.println("Catalogo Transaccion: " + ct.toString());
        }*/

    }

//    public void inicializarValores() {
//        setSeleccionado(false);
//        setRespuestaCorrecta("");
//        setFundamentoCorrecto("");
//        preguntaDto = new PreguntaDto();
//
//        inicializarRespuesta(true);
//
//    }
//
//    public void respuestaSeleccionada() {
//        System.out.println("Verificar respuesta correcta");
//        System.out.println(getRespuestaCorrecta());
//        this.seleccionado = getRespuestaCorrecta().length() == 1 ? true : false;
//        inicializarRespuesta(false);
//        switch (getRespuestaCorrecta()) {
//            case "a":
//                preguntaDto.getRespuestaA().setEsCorrecta(true);
//                break;
//            case "b":
//                preguntaDto.getRespuestaB().setEsCorrecta(true);
//                break;
//            case "c":
//                preguntaDto.getRespuestaC().setEsCorrecta(true);
//                break;
//            case "d":
//                preguntaDto.getRespuestaD().setEsCorrecta(true);
//                break;
//            default:
//                inicializarRespuesta(true);
//        }
//    }
//
//    public void guardarPregunta() {
//        System.out.println("guarda objeto");
//        if (!getRespuestaCorrecta().equals("Seleccionar...")) {
//            switch (getRespuestaCorrecta()) {
//                case "a":
//                    getPreguntaDto().getRespuestaA().setFundamento(getFundamentoCorrecto());
//                    break;
//                case "b":
//                    getPreguntaDto().getRespuestaB().setFundamento(getFundamentoCorrecto());
//                    break;
//                case "c":
//                    getPreguntaDto().getRespuestaC().setFundamento(getFundamentoCorrecto());
//                    break;
//                case "d":
//                    getPreguntaDto().getRespuestaD().setFundamento(getFundamentoCorrecto());
//                    break;
//            }
//            getPreguntaDto().getPregunta().setRegistradoPor(getLoggedUser());
//            preguntaServicio.guardarPregunta(getPreguntaDto());
//
//            System.out.println(getPreguntaDto().getPregunta().getDescripcion());
//            System.out.println(getRespuestaCorrecta());
//            System.out.println(getFundamentoCorrecto());
//            System.out.println(getPreguntaDto().getRespuestaA().getContenido());
//            System.out.println(getPreguntaDto().getRespuestaB().getContenido());
//            System.out.println(getPreguntaDto().getRespuestaC().getContenido());
//            System.out.println(getPreguntaDto().getRespuestaD().getContenido());
//
//            inicializarValores();
//            addInfoMessage(getBundleMensaje("pregunta.guardada", null), null);
//        } else {
//            addErrorMessage(null, getBundleMensaje("error.validacion", null), null);
//        }
//    }
//
//    private void inicializarRespuesta(boolean valor) {
//        preguntaDto.getRespuestaA().setEsCorrecta(valor);
//        preguntaDto.getRespuestaB().setEsCorrecta(valor);
//        preguntaDto.getRespuestaC().setEsCorrecta(valor);
//        preguntaDto.getRespuestaD().setEsCorrecta(valor);
//
//        preguntaDto.getRespuestaA().setFundamento("");
//        preguntaDto.getRespuestaB().setFundamento("");
//        preguntaDto.getRespuestaC().setFundamento("");
//        preguntaDto.getRespuestaD().setFundamento("");
//    }
//
//    public PreguntaDto getPreguntaDto() {
//        return preguntaDto;
//    }
//
//    public void setPreguntaDto(PreguntaDto preguntaDto) {
//        this.preguntaDto = preguntaDto;
//    }
//
//    public String getRespuestaCorrecta() {
//        return respuestaCorrecta;
//    }
//
//    public void setRespuestaCorrecta(String respuestaCorrecta) {
//        this.respuestaCorrecta = respuestaCorrecta;
//    }
//
//    public String getFundamentoCorrecto() {
//        return fundamentoCorrecto;
//    }
//
//    public void setFundamentoCorrecto(String fundamentoCorrecto) {
//        this.fundamentoCorrecto = fundamentoCorrecto;
//    }
//
//    public boolean isSeleccionado() {
//        return seleccionado;
//    }
//
//    public void setSeleccionado(boolean seleccionado) {
//        this.seleccionado = seleccionado;
//    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
