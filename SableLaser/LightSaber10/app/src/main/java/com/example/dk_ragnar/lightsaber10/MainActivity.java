package com.example.dk_ragnar.lightsaber10;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private static final String TAG = "MainActivity";
    private MediaPlayer sonidoEncendido, sonidoApagado, sonidoFondo, movimiento1, movimiento2, disparoderecha, disparoizquierda;
    SensorManager sm;
    Sensor proximidad, acelerometro;
    TextView informe;
    RelativeLayout imagenFondo;
    String mostrar;
    private float proximityCalibratedMax = Float.MIN_VALUE;
    Boolean estado=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        informe = (TextView) findViewById(R.id.informe);
        imagenFondo = (RelativeLayout) findViewById(R.id.imagenFondo);
        sonidoEncendido = MediaPlayer.create(this, R.raw.ensendido);
        sonidoApagado = MediaPlayer.create(this, R.raw.apagado);
        sonidoFondo = MediaPlayer.create(this, R.raw.fondo);
        movimiento1 = MediaPlayer.create(this, R.raw.movimiento);
        movimiento2 = MediaPlayer.create(this, R.raw.movimientoo);
        disparoderecha = MediaPlayer.create( this, R.raw.disparoderecho);
        disparoizquierda = MediaPlayer.create( this, R.raw.disparoizquierdo);

        sm = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        proximidad = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sm.registerListener(this,proximidad,SensorManager.SENSOR_DELAY_GAME);

        acelerometro = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this,acelerometro,SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;


        String valor="";

        if(event.values[0] > proximityCalibratedMax){
            proximityCalibratedMax = event.values[0];
        }
    switch (sensor.getType()) {
        case Sensor.TYPE_PROXIMITY:
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY && proximityCalibratedMax != Float.MIN_VALUE) {
            valor = event.values[0] + "";
            informe.setText(valor);


            if (valor.equals("8.0")) {
                estado = false;
                sonidoApagado.start();
                imagenFondo.setBackgroundResource(R.drawable.sableapagado);

            }
            if (valor.equals("0.0")) {
                estado = true;
                sonidoEncendido.start();
                imagenFondo.setBackgroundResource(R.drawable.sableencendido);

                Thread timer = new Thread() {
                    public void run() {
                        try {
                            sleep(100);

                        } catch (InterruptedException e) {

                        } finally {
                            sonidoFondo.start();
                        }
                    }
                };
                timer.start();


            }

            if (estado == true) {

            }
            if (estado == false) {
                if (sonidoFondo.isPlaying()) {
                    if (sonidoFondo != null) {
                        sonidoFondo.pause();
                    }
                }
            }
        }
            break;
        case Sensor.TYPE_ACCELEROMETER:
            float z = event.values[2];

            //Log.d(TAG,"UDB"+estado);
            if(estado==true){
                if (z > 10) {
                    //movimiento1.start();
                    disparoizquierda.start();
                }
                if (z < -10) {
                    //movimiento2.start();
                    disparoderecha.start();
                }
            }
            break;
        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        sm.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        sm.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, proximidad, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

}
