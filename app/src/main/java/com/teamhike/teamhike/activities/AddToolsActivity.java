package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.teamhike.teamhike.Models.Tool;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.SqliteDataBase.DataBase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddToolsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner toolsGroupsSpinner, toolsNamesSpinner, toolsNumbersSpinner;
    private RecyclerView recyclerView;
    private Button cancelBtn, submitBtn;

    private ArrayAdapter<String> generalToolsSpinnerAdapter;
    private ArrayAdapter<String> seaAndBeachToolsSpinnerAdapter;
    private ArrayAdapter<String> desertToolsSpinnerAdapter;
    private ArrayAdapter<String> jungleToolsSpinnerAdapter;
    private ArrayAdapter<String> mountainToolsSpinnerAdapter;
    private ArrayAdapter<String> firstAidToolsSpinnerAdapter;

    private List<Tool> toolsForShow;
    private List<String> toolsNames;
    private List<String> toolsNumbers;

    private String[] toolsGroupsSpinnerTitles;
    private String[] generalToolsSpinnerTitles;
    private String[] seaAndBeachToolsSpinnerTitles;
    private String[] desertToolsSpinnerTitles;
    private String[] jungleToolsSpinnerTitles;
    private String[] mountainToolsSpinnerTitles;
    private String[] firstAidToolsSpinnerTitles;
    private String[] toolsNumbersSpinnerTitles;
    private ToolsAdapter adapter;

    private int totalToolsAdded;
    private boolean toolGroupSelected;
    private boolean toolAdded;
    private boolean toolNumberAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tools);
        setupArrays();
        setupViews();
        setupSpinners();
        setupEvents();
        setupRecyclerView();
    }

    private void setupArrays() {
        toolsForShow = new ArrayList<>();
        toolsNames = new ArrayList<>();
        toolsNumbers = new ArrayList<>();
    }

    private void setupViews() {
        toolsGroupsSpinner = findViewById(R.id.addTools_toolsGroupsSpinner);
        toolsNamesSpinner = findViewById(R.id.addTools_toolsNamesSpinner);
        toolsNumbersSpinner = findViewById(R.id.addTools_toolsNumbersSpinner);
        recyclerView = findViewById(R.id.addTools_recyclerView);
        submitBtn = findViewById(R.id.addTools_submitBtn);
        cancelBtn = findViewById(R.id.addTools_cancelBtn);
    }

    private void setupSpinners() {
        toolsGroupsSpinnerTitles = new String[]{
                "انتخاب کنید",
                "عمومی",
                "گردشگری آبی / ساحلی",
                "بیابان و صحرا نوردی",
                "جنگل نوردی",
                "کوهنوردی",
                "کمک های اولیه"
        };
        generalToolsSpinnerTitles = new String[]{
                "انتخاب کنید",
                "اجاق گاز مسافرتی",
                "آیینه",
                "باتوم",
                "بادگیر",
                "بارونی",
                "پاوربانک",
                "تیشرت آستین بلند",
                "تیشرت ضد رطوبت",
                "چادر",
                "چاقو همه کاره",
                "چراغ قوه",
                "چراغ قوه سربند",
                "حوله مسافرتی",
                "خمیر دندان",
                "دستمال کاغذی",
                "زیرانداز کیسه خواب",
                "سوت",
                "شلوار ضد آب",
                "ضد آفتاب",
                "ضد عفونی کننده دست",
                "ظروف پخت و پز",
                "عینک آفتابی",
                "فندک / کبریت",
                "فیلتر آب",
                "قاشق و چنگال",
                "قطب نما",
                "کاپشن",
                "کرم ضد آفتاب",
                "کلاه ایمنی",
                "کلاه آفتابی",
                "کمک های اولیه",
                "کیسه خواب",
                "کیف کمری",
                "گتر",
                "لیوان",
                "نقشه"
        };
        seaAndBeachToolsSpinnerTitles = new String[]{
                "انتخاب کنید",
                "اجاق گاز مسافرتی",
                "آیینه",
                "جلیقه نجات",
                "چادر",
                "چادر",
                "چاقو همه کاره",
                "چراغ قوه",
                "چراغ قوه سربند",
                "حوله مسافرتی",
                "زیرانداز چادر",
                "زیرانداز کیسه خواب",
                "ساعت چند کاره ضد آب",
                "سوت",
                "شلوار ضد آب",
                "ظروف پخت و پز",
                "عینک آفتابی",
                "فندک / کبریت",
                "فیلتر آب",
                "کرم ضد آفتاب",
                "کفش ضد آب",
                "کلاه ایمنی",
                "کمک های اولیه",
                "کیسه خواب",
                "کیسه های شناور",
                "کیف محافظ ضد آب",
                "مایو"
        };
        desertToolsSpinnerTitles = new String[]{
                "انتخاب کنید",
                "اجاق گاز مسافرتی",
                "اسپری ضد حشرات",
                "آیینه",
                "پاوربانک",
                "پیراهن آستین بلند",
                "تیشرت نخی",
                "چادر",
                "چاقو همه کاره",
                "دستمال سر",
                "دستمال کاغذی",
                "دستمال گردن",
                "روشنایی",
                "زیرانداز",
                "زیرانداز کیسه خواب",
                "شلوار نخی",
                "صندل راحتی",
                "ضد آفتاب",
                "ظروف پخت و پز",
                "عینک آفتابی",
                "فندک / کبریت",
                "فیلتر آب",
                "قطب نما",
                "کاپشن",
                "کفش پیاده روی",
                "کلاه آفتابی",
                "کوله",
                "کیسه خواب",
                "کیف کمری",
                "ماسک صورت"
        };
        jungleToolsSpinnerTitles = new String[]{
                "انتخاب کنید",
                "اسپری ضد حشرات",
                "اجاق گاز مسافرتی",
                "آستر کیسه خواب",
                "باتوم",
                "بادگیر",
                "بارونی",
                "بطری آب",
                "پشه بند",
                "تخت آویز (هموک)",
                "تیشرت آستین بلند",
                "تیشرت آستین بلند",
                "تیشرت آستین کوتاه",
                "تیشرت ضد رطوبت",
                "جوراب پشمی",
                "چادر",
                "چاقو همه کاره",
                "چراغ قوه",
                "چراغ قوه سربند",
                "حوله مسافرتی",
                "خمیر دندان",
                "دستمال کاغذی",
                "روشنایی",
                "زیرانداز",
                "زیرانداز کیسه خواب",
                "سوت",
                "سویشرت سبک",
                "شلوار سبک",
                "شلوار ضد آب",
                "شلوار طبیعت گردی",
                "صندل طبیعت گردی",
                "ضد عفونی کننده دست",
                "ظروف پخت و پز",
                "عینک آفتابی",
                "فندک / کبریت",
                "فیلتر آب",
                "قاشق و چنگال",
                "قطب نما",
                "کاپشن",
                "کاپشن سیک",
                "کاور بارونی کوله",
                "کرم ضد آفتاب",
                "کفش",
                "کفش طبیعت گردی",
                "کلاه افتابی",
                "کمک های اولیه",
                "کوله پشتی",
                "کیسه خواب",
                "کیف کمری",
                "گتر",
                "لباس زیر",
                "لباس زیر ضد رطوبت",
                "لیوان",
                "نقشه"
        };
        mountainToolsSpinnerTitles = new String[]{
                "انتخاب کنید",
                "ابزار حمایت و فرود",
                "اجاق گاز مسافرتی",
                "ارتفاع سنج",
                "آتشزنه",
                "آیینه",
                "باتوم",
                "بادگیر",
                "بارونی",
                "بطری آب",
                "بیل",
                "پاوربانک",
                "پروب",
                "پله رکاب",
                "پنل خورشیدی",
                "پیراهن / تیشرت آستین بلند",
                "تبر یخ",
                "تیشرت ضد رطوبت",
                "جوراب",
                "جی پی اس",
                "چادر کوهنوردی",
                "چراغ قوه",
                "چراغ قوه سربند",
                "چراق قوه سربند",
                "خمیر دندان",
                "دستکش",
                "دستمال سر",
                "دستمال کاغذی",
                "روشنایی",
                "زیر پوش",
                "زیر شلواری",
                "زیرانداز کیسه خواب",
                "سوت",
                "شلوار کوهنوردی",
                "صابون خشک",
                "ضد عفونی کننده دست",
                "طناب",
                "ظروف پخت و پز",
                "عینک آفتابی",
                "فندک / کبریت",
                "فیلتر آب",
                "قاشق و چنگال",
                "قرقره",
                "قطب نما",
                "کاپشن گرم",
                "کارابین بدون قفل",
                "کارابین قفل دار",
                "کرامپون",
                "کفش کوهنوردی",
                "کلاه ایمنی",
                "کلاه پشمی",
                "کلاه کوهنوردی",
                "کمک های اولیه",
                "کوله پشتی کوهنوردی",
                "کیسه خواب",
                "گتر",
                "لیوان",
                "ماسک",
                "مسواک",
                "مهار",
                "نقشه"
        };
        firstAidToolsSpinnerTitles = new String[]{
                "انتخاب کنید",
                "اسپری ضد باکتری",
                "اسپری ضد حشرات",
                "آتل",
                "باند",
                "بتادین",
                "چسب زخم",
                "دستمال مرطوب",
                "قرص آنتی هیستامین",
                "قرص مسکن",
                "گاز استریل"
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
        generalToolsSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, generalToolsSpinnerTitles) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        seaAndBeachToolsSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, seaAndBeachToolsSpinnerTitles) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        desertToolsSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, desertToolsSpinnerTitles) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        jungleToolsSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jungleToolsSpinnerTitles) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        mountainToolsSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mountainToolsSpinnerTitles) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        firstAidToolsSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, firstAidToolsSpinnerTitles) {
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
        generalToolsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seaAndBeachToolsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        desertToolsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jungleToolsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mountainToolsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstAidToolsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolNumberSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolsGroupsSpinner.setAdapter(groupsSpinnerAdapter);
        toolsNamesSpinner.setAdapter(generalToolsSpinnerAdapter);
        toolsNumbersSpinner.setAdapter(toolNumberSpinnerAdapter);
    }

    private void setupEvents() {
        toolsGroupsSpinner.setOnItemSelectedListener(this);
        toolsNamesSpinner.setOnItemSelectedListener(this);
        toolsNumbersSpinner.setOnItemSelectedListener(this);
        submitBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    private void setupRecyclerView() {
        List<Tool> tools = new ArrayList<>();
        Tool tool = new Tool();
        tool.setToolName("");
        tool.setToolNumber("");
        tools.add(tool);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ToolsAdapter(this, tools);
        recyclerView.setAdapter(adapter);
        tools.clear();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.addTools_cancelBtn) {
            onBackPressed();
        } else if (viewId == R.id.addTools_submitBtn) {
            saveItems(this);
        }
    }

    private void saveItems(Context context) {
        String userUniqueId = new DataBase(this).getActiveLocalUser().getUniqueId();
        MainActivity.apiInterface.getTools(userUniqueId).enqueue(new Callback<List<Tool>>() {
            @Override
            public void onResponse(@NonNull Call<List<Tool>> call, @NonNull Response<List<Tool>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Tool> tools = response.body();
                    adapter.saveItem(userUniqueId, tools);
                    Toast.makeText(context, "تجهیزات ذخیره شدند", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Tool>> call, @NonNull Throwable t) {
                Log.e("Network", "Get Tool Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private boolean isItemExist(List<Tool> tools, String itemName) {
        boolean exist = false;
        if (tools != null) {
            for (Tool tool : tools) {
                if (itemName.equals(tool.getToolName())) {
                    exist = true;
                    break;
                }
            }
        }
        return exist;
    }

    private void registerTool(String toolName, String toolNumber) {
        String userUniqueId = new DataBase(this).getActiveLocalUser().getUniqueId();
        MainActivity.apiInterface.registerTool(userUniqueId, toolName, toolNumber).enqueue(new Callback<Tool>() {
            @Override
            public void onResponse(@NonNull Call<Tool> call, @NonNull Response<Tool> response) {}

            @Override
            public void onFailure(@NonNull Call<Tool> call, @NonNull Throwable t) {
                Log.e("Network", "Register Tool Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void updateTool(Context context, String userUniqueId, String toolName, String toolNum) {
        MainActivity.apiInterface.updateTool(userUniqueId, toolName, toolNum).enqueue(new Callback<Tool>() {
            @Override
            public void onResponse(@NonNull Call<Tool> call, @NonNull Response<Tool> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResponse().equals("successfully")) {
                        Toast.makeText(context, "تجهیزات بروزرسانی شدند", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Tool> call, @NonNull Throwable t) {
                Log.e("Network", "Update Tool Failure: " + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position != 0) {
            if (totalToolsAdded < 20) {
                int spinnerID = adapterView.getId();
                boolean exist = false;
                if (spinnerID == R.id.addTools_toolsGroupsSpinner) {
                    if(position == 1) {
                        toolsNamesSpinner.setAdapter(generalToolsSpinnerAdapter);
                    } else if (position == 2) {
                        toolsNamesSpinner.setAdapter(seaAndBeachToolsSpinnerAdapter);
                    } else if (position == 3) {
                        toolsNamesSpinner.setAdapter(desertToolsSpinnerAdapter);
                    } else if (position == 4) {
                        toolsNamesSpinner.setAdapter(jungleToolsSpinnerAdapter);
                    } else if (position == 5) {
                        toolsNamesSpinner.setAdapter(mountainToolsSpinnerAdapter);
                    } else {
                        toolsNamesSpinner.setAdapter(firstAidToolsSpinnerAdapter);
                    }
                    toolGroupSelected = true;
                } else if (spinnerID == R.id.addTools_toolsNamesSpinner) {
                    if (toolGroupSelected) {
//                    Log.d("debug", "onItemSelected: " + toolsNamesSpinner.getSelectedItem().toString());
                        String selectedToolName = toolsNamesSpinner.getSelectedItem().toString();
                        if (toolsNames != null) {
                            for (String toolName : toolsNames) {
                                if (toolName.equals(selectedToolName)) {
                                    exist = true;
                                    break;
                                }
                            }
                        }
                        if (!exist) {
                            if (toolAdded && !toolNumberAdded) {
                                if (toolsNames != null) {
                                    toolsNames.remove(toolsNames.size() - 1);
                                }
                            }
                            if (toolsNames != null) {
                                toolsNames.add(selectedToolName);
                            }
                            toolAdded = true;
                            toolNumberAdded = false;
                        } else {
                            Toast.makeText(this, "این ابزار قبلا اضافه شده", Toast.LENGTH_SHORT).show();
                            toolsNamesSpinner.setSelection(0);
                            toolsNumbersSpinner.setSelection(0);
                        }
                    } else {
                        Toast.makeText(this, "گروه ابزار مشخص نشده است", Toast.LENGTH_SHORT).show();
                        toolsNamesSpinner.setSelection(0);
                        toolsNumbersSpinner.setSelection(0);
                    }
                } else if (spinnerID == R.id.addTools_toolsNumbersSpinner) {
                    if (toolAdded) {
                        toolsNumbers.add(toolsNumbersSpinnerTitles[position]);
                        showItem();
                        toolAdded = false;
                        toolNumberAdded = true;
                        toolsNamesSpinner.setSelection(0);
                        toolsNumbersSpinner.setSelection(0);
                    } else {
                        toolsNumbersSpinner.setSelection(0);
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
            if (!toolsNames.get(i).isEmpty() && !toolsNumbers.get(i).isEmpty()) {
                Tool tool = new Tool();
                tool.setToolName(toolsNames.get(i));
                tool.setToolNumber(toolsNumbers.get(i));
                toolsForShow.add(tool);
            }
        }
        adapter.updateTools(toolsForShow);
        //spinner1.setSelection(0);
        toolsNamesSpinner.setSelection(0);
        toolsNumbersSpinner.setSelection(0);
        totalToolsAdded++;
    }

    private class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ItemViewHolder> {

        Context context;
        List<Tool> tools;

        public ToolsAdapter(Context context, List<Tool> tools) {
            this.context = context;
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
                holder.deleteBtn.setOnClickListener(v -> {
                    tools.remove(position);
                    updateTools(tools);
                    toolsNames.remove(position);
                    toolsNumbers.remove(position);
                    totalToolsAdded--;
                });
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

        public void saveItem(String userUniqueId, List<Tool> userTools) {
            boolean exist;
            for (int i = 0; i < tools.size(); i++) {
                exist = isItemExist(userTools, tools.get(i).getToolName());
                if (exist) {
                    updateTool(context, userUniqueId, tools.get(i).getToolName(), tools.get(i).getToolNumber());
                } else {
                    registerTool(tools.get(i).getToolName(), tools.get(i).getToolNumber());
                }
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