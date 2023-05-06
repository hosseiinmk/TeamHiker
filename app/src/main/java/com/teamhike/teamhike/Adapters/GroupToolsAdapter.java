package com.teamhike.teamhike.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamhike.teamhike.Models.GroupTool;
import com.teamhike.teamhike.R;

import java.util.List;

public class GroupToolsAdapter extends RecyclerView.Adapter<GroupToolsAdapter.ItemViewHolder> {

    List<GroupTool> tools;

    public GroupToolsAdapter(List<GroupTool> tools) {
        this.tools = tools;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tool, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        int toolType = Integer.parseInt(tools.get(position).getToolType());
        switch (toolType) {
            case 1:
                holder.toolImage.setImageResource(R.drawable.ic_back_pack);
                break;
            case 2:
                holder.toolImage.setImageResource(R.drawable.ic_tent);
                break;
            case 3:
                holder.toolImage.setImageResource(R.drawable.ic_hiking_pole);
                break;
            case 4:
                holder.toolImage.setImageResource(R.drawable.ic_termos);
                break;
            case 5:
                holder.toolImage.setImageResource(R.drawable.ic_rope);
                break;
            case 6:
                holder.toolImage.setImageResource(R.drawable.ic_flash_light);
                break;
            case 7:
                holder.toolImage.setImageResource(R.drawable.ic_compass);
                break;
            case 8:
                holder.toolImage.setImageResource(R.drawable.ic_first_aid);
                break;
            case 9:
                holder.toolImage.setImageResource(R.drawable.ic_knife);
                break;
        }
        holder.toolName.setText(tools.get(position).getToolName());
        holder.toolNum.setText(tools.get(position).getToolNumber());
        holder.deleteBtn.setOnClickListener(v -> {
            tools.remove(position);
            updateTools(tools);
        });
    }

    @Override
    public int getItemCount() {
        return tools.size();
    }

    public void updateTools(List<GroupTool> tools) {
        this.tools = tools;
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView toolImage, deleteBtn;
        TextView toolName, toolNum;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            toolImage = itemView.findViewById(R.id.toolsItem_image);
            toolName = itemView.findViewById(R.id.toolsItem_name);
            toolNum = itemView.findViewById(R.id.toolsItem_Number);
            toolNum = itemView.findViewById(R.id.toolsItem_Number);
            deleteBtn = itemView.findViewById(R.id.toolsItem_deleteBtn);
        }
    }
}
