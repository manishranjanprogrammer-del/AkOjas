package com.ojassoft.astrosage.ui.act;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.PdfFileData;
import com.ojassoft.astrosage.varta.adapters.PdfListAdapter;

import org.json.JSONArray;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TotalDownloadActivity extends BaseInputActivity {

    private RecyclerView recyclerView;
    private PdfListAdapter adapter;
    LinearLayout no_item_available;
    private List<Pair<String, String>> pdfFiles;


    public TotalDownloadActivity() {
        super(R.string.app_name);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_download);
        View includedLayout = findViewById(R.id.appbarAppModule);

        ImageView ivBack = includedLayout.findViewById(R.id.ivBack);
        recyclerView = findViewById(R.id.recyclerView);
        no_item_available = findViewById(R.id.no_item_available);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (!getSortedFileList(this).isEmpty() && getSortedFileList(this).size() >0){
            recyclerView.setVisibility(View.VISIBLE);
            no_item_available.setVisibility(View.GONE);
        }else {
            recyclerView.setVisibility(View.GONE);
            no_item_available.setVisibility(View.VISIBLE);
        }
        adapter = new PdfListAdapter(this, getSortedFileList(this));
        recyclerView.setAdapter(adapter);

        ivBack.setOnClickListener(v -> onBackPressed());

    }

    public List<PdfFileData> getSortedFileList(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyAppPdfPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String json = prefs.getString("file_list", null);
        //Log.d("jsonParser",json);
        Type type = new TypeToken<ArrayList<PdfFileData>>() {}.getType();

        if (json == null) {
            return new ArrayList<>(); // Return an empty list if no data exists
        }

        ArrayList<PdfFileData> fileList = gson.fromJson(json, type);

        // Sort list by last modified (latest first)
        //Collections.sort(fileList, (a, b) -> Long.compare(b.getLastModified(), a.getLastModified()));
        if (!fileList.isEmpty()){
            recyclerView.setVisibility(View.VISIBLE);
            no_item_available.setVisibility(View.GONE);
        }else {
            recyclerView.setVisibility(View.GONE);
            no_item_available.setVisibility(View.VISIBLE);
        }
        return fileList;
    }

}
