package com.importio.nitin.bakers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.importio.nitin.bakers.Database.AppDatabase;
import com.importio.nitin.bakers.Database.IngredientEntry;
import com.importio.nitin.bakers.Database.ReceipeEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Home extends AppCompatActivity {
    AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDb = AppDatabase.getsInstance(this);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<ReceipeEntry> receipeList = mDb.ReceipeDao().getAllReceipe();
                if (receipeList.size() == 0) { //make network call to get data and then fill the database
                    Log.d("Nitin", "db is empty, so get data from net and store in db");
                    getReceipeData();
                } else {   //we already have data in db,so simply get data from db and show it
                    Log.d("Nitin", receipeList.size() + " receipes found in db");
                    for (ReceipeEntry r : receipeList) {
                        Log.d("Nitin", r.getName());
                    }
                }
                Log.d("Nitin", "**********PRINTING INGREDIENTS************");
                List<IngredientEntry> ingredientEntries = mDb.IngredientDao().getAllIngredients();
                for (IngredientEntry i : ingredientEntries) {
                    Log.d("Nitin", i.toString());
                }
            }
        });

    }

    private void getReceipeData() {
        GetReceipesAsyncTask task = new GetReceipesAsyncTask();
        task.execute();
    }

    private void addReceipeToDatabase(final ReceipeEntry receipe) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.ReceipeDao().insertReceipe(receipe);
            }
        });
    }

    private void addIngredientsToDatabase(JSONArray ingredients, int receipeId) {
        for (int i = 0; i < ingredients.length(); i++) {
            try {
                JSONObject ingredient = ingredients.getJSONObject(i);
                int quantity = ingredient.getInt("quantity");
                String measure = ingredient.getString("measure");
                String name = ingredient.getString("ingredient");

                final IngredientEntry ingredientEntry = new IngredientEntry(receipeId, quantity, measure, name);
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.IngredientDao().insertIngredient(ingredientEntry);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class GetReceipesAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return NetworkUtils.getJSONResponse();
        }

        @Override
        protected void onPostExecute(String data) {
            if (data != null) {
                try {
                    JSONArray receipes = new JSONArray(data);
                    for (int i = 0; i < receipes.length(); i++) {
                        JSONObject receipe = receipes.getJSONObject(i);
                        int id = receipe.getInt("id");
                        String name = receipe.getString("name");
                        int servings = receipe.getInt("servings");

                        ReceipeEntry receipeEntry = new ReceipeEntry(id, name, servings);
                        addReceipeToDatabase(receipeEntry);

                        JSONArray ingredients = receipe.getJSONArray("ingredients");
                        addIngredientsToDatabase(ingredients, id);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
