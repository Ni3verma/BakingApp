package com.importio.nitin.bakers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.importio.nitin.bakers.Adapter.HomeAdapter;
import com.importio.nitin.bakers.Database.AppDatabase;
import com.importio.nitin.bakers.Database.IngredientEntry;
import com.importio.nitin.bakers.Database.ReceipeEntry;
import com.importio.nitin.bakers.Database.StepEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private AppDatabase mDb;
    private List<ReceipeEntry> mReceipeList;
    private ListView mListView;
    private HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDb = AppDatabase.getsInstance(this);
        mReceipeList = new ArrayList<>();
        mListView = findViewById(R.id.receipe_lv);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Nitin", (position + 1) + " clicked");
            }
        });

        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<ReceipeEntry> receipeList = mDb.ReceipeDao().getAllReceipe();
                if (receipeList.size() == 0) { //make network call to get data and then fill the database
                    Log.d("Nitin", "db is empty, so get data from net and store in db");
                    getReceipeData();
                } else {   //we already have data in db,so simply get data from db and show it
                    Log.d("Nitin", receipeList.size() + " receipes found in db");
                    showReceipes();
                }

            }
        });

    }

    private void showReceipes() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mReceipeList = mDb.ReceipeDao().getAllReceipe();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new HomeAdapter(getApplicationContext(), mReceipeList);
                        mListView.setAdapter(mAdapter);
                    }
                });
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
                mReceipeList.add(receipe);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new HomeAdapter(getApplicationContext(), mReceipeList);
                        mListView.setAdapter(mAdapter);
                    }
                });
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

    private void addStepsToDatabase(JSONArray steps, int receipeId) {
        try {
            for (int i = 0; i < steps.length(); i++) {
                JSONObject step = steps.getJSONObject(i);
                String shortDesc = step.getString("shortDescription");
                String desc = step.getString("description");
                String videoURL = step.getString("videoURL");
                String thumbnailURL = step.getString("thumbnailURL");

                final StepEntry stepEntry = new StepEntry(receipeId, shortDesc, desc, videoURL, thumbnailURL);
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.StepDao().insertStep(stepEntry);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

                        JSONArray steps = receipe.getJSONArray("steps");
                        addStepsToDatabase(steps, id);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
