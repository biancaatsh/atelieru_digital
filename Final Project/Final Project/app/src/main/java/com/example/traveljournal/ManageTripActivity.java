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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ManageTripActivity extends AppCompatActivity {

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
    Context mContext;

    EditText mName, mDestiantion, mRating, mPhoto;
    RatingBar rBar;
    ImageView mImageView;
    Button mbtntakephoto, msetstartdate, msetenddate, msave;

    private FirebaseFirestore mFirestore;
    private DownloadManager.Query mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_trip);
        mContext=this;

        //-----TAKE PHOTO
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
        mbtntakephoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (pictureIntent.resolveActivity(getPackageManager()) != null) {

                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    Uri photoUri = FileProvider
                            .getUriForFile(ManageTripActivity.this, getPackageName() + ".provider", photoFile);
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(pictureIntent, REQUEST_IMAGE);
                }

            }
        });
        //-----TAKE PHOTO

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
                mTrip.setFavorited(false);
                mTrip.setmPhoto(imageFilePath);
                mTrip.setmStartDate(msetstartdate.getText().toString());
                mTrip.setmEndDate(msetenddate.getText().toString());
                mTrip.setFromGallery(isgallery);

                FirebaseFirestore db=FirebaseFirestore.getInstance();
                CollectionReference trips = db.collection("trips");
                DocumentReference ref = trips.document();
                mTrip.setmDocId(ref.getId());

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                mTrip.setmUserId(uid);

                //trips.add(mTrip);

                ref.set(mTrip).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(mContext, "Successfully Added", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(mContext, "FAILED BRUH", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
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
        new AlertDialog.Builder(ManageTripActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    //-----GALLERY PHOTO

    //-----TAKE PHOTO
    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();

        return image;
    }
    //-----TAKE PHOTO

    //-----GALLERY PHOTO
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

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

}
