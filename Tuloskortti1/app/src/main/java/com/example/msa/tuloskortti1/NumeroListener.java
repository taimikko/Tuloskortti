package com.example.msa.tuloskortti1;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.support.design.widget.Snackbar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 114440 on 11.1.2016.
 */
public class NumeroListener implements View.OnClickListener {
    private ListView lista;
    //private int vayla=1;
    ArrayList<Vayla> vaylaArrayList;
    VaylaArrayAdapter adapteri;
    MainActivity paaohjelma;

    NumeroListener(MainActivity paaohjelma, ListView lv, ArrayList<Vayla> vaylaArrayList, VaylaArrayAdapter adapteri) {
        lista = lv;
        this.vaylaArrayList = vaylaArrayList;
        this.adapteri = adapteri;
        this.paaohjelma = paaohjelma;
    }

    static int annaPositio(int vayla) {
        int positio = 0;
        switch (vayla) {
            case 0:
                positio = 1; // ekalle väylälle
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                positio = vayla;
                break;
            case 9:
                positio = vayla;
                break;
            case 19:
            case 20:
                positio = 20;
                break;
            default: // 10 - 18
                positio = vayla + 1;
                break;
        }
        Log.i("oma", "annaPositio(" + vayla + ")=" + positio);
        return positio;
    }

    @Override
    public void onClick(View v) {
        String s = (String) ((Button) v).getText();
        boolean siirry = false;
        int i = Integer.valueOf(s);
        i = lisaaLyonnit(paaohjelma.getVayla(), i);
        String str = "Väylä " + paaohjelma.getVayla() + "[" + s + "] = " + i; // +" "+aktVayla.toString();
        // Log.i("oma", "NumeroListener.onClick() "+str);
        if (i > 1 || i == 0) {
            if (paaohjelma.getVayla() >= 18 ) {
                siirry = true;// viimeinen väylä on jo annettu
            }
            paaohjelma.setVayla(paaohjelma.getVayla() +1);

            int positio; // Position is zero based index, mutta ekalla rivillä on otsikot
            positio = annaPositio(paaohjelma.getVayla()); // 9. jälkeen tulee välituloste (OUT) joten pitäisi siirtyä positioon 10 ja jatkossa positio on aina yhtä suurempi kuin pelattava väylä.
            lista.smoothScrollToPosition(positio);

               if (paaohjelma.getVayla() > 2) {
               ScrollView scrollView = (ScrollView)paaohjelma.findViewById(R.id.scrollView);
               scrollView.smoothScrollBy(0, 60);
                //scrollView.scrollTo(1, 1);
            }
            if (siirry) {
                //paaohjelma.onOkClick(v); // piilotaNumeronappaimet();
                // TODO: siirretään focus alareunan kommentti/merkitsijä -kenttään
                /*RelativeLayout rl;
                rl = (RelativeLayout) paaohjelma.findViewById(R.id.loppuosa);
                rl.foc setFocus(); */
            }
        }
        v.invalidate();
        // Snackbar.make(v, str, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
    }

    private int lisaaLyonnit(int vaylanro, int i) {
        Vayla v = vaylaArrayList.get(annaPositio(vaylanro));//.getSelectedItem();
        int lyonnit;
        if (paaohjelma.replace) {
            lyonnit = i;
        } else {
            lyonnit = v.getLyonnit() * 10 + i;
        }
        if (i == 1) {
            paaohjelma.replace = false;
        }
        if (lyonnit > 9) {
            paaohjelma.replace = true;
        }
        v.setLyonnit(lyonnit);

        int tavoite = v.getTavoite();
        int pisteet = tavoite + 2 - lyonnit;
        if (pisteet < 0 || lyonnit < 1) { // jos ei tulosta ei pisteitä. eikä negatiivisia pisteitä.
            pisteet = 0;
        }
        v.setPisteet(pisteet);
        laskeLyonnitJaPisteet(vaylanro);
        adapteri.notifyDataSetChanged();
        return lyonnit;
    }

    private void laskeLyonnitJaPisteet(int vaylanro) {
        int lyonnit;
        int pisteet;
        Vayla v;
        if (vaylanro >= 1 && vaylanro <= 18) {
            if (vaylanro <= 9) {
                // 1 -9
                lyonnit = adapteri.laskeKentanLyonnit(1, 9);
                pisteet = adapteri.laskeKentanPisteet(1, 9);
                v = vaylaArrayList.get(10); //adapteri.OUT);//.getSelectedItem();
                v.setLyonnit(lyonnit);
                v.setPisteet(pisteet);
            } else {
                // 10 -18
                lyonnit = adapteri.laskeKentanLyonnit(10, 18);
                pisteet = adapteri.laskeKentanPisteet(10, 18);
                v = vaylaArrayList.get(20); //adapteri.IN);//.getSelectedItem();
                v.setLyonnit(lyonnit);
                v.setPisteet(pisteet);
            }
            v = vaylaArrayList.get(21); //adapteri.TOTAL);//.getSelectedItem();
            lyonnit = adapteri.laskeKentanLyonnit(1, 18);
            pisteet = adapteri.laskeKentanPisteet(1, 18);
            v.setLyonnit(lyonnit);
            v.setPisteet(pisteet);
        }
    }

}
