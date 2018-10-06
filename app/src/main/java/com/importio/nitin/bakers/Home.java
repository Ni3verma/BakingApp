package com.importio.nitin.bakers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        GetReceipesAsyncTask task = new GetReceipesAsyncTask();
        task.execute();
    }

    private class GetReceipesAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return NetworkUtils.getJSONResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null)
                Log.d("Nitin", s.substring(0, 30));
        }
    }
}
