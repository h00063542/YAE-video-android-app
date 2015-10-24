package com.yilos.nailstar.requirelession.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yilos.nailstar.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RequireLessionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RequireLessionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequireLessionFragment extends Fragment {

    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    public static final int CROP_IMAGE = 3;

    public static final String YILOS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yilos";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private File tempImage = new File(YILOS_PATH + "/temp.jpg");

    private File cropImage = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequireLessionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequireLessionFragment newInstance(String param1, String param2) {
        RequireLessionFragment fragment = new RequireLessionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RequireLessionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_require_lession, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
//        Button button = (Button) view.findViewById(R.id.hand_in_homework_btn);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                takeImage();
//            }
//        });
    }

    private void takeImage() {

        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (1 == item) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (2 == item) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

            super.onActivityResult(requestCode, resultCode, data);

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
        startActivityForResult(intent, CROP_IMAGE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
