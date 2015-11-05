package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.AboutMeNumber;
import com.yilos.nailstar.aboutme.entity.MessageCount;
import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.aboutme.presenter.AboutMePresenter;
import com.yilos.widget.titlebar.TitleBar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutMeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutMeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutMeFragment extends Fragment implements IAboutMeView, View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RelativeLayout relativeLayout;
    private TextView messageCountText;//信息数
    private TitleBar titleBar;//标题栏
    private TextView titleText;//标题栏标题
    private ImageView backButton;
    private ImageView rightTwoButton;
    private ImageView rightOneButton;

    private AboutMePresenter aboutMePresenter;
    private RelativeLayout personInfoLayout;

    private TextView levelText;//等级
    private TextView attentionText;//关注
    private TextView fansText;//粉丝
    private TextView kaBiText;//咖币

    private ImageView profileImage;//头像
    private TextView nameText;//名字
    private TextView identityText;//身份

    private LinearLayout myFollowList;
    private LinearLayout myFansList;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutMeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutMeFragment newInstance(String param1, String param2) {
        AboutMeFragment fragment = new AboutMeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AboutMeFragment() {
        // Required empty public constructor
    }

    @Override
    public void initMessageCount(MessageCount messageCount) {
        if (messageCount == null) {
            return;
        }
        messageCountText.setText(String.valueOf(messageCount.getCount()));
    }

    private enum Level{lv1,lv2,lv3,lv4,lv5};
    public Level calcLevel (int exp) {
        if (exp >= 0 && exp < 50) {
            return Level.lv1;
        } else if (exp >= 50 && exp < 100) {
            return Level.lv2;
        } else if (exp >= 100 && exp < 200) {
            return Level.lv3;
        } else if (exp >= 200 && exp < 1000) {
            return Level.lv4;
        } else if (exp >= 1000) {
            return Level.lv5;
        }
        return Level.lv1;
    }

    @Override
    public void getPersonInfo(PersonInfo personInfo) {
        String nickName = personInfo.getNickname();
        Bitmap bitmap = personInfo.getImageBitmap();
        int type = personInfo.getType();
        String identity;
        //        1美甲店主
        //        2美甲师
        //        3美甲从业者
        //        4美甲消费者
        //        5美甲老师
        //        6其他
        switch (type) {
            case 1:
                identity = "美甲店主";
                break;
            case 2:
                identity = "美甲师";
                break;
            case 3:
                identity = "美甲从业者";
                break;
            case 4:
                identity = "美甲消费者";
                break;
            case 5:
                identity = "美甲老师";
                break;
            case 6:
                identity = "其他";
                break;
            default:
                identity = "身份";
                break;
        }
        nameText.setText(nickName);
        identityText.setText(identity);
        profileImage.setImageBitmap(bitmap);
    }

    @Override
    public void getAboutMeNumber(AboutMeNumber aboutMeNumber) {
        if (aboutMeNumber == null) {
            return;
        }
        kaBiText.setText(String.valueOf(aboutMeNumber.getDakaCoin()));
        int level = 1;
        Level exp = calcLevel(aboutMeNumber.getExp());
        switch (exp) {
            case lv1 :
                level = 1;
                break;
            case lv2 :
                level = 2;
                break;
            case lv3 :
                level = 3;
                break;
            case lv4 :
                level = 4;
                break;
            case lv5 :
                level = 5;
                break;
        }
        levelText.setText("lv" + String.valueOf(level));
        fansText.setText(String.valueOf(aboutMeNumber.getFansNumber()));
        attentionText.setText(String.valueOf(aboutMeNumber.getFocusNumber()));
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
        View view = inflater.inflate(R.layout.fragment_about_me, container, false);
        initViews(view);
        initEvents();
        return view;
    }

    private void initViews(View view){
        relativeLayout = (RelativeLayout)view.findViewById(R.id.about_me_message_group);
        messageCountText = (TextView)view.findViewById(R.id.about_me_message_count);

        nameText = (TextView)view.findViewById(R.id.about_me_name);
        identityText = (TextView)view.findViewById(R.id.about_me_identity);
        profileImage = (ImageView)view.findViewById(R.id.profile_image);

        levelText = (TextView) view.findViewById(R.id.about_me_level);
        attentionText = (TextView) view.findViewById(R.id.about_me_attention_count);
        fansText = (TextView) view.findViewById(R.id.about_me_fans_count);
        kaBiText = (TextView) view.findViewById(R.id.about_me_ka_bi_count);

        titleBar = (TitleBar)view.findViewById(R.id.about_me_header_nav);
        personInfoLayout = (RelativeLayout)view.findViewById(R.id.about_me_person_info_layout);

        myFollowList = (LinearLayout)view.findViewById(R.id.my_follow_list);
        myFansList = (LinearLayout)view.findViewById(R.id.my_fans_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_follow_list:
                Intent myFollowListIntent = new Intent(getActivity(),FollowListActivity.class);
                startActivity(myFollowListIntent);
                break;
            case R.id.my_fans_list:
                Intent myFansListIntent = new Intent(getActivity(),FansListActivity.class);
                startActivity(myFansListIntent);
                break;
            default:
                break;
        }
    }

    private void initEvents() {
        titleText = titleBar.getTitleView();
        titleText.setText(R.string.about_me_my);

        aboutMePresenter = AboutMePresenter.getInstance(this);
        aboutMePresenter.getMessageCount();
        aboutMePresenter.getAboutMeNumber();
        aboutMePresenter.getPersonInfo();

        myFollowList.setOnClickListener(this);
        myFansList.setOnClickListener(this);

        personInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PersonInfoActivity.class);
                startActivity(intent);
            }
        });
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
