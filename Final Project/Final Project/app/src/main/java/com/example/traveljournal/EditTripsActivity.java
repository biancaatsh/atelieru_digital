package com.example.traveljournal;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Messenger;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.Ref;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
//pagina principala in care pot adauga o vacanta cu atributele ei
public class EditTripsActivity extends AppCompatActivity {
//declar variabilele pe care vreau sa le utilizez in activity_manage_trips.xml
    public static final String NAME = "name";
    public static final String DESTINATION = "destination";
    public static final String RATING = "rating";
    public static final String PHOTO = "photo";

    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_PERMISSION = 200;
    private String imageFilePath = "";

    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_CODE = 1;
    private Bitmap mBitmap;
    private boolean isgallery;
    private boolean isfav;
    Context mContext;
    private Trip tripp;
    private String ID;

    EditText mName, mDestiantion, mRating, mPhoto;
    RatingBar rBar;
    ImageView mImageView;
    Button mbtntakephoto, msetstartdate, msetenddate, msave;

    //declar instanta lui FirebaseFirestore

    private FirebaseFirestore mFirestore;
    private DownloadManager.Query mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_trip);
        mContext=this;

        //-----TAKE PHOTO

        // cer permisiunea utilizatorului pentru a putea face fotografia
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }
        //---------------
        initView();
        initFirestore();

        //-----TAKE PHOTO

        //butonul pentru a face fotografia
        mbtntakephoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // Standard Intent action that can be sent to have the camera application capture an image and return it.
                //resolveActivity (getPackageManager()) deschid o adresa url (uri) in aplicatia mea
                if (pictureIntent.resolveActivity(getPackageManager()) != null) {

                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    Uri photoUri = FileProvider
                            .getUriForFile(EditTripsActivity.this, getPackageName() + ".provider", photoFile);
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(pictureIntent, REQUEST_IMAGE);
                }

            }
        });
        //-----TAKE PHOTO
        //butonul pentru a salva vacanta
        msave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Trip mTrip = new Trip();
                mTrip.setmName(mName.getText().toString());
                mTrip.setmDestination(mDestiantion.getText().toString());
                int noofstars = rBar.getNumStars();
                float getrating = rBar.getRating();
                //mRating.setText(getrating+"/"+noofstars);
                String str = getrating + "/" + noofstars;
                mTrip.setmRating(str);
                mTrip.setFavorited(isfav);
                mTrip.setmPhoto(imageFilePath);
                mTrip.setmStartDate(msetstartdate.getText().toString());
                mTrip.setmEndDate(msetenddate.getText().toString());
                mTrip.setFromGallery(isgallery);

                FirebaseFirestore db=FirebaseFirestore.getInstance();
                CollectionReference trips = db.collection("trips");
                DocumentReference ref = trips.document(ID);
                mTrip.setmDocId(ID);

                //trips.add(mTrip);

                /*ref.set(mTrip).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(mContext, "Successfully Added", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(mContext, "FAILED BRUH", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

                //update la campurile calatoriei
                ref.update("mName",mTrip.getmName());
                ref.update("mDestination",mTrip.getmDestination());
                ref.update("mRating",mTrip.getmRating());
                ref.update("mStartDate",mTrip.getmStartDate());
                ref.update("mEndDate",mTrip.getmEndDate());
                ref.update("mPhoto",mTrip.getmPhoto());
                ref.update("fromGallery",mTrip.isFromGallery());
                ref.update("favorited",mTrip.isFavorited());

                Intent myIntent = new Intent(mContext,NavigationMenuActivity.class);
                startActivity(myIntent);

                finish();
            }
        });
    }

    private void initView() {
        mName = findViewById(R.id.editTextTripName);
        mDestiantion = findViewById(R.id.editTextDestination);
        rBar = findViewById(R.id.ratingBar);
        mImageView = findViewById(R.id.imagePreview);
        mbtntakephoto = findViewById(R.id.take_photo_camera);
        msetstartdate = findViewById(R.id.start_date);
        msetenddate = findViewById(R.id.end_date);
        msave = findViewById(R.id.save);

        Bundle extras = getIntent().getExtras();

        ID = extras.getString("ID");

        mFirestore = FirebaseFirestore.getInstance();

        CollectionReference tripsCollectionRef = mFirestore.collection("trips");

        Query mQuery=tripsCollectionRef.whereEqualTo("mDocId",ID);

        mQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        tripp = document.toObject(Trip.class);

                        mName.setText(tripp.getmName());
                        mDestiantion.setText(tripp.getmDestination());
                        String s= tripp.getmRating().substring(0,3);
                        Log.e("RATINGGGG",s);
                        float f=Float.valueOf(s).floatValue();
                        rBar.setRating(f);
                        msetenddate.setText(tripp.getmEndDate());
                        msetstartdate.setText(tripp.getmStartDate());
                        imageFilePath=tripp.getmPhoto();
                        isfav=tripp.isFavorited();

                        if(!tripp.getmPhoto().isEmpty()){
                            Uri k = Uri.parse(tripp.getmPhoto());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && tripp.isFromGallery()==true) {
                                mContext.getContentResolver().takePersistableUriPermission(k, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            }

                            mImageView.setImageURI(k);
                        }
                        else {
                            mImageView.setImageResource(R.drawable.rome);
                        }

                    }
                }
                else{
                    Toast.makeText(EditTripsActivity.this, "Querry Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });




        //-----GALLERY PHOTO
       /* if (checkPermission()) {
            //main logic or main code
            // . write your main code to execute, It will execute if the permission is already given.
        } else {
            requestPermission();
        }*/
        //-----GALLERY PHOTO

    }

    //-----GALLERY PHOTO
    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }
    //-----GALLERY PHOTO

    //dupa ce utilizatorul a raspuns dialog box ului, sistemul o sa invoce
    //metoda onRequestPermissionsResult
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //-----TAKE PHOTO
        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
        //-----TAKE PHOTO

        //-----GALLERY PHOTO

        //daca cererea este anulata, rezultatul va fi un array gol
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                    // ii explic utilizatorului ca acea caracteristica nu este disponibila
                    //deoarece functiile necestia o permisiune pe care a refuzat-o.
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }
    //-----GALLERY PHOTO


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //-----TAKE PHOTO
        // afisez imaginea facuta cu camera stiind calea absoluta a imaginii
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mImageView.setImageURI(Uri.parse(imageFilePath));
                isgallery=false;
                //mImageView.setImageResource(R.drawable.rome);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        }
        //-----TAKE PHOTO

        //-----GALLERY PHOTO
        /*InputStream stream = null;
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            try {
                // recyle unused bitmaps
                if (mBitmap != null) {
                    mBitmap.recycle();
                }
                stream = getContentResolver().openInputStream(data.getData());
                mBitmap = BitmapFactory.decodeStream(stream);

                mImageView.setImageBitmap(mBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                {
                    if (stream != null)
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }*/

        //afisez imaginea din galerie
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            android.net.Uri k=data.getData();
            imageFilePath=k.toString();
            Log.e("GALERIEEEEEEEE",imageFilePath);
            mImageView.setImageURI(Uri.parse(imageFilePath));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                this.getContentResolver().takePersistableUriPermission(k, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            isgallery=true;

        }
        //-----GALLERY PHOTO
    }

    //-----GALLERY PHOTO
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(EditTripsActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    //-----GALLERY PHOTO

    //-----TAKE PHOTO

    private File createImageFile() throws IOException {
        //create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir); //prefix, sufix, director

        //save file, path for use with ACTION_VIEW intents
        imageFilePath = image.getAbsolutePath();

        return image;
    }
    //-----TAKE PHOTO

    //-----GALLERY PHOTO
    //incarc fotografia aleasa de user din galerie
    public void loadPictureFromGallery(View view) {
       /*Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_CODE);*/

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE);
    }
    //-----GALLERY PHOTO

   /* public void sendInfoToNavigation(View view) {
        Trip mTrip=new Trip();
        mTrip.setmName(mName.getText().toString());
        mTrip.setmDestination(mDestiantion.getText().toString());
        int noofstars = rBar.getNumStars();
        float getrating = rBar.getRating();
        mRating.setText(getrating+"/"+noofstars);
        mTrip.setmRating(mRating.getText().toString());
        mTrip.setFavorited(false);

        CollectionReference trips = mFirestore.collection("trips");
        trips.add(mTrip);

    }*/
