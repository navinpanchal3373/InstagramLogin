package com.example.dell.instagramlogin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.instagramlogin.adapter.DataAdapter;
import com.example.dell.instagramlogin.database.DBInstagramPostImage;
import com.example.dell.instagramlogin.instagram.ApplicationData;
import com.example.dell.instagramlogin.instagram.InstagramApp;
import com.example.dell.instagramlogin.model.ModelInstaProfile;
import com.example.dell.instagramlogin.model.instamedia.ModelInstaMedia;
import com.example.dell.instagramlogin.myinterface.GetInstaRes;
import com.example.dell.instagramlogin.myinterface.RecyclerViewClickListener;
import com.example.dell.instagramlogin.services.GetProfileInsta;
import com.example.dell.instagramlogin.utilities.Constants;
import com.example.dell.instagramlogin.utilities.UserPreferences;
import com.example.dell.instagramlogin.utilities.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by navinpanchal3373@gmail.com on 11/4/2017.
 */

public class HomeActivity extends AppCompatActivity implements GetInstaRes {

    private InstagramApp mApp;
    private Context mContext;

    private TextView mtvWelcome, mtvUserName, mtvFullname;
    private TextView tvmedia, tvfollows, tvfollowed_by;
    private ImageView mimgProfilepic;
    private UserPreferences userPreferences;
    private DisplayImageOptions options;
    private List<ModelInstaMedia> modelInstaMedias;
    private SQLiteDatabase sqLiteDatabase;
    private DBInstagramPostImage webMobDataBase;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = HomeActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userPreferences = new UserPreferences(mContext);

