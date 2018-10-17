package com.importio.nitin.bakers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.importio.nitin.bakers.Database.AppDatabase;
import com.importio.nitin.bakers.Database.IngredientEntry;
import com.importio.nitin.bakers.Database.ShortStepEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    public static String RECEIPE_ID = "receipe_id";
    TextView ingredientsTextView;
    private int receipeId;
    private AppDatabase mDb;
    RecyclerView mRecyclerView;
    private List<ShortStepEntry> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        mDb = AppDatabase.getsInstance(this);
        mSteps = new ArrayList<>();

        ingredientsTextView = findViewById(R.id.ingredients_tv);
        receipeId = getIntent().getIntExtra(RECEIPE_ID, 1);
        getSteps(receipeId);
        getIngredients(receipeId);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mRecyclerView = findViewById(R.id.item_list);
        assert mRecyclerView != null;
        setupRecyclerView();
    }

    private void getSteps(final int id) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mSteps = mDb.StepDao().getStepsOfReceipe(id);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupRecyclerView();
                    }
                });
            }

        });
    }

    private void getIngredients(final int id) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
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
                ingredientsTextView.setText(ingredientText.toString());
            }
        });
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, mSteps, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<ShortStepEntry> mSteps;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShortStepEntry item = (ShortStepEntry) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(ItemDetailFragment.ARG_ITEM_ID, item.getId());
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<ShortStepEntry> steps,
                                      boolean twoPane) {
            mSteps = steps;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText("" + (position + 1));
            holder.mContentView.setText(mSteps.get(position).getShortDesc());

            holder.itemView.setTag(mSteps.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mSteps.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }
}
