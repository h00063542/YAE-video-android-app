package com.yilos.nailstar.util;

import android.app.Activity;
import android.content.Intent;

import com.yilos.nailstar.R;
import com.yilos.nailstar.player.VideoPlayerActivity;

/**
 * Created by yilos on 2015-10-28.
 */
public class ActivityUtil {

    public static void toVideoPlayerPage(Activity activity, String topicId) {
        Intent intent = new Intent(activity, VideoPlayerActivity.class);
        intent.putExtra(Constants.TOPICID, topicId);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
