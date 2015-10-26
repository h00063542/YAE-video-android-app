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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yilos.nailstar.R;
import com.yilos.nailstar.takeImage.TakeImage;
import com.yilos.nailstar.takeImage.TakeImageCallback;

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

    private OnFragmentInteractionListener mListener;

    private static final String YILOS_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yilos";
    private TakeImage takeImage;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RequireLessionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequireLessionFragment newInstance() {
        RequireLessionFragment fragment = new RequireLessionFragment();
        return fragment;
    }

    public RequireLessionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        takeImage = new TakeImage.Builder().context(this).uri(YILOS_PATH).callback(new TakeImageCallback() {
            @Override
            public void callback(Uri uri) {
                Log.d(RequireLessionFragment.class.getName(), "callback " + uri);
            }
        }).build();
        Button button = (Button) view.findViewById(R.id.hand_in_homework_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImage.initTakeImage();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takeImage.onActivityResult(requestCode, resultCode, data);
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
