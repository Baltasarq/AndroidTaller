package com.example.taller.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
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
        final ListView LV_SERVICIOS = this.findViewById( R.id.lvServicios );

        BT_INSERTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.inserta();
            }
        });
        this.registerForContextMenu( LV_SERVICIOS );

        this.actualiza();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        // Guardar los servicios del taller
        SharedPreferences prefs = this.getPreferences( MODE_PRIVATE );
        SharedPreferences.Editor prefsEditor = prefs.edit();
        final boolean[] SERVICIOS = this.taller.getServicios();
        final StringBuilder STR_SERVICIOS = new StringBuilder();

        // Crear texto con servicios: "0 1" ó "0 2", "0 1 2"
        for(int i = 0; i < SERVICIOS.length; ++i) {
            if ( SERVICIOS[ i ] ) {
                STR_SERVICIOS.append( i );
                STR_SERVICIOS.append( ' ' );
            }
        }

        /*prefsEditor.putStringSet( "servicios",
                new HashSet<String>( Arrays.asList(
                                        this.taller.getServiciosAsString() ) ) );
         */
        prefsEditor.putString( "servicios", STR_SERVICIOS.toString().trim() );
        prefsEditor.apply();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // Recuperar los servicios del taller
        this.taller.eliminaServicios();

        final SharedPreferences PREFS = this.getPreferences( MODE_PRIVATE );
        final String SERVICIOS = PREFS.getString( "servicios", "" );
        final String[] STR_SERVICIOS = SERVICIOS.split( " " );

        if ( STR_SERVICIOS.length == 1
          && STR_SERVICIOS[ 0 ].equals( "" ) )
        {
            this.taller.contrataServicio( 0, true );
        } else {
            for(String str_servicio: STR_SERVICIOS) {
                if ( !str_servicio.isEmpty() ) {
                    int indice_servicio = Integer.parseInt( str_servicio );
                    this.taller.contrataServicio( indice_servicio, true );
                }
            }
        }

        this.actualiza();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu( menu );

        this.getMenuInflater().inflate( R.menu.menu_ppal, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        super.onOptionsItemSelected( item );

        boolean toret = false;

        switch ( item.getItemId() ) {
            case R.id.op_modificar_servicios:
                this.inserta();
                toret = true;
                break;
            case R.id.op_salir:
                this.finish();
                toret = true;
                break;
            default:
                //Toast.makeText( this, "Option item???", Toast.LENGTH_LONG ).show();
                throw new Error( "Option item???" );
                //break;
        }

        return toret;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu( menu, v, menuInfo );

        if ( v.getId() == R.id.lvServicios ) {
            this.getMenuInflater().inflate( R.menu.cntxt_lista, menu );
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item)
    {
        super.onContextItemSelected( item );

        boolean toret = false;

        if ( item.getItemId() == R.id.op_cntxt_modificar_servicios) {
            toret = true;
            this.inserta();
        }

        return toret;
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
