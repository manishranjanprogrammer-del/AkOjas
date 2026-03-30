package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.PdfFileData;
import com.ojassoft.astrosage.ui.act.PdfViewerActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PdfListAdapter extends RecyclerView.Adapter<PdfListAdapter.ViewHolder> {

    //    private Context context;
//    private ArrayList<String> pdfList;
//    private ArrayList<String> pdfPaths;
//    private static final String PREF_NAME = "PdfDownloads";
//    private static final String KEY_PDF_LIST = "downloaded_pdfs";
//
//    public PdfListAdapter(Context context, ArrayList<String> pdfList, ArrayList<String> pdfPaths) {
//        this.context = context;
//        this.pdfList = pdfList;
//        this.pdfPaths = pdfPaths;
//    }
    private final List<PdfFileData> pdfList;
    private final Context context;

    public PdfListAdapter(Context context, List<PdfFileData> pdfList) {
        this.context = context;
        Collections.reverse(pdfList); // Reverse list here
        this.pdfList = pdfList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pdf, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        String pdfName = pdfList.get(position);
//        String pdfPath = pdfPaths.get(position);
//
//        holder.tvPdfName.setText(pdfName);
//
//        holder.itemView.setOnClickListener(v -> {
//            File file = new File(pdfPath);
//            if (!file.exists()) {
//                Toast.makeText(context, "File deleted. Download again!", Toast.LENGTH_LONG).show();
//                removePdf(position);
//                return;
//            }
//
//            Intent intent = new Intent(context, PdfViewerActivity.class);
//            intent.putExtra("pdf_path", pdfPath);
//            context.startActivity(intent);
//        });
//
//        holder.ivDelete.setOnClickListener(v -> removePdf(position));
        PdfFileData fileData = pdfList.get(position);
        holder.fileName.setText(fileData.getKundliName().trim());
        holder.fileDate.setText(convertDateFormat(fileData.getDateOfBirth())+", "+fileData.getTimeOfBirth()+", "+fileData.getAddress());

        // Set onClickListener to open the PDF
        holder.itemView.setOnClickListener(v -> {
            openPdf(fileData.getFilePath(),fileData.getKundliName(),fileData.getIsFromMatchMaking(),fileData.getGetLocaleChartId(),fileData.getReportType());
            //AstrosageKundliApplication.userKundliName = fileData.getKundliName();
                }
        );

        if (fileData.getPlanId()> 1){
            holder.imgViewPremium.setVisibility(View.VISIBLE);
        }else {
            holder.imgViewPremium.setVisibility(View.GONE);
        }

// Hide divider for the last item
//        if (position == pdfList.size() - 1) {
//            holder.dividerView.setVisibility(View.GONE);
//        } else {
//            holder.dividerView.setVisibility(View.VISIBLE);
//        }

        FontUtils.changeFont(context, holder.fileName, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, holder.fileDate, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        if(fileData.getIsFromMatchMaking()){
            holder.imgViewPdf.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pdf_both));
        }else {
            if (fileData.getGender().equals("F")){
                holder.imgViewPdf.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pdf_female));
            }else if(fileData.getGender().equals("M")) {
                holder.imgViewPdf.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pdf_male));
            }
        }

    }

    public  String convertDateFormat(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return dateStr; // Return original if parsing fails
        }
    }

    // Method to open the PDF
    private void openPdf(String fileName, String kundliName, boolean isFromMatchMaking, long getLocaleChartId, String reportType) {

        File file = new File(fileName);

        Intent intent = new Intent(context, PdfViewerActivity.class);
        intent.putExtra(CGlobalVariables.PDF_FILE, file.getAbsolutePath());
        intent.putExtra(CGlobalVariables.IS_FROM_MATCH_MAKING, isFromMatchMaking);
        intent.putExtra(CGlobalVariables.PDF_KUNDLI_TOP_TITLE, kundliName);
        intent.putExtra("is_from_Receiver", false);
        intent.putExtra("local_chart_id", String.valueOf(getLocaleChartId));
        intent.putExtra("report_type", reportType);

        context.startActivity(intent);
    }
//    private void removePdf(int position) {
//        String filePath = pdfPaths.get(position);
//        File file = new File(filePath);
//
//        if (file.exists()) {
//            file.delete();
//        }
//
//        pdfList.remove(position);
//        pdfPaths.remove(position);
//        notifyItemRemoved(position);
//
//        saveUpdatedList();
//        Toast.makeText(context, "PDF Deleted!", Toast.LENGTH_SHORT).show();
//    }

//    private void saveUpdatedList() {
//        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        JSONArray jsonArray = new JSONArray();
//
//        for (String path : pdfPaths) {
//            jsonArray.put(path);
//        }
//
//        editor.putString(KEY_PDF_LIST, jsonArray.toString());
//        editor.apply();
//    }

    @Override
    public int getItemCount() {

        //return pdfList.size();
        return pdfList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //        TextView tvPdfName;
//        ImageView ivDelete;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvPdfName = itemView.findViewById(R.id.tvPdfName);
//            ivDelete = itemView.findViewById(R.id.ivDelete);
//        }
        TextView fileName, fileDate;
        View dividerView;
        ImageView imgViewPdf,imgViewPremium;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.fileName);
            fileDate = itemView.findViewById(R.id.fileDate);
            dividerView = itemView.findViewById(R.id.dividerView);
            imgViewPdf = itemView.findViewById(R.id.imgViewPdf);
            imgViewPremium = itemView.findViewById(R.id.imgViewPremium);

        }
    }
}