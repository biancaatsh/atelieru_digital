package com.example.traveljournal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
//converteste datele mele in view uri
public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder>{

    private ArrayList<Trip> mTripsArray = new ArrayList<>();
    private Context mContext;

    private static final String TAG = "TripAdapter";

     // am creat un constructor care are ca si parametru un arraylist cu trip urile mele
    //salvez trip urile intr-o stare interna a obiectului meu

        public TripAdapter(ArrayList<Trip> mTripArray, Context mContext){
        this.mTripsArray= mTripArray;
        this.mContext = mContext;

        Log.e(TAG,mTripArray.size()+"");
    }

    @NonNull
    @Override
    //aceasta metoda apeleaza onCreateViewHolder pt a crea un nou ViewHolder pt o anumita pozitie
    //initializeaza campurile private pt a putea fi utilizate de RecyclerView
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            // pentru a exapanda din xml = inflate.
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trip_item,viewGroup,false);
        ViewHolder TripViewHolder = new ViewHolder(view);
        return TripViewHolder;
    }


    //aceasta metoda apeleaza onBindViewHolder
    // pt a actualiza continutul RecyclerView.ViewHolder cu elementul in pozitia data

    //populesc ui ul cu datele mele despre trips
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder tripViewHolder, final int i) {

            if(!mTripsArray.get(i).getmPhoto().isEmpty()){
                Uri k = Uri.parse(mTripsArray.get(i).getmPhoto());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mTripsArray.get(i).isFromGallery()==true) {
                    mContext.getContentResolver().takePersistableUriPermission(k, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                tripViewHolder.mTripImage.setImageURI(k);
            }
            else {
                tripViewHolder.mTripImage.setImageResource(R.drawable.rome);
            }


          //tripViewHolder.mTripImage.setImageURI(Uri.parse(mTripsArray.get(1).getmPhoto()));
        Log.e(TAG,mTripsArray.get(0).getmPhoto());
          tripViewHolder.mTripName.setText(mTripsArray.get(i).getmName());
          tripViewHolder.mTripDestination.setText(mTripsArray.get(i).getmDestination());
          tripViewHolder.mTripRating.setText(mTripsArray.get(i).getmRating());
        if(mTripsArray.get(i).isFavorited()==true){
            tripViewHolder.mCardView.setCardBackgroundColor(0xff2ecc71);
        }
          //Cand apas pe el sa se faca background-ul alb
          tripViewHolder.mParentLayout.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View v) {
                  /*if(tripViewHolder.mCardView.getCardBackgroundColor().getDefaultColor()==0xffE8E8E8)
                  {
                      tripViewHolder.mCardView.setCardBackgroundColor(0xff2ecc71);
                  }
                  else
                  {
                      tripViewHolder.mCardView.setCardBackgroundColor(0xffE8E8E8);
                  }*/
                  if(mTripsArray.get(i).isFavorited()==true){
                      tripViewHolder.mCardView.setCardBackgroundColor(0xff2ecc71);
                  }
                  else{
                      tripViewHolder.mCardView.setCardBackgroundColor(0xffE8E8E8);
                  }
              }
          });
    }

    //returneaza nr de elementele pe care doresc sa le pun in lista
    @Override
    public int getItemCount() {
        return mTripsArray.size();
    }


    //folosit de Adapter, ViewHolder-ul este utilizat pentru a ne creea propriile componente
    //A ViewHolder describes an item view and metadata about its place within the RecyclerView.
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mTripImage;
        TextView mTripName,mTripDestination,mTripRating;
        RelativeLayout mParentLayout;
        CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView); //implementarea din clasa de baza
            //ViewHolder ne tine referinta la view urile randurilor noastre

            //salvez view urile din pagina mea
            mTripImage=itemView.findViewById(R.id.trip_image);
            mTripName=itemView.findViewById(R.id.trip_name);
            mTripDestination=itemView.findViewById(R.id.trip_destination);
            mTripRating=itemView.findViewById(R.id.trip_rating);
            mParentLayout=itemView.findViewById(R.id.parent_layout_trip_item);
            mCardView = itemView.findViewById(R.id.cardview);
        }
    }
}
