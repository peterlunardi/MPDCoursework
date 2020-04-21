package com.example.mpdcoursework;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {
    private ArrayList<RoadTrafficItem> roadTrafficItems;
    private ArrayList<RoadTrafficItem> roadTrafficItemsFull;
    private OnItemClickListener mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView mTitle;
        public TextView mDescription;
        public TextView mDates;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTitle = itemView.findViewById(R.id.title);
            mDescription = itemView.findViewById(R.id.description);
            mDates = itemView.findViewById(R.id.dates);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    RecyclerViewAdapter()
    {
        roadTrafficItems = new ArrayList<RoadTrafficItem>();
        roadTrafficItemsFull = new ArrayList<RoadTrafficItem>();
    }

    public RoadTrafficItem clickedItem(int position)
    {
        return roadTrafficItems.get(position);
    }

    public void SetItemList(ArrayList<RoadTrafficItem> items)
    {
        roadTrafficItems = items;
        roadTrafficItemsFull = new ArrayList<RoadTrafficItem>(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.traffic_item, parent, false);
        ViewHolder vh = new ViewHolder(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoadTrafficItem currentItem = roadTrafficItems.get(position);

        holder.mImageView.setImageResource(currentItem.getImage());
        holder.mTitle.setText(currentItem.getTitle());
        holder.mDescription.setText(currentItem.getDescription());
        if(currentItem.getStartDate() != null)
        {
            holder.mDates.setText(currentItem.getStringStartDate() + "\n" + currentItem.getStringEndDate() + "\n\n" + "Delay: " + currentItem.getDelayTime() + " days.");
        }
        else holder.mDates.setText("");

    }

    @Override
    public int getItemCount() {
        return roadTrafficItems.size();
    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<RoadTrafficItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(roadTrafficItemsFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                OUTER_LOOP:
                for (RoadTrafficItem item : roadTrafficItemsFull)
                {
                    for (String date : item.getDates())
                    {
                        if(date.contains(filterPattern))
                        {
                            filteredList.add(item);
                            continue OUTER_LOOP;
                        }
                    }

                    if(item.getTitle().toLowerCase().contains(filterPattern)
                            || item.getNumericalStartDate(item.getStartDate()).contains(filterPattern)
                            || item.getNumericalEndDate(item.getEndDate()).contains(filterPattern)
                    )
                    {
                        filteredList.add(item);
                    }
                }
            }

            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, @NonNull FilterResults results) {
            roadTrafficItems.clear();
            roadTrafficItems.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
}
