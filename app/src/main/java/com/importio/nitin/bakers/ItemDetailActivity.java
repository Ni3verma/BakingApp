package com.importio.nitin.bakers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.importio.nitin.bakers.Database.AppDatabase;

import java.util.List;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity {
    private static int id;
    private static int min;
    private static int max;
    private static int receipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        id = getIntent().getIntExtra(ItemDetailFragment.ARG_ITEM_ID, -1);
        getReceipeId(id);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(ItemDetailFragment.ARG_ITEM_ID, id);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }

    }

    private void navigateStep(int id) {
        Bundle arguments = new Bundle();
        arguments.putInt(ItemDetailFragment.ARG_ITEM_ID, id);
        ItemDetailFragment fragment = new ItemDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getReceipeId(final int stepId) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                receipeId = AppDatabase.getsInstance(ItemDetailActivity.this).StepDao().getReceipeId(stepId);
                setLimit(receipeId);
            }
        });

    }

    private void setLimit(final int receipeId) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Integer> range = AppDatabase.getsInstance(ItemDetailActivity.this).StepDao().getRangeOfIds(receipeId);
                min = range.get(0);
                max = range.get(range.size() - 1);

            }
        });
    }

    public void nextButtonClicked(View view) {
        id++;
        if (id <= max)
            navigateStep(id);
        else {
            id--;
            Toast.makeText(this, "Last Step", Toast.LENGTH_SHORT).show();
        }

    }

    public void prevButtonClicked(View view) {
        id--;
        if (id >= min)
            navigateStep(id);
        else {
            id++;
            Toast.makeText(this, "First Step", Toast.LENGTH_SHORT).show();
        }
    }
}
