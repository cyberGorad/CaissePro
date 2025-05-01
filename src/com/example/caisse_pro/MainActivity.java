package com.example.caisse_pro;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    // Déclaration des vues
    private EditText editTextDebit, editTextCredit;
    private TextView textViewCaisse;
    private Button buttonDebiter, buttonCrediter;
    
    // Instance de la base de données
    private DatabaseHelper dbHelper;
    
    // Variable fond de caisse
    private double caisse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des vues
        editTextDebit = (EditText) findViewById(R.id.editText_debit);
        editTextCredit = (EditText) findViewById(R.id.editText_credit);
        textViewCaisse = (TextView) findViewById(R.id.textView_caisse);
        buttonDebiter = (Button) findViewById(R.id.button_debiter);
        buttonCrediter = (Button) findViewById(R.id.button_crediter);
        
        // Initialisation de la base de données
        dbHelper = new DatabaseHelper(this);

        // Charger le montant initial de la caisse depuis la base de données
        caisse = dbHelper.getCaisseAmount();
        updateCaisse();

        // Bouton pour débiter
        buttonDebiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double debit = Double.parseDouble(editTextDebit.getText().toString());
                    if (debit > 0) {
                        caisse += debit;
                        dbHelper.updateCaisseAmount(caisse); // Sauvegarder en base de données
                        updateCaisse();
                        Toast.makeText(MainActivity.this, "Débit effectué!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Entrez un montant valide pour débiter.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Veuillez entrer un montant valide.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Bouton pour créditer
        buttonCrediter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double credit = Double.parseDouble(editTextCredit.getText().toString());
                    if (credit > 0 && credit <= caisse) {
                        caisse -= credit;
                        dbHelper.updateCaisseAmount(caisse); // Sauvegarder en base de données
                        updateCaisse();
                        Toast.makeText(MainActivity.this, "Crédit effectué!", Toast.LENGTH_SHORT).show();
                    } else if (credit > caisse) {
                        Toast.makeText(MainActivity.this, "Montant trop élevé, fond de caisse insuffisant.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Entrez un montant valide pour créditer.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Veuillez entrer un montant valide.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Met à jour l'affichage du fond de caisse
    private void updateCaisse() {
        textViewCaisse.setText("\ud83c\udfe6 Fond de caisse: " + caisse + " AR");
    }
}
