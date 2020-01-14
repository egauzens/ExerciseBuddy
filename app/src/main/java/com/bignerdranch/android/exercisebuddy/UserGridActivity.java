package com.bignerdranch.android.exercisebuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserGridActivity extends AppCompatActivity {
    private RecyclerView mUserGrid;
    private GridItemAdapter mAdpater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_grid);

        mUserGrid = findViewById(R.id.recycler_view);
        mUserGrid.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        updateUI();
    }

    private void updateUI(){
        List<String> usernames = new ArrayList<>();
        usernames.add("Eric");
        usernames.add("David");
        usernames.add("Danny");
        usernames.add("Amanda");

        mAdpater = new GridItemAdapter(usernames);
        mUserGrid.setAdapter(mAdpater);
    }

    private class GridItemHolder extends RecyclerView.ViewHolder{
        private TextView mUsernameTextView;

        public GridItemHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.grid_item_user, parent, false));

            mUsernameTextView = (TextView) itemView.findViewById(R.id.username);
        }

        public void bind(String username){
            mUsernameTextView.setText(username);
        }
    }

    private class GridItemAdapter extends RecyclerView.Adapter<GridItemHolder> {
        private List<String> mUsernames;

        public GridItemAdapter(List<String> usernames){
            mUsernames = usernames;
        }

        @NonNull
        @Override
        public GridItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(UserGridActivity.this);

            return new GridItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull GridItemHolder holder, int position) {
            String username = mUsernames.get(position);
            holder.bind(username);
        }

        @Override
        public int getItemCount() {
            return mUsernames.size();
        }
    }
}
