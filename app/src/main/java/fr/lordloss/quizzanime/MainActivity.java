package fr.lordloss.quizzanime;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MainActivity activity;
    private Button changeRegle;
    private Button param;
    private Button start;
    private Button swapLeft;
    private Button swapRight;
    private TextView titrel;
    private TextView descl;
    private int mode = 0;

    /**
     * variable de la pop-up
     */
    private SeekBar vieSeek;
    private SeekBar tempsSeek;
    private int vie = 3;
    private int temps = 10;
    private Button appliquer;
    private TextView vieTV;
    private TextView tempsTV;

    int vieProgressChangedValue = 0;
    int tempsProgressChangedValue = 0;


    private final int[] DESC = {R.string.page_accueil_section_classique_texte, R.string.page_accueil_section_survie_texte,
                  R.string.page_accueil_section_1_seconde_texte, R.string.page_accueil_section_aleatoire_texte};

    private final int[] TITRE = {R.string.page_accueil_section_classique, R.string.page_accueil_section_survie,
                   R.string.page_accueil_section_1_seconde, R.string.page_accueil_section_aleatoire};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Bouton paramètre */
        param = (Button) findViewById(R.id.param);
        param.setOnClickListener(new View.OnClickListener() {
            /**
             * Si l'on clique sur le bouton parametre l'on chnage de page vers la page de parametre
             * @param view :
             */
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ParamActivity1.class);
                startActivity(i);
            }
        });
        /* Bouton commencer */
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PageJeuActivity1.class);
                i.putExtra("vie", vie);
                i.putExtra("temps", temps);
                startActivity(i);
            }
        });


        swapRight = (Button) findViewById(R.id.right);
        swapLeft = (Button) findViewById(R.id.left);
        titrel = (TextView) findViewById(R.id.titre1);
        descl = (TextView) findViewById(R.id.resumeMode);
        mode = 0;


        swapRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(mode == 3) {
                    mode = 0;
                } else {
                    mode++;
                }
                titrel.setText(getString(TITRE[mode]));
                descl.setText(getString(DESC[mode]));
            }

        });

        swapLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(mode == 0) {
                    mode = DESC.length-1;
                } else {
                    mode--;
                }
                titrel.setText(getString(TITRE[mode]));
                descl.setText(getString(DESC[mode]));
            }
        });
    }

    public void showAlert() {
    }

    /**
     * A partir d'ici on affiche la pop-up des règles
     *
     */
    public void affichePopUp(View view) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_popup_regle);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        /*
         * A partir d'ici c'est un copié coller tout mignon tout beau tout kawaii
         */
        tempsSeek = (SeekBar) dialog.findViewById(R.id.seekBarTemps);
        vieSeek = (SeekBar) dialog.findViewById(R.id.seekBarVie);
        vieTV = (TextView) dialog.findViewById(R.id.vie);
        tempsTV = (TextView) dialog.findViewById(R.id.temps);
        tempsTV.setText(getString(R.string.tempsString, temps));
        vieTV.setText(getString(R.string.vieString, vie));

        vieSeek.setProgress(vie - 1);
        switch (temps) {
            case 3:
                tempsSeek.setProgress(1);
                break;
            case 5:
                tempsSeek.setProgress(2);
                break;
            case 10:
                tempsSeek.setProgress(3);
                break;
            case 15:
                tempsSeek.setProgress(4);
                break;
            case 20:
                tempsSeek.setProgress(5);
                break;
            default :
                tempsSeek.setProgress(0);
                break;
        }

        vieProgressChangedValue = vieSeek.getProgress();
        tempsProgressChangedValue = tempsSeek.getProgress();

        /**
         * Méthode Controllant la progress barre de vie
         */
        vieSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * Méthode appelée quand la Progress barre change
             * Permet de sauvegarder temporairerement le vie choisi
             * Le temps ne sera sauvegarder d�finitivement que lorsqu'on appuiera sur Appliquer
             */
            public void onProgressChanged ( SeekBar seekBar , int progress , boolean fromUser ) {
                vieProgressChangedValue = progress;
                vieTV.setText(getString(R.string.vieString, vieSeek.getProgress() + 1));
            }

            /**
             * M�thode permettant d'ajouter des �venement quand le doigt touche la Progress Barre
             * Non utilis� ici
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /**
             * M�thode permettant d'ajouter des �venement quand le doigt lache la Progress Barre
             * Non utilis� ici
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        /*
         * Methode qui controle la progress barre du temps
         */
        tempsSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * methode qui est appele quand la Progress barre change
             * Permet de sauvegarder temporairerement le temps choisi
             * Le temps ne sera sauvegarder d�finitivement que lorsqu'on appuiera sur Appliquer
             */
            public void onProgressChanged ( SeekBar seekBar , int progress , boolean fromUser ) {
                tempsProgressChangedValue = progress;
                switch (progress) {
                    case 0:
                        tempsProgressChangedValue = 1;
                        break;
                    case 1:
                        tempsProgressChangedValue = 3;
                        break;
                    case 2:
                        tempsProgressChangedValue = 5;
                        break;
                    case 3:
                        tempsProgressChangedValue = 10;
                        break;
                    case 4:
                        tempsProgressChangedValue = 15;
                        break;
                    case 5:
                        tempsProgressChangedValue = 20;
                        break;
                    default :
                        tempsProgressChangedValue = 0;
                        break;
                }
                tempsTV.setText(getString(R.string.tempsString, tempsProgressChangedValue));
            }

            /**
             * M�thode permettant d'ajouter des �venement quand le doigt touche la Progress Barre
             * Non utilis� ici
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /**
             * M�thode permettant d'ajouter des �venement quand le doigt lache la Progress Barre
             * Non utilis� ici
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /**
         * Méthode du bouton Appliquer
         * permet de sauvegarder le nb de vie et le temps choisis par l'utilisateur
         */
        appliquer = (Button) dialog.findViewById(R.id.appliquer);
        appliquer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                vie = vieSeek.getProgress() + 1;
                temps = tempsSeek.getProgress();
                dialog.dismiss();

            }
        });

    }
}