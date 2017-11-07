package com.example.dell.instagramlogin.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.example.dell.instagramlogin.model.instamedia.Caption;
import com.example.dell.instagramlogin.model.instamedia.Comments;
import com.example.dell.instagramlogin.model.instamedia.Images;
import com.example.dell.instagramlogin.model.instamedia.Likes;
import com.example.dell.instagramlogin.model.instamedia.ModelInstaMedia;
import com.example.dell.instagramlogin.model.instamedia.MyLocation;
import com.example.dell.instagramlogin.model.instamedia.StandardResolution;
import com.example.dell.instagramlogin.model.instamedia.Thumbnail;
import com.example.dell.instagramlogin.model.instamedia.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 11/4/2017.
 */

public class DBInstagramPostImage extends SQLiteOpenHelper {

    Context context;
    private static String DATABASENAME = "webmobtechno";
    private static int DATABASEVERSION = 1;

    SQLiteDatabase sqLiteDatabase;

    public DBInstagramPostImage(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
        this.context = context;
    }

    public static String TBL_MEDIA = "insta_media";
    public static String MEDIA_ID = "media_id";
    public static String MEDIA_MAINURL = "media_mainurl";
    public static String MEDIA_THUMBURL = "media_thumburl";
    public static String MEDIA_CREATEDTIME = "created_time";
    public static String MEDIA_CAPTION = "caption";
    public static String MEDIA_FROMNAME = "from_name";
    public static String MEDIA_FROMPICTURE = "from_picture";
    public static String MEDIA_LIKECOUNT = "likes_count";
    public static String MEDIA_COMMENTCOUNT = "comments_count";
    public static String MEDIA_TYPE = "media_type";
    public static String MEDIA_LOCATION = "media_location";

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {

            String CREATETABEL_MEDIA = "CREATE TABLE "
                    + TBL_MEDIA + "( _id INTEGER PRIMARY KEY," + MEDIA_ID + " TEXT,"
                    + MEDIA_MAINURL + " TEXT," + MEDIA_THUMBURL + " TEXT,"
                    + MEDIA_CREATEDTIME + " TEXT," + MEDIA_CAPTION + " TEXT,"
                    + MEDIA_FROMNAME + " TEXT," + MEDIA_FROMPICTURE + " TEXT,"
                    + MEDIA_LIKECOUNT + " TEXT," + MEDIA_COMMENTCOUNT + " TEXT,"
                    + MEDIA_TYPE + " TEXT,"
                    + MEDIA_LOCATION + " TEXT" + ")";

            db.execSQL(CREATETABEL_MEDIA);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void reCreateTable() {

        sqLiteDatabase = this.getWritableDatabase();
        try {
            sqLiteDatabase.delete(TBL_MEDIA, null, null);
            sqLiteDatabase.close();
        } catch (Exception e) {
            sqLiteDatabase.close();
            e.printStackTrace();
        } finally {
            sqLiteDatabase.close();
        }

//        SQLiteDatabase sqLiteDatabase1 = getWritableDatabase();
//        String CREATETABEL_MEDIA = "CREATE TABLE "
//                + TBL_MEDIA + "( _id INTEGER PRIMARY KEY," + MEDIA_ID + " TEXT,"
//                + MEDIA_MAINURL + " TEXT," + MEDIA_THUMBURL + " TEXT,"
//                + MEDIA_CREATEDTIME + " TEXT," + MEDIA_CAPTION + " TEXT,"
//                + MEDIA_FROMNAME + " TEXT," + MEDIA_FROMPICTURE + " TEXT,"
//                + MEDIA_LIKECOUNT + " TEXT," + MEDIA_COMMENTCOUNT + " TEXT,"
//                + MEDIA_TYPE + " TEXT,"
//                + MEDIA_LOCATION + " TEXT" + ")";
//        sqLiteDatabase1.execSQL(CREATETABEL_MEDIA);
//        sqLiteDatabase1.close();
    }

    public void saveInstaMedia(List<ModelInstaMedia> modelInstaMedias) {

        reCreateTable();

        sqLiteDatabase = this.getWritableDatabase();

        try {

            String query = "INSERT INTO " + TBL_MEDIA
                    + " (media_id, media_mainurl, media_thumburl, created_time, caption, from_name, from_picture, likes_count, comments_count, media_type, media_location) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            SQLiteStatement insStmt = sqLiteDatabase.compileStatement(query);
            sqLiteDatabase.beginTransaction();
            for (ModelInstaMedia modelInstaMedia : modelInstaMedias) {

                if (modelInstaMedia.getId() != null)
                    insStmt.bindString(1, "" + modelInstaMedia.getId());
                else
                    insStmt.bindString(1, "");


                if (modelInstaMedia.getImages() != null && modelInstaMedia.getImages().getStandardResolution().getUrl() != null)
                    insStmt.bindString(2, "" + modelInstaMedia.getImages().getStandardResolution().getUrl());
                else
                    insStmt.bindString(2, "");

                if (modelInstaMedia.getImages() != null && modelInstaMedia.getImages().getThumbnail().getUrl() != null)
                    insStmt.bindString(3, "" + modelInstaMedia.getImages().getThumbnail().getUrl());
                else
                    insStmt.bindString(3, "");

                if (modelInstaMedia.getCreatedTime() != null)
                    insStmt.bindString(4, "" + modelInstaMedia.getCreatedTime());
                else
                    insStmt.bindString(4, "");

                if (modelInstaMedia.getCaption() != null && modelInstaMedia.getCaption().getText() != null)
                    insStmt.bindString(5, "" + modelInstaMedia.getCaption().getText());
                else
                    insStmt.bindString(5, "");

                if (modelInstaMedia.getUser() != null && modelInstaMedia.getUser().getFullName() != null)
                    insStmt.bindString(6, "" + modelInstaMedia.getUser().getFullName());
                else
                    insStmt.bindString(6, "");

                if (modelInstaMedia.getUser() != null && modelInstaMedia.getUser().getProfilePicture() != null)
                    insStmt.bindString(7, "" + modelInstaMedia.getUser().getProfilePicture());
                else
                    insStmt.bindString(7, "");

                if (modelInstaMedia.getLikes() != null && modelInstaMedia.getLikes().getCount() != null)
                    insStmt.bindString(8, "" + modelInstaMedia.getLikes().getCount());
                else
                    insStmt.bindString(8, "");

                if (modelInstaMedia.getComments() != null && modelInstaMedia.getComments().getCount() != null)
                    insStmt.bindString(9, "" + modelInstaMedia.getComments().getCount());
                else
                    insStmt.bindString(9, "");

                if (modelInstaMedia.getType() != null)
                    insStmt.bindString(10, "" + modelInstaMedia.getType());
                else
                    insStmt.bindString(10, "");

                if (modelInstaMedia.getLocation() != null) {
                    MyLocation myLocation = modelInstaMedia.getLocation();
                    insStmt.bindString(11, "" + new Gson().toJson(myLocation));
                } else
                    insStmt.bindString(11, "");

                insStmt.execute();
            }

            insStmt.clearBindings();

            sqLiteDatabase.setTransactionSuccessful();

            sqLiteDatabase.endTransaction();

            sqLiteDatabase.close();

        } catch (Exception e) {
            sqLiteDatabase.close();
            e.printStackTrace();
        } finally {
            sqLiteDatabase.close();
        }
    }

    public List<ModelInstaMedia> getMediaList() {

        List<ModelInstaMedia> modelInstaMedias = new ArrayList<>();

        try {

            sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(TBL_MEDIA, null, null, null, null, null, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                while (!cursor.isAfterLast()) {

                    ModelInstaMedia modelInstaMedia = new ModelInstaMedia();

                    String media_id = cursor.getString(cursor.getColumnIndex(MEDIA_ID));
                    modelInstaMedia.setId(media_id);

                    String media_mainurl = cursor.getString(cursor.getColumnIndex(MEDIA_MAINURL));
                    Images images = new Images();
                    if (media_mainurl != null) {
                        StandardResolution standardResolution = new StandardResolution();
                        standardResolution.setUrl(media_mainurl);
                        images.setStandardResolution(standardResolution);
                    }

                    String media_thumburl = cursor.getString(cursor.getColumnIndex(MEDIA_THUMBURL));
                    if (media_thumburl != null) {
                        Thumbnail thumbnail = new Thumbnail();
                        thumbnail.setUrl(media_thumburl);
                        images.setThumbnail(thumbnail);
                    }

                    modelInstaMedia.setImages(images);

                    String media_time = cursor.getString(cursor.getColumnIndex(MEDIA_CREATEDTIME));
                    modelInstaMedia.setCreatedTime(media_time);

                    String media_caption = cursor.getString(cursor.getColumnIndex(MEDIA_CAPTION));
                    Caption caption = new Caption();
                    caption.setText(media_caption);
                    modelInstaMedia.setCaption(caption);

                    String media_fullname = cursor.getString(cursor.getColumnIndex(MEDIA_FROMNAME));
                    User user = new User();
                    user.setFullName(media_fullname);

                    String media_profile = cursor.getString(cursor.getColumnIndex(MEDIA_FROMPICTURE));
                    user.setProfilePicture(media_profile);

                    modelInstaMedia.setUser(user);

                    String media_likecount = cursor.getString(cursor.getColumnIndex(MEDIA_LIKECOUNT));
                    Likes likes = new Likes();
                    likes.setCount(Integer.parseInt(media_likecount));
                    modelInstaMedia.setLikes(likes);

                    String media_commentcount = cursor.getString(cursor.getColumnIndex(MEDIA_COMMENTCOUNT));
                    Comments comments = new Comments();
                    comments.setCount(Integer.parseInt(media_commentcount));
                    modelInstaMedia.setComments(comments);

                    String media_type = cursor.getString(cursor.getColumnIndex(MEDIA_TYPE));
                    modelInstaMedia.setType(media_type);

                    String media_location = cursor.getString(cursor.getColumnIndex(MEDIA_LOCATION));
                    if (media_location != null) {
                        MyLocation myLocation = new Gson().fromJson(media_location, MyLocation.class);
                        modelInstaMedia.setLocation(myLocation);
                    } else {
                        modelInstaMedia.setLocation(null);
                    }

                    modelInstaMedias.add(modelInstaMedia);

                    cursor.moveToNext();
                }
            }

            cursor.close();
            sqLiteDatabase.close();

        } catch (Exception e) {
            sqLiteDatabase.close();
            e.printStackTrace();
        } finally {
            sqLiteDatabase.close();
        }

        return modelInstaMedias;
    }
}
