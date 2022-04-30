package fr.lordloss.quizzanime;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;




public class PageJeuActivity1 extends AppCompatActivity {

    /* tableaux qui vont de pair, comportant les noms des musique ainsi que leur id */
    private final ArrayList<String> musiqueNom = new ArrayList<>();
    private final ArrayList<Integer> musiqueId = new ArrayList<>();

    /* objet media player pour les sons */
    private MediaPlayer mediaPlayer;

    /* boutons de choix de réponses */
    private Button btnChoix1;
    private Button btnChoix2;
    private Button btnChoix3;
    private Button btnChoix4;

    /* */
    private TextView vieTv;
    private TextView scoreTv;
    private Button fleches;
    private Button play;

    /* tableaux de ressources */
    private final int[] images = {R.drawable.ic_baseline_not_started_24, R.drawable.ic_baseline_pause_24};
    private final int[] repBouton = {R.drawable.blancla, R.drawable.vertla, R.drawable.rougela};

    private int indexEnCours; // index de la musique en cours
    private int vieBase, vie;
    private int tempsBase, temps;
    private int score = 0;


    /* Bouton de la popup Replay */
    private Button rejouer, goMenu;
    private TextView grosScoreTV;
    private static PageJeuActivity1 PageJeuActivity1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_jeu1);

        /* On récupère les données de l'autre page */
        Intent intent = getIntent();
        vieBase = intent.getIntExtra("vie",1);
        vie = vieBase;
        tempsBase = intent.getIntExtra("temps",10);
        score = intent.getIntExtra("score",0);

        initialisation(); // les findview by id
        ajoutMusique(); // ajout des musiques de raw dans tableaux
        debutDeManche(); // debut de la 1ere manche, mise en place

        PageJeuActivity1 = this; //permet de fermer cette activite depuis d'autre activite

        vieTv.setText(getString(R.string.page_jeu_entete_vie, vie)); //Le vie du header

        scoreTv.setText(getString(R.string.page_jeu_entete_score, score)); // Le score du header


        fleches.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mediaPlayer.stop();
                Intent i = new Intent(PageJeuActivity1.this, MainActivity.class);
                startActivity(i);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play.setBackgroundResource(images[0]);
                } else {
                    mediaPlayer.start();
                    play.setBackgroundResource(images[1]);
                }
            }
        });



        /** En jeu lorsque l'on va cliquer sur l'un des quatres boutons l'on va devoir
         * - Verifier si la reponse est bonne ou mauvaise
         *      - si la reponse est bonne :
         *          - afficher la case en vert
         *          - mettre une pause de temps de 1 seconde
         *          - ajouter plus 1 au score
         *          - remettre en blanc toutes les cases
         *          - changer de musique
         *      - si la reponse est mauvaise :
         *          - afficher la case cliquee en rouge
         *          - affiche la bonne reponse en vert
         *          - mettre une pause de temps de 1 seconde
         *          - enleve une vie
         *          - remettre en blanc toutes les cases
         *          - changer de musique
         *
         * Gestion d'erreur debut de manche :
         * - Active les boutons
         * - Demarre la musique
         * - changer l'image du bouton pause
         *
         * Gestion d'erreur fin de fin de manche :
         * - Désactiver les boutons
         * - Arrete la musique
         * - changer l'image du bouton pause
         */
        btnChoix1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finDeManche();
                /* true si la reponse est bonne */
                if(btnChoix1.getText().toString().compareTo(musiqueNom.get(indexEnCours)) == 0) {
                    btnChoix1.setBackgroundResource(repBouton[1]); //affiche la case en vert
                    attributionScore(); // ajoute +1 au score
                } else {
                    btnChoix1.setBackgroundResource(repBouton[2]); //affiche la case en rouge
                    reponseJusteEnVert(indexEnCours); // affiche l'emplacement de la bonne reponse en vert
                    gestionVie(view); // on enleve un point de vie
                }
                nHandler.postDelayed(mRunnable, 1000); // delais de 1 seconde
            }
        });
        btnChoix2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finDeManche();
                /* true si la reponse est bonne */
                if(btnChoix2.getText().toString().compareTo(musiqueNom.get(indexEnCours)) == 0) {
                    btnChoix2.setBackgroundResource(repBouton[1]); //affiche la case en vert
                    attributionScore(); // ajoute +1 au score
                } else {
                    btnChoix2.setBackgroundResource(repBouton[2]); //affiche la case en rouge
                    reponseJusteEnVert(indexEnCours);
                    gestionVie(view);
                }
                nHandler.postDelayed(mRunnable, 1000);
            }
        });
        btnChoix3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                /* true si la reponse est bonne */
                finDeManche();
                if(btnChoix3.getText().toString().compareTo(musiqueNom.get(indexEnCours)) == 0) {
                    btnChoix3.setBackgroundResource(repBouton[1]); //affiche la case en vert
                    attributionScore(); // ajoute +1 au score
                } else {
                    btnChoix3.setBackgroundResource(repBouton[2]); //affiche la case en rouge
                    reponseJusteEnVert(indexEnCours);
                    gestionVie(view);
                }
                nHandler.postDelayed(mRunnable, 1000);
            }
        });
        btnChoix4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finDeManche();
                /* true si la reponse est bonne */
                if(btnChoix4.getText().toString().compareTo(musiqueNom.get(indexEnCours)) == 0) {
                    btnChoix4.setBackgroundResource(repBouton[1]); //affiche la case en vert
                    attributionScore(); // ajoute +1 au score
                } else {
                    btnChoix4.setBackgroundResource(repBouton[2]); //affiche la case en rouge
                    reponseJusteEnVert(indexEnCours);
                    gestionVie(view);
                }
                nHandler.postDelayed(mRunnable, 1000);
            }
        });
    }

    /**
     * initialisation de toute les variables xml
     */
    private void initialisation() {
        btnChoix1 = (Button) findViewById(R.id.choix1);
        btnChoix2 = (Button) findViewById(R.id.choix2);
        btnChoix3 = (Button) findViewById(R.id.choix3);
        btnChoix4 = (Button) findViewById(R.id.choix4);
        vieTv = (TextView) findViewById(R.id.life);
        scoreTv = (TextView) findViewById(R.id.scorer);
        fleches = (Button) findViewById(R.id.flechess);
        play = (Button) findViewById(R.id.startmusic);
    }


    private static class MyHandler extends Handler {}
    private final MyHandler nHandler = new MyHandler();

    public static class MyRunnable implements Runnable {
        private final WeakReference<Activity> mActivity;

        public MyRunnable(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            Activity activity = mActivity.get();
            if (activity != null) {
                if(PageJeuActivity1.vie > 0) {
                    PageJeuActivity1.caseEnBlanc(); // remet le fond des cases en blanc
                    PageJeuActivity1.debutDeManche(); // on relance une nouvelle manche
                }
            }
        }
    }

    private MyRunnable mRunnable = new MyRunnable(this);


    /**
     * Désactive ou active tous les boutons
     */
    public void boutonActivation(boolean act) {
        if(act) {
            btnChoix1.setClickable(true);
            btnChoix1.setEnabled(true);
            btnChoix2.setClickable(true);
            btnChoix2.setEnabled(true);
            btnChoix3.setClickable(true);
            btnChoix3.setEnabled(true);
            btnChoix4.setClickable(true);
            btnChoix4.setEnabled(true);
        } else {
            btnChoix1.setClickable(false);
            btnChoix1.setEnabled(false);
            btnChoix2.setClickable(false);
            btnChoix2.setEnabled(false);
            btnChoix3.setClickable(false);
            btnChoix3.setEnabled(false);
            btnChoix4.setClickable(false);
            btnChoix4.setEnabled(false);
        }
    }

    /**
     *
     */
    public void finDeManche() {
        boutonActivation(false);
        mediaPlayer.stop();
    }

    /**
     * On s'assure que le média player est bien détruit
     * Pour cela on change la méthode on Destroy
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
    }


    /**
     *
     */
    public void debutDeManche() {
        indexEnCours = changeMusique();
        boutonActivation(true);
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), musiqueId.get(indexEnCours));
        mediaPlayer.start();
        play = (Button) findViewById(R.id.startmusic);
        play.setBackgroundResource(images[1]);
    }

    /**
     *
     */
    public void reponseJusteEnVert(int musique)  {
        if(btnChoix1.getText().toString().compareTo(musiqueNom.get(indexEnCours)) == 0) {
            btnChoix1.setBackgroundResource(repBouton[1]);
        } else if (btnChoix2.getText().toString().compareTo(musiqueNom.get(indexEnCours)) == 0) {
            btnChoix2.setBackgroundResource(repBouton[1]);
        } else if (btnChoix3.getText().toString().compareTo(musiqueNom.get(indexEnCours)) == 0){
            btnChoix3.setBackgroundResource(repBouton[1]);
        } else if(btnChoix4.getText().toString().compareTo(musiqueNom.get(indexEnCours)) == 0) {
            btnChoix4.setBackgroundResource(repBouton[1]);
        } else {
            System.err.println("erreur => reponseJusteEnVert");
        }
    }

    /**
     * Choisi une chanson et écrit les propositions dans les boutons
     * @return l'index de la chanson jouee'
     */
    public int changeMusique() {
        int[] nom = { -1, -2, -3, -4};
        int bonneReponse;

        // choix de la proposition 1
        nom[0] = (int) Math.floor(Math.random() * musiqueNom.size());

        for (int i = 1; i < nom.length; i++) {
            do {
                nom[i] = (int) Math.floor(Math.random() * musiqueNom.size());
            } while (nom[0] == nom[1] || nom[0] == nom[2] || nom[0] == nom[3]
                    || nom[1] == nom[2] || nom[1] == nom[3]|| nom[2] == nom[3] );
        }

        /* On doit choisir un emplacement différent pour chaque proposition
         * sinon la bonne réponse serait toujours au même endroit
         */
        bonneReponse = (int) Math.floor(Math.random() * 4);

        btnChoix1.setText(musiqueNom.get(nom[0]));
        btnChoix2.setText(musiqueNom.get(nom[1]));
        btnChoix3.setText(musiqueNom.get(nom[2]));
        btnChoix4.setText(musiqueNom.get(nom[3]));

        return nom[bonneReponse];
    }

    /**
     * si la reponse est bonne alors
     *  - ajoute 1 au score
     *
     */
    public void attributionScore() {
        score++;
        scoreTv.setText(getString(R.string.page_jeu_entete_score, score));
    }

    public void setVie(int vie) {
        this.vie = vie;
    }

    public void setScore(int score) {
        this.score = score;
    }


    /**
     * methode qui permet d'enlever des vies et d'en ajouter en fonction de si l'on repond juste ou faux
     */
    public void gestionVie(View view) {
        setVie(vie-1);
        if(vie <= 0) {
            affichepopup(view);
        }
        vieTv.setText(getString(R.string.page_jeu_entete_vie, vie));
    }

    /**
     * Methode qui remet le fond des cases en blanc à chaque fois que l'on clique sur une réponse
     */
    public void caseEnBlanc() {
        btnChoix1.setBackgroundResource(repBouton[0]);
        btnChoix2.setBackgroundResource(repBouton[0]);
        btnChoix3.setBackgroundResource(repBouton[0]);
        btnChoix4.setBackgroundResource(repBouton[0]);
    }

    /**
     * Methode qui permet de mettre sur pause lorsqu'on ferme l'appli ou lors d'un retour arrière
     */
    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.pause();
        play.setBackgroundResource(images[0]);
    }

    /**
     * permet de finish l'activité depuis une autre
     * @return
     */
    public static PageJeuActivity1 getInstance(){
        return PageJeuActivity1;
    }


    /**
     * Methode qui affiche la fenetre permettant d'aller au menu ou de rejouer
     * @param view : view (ne sais pas trop ce que c'est)
     */
    public void affichepopup(View view) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_pop_up_replay);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        grosScoreTV = (TextView) dialog.findViewById(R.id.lescore);
        grosScoreTV.setText(getString(R.string.score, score));

        /**
         * Methode du bouton rejouer
         * permet de relancer une partie avec les mêmes règles que la partie précédente
         */
        rejouer = (Button) dialog.findViewById(R.id.restart);
        rejouer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                PageJeuActivity1.getInstance().finish();
                Intent i = new Intent(PageJeuActivity1.this, PageJeuActivity1.class);
                i.putExtra("vie", vieBase);
                i.putExtra("temps", temps);
                startActivity(i);
                finish();
            }
        });

        /*
          Methode du bouton rejouer
          permet de sauvegarder le nb de vie et le temps choisis par l'utilisateur
         */
        goMenu = (Button) dialog.findViewById(R.id.menu);
        goMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                PageJeuActivity1.getInstance().finish();
                Intent i = new Intent(PageJeuActivity1.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * Methode qui permet d'ajouter toute les musiques dans un tableau
     */
    private void ajoutMusique() {
//        int drawableResourceId;
//        File[] files = new File("src/main/res/raw").listFiles();
//        for (File file : files) {
//            if (file.isFile()) {
//                drawableResourceId = this.getResources().getIdentifier(file.getName(), "raw", this.getPackageName());
//                musiqueId.add(drawableResourceId);
//                musiqueNom.add(file.getName());
//            }
//        }

        musiqueNom.add("mahou shoujo madoka magica");
        musiqueNom.add("made in abyss");
        musiqueNom.add("erased");
        musiqueNom.add("coolest");

        musiqueId.add(R.raw.mahou_shoujo_madokamagica);
        musiqueId.add(R.raw.made_in_abyss);
        musiqueId.add(R.raw.erased_opening);
        musiqueId.add(R.raw.coolest_section);
    }
}

