package com.example.msa.tuloskortti1;

import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 114440 on 13.1.2016.
 */
public class VaylaKuuntelija implements AdapterView.OnItemClickListener{

        MainActivity paaohjelma;
        RelativeLayout edellinen;

        private View klikattuPaikka;
        public VaylaKuuntelija(MainActivity paaohjelma){

            this.paaohjelma=paaohjelma;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position > parent.getCount()-3) return; // kolme ylimääräistä (IN, OUT ja TOTAL)
            paaohjelma.naytaNumeronappaimet(); // vähän brutaalia, kun tarvitaan vain jos kommentti-osasta palataan takaisin numeroihin
            paaohjelma.adapteri.notifyDataSetChanged();
            RelativeLayout rl=(RelativeLayout)view;
            paaohjelma.setVayla(haeVayla(position));
            // Toast.makeText(paaohjelma, "VaylaKuuntelija: "+paaohjelma.vayla+" positio:"+position, Toast.LENGTH_SHORT).show();
            TextView tv=(TextView)rl.getChildAt(4); // 4 on lyönnnit
            tv.setBackgroundColor(Color.YELLOW);
            klikattuPaikka=view;
            paaohjelma.replace = true; // jos klikataan väylällä niin jatkossa kirjoitetaan vanhojen numeroiden päälle.
            paaohjelma.fokusScrollView();
       }

    private int haeVayla(int positio) {
        int vayla;
        switch (positio) {
                case 0:
                    vayla = 1; // ekalle väylälle
                    break;
                case 1:case 2:case 3:case 4:case 5:case 6:case 7:case 8:case 9:
                    vayla = positio;
                    break;
                case 10:
                    vayla = 10; // 9 + header + OUT
                    break;
                default: // 10 - 18
                    vayla = positio - 1;
            }
            return vayla;
    }
}
