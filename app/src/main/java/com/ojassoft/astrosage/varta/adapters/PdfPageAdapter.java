package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.PdfViewerActivity;

public class PdfPageAdapter extends RecyclerView.Adapter<PdfPageAdapter.PdfViewHolder> {
    private final PdfRenderer pdfRenderer;
    Context mContext;
    public PdfPageAdapter(PdfRenderer pdfRenderer,Context context) {
        this.pdfRenderer = pdfRenderer;
        this.mContext = context;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        ImageView imageView = new ImageView(parent.getContext());
//        imageView.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        ));
//        imageView.setAdjustViewBounds(true);
//        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        return new PdfViewHolder(imageView);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf_viewer, parent, false);
        return new PdfViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        //PdfRenderer.Page page = pdfRenderer.openPage(position);

//        int width = holder.pdfPageImage.getResources().getDisplayMetrics().widthPixels;
//        int height = (int) ((double) width / page.getWidth() * page.getHeight());
//
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//        page.close();

        //holder.imageView.setImageBitmap(bitmap);
        //holder.imageView.setBackgroundColor(0);
        if (position==0){
            holder.viewDivider.setVisibility(View.GONE);
        }else {
            holder.viewDivider.setVisibility(View.VISIBLE);
        }
        PdfRenderer.Page page = null;
        try {
            page = pdfRenderer.openPage(position);

            // Improved resolution for better clarity
            int dpiScale = 2; // Increase this for sharper rendering (try 3 or 4 if needed)
            int width = page.getWidth() * dpiScale;
            int height = page.getHeight() * dpiScale;

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            holder.pdfPageImage.setImageBitmap(bitmap);

            holder.pdfPageImage.setOnClickListener(view -> {
                if (mContext instanceof PdfViewerActivity) {
                    ((PdfViewerActivity) mContext).hideAndShow();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (page != null) {
                page.close(); // Ensure page is closed to prevent memory leaks
            }
        }
    }

    @Override
    public int getItemCount() {
        return pdfRenderer != null ? pdfRenderer.getPageCount() : 0;
    }

    public static class PdfViewHolder extends RecyclerView.ViewHolder {
        ImageView pdfPageImage;
        View viewDivider;


        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfPageImage = itemView.findViewById(R.id.imgPdfView);
            viewDivider = itemView.findViewById(R.id.viewDivider);
        }
    }
}
