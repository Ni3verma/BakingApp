package com.importio.nitin.bakers;

import android.app.ListActivity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.importio.nitin.bakers.Database.AppDatabase;
import com.importio.nitin.bakers.Database.ReceipeEntry;

import java.util.ArrayList;
import java.util.List;

public class ChooseWidgetReceipe extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        ListView receipeListView = getListView();
        final List<String> receipeNames = new ArrayList<>();

        final AppDatabase mDb = AppDatabase.getsInstance(this);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<ReceipeEntry> receipeEntryList = mDb.ReceipeDao().getAllReceipe();
                for (ReceipeEntry r : receipeEntryList) {
                    receipeNames.add(r.getName());
                }
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                receipeNames
        );

        receipeListView.setAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            int appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {

                Intent resultValue = new Intent();

                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

                ReceipeIngredientWidget.updateAppWidget(this, appWidgetManager, appWidgetId, position + 1);

                setResult(RESULT_OK, resultValue);

                finish();
            }
        }
        finish();
    }
}
