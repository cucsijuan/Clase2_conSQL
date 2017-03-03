package com.utn.android.clase2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.utn.android.clase2.BaseHelper.COL_APELLIDO;
import static com.utn.android.clase2.BaseHelper.COL_DIRECCION;
import static com.utn.android.clase2.BaseHelper.COL_FOTO;
import static com.utn.android.clase2.BaseHelper.COL_ID;
import static com.utn.android.clase2.BaseHelper.COL_NOMBRE;
import static com.utn.android.clase2.BaseHelper.COL_TELEFONO;
import static com.utn.android.clase2.BaseHelper.TABLA_PERSONA;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Persona> personas = new ArrayList<Persona>();
    private ListView lista;
    private BaseHelper dbHelper;
    private CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new BaseHelper(this);

        // recupero datos de la base de datos
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        Cursor cursor = dbWrite.rawQuery("SELECT * FROM " + BaseHelper.TABLA_PERSONA + " ORDER BY nombre ASC", null);

        customAdapter = new CustomAdapter(this, cursor);
        lista = (ListView)findViewById(R.id.lista);
        lista.setAdapter( customAdapter);

        // agregar datos
        findViewById(R.id.agregar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivityForResult(intent, 85);

            }
        });

        // modificar datos
        lista.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("personaId", id);

                startActivityForResult(intent,85);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 85){
            if(resultCode == Activity.RESULT_OK){
                customAdapter.notifyDataSetChanged();
            }
        }
    }

    public class CustomAdapter extends CursorAdapter{

        public CustomAdapter(Context context, Cursor cursor){
            super(context, cursor, true);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater li = getLayoutInflater();
            View newView = li.inflate(R.layout.row_persona,parent, false);

            String nombre = cursor.getString(cursor.getColumnIndex(COL_NOMBRE));
            String apellido = cursor.getString(cursor.getColumnIndex(COL_APELLIDO));
            String telefono = cursor.getString(cursor.getColumnIndex(COL_TELEFONO));
            String direccion = cursor.getString(cursor.getColumnIndex(COL_DIRECCION));
            String foto = cursor.getString(cursor.getColumnIndex(COL_FOTO));

            TextView nombreView = (TextView) newView.findViewById(R.id.nombre_de_persona);
            TextView telefonoView = (TextView) newView.findViewById(R.id.telefono);
            ImageView avatar = (ImageView) newView.findViewById(R.id.foto);

            nombreView.setText(nombre);
            if(foto != null) {
                avatar.setImageBitmap(BitmapFactory.decodeFile(foto));
            }
            return newView;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String nombre = cursor.getString(cursor.getColumnIndex(COL_NOMBRE));
            String apellido = cursor.getString(cursor.getColumnIndex(COL_APELLIDO));
            String telefono = cursor.getString(cursor.getColumnIndex(COL_TELEFONO));
            String direccion = cursor.getString(cursor.getColumnIndex(COL_DIRECCION));
            String foto = cursor.getString(cursor.getColumnIndex(COL_FOTO));

            TextView nombreView = (TextView) view.findViewById(R.id.nombre_de_persona);
            TextView telefonoView = (TextView) view.findViewById(R.id.telefono);
            ImageView avatar = (ImageView) view.findViewById(R.id.foto);

            nombreView.setText(nombre);
            if(foto != null) {
                avatar.setImageBitmap(BitmapFactory.decodeFile(foto));
            }else {
                avatar.setImageResource(R.mipmap.ic_launcher);
            }

        }
    }
}
