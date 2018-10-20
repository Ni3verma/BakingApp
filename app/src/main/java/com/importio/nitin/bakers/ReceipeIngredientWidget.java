package com.importio.nitin.bakers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.importio.nitin.bakers.Database.AppDatabase;
import com.importio.nitin.bakers.Database.IngredientEntry;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class ReceipeIngredientWidget extends AppWidgetProvider {
    private static AppDatabase mDb;
    private static RemoteViews views;
    private static AppWidgetManager widgetManager;
    private static int widgetId;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int receipeId) {
        Log.d("Nitin", "onUpdateAppWidget");

        mDb = AppDatabase.getsInstance(context);
        widgetManager = appWidgetManager;
        widgetId = appWidgetId;


        Intent intent = new Intent(context, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.receipe_ingredient_widget);

        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        if (receipeId != -1) {
            //views.setTextViewText(R.id.appwidget_text,"this is correct");
            new getIngredientsAsyncTask().execute(receipeId);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.d("Nitin", "onUpdate");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, -1);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d("Nitin", "on enabled");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d("Nitin", "on disabled");
    }

    static class getIngredientsAsyncTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            int id = integers[0];

            List<IngredientEntry> ingredients = mDb.IngredientDao().getIngredientsOfReceipe(id);
            StringBuilder ingredientText = new StringBuilder();
            for (IngredientEntry ingredient : ingredients) {
                ingredientText
                        .append("-> ")
                        .append(ingredient.getQty())
                        .append(" ")
                        .append(ingredient.getMeasure())
                        .append(" ")
                        .append(ingredient.getName());
                ingredientText.append("\n");
            }

            return ingredientText.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            views.setTextViewText(R.id.appwidget_text, s);
            widgetManager.updateAppWidget(widgetId, views);
        }
    }
}

