package ec.gob.dinardap.remanente.servicio.impl;

import ec.gob.dinardap.persistence.constante.CriteriaTypeEnum;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.dinardap.persistence.dao.GenericDao;
import ec.gob.dinardap.persistence.servicio.impl.GenericServiceImpl;
import ec.gob.dinardap.persistence.util.Criteria;
import ec.gob.dinardap.remanente.dao.DiasNoLaborablesDao;
import ec.gob.dinardap.remanente.modelo.DiasNoLaborables;
import ec.gob.dinardap.remanente.servicio.DiasNoLaborablesServicio;
import ec.gob.dinardap.remanente.servicio.ProrrogaRemanenteMensualServicio;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import org.apache.commons.lang3.SerializationUtils;

@Stateless(name = "DiasNoLaborablesServicio")
public class DiasNoLaborablesServicioImpl extends GenericServiceImpl<DiasNoLaborables, Integer> implements DiasNoLaborablesServicio {

    @EJB
    private DiasNoLaborablesDao diasNoLaborablesDao;

    @EJB
    private ProrrogaRemanenteMensualServicio prorrogaRemanenteMensualServicio;

    @Override
    public GenericDao<DiasNoLaborables, Integer> getDao() {
        return diasNoLaborablesDao;
    }

    private List<DiasNoLaborables> diasFestivosMes(Integer mes, Integer año) {
        List<DiasNoLaborables> fechaList = new ArrayList<DiasNoLaborables>();
        String[] criteriaNombres = {"estado", "mes", "anio"};
        CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS, CriteriaTypeEnum.INTEGER_EQUALS};
        Object[] criteriaValores = {"A", mes, año};
        String[] orderBy = {"dia"};
        boolean[] asc = {true};
        Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
        fechaList = findByCriterias(criteria);
        return fechaList;
    }

    @Override
    public Boolean habilitarDiasAdicionales(Integer año, Integer mes) {
        //Declaración        
        Calendar fechaActual = Calendar.getInstance();
        Integer añoActual = fechaActual.get(Calendar.YEAR);
        Integer mesActual = fechaActual.get(Calendar.MONTH);
        Integer diaActual = fechaActual.get(Calendar.DAY_OF_MONTH);

        Calendar fechaSeleccionada = Calendar.getInstance();
        fechaSeleccionada.set(Calendar.YEAR, año);
        fechaSeleccionada.set(Calendar.MONTH, mes - 1);
        Integer añoSeleccionado = fechaSeleccionada.get(Calendar.YEAR);
        Integer mesSeleccionado = fechaSeleccionada.get(Calendar.MONTH);
        Integer diaSeleccionado = fechaSeleccionada.get(Calendar.DAY_OF_MONTH);

        List<DiasNoLaborables> feriados = new ArrayList<DiasNoLaborables>();
        feriados = diasFestivosMes(mesActual + 1, añoActual);

        Integer diasAdicionales = 2; // Obtener desde bdd     

        Integer contadorDias = 0;
        Boolean habilitar = Boolean.FALSE;

        Calendar diaAux = Calendar.getInstance();

        if (añoActual.equals(añoSeleccionado)
                && mesActual.equals(mesSeleccionado)) {
            habilitar = Boolean.TRUE;
        } else {
            if ((mesActual - 1 == mesSeleccionado && añoActual.equals(añoSeleccionado))
                    || ((mesActual == 0 && mesSeleccionado == 11) && (añoActual - 1 == añoSeleccionado))) {
                for (int i = 1; i <= diaActual; i++) {
                    Boolean flagDiaFeriado = Boolean.FALSE;
                    diaAux.set(Calendar.DAY_OF_MONTH, i);
                    String dayOfWeek = diaAux.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US).toUpperCase();
                    if (!dayOfWeek.equals("SUNDAY") && !dayOfWeek.equals("SATURDAY")) {
                        for (DiasNoLaborables dnl : feriados) {
                            if (dnl.getDia() == i) {
                                flagDiaFeriado = Boolean.TRUE;
                                break;
                            }
                        }
                        if (!flagDiaFeriado) {
                            contadorDias++;
                        }
                    } else {
                        if (contadorDias == diasAdicionales) {
                            contadorDias++;
                        }
                    }
                    if (contadorDias <= diasAdicionales) {
                        habilitar = Boolean.TRUE;
                    } else {
                        habilitar = Boolean.FALSE;
                        break;
                    }
                }
            }
        }
        //Cambio para desarrollo y pruebas
        Boolean flagCambios = Boolean.TRUE; //False para pro True para desarrollo y pruebas
        if (flagCambios) {
            return Boolean.TRUE;
        } else {
            return habilitar;
        }
    }

    @Override
    public Boolean habilitarDiasAdicionales(Integer año, Integer mes, Integer remanenteMensualId) {
        //Declaración        
        Calendar fechaActual = Calendar.getInstance();
        Integer añoActual = fechaActual.get(Calendar.YEAR);
        Integer mesActual = fechaActual.get(Calendar.MONTH);
        Integer diaActual = fechaActual.get(Calendar.DAY_OF_MONTH);

        Calendar fechaSeleccionada = Calendar.getInstance();
        fechaSeleccionada.set(Calendar.YEAR, año);
        fechaSeleccionada.set(Calendar.MONTH, mes - 1);
        Integer añoSeleccionado = fechaSeleccionada.get(Calendar.YEAR);
        Integer mesSeleccionado = fechaSeleccionada.get(Calendar.MONTH);
        Integer diaSeleccionado = fechaSeleccionada.get(Calendar.DAY_OF_MONTH);

        List<DiasNoLaborables> feriados = new ArrayList<DiasNoLaborables>();
        feriados = diasFestivosMes(mesActual + 1, añoActual);

        Integer diasAdicionales = 2; // Obtener desde bdd     

        Integer contadorDias = 0;
        Boolean habilitar = Boolean.FALSE;

        Calendar diaAux = Calendar.getInstance();

        if (añoActual.equals(añoSeleccionado)
                && mesActual.equals(mesSeleccionado)) {
            habilitar = Boolean.TRUE;
        } else {
            if ((mesActual - 1 == mesSeleccionado && añoActual.equals(añoSeleccionado))
                    || ((mesActual == 0 && mesSeleccionado == 11) && (añoActual - 1 == añoSeleccionado))) {
                for (int i = 1; i <= diaActual; i++) {
                    Boolean flagDiaFeriado = Boolean.FALSE;
                    diaAux.set(Calendar.DAY_OF_MONTH, i);
                    String dayOfWeek = diaAux.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US).toUpperCase();
                    if (!dayOfWeek.equals("SUNDAY") && !dayOfWeek.equals("SATURDAY")) {
                        for (DiasNoLaborables dnl : feriados) {
                            if (dnl.getDia() == i) {
                                flagDiaFeriado = Boolean.TRUE;
                                break;
                            }
                        }
                        if (!flagDiaFeriado) {
                            contadorDias++;
                        }
                    } else {
                        if (contadorDias == diasAdicionales) {
                            contadorDias++;
                        }
                    }
                    if (contadorDias <= diasAdicionales) {
                        habilitar = Boolean.TRUE;
                    } else {
                        habilitar = Boolean.FALSE;
                        break;
                    }
                }
            }
        }
        //Sección para el manejo de prórrogas
        if (prorrogaRemanenteMensualServicio.getProrrogaGeneral(año, mes) != null) {
            return Boolean.TRUE;
        } else {
            if (prorrogaRemanenteMensualServicio.getProrrogaRemanenteMensual(remanenteMensualId) != null) {
                return Boolean.TRUE;
            } else {
                return habilitar;
            }
        }
    }

    @Override
    public Boolean habilitarDiasAdicionalesCS(Integer año, Integer mes, Integer dia, Integer remanenteMensualId) {
        //Declaración        
        Calendar fechaSeleccionada = Calendar.getInstance();
        Calendar fechaActual = (Calendar) SerializationUtils.clone(fechaSeleccionada);
        fechaSeleccionada.set(Calendar.YEAR, año);
        fechaSeleccionada.set(Calendar.MONTH, mes - 1);
        fechaSeleccionada.set(Calendar.DAY_OF_MONTH, dia);
        List<DiasNoLaborables> feriados = new ArrayList<DiasNoLaborables>();
        Integer diasAdicionales = 2; //SP7
        Integer contadorDias = 0;
        Boolean habilitar = Boolean.TRUE;
        while (fechaActual.after(fechaSeleccionada)) {
            Boolean flagDiaFeriado = Boolean.FALSE;
            fechaActual.add(Calendar.DAY_OF_YEAR, -1);
            String dayOfWeek = fechaActual.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US).toUpperCase();
            feriados = diasFestivosMes(fechaActual.get(Calendar.MONTH) + 1, fechaActual.get(Calendar.YEAR));
            if (!dayOfWeek.equals("SUNDAY") && !dayOfWeek.equals("SATURDAY")) {
                for (DiasNoLaborables dnl : feriados) {
                    if (dnl.getDia().equals(fechaActual.get(Calendar.DAY_OF_MONTH))) {
                        flagDiaFeriado = Boolean.TRUE;
                        break;
                    }
                }
                if (!flagDiaFeriado) {
                    contadorDias++;
                }
            } else {
                if (contadorDias.equals(diasAdicionales)) {
                    contadorDias++;
                }
            }
            if (contadorDias <= diasAdicionales) {
                habilitar = Boolean.TRUE;
            } else {
                habilitar = Boolean.FALSE;
                break;
            }
        }
        //Sección para el manejo de prórrogas
        if (prorrogaRemanenteMensualServicio.getProrrogaRemanenteMensual(remanenteMensualId) != null) {
            return Boolean.TRUE;
        } else {
            return habilitar;
        }
    }
}
