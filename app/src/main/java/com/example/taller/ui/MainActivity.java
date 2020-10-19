package com.example.taller.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.taller.R;
import com.example.taller.core.Taller;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa atributos
        this.taller = new Taller();

        // Inicializar vistas
        final Button BT_INSERTA = this.findViewById( R.id.btInserta );

        BT_INSERTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.inserta();
            }
        });

        this.actualiza();
    }

    private void actualizaTotal()
    {
        final TextView LBL_TOTAL = this.findViewById( R.id.lblTotal );

        LBL_TOTAL.setText( String.format( "Total: %4.2f€",
                                this.taller.getTotalServicios() ));
    }

    private void actualizaLista()
    {
        final ListView LV_SERVICIOS = this.findViewById( R.id.lvServicios );

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(
            this,
            android.R.layout.simple_list_item_1,
            this.taller.getServiciosAsString()
        );

        LV_SERVICIOS.setAdapter( adaptador );
    }

    private void actualiza()
    {
        this.actualizaLista();
        this.actualizaTotal();
    }

    private void inserta()
    {
        final AlertDialog.Builder DLG = new AlertDialog.Builder( this );

        DLG.setMultiChoiceItems(
                Taller.SERVICIOS,
                this.taller.getServicios(),
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        MainActivity.this.taller.contrataServicio( which, isChecked );
                    }
                }
        );

        DLG.setPositiveButton("+",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.actualiza();
                    }
                });

        DLG.create().show();
    }

    private Taller taller;
}
