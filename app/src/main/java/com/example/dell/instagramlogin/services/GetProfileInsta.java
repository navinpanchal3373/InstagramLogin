package com.example.dell.instagramlogin.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.dell.instagramlogin.myinterface.GetInstaRes;
import com.example.dell.instagramlogin.utilities.Utility;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by navinpanchal3373@gmail.com on 11/4/2017.
 */

public class GetProfileInsta extends AsyncTask<JSONObject, Void, String> {

    private Context mContext;
    private String TAG = "GetProfileInsta";
    ProgressDialog mProgress;
    GetInstaRes getInstaRes;
    String methodName = "";
    boolean isLoader = false;

    public GetProfileInsta(Context mContext, String methodName, boolean isLoader, GetInstaRes getInstaRes) {
        this.mContext = mContext;
        this.methodName = methodName;
        this.isLoader = isLoader;
        this.getInstaRes = getInstaRes;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isLoader) {
            mProgress = new ProgressDialog(mContext);
            mProgress.setMessage("Loading ...");
            mProgress.show();
        }
    }

    @Override
    protected String doInBackground(JSONObject... params) {

        String result = "";

        try {

            String reqUrl = params[0].optString("url");

            URL url = new URL(reqUrl);

            Log.d(TAG, "Opening URL " + url.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            String response = Utility.streamToString(urlConnection
                    .getInputStream());
            System.out.println(response);

            JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();

            result = jsonObj.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (isLoader && mProgress != null && mProgress.isShowing())
            mProgress.dismiss();

        if (result != null)
            getInstaRes.getInstaResponse(result, methodName);
    }
}
