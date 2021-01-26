package com.example.traveljournal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class NavigationMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView mRecyclerViewTrips;
    private FloatingActionButton mFab;
    private Trip mTrips;
    private ArrayList<Trip> mTripsArray = new ArrayList<>();

    private TextView mNavEmail,mNavName;

    private FirebaseFirestore mFirestore;
    private Query mQuery;

    TripAdapter tripAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        initView();
        getTrips();
        setLayoutManager();
        setAdapter();
        setRecyclerViewListener();

        //-----NAVI MENU
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //ActionBarDrawerToggle ofera o modalitate utila de a lega functionalitate
        //DrawerLayout si cadrul ActionBar pt a implementa designul recomandat pentru navigation drawers
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*mNavEmail=findViewById(R.id.nav_email);
        Log.e("EEEEEEEEEEE",mNavEmail.getText().toString());
        mNavName=findViewById(R.id.nav_name);*/

        Log.e("DDDDDDDDDDDDDDDDDD",FirebaseAuth.getInstance().getCurrentUser().getEmail());
        //--------------
    }

    //------NAVI MENU
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        //mNavEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this,NavigationMenuActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_favorites) {
            Intent intent = new Intent(this,NavigationMenuActivity.class);
            intent.putExtra("FAV",1);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //am creat lista pe care user ul o populeaza, iar valorile sunt salvate in db
    private void getTrips() {
        mFirestore = FirebaseFirestore.getInstance();

        CollectionReference tripsCollectionRef = mFirestore.collection("trips");

        mQuery=tripsCollectionRef.whereEqualTo("mUserId",FirebaseAuth.getInstance().getCurrentUser().getUid());

        Bundle extras =getIntent().getExtras();
        int x=0;
        if(extras!=null)
            x=extras.getInt("FAV");
        if(x!=0)
            mQuery=tripsCollectionRef.whereEqualTo("mUserId",FirebaseAuth.getInstance().getCurrentUser().getUid()).whereEqualTo("favorited",true);

        mQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Trip tripp = document.toObject(Trip.class);
                        mTripsArray.add(tripp);
                        Log.e("Baza de date", "s-a incarcat");
                    }

                    tripAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(NavigationMenuActivity.this, "Querry Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //adapter care converteste setul de date in randuri de ui pe ecran
    private void setAdapter() {

        tripAdapter = new TripAdapter(mTripsArray,this);
        mRecyclerViewTrips.setAdapter(tripAdapter);
    }

    private void initView() {
        mRecyclerViewTrips=findViewById(R.id.recyclerview_trips);
    }

    //assignarea layoutManagerului catre recyclerview
    private void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(this);
        mRecyclerViewTrips.setLayoutManager(layoutManager);
        mFab=findViewById(R.id.fab);
    }

    public void onFabClick(View view) {
        Intent myIntent = new Intent(this, ManageTripActivity.class);
        startActivity(myIntent);
        finish();
    }

    //----OnClick si OnLongClick
    private void setRecyclerViewListener() {
        mRecyclerViewTrips.addOnItemTouchListener(new NavigationTouchListener(this,
                mRecyclerViewTrips, new NavigationClickListener() {
            @Override
            public void onClick(View view, final int position) {
                /*Toast.makeText(NavigationMenuActivity.this, getString(R.string.single_click) + position,
                        Toast.LENGTH_SHORT).show();*/
                if(mTripsArray.get(position).isFavorited()==true){
                    mTripsArray.get(position).setFavorited(false);
                }
                else{
                    mTripsArray.get(position).setFavorited(true);
                }

                mFirestore=FirebaseFirestore.getInstance();
                DocumentReference Ref = mFirestore.collection("trips").document(mTripsArray.get(position).getmDocId());
                Ref.update("favorited",mTripsArray.get(position).isFavorited());

            }

            @Override
            public void onLongClick(View view, int position) {
                /*Toast.makeText(NavigationMenuActivity.this, getString(R.string.long_click) + position,
                        Toast.LENGTH_LONG).show();*/

                Intent intent = new Intent(NavigationMenuActivity.this,EditTripsActivity.class);
                intent.putExtra("ID",mTripsArray.get(position).getmDocId());
                startActivity(intent);
                finish();
            }
        }));
    }
}
