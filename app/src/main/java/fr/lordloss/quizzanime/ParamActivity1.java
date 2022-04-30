package fr.lordloss.quizzanime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ParamActivity1 extends AppCompatActivity {

    Button fleche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param1);
        fleche = (Button) findViewById(R.id.retour);
        fleche.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(ParamActivity1.this, MainActivity.class);
                startActivity(i);

            }


        });
    }
}