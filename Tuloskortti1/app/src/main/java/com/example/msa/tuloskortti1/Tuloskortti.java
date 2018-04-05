package com.example.msa.tuloskortti1;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 114440 on 15.1.2016.
 */
public class Tuloskortti implements Serializable {
    private final ArrayList<Vayla> vaylaArrayList=new ArrayList<>();
    int slope=0;
    int vayla;

    Tuloskortti() {

    }


    public void lisaaVayla(int nro, int par, int hcp, int pituus) {
        Vayla vayla;
        vayla=new Vayla(nro,par,hcp,pituus);
        vayla.setTavoiteSlope(13);
        vaylaArrayList.add(vayla);
    }

    public int laskeKentanPituus() {
        int pituus = 0;
        for (Vayla v : vaylaArrayList) {
            if (v.getNro() >= 1 && v.getNro() <=18) {
                pituus += v.getPituus();
            }
        }
        return pituus;
    }

    public int laskeKentanPar() {
        int par = 0;
        for (Vayla v : vaylaArrayList) {
            if (v.getNro() >= 1 && v.getNro() <=18) {
                par += v.getPar();
            }
        }
        return par;
    }

    public int laskeKentanLyonnit() {
        int i = 0;
        for (Vayla v : vaylaArrayList) {
            if (v.getNro() >= 1 && v.getNro() <=18) {
                i += v.getLyonnit();
            }
        }
        return i;
    }

    public int laskeKentanPisteet() {
        int i = 0;
        for (Vayla v : vaylaArrayList) {
            if (v.getNro() >= 1 && v.getNro() <=18) {
                i += v.getPisteet();
            }
        }
        return i;
    }

}
