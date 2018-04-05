package com.example.msa.tuloskortti1;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by 114440 on 21.1.2016.
 */
public class DB {

    public static int kentanSlope(String kentta, String tii) {
        // pitäisi hakea kannasta, nyt tämä antaa nyt vain miesten arvon TG:llä
        int slope = 0;
        switch (kentta) {
            default:
                switch (tii) {
                    case "Valkoinen":
                        slope = 128;
                        break;
                    case "Keltainen":
                        slope = 125;
                        break;
                    case "Sininen":
                        slope = 121;
                        break;
                    case "Punainen":
                        slope = 119;
                        break;
                }

        }
        return slope;
    /*
    TG:

    Par 72
    miehet
    valkoinen: slope 128, CR 72,3
    keltainen: slope 125, CR 70,7
    sininen:  slope 121, cr 68,9
    punainen: slope 119, cr 68,0

    naiset
    keltainen: slope 131, CR 76,7
    sininen:  slope 126, cr 74,5
    punainen: slope 124, cr 73,3
     */
    }

    public static double kentanCR(String kentta, String tii) {
        // pitäisi hakea kannasta, nyt tämä antaa nyt vain miesten arvon TG:llä
        double cr = 0;
        switch (kentta) {
            default:
                switch (tii) {
                    case "Valkoinen":
                        cr = 72.3;
                        break;
                    case "Keltainen":
                        cr = 70.7;
                        break;
                    case "Sininen":
                        cr = 68.9;
                        break;
                    case "Punainen":
                        cr = 68.0;
                        break;
                }

        }
        Log.i("oma", "kentanCR " + String.valueOf(cr));
        return cr;
    }

    public static int kentanPar(String kentta) {
        // pitäisi hakea jostain kannasta
        return 72;
    }



    public static void readerWriterMain(String[] args) {
        // TODO Auto-generated method stub
        StringReader sr=new StringReader("Testiä\nuseampi rivi\n kaiken varalta...");
        BufferedReader br=new BufferedReader(sr);

        String ss=null;
        try {
            while ((ss=br.readLine())!=null){
                System.out.println(ss);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        StringWriter sw=new StringWriter();

        InputStreamReader sr2=new InputStreamReader(System.in);
        br=new BufferedReader(sr2);

        ss=" ";
        while(ss.equals("")==false){
            try {
                ss=br.readLine();
                System.out.println("M: "+ss);
                sw.write(ss+"\n");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


        sr=new StringReader(sw.toString());
        br=new BufferedReader(sr);

        ss=null;
        try {
            while ((ss=br.readLine())!=null){
                System.out.println(ss);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


}
