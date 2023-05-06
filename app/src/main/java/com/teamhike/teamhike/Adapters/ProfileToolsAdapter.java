package com.teamhike.teamhike.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamhike.teamhike.Models.Tool;
import com.teamhike.teamhike.R;

import java.util.List;

public class ProfileToolsAdapter extends RecyclerView.Adapter<ProfileToolsAdapter.ItemViewHolder> {

    List<Tool> tools;

    public ProfileToolsAdapter(List<Tool> tools) {
        this.tools = tools;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_tool_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (!tools.get(position).getToolName().isEmpty()) {
            String toolName = tools.get(position).getToolName();
            switch (toolName) {
                case "ابزار حمایت و فرود":
                    holder.toolImage.setImageResource(R.drawable.tool_1);
                    break;
                case "آتل":
                    holder.toolImage.setImageResource(R.drawable.tool_2);
                    break;
                case "اجاق گاز مسافرتی":
                    holder.toolImage.setImageResource(R.drawable.tool_3);
                    break;
                case "ارتفاع سنج":
                    holder.toolImage.setImageResource(R.drawable.tool_4);
                    break;
                case "اسپری ضد باکتری":
                    holder.toolImage.setImageResource(R.drawable.tool_5);
                    break;
                case "اسپری ضد حشرات":
                    holder.toolImage.setImageResource(R.drawable.tool_6);
                    break;
                case "آنتی هیستامین و مسکن":
                    holder.toolImage.setImageResource(R.drawable.tool_7);
                    break;
                case "آیینه":
                    holder.toolImage.setImageResource(R.drawable.tool_8);
                    break;
                case "باتوم":
                    holder.toolImage.setImageResource(R.drawable.tool_9);
                    break;
                case "بادگیر":
                    holder.toolImage.setImageResource(R.drawable.tool_10);
                    break;
                case "بارونی":
                    holder.toolImage.setImageResource(R.drawable.tool_11);
                    break;
                case "بتادین":
                    holder.toolImage.setImageResource(R.drawable.tool_12);
                    break;
                case "بیل":
                    holder.toolImage.setImageResource(R.drawable.tool_13);
                    break;
                case "پاوربانک":
                    holder.toolImage.setImageResource(R.drawable.tool_14);
                    break;
                case "پروب":
                    holder.toolImage.setImageResource(R.drawable.tool_15);
                    break;
                case "پشه بند":
                    holder.toolImage.setImageResource(R.drawable.tool_16);
                    break;
                case "پله رکاب":
                    holder.toolImage.setImageResource(R.drawable.tool_17);
                    break;
                case "تبر یخ":
                    holder.toolImage.setImageResource(R.drawable.tool_18);
                    break;
                case "تخت آویز (هموک)":
                    holder.toolImage.setImageResource(R.drawable.tool_19);
                    break;
                case "تی شرت آستین بلند":
                    holder.toolImage.setImageResource(R.drawable.tool_20);
                    break;
                case "تی شرت آستین کوتاه":
                    holder.toolImage.setImageResource(R.drawable.tool_21);
                    break;
                case "تیشرت ضد رطوبت":
                    holder.toolImage.setImageResource(R.drawable.tool_22);
                    break;
                case "جلیقه نجات":
                    holder.toolImage.setImageResource(R.drawable.tool_23);
                    break;
                case "جوراب پشمی":
                    holder.toolImage.setImageResource(R.drawable.tool_24);
                    break;
                case "جوراب":
                    holder.toolImage.setImageResource(R.drawable.tool_25);
                    break;
                case "جی پی اس":
                    holder.toolImage.setImageResource(R.drawable.tool_26);
                    break;
                case "چادر":
                    holder.toolImage.setImageResource(R.drawable.tool_27);
                    break;
                case "چاقو همه کاره":
                    holder.toolImage.setImageResource(R.drawable.tool_28);
                    break;
                case "چراغ قوه سربند":
                    holder.toolImage.setImageResource(R.drawable.tool_29);
                    break;
                case "چراغ قوه":
                    holder.toolImage.setImageResource(R.drawable.tool_30);
                    break;
                case "چسب زخم":
                    holder.toolImage.setImageResource(R.drawable.tool_31);
                    break;
                case "حوله":
                    holder.toolImage.setImageResource(R.drawable.tool_32);
                    break;
                case "خمیردندان":
                    holder.toolImage.setImageResource(R.drawable.tool_33);
                    break;
                case "دستکش":
                    holder.toolImage.setImageResource(R.drawable.tool_34);
                    break;
                case "دستمال سر":
                    holder.toolImage.setImageResource(R.drawable.tool_35);
                    break;
                case "دستمال کاغذی":
                    holder.toolImage.setImageResource(R.drawable.tool_36);
                    break;
                case "دستمال مرطوب":
                    holder.toolImage.setImageResource(R.drawable.tool_37);
                    break;
                case "زیرانداز":
                    holder.toolImage.setImageResource(R.drawable.tool_38);
                    break;
                case "زیرانداز کیسه خواب":
                    holder.toolImage.setImageResource(R.drawable.tool_39);
                    break;
                case "ساعت ضد آب":
                    holder.toolImage.setImageResource(R.drawable.tool_40);
                    break;
                case "سوت":
                    holder.toolImage.setImageResource(R.drawable.tool_41);
                    break;
                case "سویشرت":
                    holder.toolImage.setImageResource(R.drawable.tool_42);
                    break;
                case "شلوار سبک":
                    holder.toolImage.setImageResource(R.drawable.tool_43);
                    break;
                case "شلوار نخی":
                    holder.toolImage.setImageResource(R.drawable.tool_44);
                    break;
                case "صندل":
                    holder.toolImage.setImageResource(R.drawable.tool_45);
                    break;
                case "فندک":
                    holder.toolImage.setImageResource(R.drawable.tool_46);
                    break;
                case "فیلتر آب":
                    holder.toolImage.setImageResource(R.drawable.tool_47);
                    break;
                case "قاشق و چنگال":
                    holder.toolImage.setImageResource(R.drawable.tool_48);
                    break;
                case "قرقره":
                    holder.toolImage.setImageResource(R.drawable.tool_49);
                    break;
                case "قطب نما":
                    holder.toolImage.setImageResource(R.drawable.tool_50);
                    break;
                case "قمقمه":
                    holder.toolImage.setImageResource(R.drawable.tool_51);
                    break;
                case "کاپشن":
                    holder.toolImage.setImageResource(R.drawable.tool_52);
                    break;
                case "کارابین":
                    holder.toolImage.setImageResource(R.drawable.tool_53);
                    break;
                case "کپسول گاز مسافرتی":
                    holder.toolImage.setImageResource(R.drawable.tool_54);
                    break;
                case "کرامپون":
                    holder.toolImage.setImageResource(R.drawable.tool_55);
                    break;
                case "کرم ضد آفتاب":
                    holder.toolImage.setImageResource(R.drawable.tool_56);
                    break;
                case "کفش پیاده روی":
                    holder.toolImage.setImageResource(R.drawable.tool_57);
                    break;
                case "کفش":
                    holder.toolImage.setImageResource(R.drawable.tool_58);
                    break;
                case "کلاه آفتابی":
                    holder.toolImage.setImageResource(R.drawable.tool_59);
                    break;
                case "کلاه ایمنی":
                    holder.toolImage.setImageResource(R.drawable.tool_60);
                    break;
                case "کلاه پشمی":
                    holder.toolImage.setImageResource(R.drawable.tool_61);
                    break;
                case "کمک های اولیه":
                    holder.toolImage.setImageResource(R.drawable.tool_62);
                    break;
                case "کوله پشتی":
                    holder.toolImage.setImageResource(R.drawable.tool_63);
                    break;
                case "کیسه خواب":
                    holder.toolImage.setImageResource(R.drawable.tool_64);
                    break;
                case "کیف کمری":
                    holder.toolImage.setImageResource(R.drawable.tool_65);
                    break;
                case "کیف محافظ ضد آب":
                    holder.toolImage.setImageResource(R.drawable.tool_66);
                    break;
                case "گاز استریل":
                    holder.toolImage.setImageResource(R.drawable.tool_67);
                    break;
                case "گتر":
                    holder.toolImage.setImageResource(R.drawable.tool_68);
                    break;
                case "لباس زیر":
                    holder.toolImage.setImageResource(R.drawable.tool_69);
                    break;
                case "لوازم پخت و پز":
                    holder.toolImage.setImageResource(R.drawable.tool_70);
                    break;
                case "لیوان":
                    holder.toolImage.setImageResource(R.drawable.tool_71);
                    break;
                case "ماسک صورت":
                    holder.toolImage.setImageResource(R.drawable.tool_72);
                    break;
                case "مایو":
                    holder.toolImage.setImageResource(R.drawable.tool_73);
                    break;
                case "مسواک":
                    holder.toolImage.setImageResource(R.drawable.tool_74);
                    break;
                case "مهار":
                    holder.toolImage.setImageResource(R.drawable.tool_75);
                    break;
                case "نقشه":
                    holder.toolImage.setImageResource(R.drawable.tool_76);
                    break;
            }
            holder.toolName.setText(tools.get(position).getToolName());
            holder.toolNum.setText(tools.get(position).getToolNumber());
        }
    }

    @Override
    public int getItemCount() {
        return tools.size();
    }

    public void updateTools(List<Tool> tools) {
        this.tools = tools;
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView toolImage;
        TextView toolName, toolNum;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            toolImage = itemView.findViewById(R.id.toolsItem_image);
            toolName = itemView.findViewById(R.id.toolsItem_name);
            toolNum = itemView.findViewById(R.id.toolsItem_Number);
            toolNum = itemView.findViewById(R.id.toolsItem_Number);
        }
    }
}
