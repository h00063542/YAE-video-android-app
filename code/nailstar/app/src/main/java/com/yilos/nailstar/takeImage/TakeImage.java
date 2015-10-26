package com.yilos.nailstar.takeImage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

public class TakeImage {

    private Uri resultUri;

    private int requestCamera;
    private int requestSelectFile;
    private int requestCropImage;

    private Object context;
    private Activity activity;
    private TakeImageCallback callback;
    private Uri uri;

    private int aspectX;
    private int aspectY;
    private int outputX;
    private int outputY;

    public static class Builder {

        private int requestCode = 0;

        private Activity activity;
        private Object context;
        private TakeImageCallback callback;
        private Uri uri;

        private int aspectX = 1;
        private int aspectY = 1;
        private int outputX = 400;
        private int outputY = 400;

        public Builder requestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        };

        public Builder context(Activity object) {
            this.context = object;
            return this;
        }

        public Builder context(Fragment object) {
            this.context = object;
            return this;
        }

        public Builder callback(TakeImageCallback callback) {
            this.callback = callback;
            return this;
        }

        public Builder uri(Uri uri) {
            this.uri = uri;
            return this;
        }

        public Builder uri(File uri) {
            this.uri = Uri.fromFile(uri);
            return this;
        }

        public Builder uri(String uri) {
            this.uri = Uri.fromFile(new File(uri));
            return this;
        }

        public Builder aspectX(int aspectX) {
            this.aspectX = aspectX;
            return this;
        }

        public Builder aspectY(int aspectY) {
            this.aspectY = aspectY;
            return this;
        }

        public Builder outputX(int outputX) {
            this.outputX = outputX;
            return this;
        }

        public Builder outputY(int outputY) {
            this.outputY = outputY;
            return this;
        }

        public TakeImage build() {
            initEmptyField();
            return new TakeImage(this);
        }

        private void initEmptyField() {

            if (context == null) {
                throw new IllegalArgumentException("context is null");
            }

            if (context instanceof Activity) {
                activity = (Activity)context;
            }
            if (context instanceof Fragment) {
                activity = ((Fragment)context).getActivity();
            }

            if (uri == null) {
                throw new IllegalArgumentException("uri is null");
            }

            if (new File(uri.getPath()).isFile()){
                throw new IllegalArgumentException("uri is not directory");
            }

            if (callback == null) {
                callback = new TakeImageCallback() {
                    @Override
                    public void callback(Uri resultUri) {
                        Log.d(TakeImage.class.getName(), "callback " + resultUri);
                    }
                };
            }
        }
    }

    private TakeImage(Builder builder) {
        requestCamera = builder.requestCode;
        requestSelectFile =  builder.requestCode + 1;
        requestCropImage = builder.requestCode + 2;
        context = builder.context;
        activity = builder.activity;
        callback = builder.callback;
        uri = builder.uri;
        aspectX = builder.aspectX;
        aspectY = builder.aspectY;
        outputX = builder.outputX;
        outputY = builder.outputY;
    }

    private String YILOS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yilos";

    private File tempImage = new File(YILOS_PATH + "/temp.jpg");

    public void initTakeImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
                    if (context instanceof Activity) {
                        ((Activity)context).startActivityForResult(intent, requestCamera);
                    }
                    if (context instanceof Fragment) {
                        ((Fragment)context).startActivityForResult(intent, requestCamera);
                    }
                } else if (1 == item) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    if (context instanceof Activity) {
                        ((Activity)context).startActivityForResult(Intent.createChooser(intent, "Select File"),
                                requestSelectFile);
                    }
                    if (context instanceof Fragment) {
                        ((Fragment)context).startActivityForResult(Intent.createChooser(intent, "Select File"),
                                requestSelectFile);
                    }

                } else if (2 == item) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == requestCamera) {

                generateFileUri();
                cropImageUri(Uri.fromFile(tempImage), resultUri);

            } else if (requestCode == requestSelectFile) {

                generateFileUri();
                cropImageUri(data.getData(), resultUri);

            } else if (requestCode == requestCropImage) {
                callback.callback(resultUri);
            }
        }
    }

    private void generateFileUri() {
        File resultFile = new File(uri.getPath() + "/" + System.currentTimeMillis() + ".jpg");
        if (!resultFile.getParentFile().exists()) {
            resultFile.getParentFile().mkdirs();
        }
        resultUri = Uri.fromFile(resultFile);
    }

    private void cropImageUri(Uri imageUri, Uri resultUri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, resultUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        if (context instanceof Activity) {
            ((Activity)context).startActivityForResult(intent, requestCropImage);
        }
        if (context instanceof Fragment) {
            ((Fragment)context).startActivityForResult(intent, requestCropImage);
        }
    }
}
