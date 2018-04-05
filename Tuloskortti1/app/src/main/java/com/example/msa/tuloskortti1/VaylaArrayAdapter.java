package com.example.msa.tuloskortti1;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 114440 on 11.1.2016.
 */
public class VaylaArrayAdapter extends ArrayAdapter {
    MainActivity paaohjelma;
    ArrayList<Vayla> lista;
    private int imageResource;
    // TODO: näistä enum ?
    public static final int OUT = 19;
    public static final int IN = 20;
    public static final int TOTAL = 21;
    public static final int HEADER = 0;


    public VaylaArrayAdapter(MainActivity paaohjelma, int resource, ArrayList<Vayla> vaylat) {
        super(paaohjelma, resource, vaylat);
        this.paaohjelma = paaohjelma;
        this.lista = vaylat;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) paaohjelma.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.vayla_layout, parent, false);
        TextView txtView;
        int vaylanro = lista.get(position).getNro();
        int min = 0;
        int max = 0;

        switch (vaylanro) {
            case HEADER: // ensimmäinen rivi = otsikot

                txtView = (TextView) rowView.findViewById(R.id.txtVayla);
                txtView.setText("Väylä");

                txtView = (TextView) rowView.findViewById(R.id.txtPar);
                txtView.setText(" PAR ");

                txtView = (TextView) rowView.findViewById(R.id.txtHcp);
                txtView.setText(" HCP ");

                txtView = (TextView) rowView.findViewById(R.id.txtPituus);
                txtView.setText("Pituus"); // TODO: periaatteessa pituuden yksikkö voisi olla myös jaardeja.

                txtView = (TextView) rowView.findViewById(R.id.edtLyonnit);
                txtView.setText("Lyönnit");

                txtView = (TextView) rowView.findViewById(R.id.txtPisteet);
                txtView.setText(" PB ");
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
                // väylät
                txtView = (TextView) rowView.findViewById(R.id.txtVayla);
                txtView.setText(lista.get(position).getNro() + ".");

                txtView = (TextView) rowView.findViewById(R.id.txtPar);
                //txtView.setText("PAR " + lista.get(position).getPar());
                txtView.setText("" + lista.get(position).getPar());

                txtView = (TextView) rowView.findViewById(R.id.txtHcp);
                txtView.setText("" + lista.get(position).getHcp());

                txtView = (TextView) rowView.findViewById(R.id.txtPituus);
                txtView.setText(lista.get(position).getPituus() + "m"); // TODO: periaatteessa pituuden yksikkö voisi olla myös jaardeja.

                txtView = (TextView) rowView.findViewById(R.id.edtLyonnit);
                txtView.setText("" + lista.get(position).getLyonnit());

                if (position== NumeroListener.annaPositio(paaohjelma.getVayla())){
                    txtView.setBackgroundColor(Color.YELLOW);
                } else {
                    txtView.setBackgroundColor(Color.rgb(204,255,204)); // #cfc
                }

                txtView = (TextView) rowView.findViewById(R.id.txtPisteet);
                txtView.setText("" + lista.get(position).getPisteet());
                break;
            case IN:
            case OUT:
            case TOTAL:
                txtView = (TextView) rowView.findViewById(R.id.txtVayla);
                switch (vaylanro) {
                    case IN:
                        txtView.setText("IN");
                        min = 10;
                        max = 18;
                        break;
                    case OUT:
                        txtView.setText("OUT");
                        min = 1;
                        max = 9;
                        break;
                    case TOTAL:
                        txtView.setText("yht.");
                        min = 1;
                        max = 18;
                        break;
                }
                txtView = (TextView) rowView.findViewById(R.id.txtPar);
                txtView.setText("" + lista.get(position).getPar());

                txtView = (TextView) rowView.findViewById(R.id.txtHcp);
                txtView.setText(" ");

                txtView = (TextView) rowView.findViewById(R.id.txtPituus);
                txtView.setText(lista.get(position).getPituus() + ""); // TODO: periaatteessa pituuden yksikkö voisi olla myös jaardeja.

                txtView = (TextView) rowView.findViewById(R.id.edtLyonnit);
                txtView.setText("" + lista.get(position).getLyonnit());
//                txtView.setText("" + laskeKentanLyonnit(min, max));

                txtView = (TextView) rowView.findViewById(R.id.txtPisteet);
                txtView.setText("" + lista.get(position).getPisteet());
//                txtView.setText("" + laskeKentanPisteet(min, max));

        }

        return rowView;
    }

    public int laskeKentanPituus(int min, int max) {
        int pituus = 0;
        for (Vayla v : paaohjelma.vaylaArrayList) {
            if (v.getNro() >= min && v.getNro() <= max) {
                pituus += v.getPituus();
            }
        }
        return pituus;
    }

    public int laskeKentanPar(int min, int max) {
        int par = 0;
        for (Vayla v : paaohjelma.vaylaArrayList) {
            if (v.getNro() >= min && v.getNro() <= max) {
                par += v.getPar();
            }
        }
        return par;
    }

    public int laskeKentanLyonnit(int min, int max) {
        int i = 0;
        for (Vayla v : paaohjelma.vaylaArrayList) {
            if (v.getNro() >= min && v.getNro() <= max) {
                if (v.getLyonnit() == 0) {
                    // lyöntipelitulosta ei voi laskea, jos joltain väylätä puuttuu tulos.
                    return 0;
                } else {
                    i += v.getLyonnit();
                }
            }
        }
        return i;
    }

    public int laskeKentanPisteet(int min, int max) {
        int i = 0;
        for (Vayla v : paaohjelma.vaylaArrayList) {
            if (v.getNro() >= min && v.getNro() <= max) {
                i += v.getPisteet();
            }
        }
        return i;
    }

}
