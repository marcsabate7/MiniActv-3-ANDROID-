package com.marc.miniactivity3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PHONE_CALL = 100;
    private static final int REQUEST_EXTERNAL_STORAGE = 101;
    private static final int REQUEST_CONTACTS_ACCESS = 102;
    private static final int REQUEST_IMG = 10;
    private static final int REQUEST_CONTACTS = 11;
    private static final int REQ_PICK = 12;

    ImageView image_gallery;
    TextView text_contacts;
    TextView textpick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Event listenners declaration to set event Click
        Button url = findViewById(R.id.buttonurl);
        Button google = findViewById(R.id.buttongoogle);
        Button llamar = findViewById(R.id.buttonllamar);
        Button marcar = findViewById(R.id.buttonmarcar);
        Button sms = findViewById(R.id.buttonsms);
        Button email = findViewById(R.id.buttonemail);
        //Button images = findViewById(R.id.buttonimages);
        Button images2 = findViewById(R.id.buttonimages2);
        Button contacts = findViewById(R.id.buttoncontacts);
        image_gallery = findViewById(R.id.imageGalery);
        text_contacts = findViewById(R.id.textcontacts);
        Button pick = findViewById(R.id.buttonpick);
        textpick = findViewById(R.id.textpick);

        url.setOnClickListener(this);
        google.setOnClickListener(this);
        llamar.setOnClickListener(this);
        marcar.setOnClickListener(this);
        sms.setOnClickListener(this);
        email.setOnClickListener(this);
        //images.setOnClickListener(this);
        images2.setOnClickListener(this);
        contacts.setOnClickListener(this);
        pick.setOnClickListener(this);

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        String data = "Escola politecnica superior Udl";
        String url_udl = "http://www.udl.es/ca/";
        String text_sms = "Aquest es el missatge del sms";
        String correu = "marc-saba@hotmail.com";
        String text_correu = "Aquest es el missatge pel correu --> Bondia a tots i a totes";
        switch (v.getId()) {
            case R.id.buttonurl:
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url_udl));
                startActivity(intent);
                break;
            case R.id.buttongoogle:
                Intent intent2 = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com/search?q="+data));
                startActivity(intent2);
                break;
            case R.id.buttonmarcar:
                Intent intent3 = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:666666666"));
                startActivity(intent3);
                break;
            case R.id.buttonllamar:
                permissionPhone();
                break;
            case R.id.buttonsms:
                Intent intentSMS = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:666666"));
                intentSMS.putExtra("sms_body", text_sms);
                startActivity(intentSMS);
                break;
            case R.id.buttonemail:
                Intent emailIntent = new Intent(Intent.ACTION_SEND,Uri.parse("mailto:"));
                // Utilitzant el seguent array podrem incloure tants correus com vulguem, cosa que ens pot ser util en algunes ocasions
                String[] TO = {correu};
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO); // * configurar email aquí!
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Aquest es el subject del correu");
                emailIntent.putExtra(Intent.EXTRA_TEXT, text_correu);
                startActivity(emailIntent);
                break;
                // First button from images only to view --> deleted on xml layout and implemented a one that is more complex, behind
            /*case R.id.buttonimages:
                Intent images = new Intent();
                images.setAction(android.content.Intent.ACTION_VIEW);
                images.setType("image/*");
                // Podem utilitzar el següent flag per iniciar la galeria de imatges en una nova 'pestanya' / tarea i no ferho des de la mateixa aplicació
                //images.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(images);
                break;*/
            case R.id.buttonimages2:
                permissionImages();
                break;
            case R.id.buttoncontacts:
                permissionContacts();
                break;
            case R.id.buttonpick:
                Intent intent5 = new Intent(Intent.ACTION_PICK);
                intent5.setType("text/plain");
                startActivityForResult(intent5,REQ_PICK);
                break;

        }
    }
    // Different permissions
    private void permissionContacts() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CONTACTS_ACCESS);
            }
            else {
                Toast.makeText(MainActivity.this, "You already have permissions to access contacts!", Toast.LENGTH_SHORT).show();
                agafar_contacts();
            }
        }else{  // System less than marshmallow
            Toast.makeText(MainActivity.this, "You already have permissions to access contacts!", Toast.LENGTH_SHORT).show();
            agafar_contacts();
        }
    }

    private void agafar_contacts() {
        Intent contacts = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        contacts.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(contacts, REQUEST_CONTACTS);
    }


    private void permissionImages() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
            }
            else {
                Toast.makeText(MainActivity.this, "You already have permissions to acces Gallery!", Toast.LENGTH_SHORT).show();
                agafar_imatge();
            }
        }else{  // System less than marshmallow
            Toast.makeText(MainActivity.this, "You already have permissions to access Gallery!", Toast.LENGTH_SHORT).show();
            agafar_imatge();
        }
    }

    private void agafar_imatge() {
        Intent images2 = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        images2.setType("image/*");
        startActivityForResult(images2, REQUEST_IMG);
    }

    private void permissionPhone() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
            } else {
                Toast.makeText(MainActivity.this, "You already have permissions to access phone!", Toast.LENGTH_SHORT).show();
                realitzarTrucada();
            }
        }else{  // System less than marshmallow
            Toast.makeText(MainActivity.this, "You already have permissions to access phone!", Toast.LENGTH_SHORT).show();
            realitzarTrucada();
        }
    }

    private void realitzarTrucada() {
        Intent intent4 = new Intent(Intent.ACTION_CALL,Uri.parse("tel:666666666"));
        startActivity(intent4);
    }
    // Permission results to tract
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_PHONE_CALL:{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    realitzarTrucada();
                }else{
                    Toast.makeText(MainActivity.this, "Permission denied...!", Toast.LENGTH_LONG).show();
                }
            }
            case REQUEST_EXTERNAL_STORAGE:{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    agafar_imatge();
                }else{
                    Toast.makeText(MainActivity.this, "Permission denied...!", Toast.LENGTH_LONG).show();
                }
            }
            case REQUEST_CONTACTS_ACCESS:{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    agafar_contacts();
                }else{
                    Toast.makeText(MainActivity.this, "Permission denied...!", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    // Tracting the result of the intents
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_IMG){
            image_gallery.setImageURI(data.getData());
        }
        if(resultCode == RESULT_OK && requestCode == REQUEST_CONTACTS){
            Uri uri = data.getData();
            Cursor c = getContentResolver().query(uri,null,null,null,null);
            if (c.moveToFirst()) {
                int name = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String nom = c.getString(name);
                text_contacts.setText(nom);
            }
        }
        if(resultCode == RESULT_OK && requestCode == REQUEST_CONTACTS){
            Uri uri = data.getData();
            Cursor c = getContentResolver().query(uri,null,null,null,null);
            if (c.moveToFirst()) {
                int name = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String nom = c.getString(name);
                text_contacts.setText(nom);
            }
        }
        if(resultCode == RESULT_OK && requestCode == REQ_PICK){
            String result = data.getStringExtra("messageReturn");
            textpick.setText(result);
        }

    }

}