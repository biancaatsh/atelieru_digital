package com.example.traveljournal;

public class Trip {

    //atribitule pentru trip
    private String mName;
    private String mDestination;
    private String mRating;
    private String mPhoto;
    private String mStartDate;
    private String mEndDate;
    private String mDocId;
    private String mUserId;
    private boolean isFavorited;
    private boolean fromGallery;


    public Trip() {
    }
    //facem constructor
    public Trip(String mName, String mDestination, String mRating, String mPhoto, String mStartDate, String mEndDate, boolean isFavorited, boolean fromGallery) {
        this.mName = mName;
        this.mDestination = mDestination;
        this.mRating = mRating;
        this.mPhoto = mPhoto;
        this.mStartDate = mStartDate;
        this.mEndDate = mEndDate;
        this.isFavorited = isFavorited;
        this.fromGallery = fromGallery;
    }
     //getterii si seterii
    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmDocId() {
        return mDocId;
    }

    public void setmDocId(String mDocId) {
        this.mDocId = mDocId;
    }

    public String getmName() {
        return mName;
    }

    public String getmDestination() {
        return mDestination;
    }

    public String getmRating() {
        return mRating;
    }

    public String getmPhoto() {
        return mPhoto;
    }

    public String getmStartDate() {
        return mStartDate;
    }

    public String getmEndDate() {
        return mEndDate;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmDestination(String mDestination) {
        this.mDestination = mDestination;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public void setmStartDate(String mStartDate) {
        this.mStartDate = mStartDate;
    }

    public boolean isFromGallery() {
        return fromGallery;
    }

    public void setFromGallery(boolean fromGallery) {
        this.fromGallery = fromGallery;
    }

    public void setmEndDate(String mEndDate) {
        this.mEndDate = mEndDate;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }
}

