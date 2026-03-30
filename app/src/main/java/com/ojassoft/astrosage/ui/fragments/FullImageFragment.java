package com.ojassoft.astrosage.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ojassoft.astrosage.R;

/**
 * Created by ojas on ६/९/१६.
 */
public class FullImageFragment extends DialogFragment {
    ImageView fullImage;
    Bitmap bitmap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bitmap = getArguments().getParcelable("bitmap");

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.full_image_view, null,
                false);
//R.style.MyCustomTheme
        //final Dialog dialog = new Dialog(getActivity(), R.style.CustomDialog);

        final Dialog dialog = new Dialog(getActivity(), R.style.CustomDialog);
        dialog.setContentView(view);
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(height, width);
        fullImage = (ImageView) dialog.findViewById(R.id.full_image);
        Button leftRotate = (Button) dialog.findViewById(R.id.rotateleft);
        Button rightRotate = (Button) dialog.findViewById(R.id.rotateright);
        fullImage.setImageBitmap(bitmap);
        rightRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = rotateImage(bitmap, (float) 90);
// fullImage.setImageBitmap(bitmap);
            }
        });
        leftRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = rotateImage(bitmap, (float) -90);
//fullImage.setImageBitmap(bitmap);
            }
        });

        return dialog;
    }


    public FullImageFragment newInstance(Bitmap fullBitmap) {
        FullImageFragment fullImageFragment = new FullImageFragment();
        Bundle b = new Bundle();
        b.putParcelable("bitmap", fullBitmap);
        fullImageFragment.setArguments(b);
        return fullImageFragment;
    }

    private Bitmap rotateImage(Bitmap bitmap, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
        fullImage.setImageBitmap(rotated);
        return rotated;
    }
}