package com.example.androidengine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.view.PixelCopy;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;

/**
 * AndroidShare implementa el compartir.
 */
public class AndroidShare {

    // Actividad principal.
    private static Activity a;
    // SurfaceView.
    private static SurfaceView s;

    /**
     * CONSTRUCTORA.
     * @param a
     * @param s
     */
    public AndroidShare(Activity a, SurfaceView s){
        this.a = a;
        this.s = s;
    }

    /**
     * Método que captura pantalla.
     */
    public static void screenshot(){
        // Comprobamos que la versión tiene soporte para capturas de pantalla (PixelCopy).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            // Creamos el Bitmap.
            Bitmap bitmap = Bitmap.createBitmap(s.getWidth(), s.getHeight(), Bitmap.Config.ARGB_8888);

            // Creamos una hebra para evitar bloquear el hilo principal.
            HandlerThread handlerThread = new HandlerThread("PixelCopier");
            handlerThread.start();

            // Hacemos la captura de pantalla y la guardamos en bitmap.
            PixelCopy.request(s, bitmap, (copyResult) -> {
                if (copyResult == PixelCopy.SUCCESS){
                    // Compartimos la imagen.
                    shareImage(bitmap);
                }
            }, new Handler(handlerThread.getLooper()));
        }
    }

    /**
     * Método que comparte la captura de pantalla.
     * @param bitmap
     */
    public static void shareImage(Bitmap bitmap){
        Context context = a.getApplicationContext();

        // Creamos el intent.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Creamos lo necesario para visualizar la captura de pantalla.
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);

        // Configuramos el intent.
        share.putExtra(Intent.EXTRA_TEXT, "¡He completado este nivel de Tower Defense!");
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        share.setType("image/*");

        // Lanzamos el intent.
        a.startActivity(Intent.createChooser(share, "Compartir"));
    }
}