// afisez si aleg data pentru primul date box
    public void displayDatePickOnClick(View view) {
        // set today as default date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // display the date picker dialog
        DialogFragment datePickerFragment = new DatePickerFragment();
        ((DatePickerFragment) datePickerFragment).setYear(year);
        ((DatePickerFragment) datePickerFragment).setMonth(month);
        ((DatePickerFragment) datePickerFragment).setDay(day);
        datePickerFragment.show(getSupportFragmentManager(), "DatePicker");
        msetstartdate.setText("DD/MM/YY");

    }
//afisez si aleg data pentru al doilea date box
    public void displayDatePickOnClick2(View view) {

        if(msetstartdate.getText().toString().equals("DD/MM/YY")==false){
            // set today as default date
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // display the date picker dialog
            DialogFragment datePickerFragment = new DatePickerFragment();
            ((DatePickerFragment) datePickerFragment).setYear(year);
            ((DatePickerFragment) datePickerFragment).setMonth(month);
            ((DatePickerFragment) datePickerFragment).setDay(day);
            datePickerFragment.show(getSupportFragmentManager(), "DatePicker");

            msetenddate.setText("DD/MM/YY");
        }
        else{
            Toast.makeText(mContext, "Please Select Start Date", Toast.LENGTH_SHORT).show();
        }

    }
//initializez Firebase Firestore
    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

}
