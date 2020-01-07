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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "DiasNoLaborablesServicio")
public class DiasNoLaborablesServicioImpl extends GenericServiceImpl<DiasNoLaborables, Integer> implements DiasNoLaborablesServicio {

    @EJB
    private DiasNoLaborablesDao diasNoLaborablesDao;

    @Override
    public GenericDao<DiasNoLaborables, Integer> getDao() {
        return diasNoLaborablesDao;
    }

    @Override
    public List<Date> diasFestivosAtivos() {
        try {
            List<Date> diasFestivosList = new ArrayList<Date>();
            List<DiasNoLaborables> fechaList = new ArrayList<DiasNoLaborables>();
            String[] criteriaNombres = {"estado"};
            CriteriaTypeEnum[] criteriaTipos = {CriteriaTypeEnum.STRING_EQUALS};
            Object[] criteriaValores = {"A"};
            String[] orderBy = {"diasNoLaborablesId"};
            boolean[] asc = {true};
            Criteria criteria = new Criteria(criteriaNombres, criteriaTipos, criteriaValores, orderBy, asc);
            fechaList = findByCriterias(criteria);
            for (DiasNoLaborables dnl : fechaList) {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dnl.getAnio() + "-" + dnl.getMes() + "-" + dnl.getDia());
                diasFestivosList.add(date);
            }
            return diasFestivosList;
        } catch (ParseException ex) {
            Logger.getLogger(DiasNoLaborablesServicioImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
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
    public Boolean habilitarDiasAdicionales(Integer mes) {
        //Declaración        
        Calendar fechaActual = Calendar.getInstance();
        Integer añoActual = fechaActual.get(Calendar.YEAR);
        Integer mesActual = fechaActual.get(Calendar.MONTH);
        Integer diaActual = fechaActual.get(Calendar.DAY_OF_MONTH);
        System.out.println("añoActual = " + añoActual);
        System.out.println("mesActual = " + mesActual);
        System.out.println("diaActual = " + diaActual);

        Calendar fechaSeleccionada = Calendar.getInstance();
        fechaSeleccionada.set(Calendar.MONTH, mes - 1);
        Integer añoSeleccionado = fechaSeleccionada.get(Calendar.YEAR);
        Integer mesSeleccionado = fechaSeleccionada.get(Calendar.MONTH);
        Integer diaSeleccionado = fechaSeleccionada.get(Calendar.DAY_OF_MONTH);
        System.out.println("añoSeleccionado = " + añoSeleccionado);
        System.out.println("mesSeleccionado = " + mesSeleccionado);
        System.out.println("diaSeleccionado = " + diaSeleccionado);

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
                System.out.println("***Validación mes anterior***");
                for (int i = 1; i <= diaActual; i++) {
                    Boolean flagDiaFeriado = Boolean.FALSE;
                    diaAux.set(Calendar.DAY_OF_MONTH, i);
                    String dayOfWeek = diaAux.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US).toUpperCase();
                    System.out.println(dayOfWeek);
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
}