        webMobDataBase = new DBInstagramPostImage(mContext);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                mApp.authorize();
            }
        });

        mApp = new InstagramApp(this, ApplicationData.CLIENT_ID,
                ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                getInstaProfileLoggedser();
            }

            @Override
            public void onFail(String error) {
                Utility.showToast(mContext, error);
            }
        });

        initWidget();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initWidget() {

        mtvWelcome = (TextView) findViewById(R.id.tvWelcome);
        mtvUserName = (TextView) findViewById(R.id.tvUserName);
        mtvFullname = (TextView) findViewById(R.id.tvFullname);

        tvmedia = (TextView) findViewById(R.id.tvmedia);
        tvfollows = (TextView) findViewById(R.id.tvfollows);
        tvfollowed_by = (TextView) findViewById(R.id.tvfollowed_by);

        mimgProfilepic = (ImageView) findViewById(R.id.imgProfilepic);

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        updateMedia();
        if (userPreferences.getUserInfo() != null) {
            updateProfile(userPreferences.getUserInfo());
        }

        if (mApp.hasAccessToken()) {

            if (mApp.getName() != null) {
                mtvWelcome.setText("Welcome, " + mApp.getName());
            }

            if (mApp.getUserName() != null) {
                mtvFullname.setText("User Name -: " + mApp.getUserName());
            }

            if (Utility.isOnline(mContext)) {
                getInstaProfileLoggedser();

            }

        }
    }

    public void updateMedia() {

        if (modelInstaMedias != null)
            modelInstaMedias = null;

        modelInstaMedias = webMobDataBase.getMediaList();
        Log.e("All Data", "" + modelInstaMedias);

        DataAdapter adapter = new DataAdapter(mContext, modelInstaMedias, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                ModelInstaMedia modelInstaMedia = modelInstaMedias.get(position);
                if (modelInstaMedia.getLocation() != null) {
                    String location = new Gson().toJson(modelInstaMedia.getLocation());
                    startActivity(new Intent(HomeActivity.this, MapsActivity.class)
                            .putExtra("data", location));
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                connectOrDisconnectUser();
                break;
            case R.id.action_synch:
                if (Utility.isOnline(mContext)) {
                    getInstaMediaLoggedser();
                } else {
                    Utility.showToast(mContext, "Faild to Refresh, please check your network");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void connectOrDisconnectUser() {
        Log.e("Data :-", "" + mApp.getUserInfo());
        if (mApp.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    mContext);
            builder.setMessage("Disconnect from Instagram?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    mApp.resetAccessToken();
                                    resetData();
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            mApp.authorize();
        }
    }

    public void resetData() {

        tvmedia.setText("");
        tvfollowed_by.setText("");
        tvfollows.setText("");
        mtvFullname.setText("");
        mtvWelcome.setText("");
        modelInstaMedias = new ArrayList<>();
        DataAdapter adapter = new DataAdapter(mContext, modelInstaMedias, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void getInstaProfileLoggedser() {

        try {

            String url = InstagramApp.API_URL + "/users/" + mApp.getId()
                    + "/?access_token=" + mApp.getTOken();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", url);

            GetProfileInsta getProfileInsta = new GetProfileInsta(mContext, Constants.METHOD_PROFILE, false, this);
            getProfileInsta.execute(jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getInstaMediaLoggedser() {

        try {

            String url = InstagramApp.API_URL + "/users/self/media/recent/?access_token=" + mApp.getTOken();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", url);

            GetProfileInsta getProfileInsta = new GetProfileInsta(mContext, Constants.METHOD_GETMEDIAPOAST, false, this);
            getProfileInsta.execute(jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getInstaResponse(String s, String method) {

        Log.e("Data Profile:-", "" + s);

        if (method.equals(Constants.METHOD_PROFILE)) {
            ModelInstaProfile modelInstaProfile = new Gson().fromJson(s, ModelInstaProfile.class);
            if (modelInstaProfile != null) {
                userPreferences.saveUserInfo(modelInstaProfile);
                updateProfile(modelInstaProfile);
            }

            getInstaMediaLoggedser();

        } else if (method.equals(Constants.METHOD_GETMEDIAPOAST)) {

            try {

                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject != null) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        Type type = new TypeToken<List<ModelInstaMedia>>() {
                        }.getType();

                        if (modelInstaMedias != null)
                            modelInstaMedias = null;

                        modelInstaMedias = new Gson().fromJson(jsonArray.toString(), type);
                        new DBInstagramPostImage(mContext).saveInstaMedia(modelInstaMedias);

//                        DataAdapter adapter = new DataAdapter(mContext, modelInstaMedias, new RecyclerViewClickListener() {
//                            @Override
//                            public void onClick(View view, int position) {
//                                ModelInstaMedia modelInstaMedia = modelInstaMedias.get(position);
//                                if (modelInstaMedia.getLocation() != null) {
//                                    String location = new Gson().toJson(modelInstaMedia.getLocation());
//                                    startActivity(new Intent(HomeActivity.this, MapsActivity.class)
//                                            .putExtra("data", location));
//                                }
//                            }
//                        });
//                        recyclerView.setAdapter(adapter);


                        updateMedia();

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateProfile(ModelInstaProfile modelInstaProfile) {

        if (modelInstaProfile.getData().getUsername() != null)
            mtvWelcome.setText("Welcome, " + modelInstaProfile.getData().getUsername());

        if (modelInstaProfile.getData().getFullName() != null)
            mtvFullname.setText("User Name -: " + modelInstaProfile.getData().getFullName());

        if (modelInstaProfile.getData().getProfilePicture() != null)
            ImageLoader.getInstance().displayImage(modelInstaProfile.getData().getProfilePicture(),
                    mimgProfilepic, options);

        if (modelInstaProfile.getData().getCounts().getFollowedBy() != null)
            tvfollowed_by.setText("Followed By : " + modelInstaProfile.getData().getCounts().getFollowedBy());

        if (modelInstaProfile.getData().getCounts().getFollows() != null)
            tvfollows.setText("Follows : " + modelInstaProfile.getData().getCounts().getFollows());

        if (modelInstaProfile.getData().getCounts().getMedia() != null)
            tvmedia.setText("Media : " + modelInstaProfile.getData().getCounts().getMedia());
    }
}
