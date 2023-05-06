package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.teamhike.teamhike.Models.GroupTool;
import com.teamhike.teamhike.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGroupNecessaryToolsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private List<GroupTool> toolsForShow;

    private List<String> toolsNames;
    private List<String> toolsNumbers;
    private List<String> toolsTypes;
    private List<String> attractions;
    private List<String> targetDestinationsNames;

    private ImageView backBtn;
    private TextView nextStepBtn;
    private Spinner toolsGroupsSpinner, toolsNamesSpinner, toolsNumberSpinner;
    private RecyclerView recyclerView;

    private String[] toolsGroupsSpinnerTitles, toolsNamesSpinnerTitles, toolsNumbersSpinnerTitles;

    private ToolsAdapter adapter;

    private String groupUniqueId;
    private String provinceName;
    private String mapLongitude;
    private String mapLatitude;
    private String ratingBar;
    private String minimumMemberNumber;
    private String maximumMemberNumber;
    private String startTravelDateText;
    private String endTravelDateText;
    private String startDestinationText;
    private String endDestinationText;
    private String neededOnWayText;
    private String mealsText;
    private String moreNotesText;
    private String startTravelTimeText;
    private String endTravelTimeText;

    private int toolAddedNumber = 0;
    private boolean toolAdded = false;
    private boolean toolNumberAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_necesaary_tools);
        setupArrays();
        getIntentExtras();
        setupViews();
        setupSpinners();
        setupEvents();
        setupRecyclerView();
    }

    private void setupArrays() {
        attractions = new ArrayList<>();
        targetDestinationsNames = new ArrayList<>();
        toolsForShow = new ArrayList<>();
        toolsNames = new ArrayList<>();
        toolsNumbers = new ArrayList<>();
        toolsTypes = new ArrayList<>();
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        groupUniqueId = intent.getStringExtra("groupUniqueId");
        provinceName = intent.getStringExtra("provinceName");
        mapLongitude = intent.getStringExtra("mapLongitude");
        mapLatitude = intent.getStringExtra("mapLatitude");
        attractions.add(intent.getStringExtra("attraction1"));
        attractions.add(intent.getStringExtra("attraction2"));
        attractions.add(intent.getStringExtra("attraction3"));
        attractions.add(intent.getStringExtra("attraction4"));
        attractions.add(intent.getStringExtra("attraction5"));
        attractions.add(intent.getStringExtra("attraction6"));
        attractions.add(intent.getStringExtra("attraction7"));
        attractions.add(intent.getStringExtra("attraction8"));
        attractions.add(intent.getStringExtra("attraction9"));
        ratingBar = intent.getStringExtra("ratingBar");
        minimumMemberNumber = intent.getStringExtra("minimumMemberNumber");
        maximumMemberNumber = intent.getStringExtra("maximumMemberNumber");
        startTravelDateText = intent.getStringExtra("startTravelDateText");
        endTravelDateText = intent.getStringExtra("endTravelDateText");
        startDestinationText = intent.getStringExtra("startDestinationText");
        endDestinationText = intent.getStringExtra("endDestinationText");
        startTravelTimeText = intent.getStringExtra("startTravelTimeText");
        endTravelTimeText = intent.getStringExtra("endTravelTimeText");
        targetDestinationsNames.add(intent.getStringExtra("targetDestinationsName1"));
        targetDestinationsNames.add(intent.getStringExtra("targetDestinationsName2"));
        targetDestinationsNames.add(intent.getStringExtra("targetDestinationsName3"));
        targetDestinationsNames.add(intent.getStringExtra("targetDestinationsName4"));
        targetDestinationsNames.add(intent.getStringExtra("targetDestinationsName5"));
        neededOnWayText = intent.getStringExtra("neededOnWayText");
        mealsText = intent.getStringExtra("mealsText");
        moreNotesText = intent.getStringExtra("moreNotesText");
    }

    private void setupViews() {
        backBtn = findViewById(R.id.createGroupNecessaryTools_backBtn);
        nextStepBtn = findViewById(R.id.createGroupNecessaryTools_nextStepBtn);
        toolsGroupsSpinner = findViewById(R.id.createGroupNecessaryTools_toolsGroupsSpinner);
        toolsNamesSpinner = findViewById(R.id.createGroupNecessaryTools_toolsNamesSpinner);
        toolsNumberSpinner = findViewById(R.id.createGroupNecessaryTools_toolsNumbersSpinner);
        recyclerView = findViewById(R.id.createGroupNecessaryTools_recyclerView);
    }

    private void setupSpinners() {
        toolsGroupsSpinnerTitles = new String[]{
                "انتخاب کنید",
                "گروه اول",
                "گروه دوم",
                "گروه سوم"
        };
        toolsNamesSpinnerTitles = new String[]{
                "انتخاب کنید",
                "کوله پشتی",
                "چادر",
                "چوب دستی",
                "فلاسک",
                "طناب کوهنوردی",
                "چراغ قوه",
                "قطب نما",
                "کمک های اولیه",
                "چاقوی همه کاره"
        };
        toolsNumbersSpinnerTitles = new String[]{
                "انتخاب کنید", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
        };
        ArrayAdapter<String> groupsSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, toolsGroupsSpinnerTitles) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        ArrayAdapter<String> toolsNamesSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, toolsNamesSpinnerTitles) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        ArrayAdapter<String> toolNumberSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, toolsNumbersSpinnerTitles) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        groupsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolsNamesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolNumberSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolsGroupsSpinner.setAdapter(groupsSpinnerAdapter);
        toolsNamesSpinner.setAdapter(toolsNamesSpinnerAdapter);
        toolsNumberSpinner.setAdapter(toolNumberSpinnerAdapter);
    }

    private void setupEvents() {
        backBtn.setOnClickListener(v -> onBackPressed());
        nextStepBtn.setOnClickListener(v -> goToNextStep());
        toolsGroupsSpinner.setOnItemSelectedListener(this);
        toolsNamesSpinner.setOnItemSelectedListener(this);
        toolsNumberSpinner.setOnItemSelectedListener(this);
    }

    private void setupRecyclerView() {
        List<GroupTool> tools = new ArrayList<>();
        GroupTool tool = new GroupTool();
        tool.setToolName("");
        tool.setToolNumber("");
        tool.setToolType("");
        tools.add(tool);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ToolsAdapter(tools);
        recyclerView.setAdapter(adapter);
        tools.clear();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position != 0) {
            if (toolAddedNumber < 9) {
                int spinnerID = adapterView.getId();
                boolean exist = false;
                if (spinnerID == R.id.createGroupNecessaryTools_toolsNamesSpinner) {
                    if (toolsNames != null) {
                        for (int i = 0; i < toolsNames.size(); i++) {
                            if (toolsNames.get(i).equals(toolsNamesSpinnerTitles[position])) {
                                exist = true;
                                break;
                            }
                        }
                    }
                    if (!exist) {
                        if (toolAdded && !toolNumberAdded) {
                            toolsNames.remove(toolsNames.size() - 1);
                            toolsTypes.remove(toolsTypes.size() - 1);
                        }
                        toolsNames.add(toolsNamesSpinnerTitles[position]);
                        toolsTypes.add(String.valueOf(position));
                        toolAdded = true;
                        toolNumberAdded = false;
                    } else {
                        Toast.makeText(this, "این ابزار قبلا اضافه شده", Toast.LENGTH_SHORT).show();
                        toolsNamesSpinner.setSelection(0);
                        toolsNumberSpinner.setSelection(0);
                    }
                } else if (spinnerID == R.id.createGroupNecessaryTools_toolsNumbersSpinner) {
                    if (toolAdded) {
                        toolsNumbers.add(toolsNumbersSpinnerTitles[position]);
                        showItem();
                        toolAdded = false;
                        toolNumberAdded = true;
                        toolsNamesSpinner.setSelection(0);
                        toolsNumberSpinner.setSelection(0);
                    } else {
                        toolsNumberSpinner.setSelection(0);
                        Toast.makeText(this, "ابتدا ابزار مورد نظر را انتخاب کنید", Toast.LENGTH_SHORT).show();
                    }
                }
            } else Toast.makeText(this, "شما مجاز به انتخاب 9 ابزار هستید", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void showItem() {
        if (toolsForShow != null) toolsForShow.clear();
        for (int i = 0; i < toolsNames.size(); i++) {
            if (!toolsNames.get(i).isEmpty() && !toolsNumbers.get(i).isEmpty() && !toolsTypes.get(i).isEmpty()) {
                GroupTool tool = new GroupTool();
                tool.setToolName(toolsNames.get(i));
                tool.setToolNumber(toolsNumbers.get(i));
                tool.setToolType(toolsTypes.get(i));
                toolsForShow.add(tool);
            }
        }
        adapter.updateTools(toolsForShow);
        //spinner1.setSelection(0);
        toolsNamesSpinner.setSelection(0);
        toolsNumberSpinner.setSelection(0);
        toolAddedNumber++;
    }

    private void goToNextStep() {
        adapter.registerTools();
        Intent intent = new Intent(this, CreateGroupInformationActivity.class);
        intent.putExtra("groupUniqueId", groupUniqueId);
        intent.putExtra("provinceName", provinceName);
        intent.putExtra("mapLongitude", mapLongitude);
        intent.putExtra("mapLatitude", mapLatitude);
        intent.putExtra("attraction1", attractions.get(0));
        intent.putExtra("attraction2", attractions.get(1));
        intent.putExtra("attraction3", attractions.get(2));
        intent.putExtra("attraction4", attractions.get(3));
        intent.putExtra("attraction5", attractions.get(4));
        intent.putExtra("attraction6", attractions.get(5));
        intent.putExtra("attraction7", attractions.get(6));
        intent.putExtra("attraction8", attractions.get(7));
        intent.putExtra("attraction9", attractions.get(8));
        intent.putExtra("ratingBar", ratingBar);
        intent.putExtra("minimumMemberNumber", minimumMemberNumber);
        intent.putExtra("maximumMemberNumber", maximumMemberNumber);
        intent.putExtra("startTravelDateText", startTravelDateText);
        intent.putExtra("endTravelDateText", endTravelDateText);
        intent.putExtra("startDestinationText", startDestinationText);
        intent.putExtra("endDestinationText", endDestinationText);
        intent.putExtra("startTravelTimeText", startTravelTimeText);
        intent.putExtra("endTravelTimeText", endTravelTimeText);
        intent.putExtra("targetDestinationsName1", targetDestinationsNames.get(0));
        intent.putExtra("targetDestinationsName2", targetDestinationsNames.get(1));
        intent.putExtra("targetDestinationsName3", targetDestinationsNames.get(2));
        intent.putExtra("targetDestinationsName4", targetDestinationsNames.get(3));
        intent.putExtra("targetDestinationsName5", targetDestinationsNames.get(4));
        intent.putExtra("neededOnWayText", neededOnWayText);
        intent.putExtra("mealsText", mealsText);
        intent.putExtra("moreNotesText", moreNotesText);
        startActivity(intent);
    }

    private void registerTool(String toolName, String toolNumber, String toolType) {
        MainActivity.apiInterface.registerGroupTool(
                groupUniqueId,
                toolName,
                toolNumber,
                toolType
        ).enqueue(new Callback<GroupTool>() {
            @Override
            public void onResponse(@NonNull Call<GroupTool> call, @NonNull Response<GroupTool> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("", "Group Tool added");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GroupTool> call, @NonNull Throwable t) {
                Log.e("Network", "Register Group Tools Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ItemViewHolder> {

        List<GroupTool> tools;

        public ToolsAdapter(List<GroupTool> tools) {
            this.tools = tools;
        }

        @NonNull
        @Override
        public ToolsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ToolsAdapter.ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tool, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ToolsAdapter.ItemViewHolder holder, int position) {
            if (!tools.get(position).getToolName().isEmpty()) {
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
                    toolsNames.remove(position);
                    toolsNumbers.remove(position);
                    toolsTypes.remove(position);
                });
            }
        }

        @Override
        public int getItemCount() {
            return tools.size();
        }

        public void updateTools(List<GroupTool> tools) {
            this.tools = tools;
            notifyDataSetChanged();
        }

        public void registerTools() {
            for (int i = 0; i < tools.size(); i++) {
                registerTool(tools.get(i).getToolName(), tools.get(i).getToolNumber(), tools.get(i).getToolType());
            }
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

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
}