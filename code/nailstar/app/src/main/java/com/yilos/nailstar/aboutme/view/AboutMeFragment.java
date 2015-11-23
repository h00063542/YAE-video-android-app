package com.yilos.nailstar.aboutme.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.aboutme.entity.AboutMeNumber;
import com.yilos.nailstar.aboutme.entity.MessageCount;
import com.yilos.nailstar.aboutme.entity.PersonInfo;
import com.yilos.nailstar.aboutme.model.LoginAPI;
import com.yilos.nailstar.aboutme.presenter.AboutMePresenter;
import com.yilos.nailstar.aboutme.requirelesson.view.RequireLessonListActivity;
import com.yilos.nailstar.social.model.SocialAPI;
import com.yilos.nailstar.util.Constants;
import com.yilos.nailstar.util.DateUtil;
import com.yilos.nailstar.util.IdentityUtil;
import com.yilos.nailstar.util.LevelUtil;
import com.yilos.widget.circleimageview.CircleImageView;
import com.yilos.widget.titlebar.TitleBar;

import org.json.JSONArray;
import org.json.JSONObject;

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

    private RelativeLayout messageGroup;
    private RelativeLayout aboutMeSetting;
    private LinearLayout aboutMeMyInfo;
    private TextView messageCountText;//信息数
    private TitleBar titleBar;//标题栏
    private TextView titleText;//标题栏标题

    private RelativeLayout personInfoLayout;

    private RelativeLayout aboutMeRequireLesson;

    private TextView levelText;//等级
    private TextView attentionText;//关注
    private TextView fansText;//粉丝
    private TextView kaBiText;//咖币

    private CircleImageView profileImage;//头像
    private TextView nameText;//名字
    private TextView identityText;//身份

    private LinearLayout myFollowList;//关注列表
    private LinearLayout myFansList;//粉丝列表

    private View downloadVideoBtn;

    private TextView aboutMeLevel;//进入等级页面按钮

    private int experience;
    private String myImageUrl;
    private int identityType;
    private String nickName;
    private String profile;
    private String uid;

    LoginAPI loginAPI = LoginAPI.getInstance();
    PersonInfo personInfo = new PersonInfo();
    AboutMePresenter aboutMePresenter = AboutMePresenter.getInstance(this);

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
        int count = messageCount.getCount() + getLatestMessageCount();
        messageCountText.setText(String.valueOf(count));
        setLatestMessageCount(count);
        if (!messageCountText.getText().toString().equals("0")) {
            messageCountText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getAboutMeNumber(AboutMeNumber aboutMeNumber) {
        if (aboutMeNumber == null) {
            return;
        }
        experience = aboutMeNumber.getExp();
        kaBiText.setText(String.valueOf(aboutMeNumber.getDakaCoin()));
        int level =  LevelUtil.calcLevel(aboutMeNumber.getExp());
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

    @Override
    public void judgeLogin() {
        if (loginAPI.isLogin()) {
            String uid = loginAPI.getLoginUserId();
            int type = loginAPI.getLoginUserType();
            personInfo.setUid(uid);
            personInfo.setNickname(loginAPI.getLoginUserNickname());
            personInfo.setType(type);
            personInfo.setPhotoUrl(loginAPI.getLoginUserPhotourl());
            personInfo.setProfile(loginAPI.getLoginUserProfile());
            profileImage.setImageSrc(personInfo.getPhotoUrl());
            nameText.setText(personInfo.getNickname());
            identityText.setText(IdentityUtil.getIdentity(personInfo.getType()));

            aboutMePresenter.getMessageCount(getLatestMessageCountTime(), uid, type);
            long lt = DateUtil.getTimestamp();
            setLatestMessageCountTime(lt);
        } else {
            identityText.setText(R.string.about_me_identity);
            nameText.setText(R.string.about_me_name);
            profileImage.setImageResource(R.mipmap.ic_default_photo);
            messageCountText.setVisibility(View.GONE);
        }
    }

    @Override
    public int getLatestMessageCount() {
        SharedPreferences mySharedPreferences= getActivity().getSharedPreferences(Constants.MESSAGES,
                Activity.MODE_PRIVATE);
        int count = mySharedPreferences.getInt(Constants.LATEST_MESSAGE_COUNT, 0);
        return count;
    }

    @Override
    public void setLatestMessageCount(int count) {
        SharedPreferences mySharedPreferences= getActivity().getSharedPreferences(Constants.MESSAGES,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt(Constants.LATEST_MESSAGE_COUNT, count);
        editor.commit();
    }

    @Override
    public long getLatestMessageCountTime() {
        SharedPreferences mySharedPreferences= getActivity().getSharedPreferences(Constants.MESSAGES,
                Activity.MODE_PRIVATE);
        long time = mySharedPreferences.getLong(Constants.LATEST_MESSAGE_COUNT_TIME, 0);
        return time;
    }

    @Override
    public void setLatestMessageCountTime(long lt) {
        SharedPreferences mySharedPreferences= getActivity().getSharedPreferences(Constants.MESSAGES,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putLong(Constants.LATEST_MESSAGE_COUNT_TIME, lt);
        editor.commit();
    }

    private void initViews(View view){
        aboutMeRequireLesson = (RelativeLayout) view.findViewById(R.id.about_me_require_lesson);
        aboutMeSetting = (RelativeLayout) view.findViewById(R.id.about_me_setting_group);
        aboutMeMyInfo = (LinearLayout) view.findViewById(R.id.about_me_my_info);
        messageGroup = (RelativeLayout) view.findViewById(R.id.about_me_message_group);
        messageCountText = (TextView) view.findViewById(R.id.about_me_message_count);

        nameText = (TextView) view.findViewById(R.id.about_me_name);
        identityText = (TextView) view.findViewById(R.id.about_me_identity);
        profileImage = (CircleImageView) view.findViewById(R.id.profile_image);

//        levelText = (TextView) view.findViewById(R.id.about_me_level);
//        attentionText = (TextView) view.findViewById(R.id.about_me_attention_count);
//        fansText = (TextView) view.findViewById(R.id.about_me_fans_count);
//        kaBiText = (TextView) view.findViewById(R.id.about_me_ka_bi_count);

        titleBar = (TitleBar) view.findViewById(R.id.about_me_header_nav);
        personInfoLayout = (RelativeLayout) view.findViewById(R.id.about_me_person_info_layout);

//        myFollowList = (LinearLayout)view.findViewById(R.id.my_follow_list);
//        myFansList = (LinearLayout)view.findViewById(R.id.my_fans_list);
//
//        aboutMeLevel = (TextView)view.findViewById(R.id.about_me_level);

        downloadVideoBtn = view.findViewById(R.id.downloadVideoBtn);

        final Activity activity = getActivity();
        view.findViewById(R.id.about_me_share_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialAPI.getInstance().share(activity, "美甲大咖，行业最专业的视频教学App", "我试过很多美甲App，最后还是选择了美甲大咖。真爱，经得起等待！", "http://s.naildaka.com/site/share_app.html", R.mipmap.ic_daka_share_image);
            }
        });
    }

    @Override
    public void onClick(View v) {
        LoginAPI loginAPI = LoginAPI.getInstance();
        switch (v.getId()) {
//            case R.id.my_follow_list:
//                Intent myFollowListIntent = new Intent(getActivity(),FollowListActivity.class);
//                startActivity(myFollowListIntent);
//                break;
//            case R.id.my_fans_list:
//                Intent myFansListIntent = new Intent(getActivity(),FansListActivity.class);
//                startActivity(myFansListIntent);
//                break;
//            case R.id.about_me_level:
//                Intent myLevelIntent = new Intent(getActivity(),LevelActivity.class);
//                myLevelIntent.putExtra("experience",experience);
//                myLevelIntent.putExtra("myImageUrl", myImageUrl);
//                startActivity(myLevelIntent);
//                break;
            case R.id.about_me_setting_group:
                Intent settingIntent = new Intent(getActivity(),SettingActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.about_me_my_info:
                if (!loginAPI.isLogin()) {
                    Intent goToLoginIntent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(goToLoginIntent);
                } else {

                }
                break;
            case R.id.downloadVideoBtn:
                    Intent downloadIntent = new Intent(getActivity(),DownloadVideo.class);
                    startActivity(downloadIntent);
                break;
            case R.id.about_me_person_info_layout:
                if (!loginAPI.isLogin()) {
                    loginAPI.gotoLoginPage(getActivity());
                } else {
                    Intent intent = new Intent(getActivity(),PersonInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("personInfo",personInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.about_me_message_group:
                if (!loginAPI.isLogin()) {
                    loginAPI.gotoLoginPage(getActivity());
                } else {
                    setLatestMessageCount(Constants.ZERO);
                    Intent intent = new Intent(getActivity(),MessageActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.about_me_require_lesson:
                if (!loginAPI.isLogin()) {
                    Intent goToLoginIntent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(goToLoginIntent);
                } else {
                    Intent requireLessonIntent = new Intent(getActivity(),RequireLessonListActivity.class);
                    startActivity(requireLessonIntent);
                }
                break;
            default:
                break;
        }
    }

    private void initEvents() {
        titleText = titleBar.getTitleView();
        titleText.setText(R.string.about_me_my);

//        myFollowList.setOnClickListener(this);
//        myFansList.setOnClickListener(this);
//        aboutMeLevel.setOnClickListener(this);
        aboutMeMyInfo.setOnClickListener(this);
        aboutMeSetting.setOnClickListener(this);

        personInfoLayout.setOnClickListener(this);

        downloadVideoBtn.setOnClickListener(this);
        messageGroup.setOnClickListener(this);
        aboutMeRequireLesson.setOnClickListener(this);
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
    public void onResume() {
        super.onResume();
        judgeLogin();
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
