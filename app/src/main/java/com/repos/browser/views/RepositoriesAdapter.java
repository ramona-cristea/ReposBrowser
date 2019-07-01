package com.repos.browser.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.repos.browser.R;
import com.repos.browser.model.Item;

import java.util.List;

public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoriesAdapter.RepositoryViewHolder> {
    private List<Item> mRepositories;
    private final RepositoryAdapterHandler mRepositoryClickListener;

    RepositoriesAdapter(List<Item> repositories, RepositoryAdapterHandler listener) {
        mRepositories = repositories;
        mRepositoryClickListener = listener;
    }

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_repository, parent, false);

        return new RepositoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryViewHolder holder, int position) {
        final Item repository = mRepositories.get(position);
        holder.bind(repository);
    }

    @Override
    public int getItemCount() {
        return mRepositories == null ? 0 : mRepositories.size();
    }

    void setData(List<Item> repositories) {
        mRepositories = repositories;
        notifyDataSetChanged();
    }

    public interface RepositoryAdapterHandler {
        void onItemClicked(Item repository);
    }

    public class RepositoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTextRepositoryName;
        TextView mTextOwnerName;
        TextView mTextStars;
        TextView mTextLanguage;
        LinearLayout mLayoutRepositoryItem;

        private Item mRepository;

        RepositoryViewHolder(View itemView) {
            super(itemView);
            mLayoutRepositoryItem = itemView.findViewById(R.id.layout_repository_item);
            mTextRepositoryName = itemView.findViewById(R.id.text_repository_name);
            mTextLanguage = itemView.findViewById(R.id.text_language);
            mTextOwnerName = itemView.findViewById(R.id.text_owner_name);
            mTextStars = itemView.findViewById(R.id.text_stars);
            mLayoutRepositoryItem.setOnClickListener(this);
        }

        void bind(@NonNull Item repository) {
            mRepository = repository;
            mTextRepositoryName.setText(mRepository.getName());
            mTextOwnerName.setText(mRepository.getOwner().getLogin());
            mTextStars.setText(String.valueOf(mRepository.getStargazersCount()));
            mTextLanguage.setText(mTextLanguage.getResources().getString(R.string.repo_language, mRepository.getLanguage()));
        }

        @Override
        public void onClick(View view) {
            mRepositoryClickListener.onItemClicked(mRepository);
        }
    }

}