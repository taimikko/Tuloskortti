package com.example.msa.tuloskortti1;

import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;

import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Formatter;


public class MainActivity extends Activity {

    static public enum tiivaihtoehdot {VALKOINEN, KELTAINEN, SININEN, PUNAINEN};

    NumeroListener nl;
    public final ArrayList<Vayla> vaylaArrayList = new ArrayList<>();
    ListView paikkalista;
    VaylaArrayAdapter adapteri;
    VaylaKuuntelija vaylaKuuntelija;
    int nykyinenVayla = 1;
    int edellinenVayla = 1;
    boolean replace = false;
    boolean nappaimet = true;
    String TIEDOSTO = "Tuloskortti.txt";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

 /*       // Tässä luodaan LayoutInflater-olio. Ja laitetaan olemassa oleva XML näkymäksi.
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater != null){
        inflater.inflate(R.layout.perustiedot, this);
        }*/

        paikkalista = (ListView) findViewById(R.id.valylalista);//Elementti, johon lista sijoittuu
        //Adapteri rakentaa listan sisällön metodissa getView
        adapteri = new VaylaArrayAdapter(this, -1, vaylaArrayList); //-1 - ei mitään listatyyppiä
        paikkalista.setAdapter(adapteri);
        vaylaKuuntelija = new VaylaKuuntelija(this);
        paikkalista.setOnItemClickListener(vaylaKuuntelija);

        lueKenttaArraylistTg();
        Button btn;

        // melkein kaikki buttonit asetetaan kuuntelamaan
        nl = new NumeroListener(this, paikkalista, vaylaArrayList, adapteri);
        btn = (Button) findViewById(R.id.button0);
        btn.setOnClickListener(nl);
        btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(nl);
        btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(nl);
        btn = (Button) findViewById(R.id.button3);
        btn.setOnClickListener(nl);
        btn = (Button) findViewById(R.id.button4);
        btn.setOnClickListener(nl);
        btn = (Button) findViewById(R.id.button5);
        btn.setOnClickListener(nl);
        btn = (Button) findViewById(R.id.button6);
        btn.setOnClickListener(nl);
        btn = (Button) findViewById(R.id.button7);
        btn.setOnClickListener(nl);
        btn = (Button) findViewById(R.id.button8);
        btn.setOnClickListener(nl);
        btn = (Button) findViewById(R.id.button9);
        btn.setOnClickListener(nl);

        RelativeLayout rl;
        rl = (RelativeLayout) findViewById(R.id.loppuosa);
        rl.setOnFocusChangeListener(new
                                            View.OnFocusChangeListener() {
                                                @Override
                                                public void onFocusChange(View v, boolean hasFocus) {
                                                    piilotaNumeronappaimet();
                                                    //Toast.makeText(v.getContext(), "View.OnFocusChangeListener()", Toast.LENGTH_SHORT).show();
                                                }
                                            });
        View.OnFocusChangeListener fcl = rl.getOnFocusChangeListener();
        EditText et = (EditText)findViewById(R.id.edtKommentti);

        et.setOnFocusChangeListener(fcl);
        et = (EditText)findViewById(R.id.edtMerkitsija);
        et.setOnFocusChangeListener(fcl);

