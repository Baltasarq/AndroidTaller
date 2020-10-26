package com.example.taller.core;

import java.util.ArrayList;
import java.util.Arrays;


public class Taller {
    public static final String[] SERVICIOS = new String[] {
        "Revisión del motor",
            "Cambio de aceite",
            "Equilibrado de ruedas"
    };

    public static final double[] COSTES = new double[] {
        9.99,
        30,
        4.99
    };

    public Taller()
    {
        this.serviciosContratados = new boolean[ SERVICIOS.length ];
        this.serviciosContratados[ 0 ] = true;
    }

    public boolean[] getServicios()
    {
        //return this.serviciosContratados;
        return Arrays.copyOf( this.serviciosContratados,
                              this.serviciosContratados.length );
    }

    public double getTotalServicios()
    {
        double toret = 0f;

        for(int i = 0; i < COSTES.length; ++i) {
            if ( this.serviciosContratados[ i ] ) {
                toret += COSTES[ i ];
            }
        }

        return toret;
    }

    public String[] getServiciosAsString()
    {
        ArrayList<String> toret = new ArrayList<>();

        for(int i = 0; i < SERVICIOS.length; ++i) {
            if ( this.serviciosContratados[ i ] ) {
                toret.add( SERVICIOS[ i ] );
            }
        }

        return toret.toArray( new String[ 0 ] );
    }

    public void contrataServicio(int i, boolean value)
    {
        this.serviciosContratados[ i ] = value;
    }

    public void eliminaServicios()
    {
        /*for(int i = 0; i < this.serviciosContratados.length; ++i) {
            this.serviciosContratados[ i ] = false;
        }
        */

        Arrays.fill( this.serviciosContratados, false );
    }

    private boolean[] serviciosContratados;
}
