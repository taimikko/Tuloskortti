package com.example.msa.tuloskortti1;

import android.widget.TextView;

/**
 * Created by 114440 on 11.1.2016.
 */
public class Vayla {
    private int nro=0;
    private int par=0;
    private int pituus=0;
    private int hcp=0;
    private int lyonnit=0;
    private int pisteet=0;
    private int tavoite=0;

    public int getNro() {
        return nro;
    }

    public void setNro(int nro) {
        this.nro = nro;
    }

    public int getPituus() {
        return pituus;
    }

    public void setPituus(int pituus) {
        this.pituus = pituus;
    }

    public int getHcp() {
        return hcp;
    }

    public void setHcp(int hcp) {
        this.hcp = hcp;
    }

    public int getLyonnit() {
        return lyonnit;
    }

    public void setLyonnit(int lyonnit) {
        this.lyonnit = lyonnit;
    }

    public int getPisteet() {
        return pisteet;
    }

    public void setPisteet(int pisteet) {
        this.pisteet = pisteet;
    }

    public int getTavoite() {
        return tavoite;
    }

    public void setTavoite(int tavoite) {
        this.tavoite = tavoite;
    }

    public void setTavoiteSlope(int slope) {
        int koko, osa;
        osa = slope%18;
        koko = slope/18;
        if (getHcp()>osa) {
            // väylän HCP = 14 ja slope 13 -> ei tasoitusta tällä väylällä
            tavoite = getPar()+koko;
        } else {
            // väylän HCP = 12 ja slope 13 -> saa tasoitusta tällä väylällä
            tavoite = getPar()+koko + 1;
        }
        if (nro <1 || nro > 18) {
            // ei ole normiväyliä, joten tavoite lasketaan muualla
            tavoite = 0;
        }
    }

    public int getPar() {
        return par;
    }

    public void setPar(int par) {
        this.par = par;
    }

    Vayla(int nro, int par, int hcp, int pituus) {
        this.nro = nro;
        this.par = par;
        this.pituus = pituus;
        this.hcp = hcp;
    }

    public String toString() {
        String s = "Väylä "+nro+" HCP "+hcp+" PAR"+par+" "+pituus+"m ("+lyonnit+") "+pisteet+"PB";
        return s;
    }

}