        CalendarView calendar = (CalendarView)findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                                             @Override
                                             public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                                                 String strDate=String.valueOf(day)+"."+String.valueOf(month+1)+"."+String.valueOf(year);
                                                 ((TextView) findViewById(R.id.edtPaiva)).setText(strDate);
                                                 piilotaNumeroPickerJaKalenteri();
                                             }
                                         } );
        ((CalendarView) findViewById(R.id.calendarView)).setVisibility(View.GONE);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onPeruClick(View v) {
        adapteri.notifyDataSetChanged();
        palaaEdelliselleVayla();
        paikkalista.smoothScrollToPosition(getVayla());
    }

    public void fokusScrollView(){
        int positio = nl.annaPositio(this.getVayla()); // 9. jälkeen tulee välituloste (OUT) joten pitäisi siirtyä positioon 10 ja jatkossa positio on aina yhtä suurempi kuin pelattava väylä.
        paikkalista.smoothScrollToPosition(positio);
        final ScrollView scrollView = (ScrollView)findViewById(R.id.scrollView);
        scrollView.smoothScrollBy(0, -60);
    }

    public void onOkClick(View v) {
        piilotaNumeronappaimet();
        // jos tämä jälkeen klikataan väylillä, niin numeronäppäimet pitää palauttaa takaisin näkyviin ja piilottaa tämä
        // siirrytään alaosaan, jossa on kentät: kommentti ja kirjurisekä tallenna/peru -nappulat

        final ScrollView scrollView = (ScrollView)findViewById(R.id.scrollView);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        //  ((ScrollView)findViewById(R.id.scrollView)).fullScroll(View.FOCUS_DOWN);
    }


    public void onClickPiilotaNumeroPicker(View v) {
        //Toast.makeText(this, "onClickPerustiedot()", Toast.LENGTH_SHORT).show();
        piilotaNumeroPickerJaKalenteri();
    }

    private void piilotaNumeroPickerJaKalenteri() {
        ((NumberPicker)findViewById(R.id.numberPicker)).setVisibility(View.GONE);
        // piilottaa myos kalenteriPickerin
        ((CalendarView)findViewById(R.id.calendarView)).setVisibility(View.GONE);
        //laskepelitasoitus();
    }

    public void onClickPeruKaikki(View v) {
        Toast.makeText(this, "Suljetaan...", Toast.LENGTH_SHORT).show();
        // pitäisikö varmistaa ?? ennen lopetusta
        finish();
    }

    public void onClickTallenna(View v) {
        //Toast.makeText(this, "tallentaminen...", Toast.LENGTH_SHORT).show();
        tallenna();
    }

    private void piilotaNumeronappaimet() {
        RelativeLayout rl;
        rl = (RelativeLayout) findViewById(R.id.numeronappaimet);
        rl.setVisibility(View.GONE);
        /* rl = (RelativeLayout) findViewById(R.id.loppuosa);
        rl.setVisibility(View.VISIBLE); */
        nappaimet = false;
    }

    void naytaNumeronappaimet() {
        if (!nappaimet) {
            RelativeLayout rl;
            /*rl = (RelativeLayout) findViewById(R.id.loppuosa);
            rl.setVisibility(View.INVISIBLE);*/
            rl = (RelativeLayout) findViewById(R.id.numeronappaimet);
            rl.setVisibility(View.VISIBLE);
            nappaimet = true;
        }
    }


    public void vaihdaKentta(String kentta) {
        ((TextView) findViewById(R.id.edtKentta)).setText(kentta);

        lataaKentanVaylat(kentta, annaTii());
        paivitaKentanTunnusluvut(kentta);
        adapteri.notifyDataSetChanged();
    }

    private tiivaihtoehdot annaTii() {
        String vari = (String)((TextView) findViewById(R.id.edtTii)).getText();
        switch (vari) {
            case "Valkoinen": return tiivaihtoehdot.VALKOINEN;
            case "Keltainen": return tiivaihtoehdot.KELTAINEN;
            case "Sininen": return tiivaihtoehdot.SININEN;
            default: return tiivaihtoehdot.PUNAINEN;
        }
    }

    public void paivitaKentanTunnusluvut(String kentta) {
        // Log.i("oma", "paivitaKentanTunnusluvut(" + kentta + ")");
        ((TextView) findViewById(R.id.edtPar)).setText(String.valueOf(DB.kentanPar(kentta)));
        String tii = (String) ((TextView) findViewById(R.id.edtTii)).getText();
        ((TextView) findViewById(R.id.edtKentanSlope)).setText(String.valueOf(DB.kentanSlope(kentta, tii)));
        ((TextView) findViewById(R.id.edtKentanCR)).setText(String.valueOf(DB.kentanCR(kentta, tii)));
        laskePelitasoitus();
    }

    private void lataaKentanVaylat(String kentta, tiivaihtoehdot tii) {
        //Toast.makeText(this,"lataaKentanVaylat("+kentta+")", Toast.LENGTH_SHORT).show();
        switch (kentta) {
            case "Tammer Golf":
                kenttaTg();
                break;
            case "Testi Golf":
                kenttaTestiGolf(tii);
                break;
            case "Lakeside Golf":
            case "LGV - Järvenranta":
                lueKenttaLGVJarvenranta(); break;
            case "LGV - Pirunpelto":
                lueKenttaLGVPirunpelto();break;
            case "Pirkanmaan Golf":
            case "Golf Pirkkala":
            case "Nokia River Golf":
            case "NRG - Kartano":
            case "NRG - River":
            default:
                kenttaTestiGolf(tii);
        }

    }

    private void kenttaTestiGolf(tiivaihtoehdot tii) {
        int i=0;
        int [] par = new int [19];
        int [] hcp= new int [19];
        int [] pit= new int [19];
        int pit3, pit4, pit5; // par 3 , 4 ja 5 väylien pituudet
        switch (tii) {
            case VALKOINEN:
                pit3=160;
                pit4=350;
                pit5=470;
                break;
            case KELTAINEN:
                pit3=150;
                pit4=320;
                pit5=450;
                break;
            case SININEN:
                pit3=130;
                pit4=300;
                pit5=420;
                break;
            case PUNAINEN:
            default:
                pit3=100;
                pit4=280;
                pit5=400;
        }
        int [] p = new int [6];
        p[0] = 0;
        p[1] = 0;
        p[2] = 0;
        p[3] = pit3;
        p[4] = pit4;
        p[5] = pit5;

        par[i] = 0; hcp [i]=0; pit[i] =0; i++; // otsikkorivi (on oikeastaan turha...
        par[i] = 4; hcp [i]=18; pit[i] =p[par[i]]; i++; // 1.väylä
        par[i] = 5; hcp [i]=17; pit[i] =p[par[i]]; i++; // 2.väylä
        par[i] = 4; hcp [i]=16; pit[i] =p[par[i]]; i++; // 3.väylä
        par[i] = 3; hcp [i]=15; pit[i] =p[par[i]]; i++; // 4.väylä
        par[i] = 4; hcp [i]=14; pit[i] =p[par[i]]; i++; // 5.väylä
        par[i] = 5; hcp [i]=13; pit[i] =p[par[i]]; i++; // 6.väylä
        par[i] = 4; hcp [i]=12; pit[i] =p[par[i]]; i++; // 7.väylä
        par[i] = 3; hcp [i]=11; pit[i] =p[par[i]]; i++; // 8.väylä
        par[i] = 4; hcp [i]=10; pit[i] =p[par[i]]; i++; // 9.väylä

        par[i] = 4; hcp [i]=9; pit[i] =p[par[i]]; i++; // 10.väylä
        par[i] = 3; hcp [i]=8; pit[i] =p[par[i]]; i++; // 11.väylä
        par[i] = 4; hcp [i]=7; pit[i] =p[par[i]]; i++; // 12.väylä
        par[i] = 5; hcp [i]=6; pit[i] =p[par[i]]; i++; // 13.väylä
        par[i] = 4; hcp [i]=5; pit[i] =p[par[i]]; i++; // 14.väylä
        par[i] = 3; hcp [i]=4; pit[i] =p[par[i]]; i++; // 15.väylä
        par[i] = 4; hcp [i]=3; pit[i] =p[par[i]]; i++; // 16.väylä
        par[i] = 5; hcp [i]=2; pit[i] =p[par[i]]; i++; // 17.väylä
        par[i] = 4; hcp [i]=1; pit[i] =p[par[i]]; i++; // 18.väylä

        lueKenttaArraylist(par, hcp, pit );
    }

    private void lueKenttaLGVPirunpelto() { // Pirunpelto
        int i=0;
        int [] par = new int [19];
        int [] hcp= new int [19];
        int [] pit= new int [19];
        par[i] = 0; hcp [i]=0; pit[i] =0; i++; // otsikkorivi (on oikeastaan turha...
        par[i] = 4; hcp [i]=15; pit[i] =267; i++; // 1.väylä
        par[i] = 5; hcp [i]=7; pit[i] =463; i++; // 2.väylä
        par[i] = 3; hcp [i]=13; pit[i] =146; i++; // 3.väylä
        par[i] = 4; hcp [i]=11; pit[i] =325; i++; // 4.väylä
        par[i] = 4; hcp [i]=9; pit[i] =314; i++; // 5.väylä
        par[i] = 5; hcp [i]=1; pit[i] =488; i++; // 6.väylä
        par[i] = 4; hcp [i]=3; pit[i] =338; i++; // 7.väylä
        par[i] = 3; hcp [i]=17; pit[i] =154; i++; // 8.väylä
        par[i] = 4; hcp [i]=5; pit[i] =334; i++; // 9.väylä

        par[i] = 5; hcp [i]=14; pit[i] =476; i++; // 10.väylä
        par[i] = 4; hcp [i]=6; pit[i] =350; i++; // 11.väylä
        par[i] = 3; hcp [i]=8; pit[i] =160; i++; // 12.väylä
        par[i] = 4; hcp [i]=16; pit[i] =317; i++; // 13.väylä
        par[i] = 4; hcp [i]=12; pit[i] =295; i++; // 14.väylä
        par[i] = 3; hcp [i]=18; pit[i] =131; i++; // 15.väylä
        par[i] = 5; hcp [i]=10; pit[i] =485; i++; // 16.väylä
        par[i] = 4; hcp [i]=2; pit[i] =355; i++; // 17.väylä
        par[i] = 4; hcp [i]=4; pit[i] =355; i++; // 18.väylä

        lueKenttaArraylist(par, hcp, pit );
    }

    private void lueKenttaLGVJarvenranta() { // miehet+keltainen
        int i=0;
        int [] par = new int [19];
        int [] hcp= new int [19];
        int [] pit= new int [19];
        par[i] = 0; hcp [i]=0; pit[i] =0; i++; // otsikkorivi (on oikeastaan turha...
        par[i] = 4; hcp [i]=12; pit[i] =307; i++; // 1.väylä
        par[i] = 3; hcp [i]=18; pit[i] =120; i++; // 2.väylä
        par[i] = 4; hcp [i]=8; pit[i] =326; i++; // 3.väylä
        par[i] = 4; hcp [i]=10; pit[i] =330; i++; // 4.väylä
        par[i] = 3; hcp [i]=6; pit[i] =150; i++; // 5.väylä
        par[i] = 5; hcp [i]=2; pit[i] =523; i++; // 6.väylä
        par[i] = 4; hcp [i]=16; pit[i] =307; i++; // 7.väylä
        par[i] = 4; hcp [i]=14; pit[i] =306; i++; // 8.väylä
        par[i] = 4; hcp [i]=4; pit[i] =355; i++; // 9.väylä

        par[i] = 5; hcp [i]=11; pit[i] =453; i++; // 10.väylä
        par[i] = 5; hcp [i]=17; pit[i] =450; i++; // 11.väylä
        par[i] = 3; hcp [i]=13; pit[i] =143; i++; // 12.väylä
        par[i] = 5; hcp [i]=3; pit[i] =468; i++; // 13.väylä
        par[i] = 4; hcp [i]=7; pit[i] =303; i++; // 14.väylä
        par[i] = 3; hcp [i]=15; pit[i] =150; i++; // 15.väylä
        par[i] = 4; hcp [i]=1; pit[i] =321; i++; // 16.väylä
        par[i] = 3; hcp [i]=9; pit[i] =137; i++; // 17.väylä
        par[i] = 4; hcp [i]=5; pit[i] =373; i++; // 18.väylä

        lueKenttaArraylist(par, hcp, pit );
    }

    public void onTempClick(View v) {
        String luettu = lue();
        if (luettu.length() == 0) {
            ((EditText) findViewById(R.id.edtKommentti)).setText("Ei \nlöytynyt \nmitään \nluettavaa...");
        } else {
            ((EditText) findViewById(R.id.edtKommentti)).setText(luettu);
            ScrollView scrollView = (ScrollView)findViewById(R.id.scrollView);
            scrollView.fullScroll(View.FOCUS_DOWN);
        }
    }

    void piilotaTulosrivit() {
        piilotaNumeronappaimet();
        RelativeLayout rl;
        ListView lv = (ListView) findViewById(R.id.valylalista);
        lv.setVisibility(View.INVISIBLE);
        // perustiedot esille
        rl = (RelativeLayout) findViewById(R.id.perustiedot);
        alustaPerustiedot();
        rl.setVisibility(View.VISIBLE);
        // haetaan näytöllä oleva tasoitus ja alustetaan sillä numberpicker
        Double d = Double.valueOf((String) ((TextView) findViewById(R.id.edtTasoitus)).getText());
        alustaNumberPicker(d);
    }

    private void alustaPerustiedot() {
        final String SEURA = "TAKK International Golf Club";
        final int JASENNUMERO = 12345;
        final String NIMI = "Pekka Pelaaja";
        Double tasoitus = 12.3;

        alustaPelaajanTiedot(SEURA, JASENNUMERO, NIMI, tasoitus);
        String kentta = haeLahinKentta();
        alustakentanTiedot(kentta);
        alustaPaiva();
        laskePelitasoitus();
    }

    private void alustaPaiva() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String strDate = sdf.format(Calendar.getInstance().getTime());
        ((TextView) findViewById(R.id.edtPaiva)).setText(strDate);
    }

    private String haeLahinKentta() {
        return "TG";
    }

    private void alustaPelaajanTiedot(String seura, int jasennumero, String nimi, Double tasoitus) {
        // Oletetaan että käyttäjä on kirjautunut sisään jollain nimellä, johon liittyy oma seura ja sen jäsennumero
        ((TextView) findViewById(R.id.edtSeura)).setText(seura);
        ((TextView) findViewById(R.id.edtJasennumero)).setText(String.valueOf(jasennumero));
        ((TextView) findViewById(R.id.edtNimi)).setText(nimi);
        ((TextView) findViewById(R.id.edtTasoitus)).setText(String.valueOf(tasoitus));
    }

    private void alustakentanTiedot(String kentta) {
        ((TextView) findViewById(R.id.edtPar)).setText(String.valueOf(DB.kentanPar(kentta)));
        // TODO: alustetaan kentällä käytetäävissä olevat tiit valikkoon (miehet)
        // alustetaan käytettävissä oleva tii: miehille Keltainen
        String tii = (String) ((TextView) findViewById(R.id.edtTii)).getText();
        ((TextView) findViewById(R.id.edtKentanSlope)).setText(String.valueOf(DB.kentanSlope(kentta, tii)));
        ((TextView) findViewById(R.id.edtKentanCR)).setText(String.valueOf(DB.kentanCR(kentta, tii)));
    }

    void alustaNumberPicker(double tarkka) {
        final int MINIMITASOITUS = -9;
        final double TARKKUUS = 10;
        NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setVisibility(View.VISIBLE);
        int size = (36 - MINIMITASOITUS + 1) * (int) TARKKUUS + (54 - 36); //  jotta saadaan nolla ja viimeinen paikka mukaan
        final String[] nums = new String[size];
        // Log.i("oma", "nums.length = " + nums.length);
        int i = 0;
        // vaihtoehtoinen tapa
        for (int koko = (int) MINIMITASOITUS; koko <= 0; koko++) {
            for (int osa = 9; osa >= 0; osa--) {
                if (koko != 0) {
                    // 0.0 tulostetaan vain kerran, positiivissa luvuissa
                    nums[i++] = String.valueOf(koko) + "." + String.valueOf(osa);
                    // Log.i("oma", "-nums[" + (i - 1) + "]=" + nums[i - 1]);
                } else {
                    if (osa != 0) {
                        // 0.0 tulostetaan vain kerran, positiivissa luvuissa
                        nums[i++] = "-" + String.valueOf(koko) + "." + String.valueOf(osa);
                        //Log.i("oma", "-nums[" + (i - 1) + "]=" + nums[i - 1]);
                    }
                }
            }
        }

        for (int koko = 0; koko <= 54; koko++) {
            if (koko < 36) {
                for (int osa = 0; osa < 10; osa++) {
                    nums[i++] = String.valueOf(koko) + "." + String.valueOf(osa);
                    //Log.i("oma", "+nums[" + (i - 1) + "]=" + nums[i - 1]);
                }
            } else {
                // 36, 37 ... 54
                nums[i++] = String.valueOf(koko);
                // Log.i("oma", " nums[" + (i - 1) + "]=" + nums[i - 1]);
            }
        }

/*
         seuraava toimii, mutta kokeillaan toista lähestymistapaa
        for (int i = 0; i < nums.length; i++) {
            // Seuraavasta ei tule tasan yksidesimaalisia lukuja, vaan paaljon pyöristysvirheitä (12.0000000000000001)
            //nums[i] = Double.toString((i / TARKKUUS) + MINIMITASOITUS); // testataan tuleeko 12.3 (eikä 12,3)

            // seuraavaan tulee desimaalierottimeksi pilkku, joka kaataa ohjelman, kun sitä yritetään lukea desimaaliluvuksi
            //nums[i] = String.format("%.1f", (i / TARKKUUS) + MINIMITASOITUS);  //Integer.toString((i-10)); //kaatuu : Float.toString((i/10f)-10);

            int koko = (int) ((i / TARKKUUS) + MINIMITASOITUS);
            int osa = (int) (i % TARKKUUS);
            nums[i] = Integer.toString(koko)+"."+Integer.toString(osa);
        }
*/
        // http://stackoverflow.com/questions/19737755/how-do-you-display-a-2-digit-numberpicker-in-android
        //http://stackoverflow.com/questions/13645927/android-numberpicker-for-floating-point-numbers
/* */

        np.setMinValue(0);
        np.setMaxValue(nums.length - 1);
        np.setWrapSelectorWheel(false); // (false);
        np.setDisplayedValues(nums);
        // pitäisi löytää, monesko item tuo "tarkka" on listassa nums[]

        np.setValue((int) (TARKKUUS * (tarkka - MINIMITASOITUS + 1) - 1)); // ota tästä selvää, mutta oikein menee
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                         @Override
                                         public void onValueChange(NumberPicker picker, int
                                                 oldVal, int newVal) {
                                             String s = nums[newVal];
                                             // Log.i("oma", "NumberPicker " + s);
                                             ((TextView) findViewById(R.id.edtTasoitus)).setText(s);
                                             laskePelitasoitus(); // laskee näytöllä olevista luvuista pelitasoituksen
                                         }
                                     }
        );
    }

    void alustaDatePicker() {
        ;
    }


    public void onClickAvaaKalenteri(View v) {
        String s = (String) ((TextView) findViewById(R.id.edtPaiva)).getText();
        ((CalendarView) findViewById(R.id.calendarView)).setVisibility(View.VISIBLE);
        // Toast.makeText(this, "onClickAvaaKalenteri()", Toast.LENGTH_SHORT).show();
    }

    public void onTarkkaTasoitusClick(View v) {
        String s = (String) ((TextView) findViewById(R.id.edtTasoitus)).getText();
        Double tasoitus = Double.valueOf(s);
        alustaNumberPicker(tasoitus);
    }

    public void onKenttaClick(View v) {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(this, v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_kentat, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                TextView tv = (TextView) findViewById(R.id.edtKentta);
                tv.setText(item.getTitle());
                vaihdaKentta((String)item.getTitle());
                paivitaTunnusluvut();
                return true;
            }

        });

        popup.show(); //showing popup menu
    }

    public void onTeeClick(View v) {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(this, v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu1, popup.getMenu());

        //TODO: menun taustaväriksi #cfc ja valittavat tekstit omalla värillään: "punainen" punaisella, jne (valkoinen näkyy vain jos tausta saadaan väritetttyä)
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                TextView tv = (TextView) findViewById(R.id.edtTii);
                tv.setText(item.getTitle());

                paivitaTunnusluvut();
                return true;
            }

        });

        popup.show(); //showing popup menu

    }

    private void paivitaTunnusluvut() {
        String kentta = (String) ((TextView) findViewById(R.id.edtKentta)).getText();
        vaihdaKentta(kentta); // tämä päivittää myös väylien pituudet taulukkoon
        // paivitaKentanTunnusluvut(kentta); // laskee vain uudet tunnusluvut
    }

    public void laskePelitasoitus() {
        String s;
        double tarkka;
        s = (String) ((TextView) findViewById(R.id.edtTasoitus)).getText();
        // merkkijonossa on nyt erottimena pilkku
        try {
            tarkka = Double.valueOf(s);
        } catch (Exception e) {
            tarkka = 0;
        }
        // Log.i("oma", "laskePelitasoitus: tarkka=" + s + " -->(" + String.valueOf(tarkka) + ")");
        s = (String) ((TextView) findViewById(R.id.edtPar)).getText();
        int par = Integer.valueOf(s);
        s = (String) ((TextView) findViewById(R.id.edtKentanSlope)).getText();
        int slope = Integer.valueOf(s);
        s = (String) ((TextView) findViewById(R.id.edtKentanCR)).getText();
        double cr = Double.valueOf(s);

        int pelitasoitus = laskePelitasoitus(tarkka, par, slope, cr);
        ((TextView) findViewById(R.id.edtPelitasoitus)).setText(String.format("%d", pelitasoitus));
    }

    int laskePelitasoitus(double tarkka, int par, int slope, double cr) {
        return (int) ((cr - par) + (tarkka * slope) / 113);
    }

    private void lueKenttaArraylistTg() {
        int par;
        int pituus;
        lisaaVayla(adapteri.HEADER, 0, 0, 0); // otsikkorivi ensimmäiseksi
        lisaaVayla(1, 5, 5, 428);
        lisaaVayla(2, 4, 9, 331);
        lisaaVayla(3, 4, 3, 319);
        lisaaVayla(4, 4, 13, 327);
        lisaaVayla(5, 4, 1, 371);
        lisaaVayla(6, 5, 7, 443);
        lisaaVayla(7, 4, 17, 308);
        lisaaVayla(8, 4, 11, 345);
        lisaaVayla(9, 3, 15, 130);
        par = adapteri.laskeKentanPar(1, 9);
        pituus = adapteri.laskeKentanPituus(1, 9);
        lisaaVayla(adapteri.OUT, par, 0, pituus);
        lisaaVayla(10, 4, 10, 310);
        lisaaVayla(11, 3, 8, 198);
        lisaaVayla(12, 5, 6, 499);
        lisaaVayla(13, 4, 14, 301);
        lisaaVayla(14, 3, 18, 145);
        lisaaVayla(15, 4, 4, 330);
        lisaaVayla(16, 5, 12, 419);
        lisaaVayla(17, 3, 16, 155);
        lisaaVayla(18, 4, 2, 381);
        par = adapteri.laskeKentanPar(10, 18);
        pituus = adapteri.laskeKentanPituus(10, 18);
        lisaaVayla(adapteri.IN, par, 0, pituus);
        par = adapteri.laskeKentanPar(1, 18);
        pituus = adapteri.laskeKentanPituus(1, 18);
        lisaaVayla(adapteri.TOTAL, par, 0, pituus); // loppuyhteenveto
        // Log.i("oma", adapteri.toString());
    }

    private void kenttaTg() {
        int i=0;
        int [] par = new int [19];
        int [] hcp= new int [19];
        int [] pit= new int [19];
        par[i] = 0; hcp [i]=0; pit[i] =0; i++; // otsikkorivi (on oikeastaan turha...
        par[i] = 5; hcp [i]=5; pit[i] =428; i++; // 1.väylä
        par[i] = 4; hcp [i]=9; pit[i] =331; i++; // 2.väylä
        par[i] = 4; hcp [i]=3; pit[i] =319; i++; // 3.väylä
        par[i] = 4; hcp [i]=13; pit[i] =327; i++; // 4.väylä
        par[i] = 4; hcp [i]=1; pit[i] =371; i++; // 5.väylä
        par[i] = 5; hcp [i]=7; pit[i] =443; i++; // 6.väylä
        par[i] = 4; hcp [i]=17; pit[i] =308; i++; // 7.väylä
        par[i] = 4; hcp [i]=11; pit[i] =345; i++; // 8.väylä
        par[i] = 3; hcp [i]=15; pit[i] =130; i++; // 9.väylä

        par[i] = 4; hcp [i]=10; pit[i] =310; i++; // 10.väylä
        par[i] = 3; hcp [i]=8; pit[i] =198; i++; // 11.väylä
        par[i] = 5; hcp [i]=6; pit[i] =499; i++; // 12.väylä
        par[i] = 4; hcp [i]=14; pit[i] =301; i++; // 13.väylä
        par[i] = 3; hcp [i]=18; pit[i] =145; i++; // 14.väylä
        par[i] = 4; hcp [i]=4; pit[i] =330; i++; // 15.väylä
        par[i] = 5; hcp [i]=12; pit[i] =419; i++; // 16.väylä
        par[i] = 3; hcp [i]=16; pit[i] =155; i++; // 17.väylä
        par[i] = 4; hcp [i]=2; pit[i] =381; i++; // 18.väylä

        lueKenttaArraylist(par, hcp, pit );
    }

    private void lueKenttaArraylist(int [] par, int[] hcp, int [] pit ) {
        //Toast.makeText(this,"lueKenttaArraylist("+")", Toast.LENGTH_SHORT).show();
        vaylaArrayList.clear();
        int parx;
        int pituus;
        int i = 0;
        lisaaVayla(adapteri.HEADER, 0, 0, 0); // vaylat[0,0] = otsikkorivi ensimmäiseksi
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        parx = adapteri.laskeKentanPar(1, 9);
        pituus = adapteri.laskeKentanPituus(1, 9);
        lisaaVayla(adapteri.OUT, parx, 0, pituus);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        lisaaVayla(++i, par[i], hcp[i], pit[i]);
        parx = adapteri.laskeKentanPar(10, 18);
        pituus = adapteri.laskeKentanPituus(10, 18);
        lisaaVayla(adapteri.IN, parx, 0, pituus);
        parx = adapteri.laskeKentanPar(1, 18);
        pituus = adapteri.laskeKentanPituus(1, 18);
        lisaaVayla(adapteri.TOTAL, parx, 0, pituus); // loppuyhteenveto
    }

    private void lisaaVayla(int nro, int par, int hcp, int pituus) {
        Vayla vayla;
        vayla = new Vayla(nro, par, hcp, pituus);
        vayla.setTavoiteSlope(13);
        vaylaArrayList.add(vayla);
    }

    public int getVayla() {
        return nykyinenVayla;
    }

    public void setVayla(int uusi) {
        edellinenVayla = nykyinenVayla;
        nykyinenVayla = uusi;
        if (nykyinenVayla > 18) {
            nykyinenVayla = 18;
        }
    }

    public int palaaEdelliselleVayla() {
        int temp;
        temp = edellinenVayla;
        edellinenVayla = nykyinenVayla;
        nykyinenVayla = temp;

        return nykyinenVayla;
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.msa.tuloskortti1/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
        alustaPerustiedot();
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.msa.tuloskortti1/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    public void onCheckClick(View v) {
        laskePelitasoitus(); // laskee näytöllä olevista luvuista pelitasoituksen
    }

       /* External Files
    Kommenteja: muistettava Manifestiin:
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

    File x=Environment.getExternalStorageDirectory(); Kaikille sovelluksille yhteinen kansio

    File x=Context.getExternalFilesDir(String type); type voi olla null. Sovelluskohtainen _ensisijainen_
    ulkoinen hakemisto. Se voi olla laitteen emuloima ulkoinen muisti tai ulkoinen muistikortti. Kun sovellus
    poistetaan, myös tämän sisältö poistetaan. Permissioita ei tarvita 4.4 alkaen

    Ks. myös:
    File x=Context.getExternalCacheDir();//Permissioita ei tarvita 4.4 alkaen
    or Context.getExternalCacheDirs()

    getExternalStorageState(File) - onko mountattu, poistettu huonosti ym
    isExternalStorageEmulated(File) - onko external kuitenkin sisäinen
    isExternalStorageRemovable(File) - onko external oikeasti external
     */

    private void tallenna() {
        String esd = Environment.getExternalStorageDirectory() + "/" + TIEDOSTO;
        // Log.i("oma", "tallenna()");
        String txt = ""; //String.valueOf(nimi.getText());
        txt += String.valueOf(((TextView) findViewById(R.id.edtJasennumero)).getText()) + ";";
        txt += String.valueOf(((TextView) findViewById(R.id.edtNimi)).getText()) + ";";
        txt += String.valueOf(((TextView) findViewById(R.id.edtPaiva)).getText()) + ";";
        txt += String.valueOf(((TextView) findViewById(R.id.edtTasoitus)).getText()) + ";";
        txt += String.valueOf(((TextView) findViewById(R.id.edtKentta)).getText()) + ";";
        txt += String.valueOf(((TextView) findViewById(R.id.edtTii)).getText()) + ";";
        txt += "\n";
        int i = 0;
        for (Vayla v : vaylaArrayList) {
            if (v.getNro() >= 1 && v.getNro() <= 18) {
                txt += String.valueOf(v.getLyonnit()) + ";";
            }
        }
        txt += "\n";
        txt += String.valueOf(((TextView) findViewById(R.id.edtKommentti)).getText()) + ";";
        txt += String.valueOf(((TextView) findViewById(R.id.edtMerkitsija)).getText()) + ";";

        if (txt == null || txt.length() == 0) {
            Toast.makeText(this, "Empty string...", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            /* tiedosto puuttuu:            http://stackoverflow.com/questions/1239026/how-to-create-a-file-in-android
                        */
            // Log.i("oma", "tallenna():" + esd);
            FileWriter fw = new FileWriter(esd, true);
            BufferedWriter bw = new BufferedWriter(fw);
            Toast.makeText(this, "Tallennetaan " + esd, Toast.LENGTH_SHORT).show();
            bw.write(txt + "\n");
            bw.close();
            fw.close();
        } catch (FileNotFoundException e) {
            Log.i("oma", "tallenna(): FileNotFoundException: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("oma", "tallenna(): IOException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private String lue() {
        String s = Environment.getExternalStorageDirectory() + "/" + TIEDOSTO;
        String luettu = "";
        StringBuffer sb = new StringBuffer();
        try {
            FileReader fr = new FileReader(s);
            BufferedReader br = new BufferedReader(fr);

            while ((s = br.readLine()) != null) {
                luettu += s + "\n";
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return luettu;
    }

}
