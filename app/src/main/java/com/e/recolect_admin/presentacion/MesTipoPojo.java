package com.e.recolect_admin.presentacion;

import java.util.ArrayList;

public class MesTipoPojo {
    TiposPojo ene, feb, mar, abr, may, jun, jul, ago, sep, oct, nov, dic;

    public MesTipoPojo() {
    }

    public ArrayList<TiposPojo> getPrimerSemestre(){
        ArrayList<TiposPojo> primerSemestre = new ArrayList<>();
        primerSemestre.add(getEne());
        primerSemestre.add(getFeb());
        primerSemestre.add(getMar());
        primerSemestre.add(getAbr());
        primerSemestre.add(getMay());
        primerSemestre.add(getJun());

        return primerSemestre;
    }

    public ArrayList<TiposPojo> getSegundoSemestre(){
        ArrayList<TiposPojo> segundoSemestre = new ArrayList<>();
        segundoSemestre.add(getJul());
        segundoSemestre.add(getAgo());
        segundoSemestre.add(getSep());
        segundoSemestre.add(getOct());
        segundoSemestre.add(getNov());
        segundoSemestre.add(getDic());
        return segundoSemestre;
    }

    public TiposPojo getEne() {
        return ene;
    }

    public void setEne(TiposPojo ene) {
        this.ene = ene;
    }

    public TiposPojo getFeb() {
        return feb;
    }

    public void setFeb(TiposPojo feb) {
        this.feb = feb;
    }

    public TiposPojo getMar() {
        return mar;
    }

    public void setMar(TiposPojo mar) {
        this.mar = mar;
    }

    public TiposPojo getAbr() {
        return abr;
    }

    public void setAbr(TiposPojo abr) {
        this.abr = abr;
    }

    public TiposPojo getMay() {
        return may;
    }

    public void setMay(TiposPojo may) {
        this.may = may;
    }

    public TiposPojo getJun() {
        return jun;
    }

    public void setJun(TiposPojo jun) {
        this.jun = jun;
    }

    public TiposPojo getJul() {
        return jul;
    }

    public void setJul(TiposPojo jul) {
        this.jul = jul;
    }

    public TiposPojo getAgo() {
        return ago;
    }

    public void setAgo(TiposPojo ago) {
        this.ago = ago;
    }

    public TiposPojo getSep() {
        return sep;
    }

    public void setSep(TiposPojo sep) {
        this.sep = sep;
    }

    public TiposPojo getOct() {
        return oct;
    }

    public void setOct(TiposPojo oct) {
        this.oct = oct;
    }

    public TiposPojo getNov() {
        return nov;
    }

    public void setNov(TiposPojo nov) {
        this.nov = nov;
    }

    public TiposPojo getDic() {
        return dic;
    }

    public void setDic(TiposPojo dic) {
        this.dic = dic;
    }
}
