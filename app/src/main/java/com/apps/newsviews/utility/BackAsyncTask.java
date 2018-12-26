package com.apps.newsviews.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.apps.newsviews.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class BackAsyncTask extends AsyncTask<String,Void,String> {

    public interface AsyncResponse {
        void processFinish(String output);
    }

    private static final String TAG = "BackAsyncTask";
    private Context context;
    private ProgressDialog progress;
    private AsyncResponse asyncResponse = null;

    public BackAsyncTask(Context ctx, AsyncResponse response) {
        this.context = ctx;
        this.asyncResponse = response;
    }

    @Override
    protected void onPreExecute() {
        this.progress = new ProgressDialog(context);
        //progress.setTitle("Retrieving data");
        this.progress.setMessage(context.getString(R.string.progress));
        this.progress.setIndeterminate(true);
        this.progress.setCancelable(false);
        this.progress.show();
    }

    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        String url;

        if(type.equals("number_api")) {
            if (params[1].matches(".*[/].*")) {
                Log.d(TAG, ""+params[1]);
                url = "http://numbersapi.com/" + params[1] + "/date";
            } else {
                Log.d(TAG, ""+params[1]);
                url = "http://numbersapi.com/" + params[1] + "/trivia";
            }
            try {
                URLConnection connection = new URL(url).openConnection();
                connection.setConnectTimeout(1000 * 30);
                connection.setReadTimeout(1000 * 30);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();

                //Log.d(TAG, String.valueOf(result));
                return result;
            } catch (Exception e) {
                if(this.progress != null) {
                    this.progress.dismiss(); //close the dialog if error occurs
                }
                e.printStackTrace();
                //alertDialog(e.getMessage());
                return null;
            }
        }

        /*HttpClient httpclient = HttpClients.createDefault();
        try {
            URIBuilder builder = new URIBuilder("https://apiphany.azure-api.net/numbers/random/math");

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);

            // Request body
            StringEntity reqEntity = new StringEntity("{body}");
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                System.out.println(EntityUtils.toString(entity));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        this.asyncResponse.processFinish(result);
        if(this.progress != null) {
            this.progress.dismiss(); //close the dialog if error occurs
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


    //====================================================| Alert Message
    /*public void alertDialog(String msg) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.alert_title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).show();
    }*/

}
