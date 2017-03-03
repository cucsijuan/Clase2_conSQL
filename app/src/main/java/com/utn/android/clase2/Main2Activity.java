package com.utn.android.clase2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.utn.android.clase2.BaseHelper.COL_APELLIDO;
import static com.utn.android.clase2.BaseHelper.COL_DIRECCION;
import static com.utn.android.clase2.BaseHelper.COL_FOTO;
import static com.utn.android.clase2.BaseHelper.COL_ID;
import static com.utn.android.clase2.BaseHelper.COL_NOMBRE;
import static com.utn.android.clase2.BaseHelper.COL_TELEFONO;
import static com.utn.android.clase2.BaseHelper.TABLA_PERSONA;

public class Main2Activity extends AppCompatActivity {

    private static final int REQUEST_CODE_PHOTO = 42342;
    private boolean fotoOk = false;
    private Intent xIntent;
    private TextView nombreView;
    private TextView apellidoView;
    private TextView direccionView;
    private TextView telefonoView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        xIntent= getIntent();
        nombreView = (TextView) findViewById(R.id.nombre);
        apellidoView = (TextView) findViewById(R.id.apellido);
        direccionView = (TextView) findViewById(R.id.direccion);
        telefonoView = (TextView) findViewById(R.id.telefono);

// obtengo parametros de entrada y seteo la vista
        Intent intent = getIntent();
        final long id = intent.getLongExtra("personaId", 0);
        final BaseHelper bh = new BaseHelper(this);
        final String idString =Long.toString(id);
        if(id != 0){
            SQLiteDatabase dbRead = bh.getReadableDatabase();
            Cursor cursor = dbRead.rawQuery("SELECT * FROM " + TABLA_PERSONA + " WHERE _id = ?", new String[]{ idString });
            cursor.moveToFirst();
            nombreView.setText(cursor.getString(cursor.getColumnIndex("nombre")));


            cursor.close();
            dbRead.close();
        }

        findViewById(R.id.guardar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String nombre = nombreView.getText().toString();
                String apellido = apellidoView.getText().toString();
                String direccion = direccionView.getText().toString();
                String telefono = telefonoView.getText().toString();

                if (nombre.length() == 0) {
                    Toast.makeText(Main2Activity.this, "Nombre no puede ser vacio", Toast.LENGTH_LONG).show();
                    return;
                }

                SQLiteDatabase dbWrite = bh.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put(COL_NOMBRE, nombre);
                cv.put(COL_APELLIDO, apellido);
                cv.put(COL_DIRECCION, direccion);
                cv.put(COL_TELEFONO, telefono);
                if(fotoOk)
                    cv.put(COL_FOTO, mCurrentPhotoPath);

                if(id != 0){
                    // Hacemos un update
                    dbWrite.update(TABLA_PERSONA, cv, "_id = ?", new String[]{Long.toString(id)});
                }else{
                    // Hacemos un insert
                    dbWrite.insert(TABLA_PERSONA, null, cv);
                }

                dbWrite.close();

                Main2Activity.this.setResult(RESULT_OK);

                Main2Activity.this.finish();

            }
        });


        findViewById(R.id.avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Toast.makeText(Main2Activity.this, "Error sacando foto", Toast.LENGTH_LONG).show();
                    return;
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(Main2Activity.this,
                            "com.utn.android.clase2",
                            photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent, REQUEST_CODE_PHOTO);
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_PHOTO && resultCode == RESULT_OK) {

            /*
            // Si en el intent usamos intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);, entonces no podemos buscar el thumb.
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
*/
            Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            ImageView avatar = (ImageView) findViewById(R.id.avatar);

            avatar.setImageBitmap(imageBitmap);
            fotoOk = true;
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main2 Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
