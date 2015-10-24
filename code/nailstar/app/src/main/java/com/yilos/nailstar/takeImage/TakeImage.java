package com.yilos.nailstar.takeImage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

public class TakeImage {

    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    public static final int CROP_IMAGE = 3;

    public static final String YILOS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yilos";

    private File tempImage = new File(YILOS_PATH + "/temp.jpg");

    private File cropImage = null;

    private Activity context;

    public void onCreate() {
        initMenu();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                File yilosDir = new File(YILOS_PATH);
                if (!yilosDir.exists()) {
                    yilosDir.mkdirs();
                }

                cropImage = new File(YILOS_PATH + "/" + System.currentTimeMillis() + ".jpg");
                Uri cropImageUri = Uri.fromFile(cropImage);
                cropImageUri(Uri.fromFile(tempImage), cropImageUri, 400, 400);

            } else if (requestCode == SELECT_FILE) {

                File yilosDir = new File(YILOS_PATH);
                if (!yilosDir.exists()) {
                    yilosDir.mkdirs();
                }

                cropImage = new File(YILOS_PATH + "/" + System.currentTimeMillis() + ".jpg");
                Uri cropImageUri = Uri.fromFile(cropImage);
                cropImageUri(data.getData(), cropImageUri, 400, 400);

            } else if (requestCode == CROP_IMAGE) {
                String imagePath = "";
                if (cropImage != null) {
                    imagePath = cropImage.getAbsolutePath();
                }
                // TODO
            }
        }
    }

    private void cropImageUri(Uri src, Uri desc, int outputX, int outputY){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(src, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, desc);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        context.startActivityForResult(intent, CROP_IMAGE);
    }

    private void initMenu() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (0 == item) {
                    File yilosDir = new File(YILOS_PATH);
                    if (!yilosDir.exists()) {
                        yilosDir.mkdirs();
                    }
                    Uri imageUri = Uri.fromFile(tempImage);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    context.startActivityForResult(intent, REQUEST_CAMERA);
                } else if (1 == item) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    context.startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (2 == item) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

}
