package com.github.rkhusainov.externalstoragepermission;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.FilesHolder> {

    private List<String> mFiles;
    private OnFileClickListener mOnFileClickListener;

    public FilesAdapter(List<String> files, OnFileClickListener onFileClickListener) {
        mFiles = files;
        mOnFileClickListener = onFileClickListener;
    }

    public void setFiles(List<String> files) {
        mFiles = files;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_files, parent, false);
        return new FilesHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesHolder holder, int position) {
        holder.bind(mFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    class FilesHolder extends RecyclerView.ViewHolder {

        private TextView mNameTextView;

        public FilesHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.tv_file_name);
        }

        private void bind(String name) {
            mNameTextView.setText(name);

            mNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnFileClickListener.onFileClick(getAdapterPosition());
                }
            });

//            mTextViewName.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnFileClickListener.onFileClick();
//                }
//            });
        }
    }

    interface OnFileClickListener {
        void onFileClick(int position);
    }
}
