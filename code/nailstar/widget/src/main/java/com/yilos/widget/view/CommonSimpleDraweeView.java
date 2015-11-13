package com.yilos.widget.view;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by yangdan on 15/11/13.
 */
public class CommonSimpleDraweeView extends SimpleDraweeView {
    private static GenericDraweeHierarchy hierarchy;

    public CommonSimpleDraweeView(Context context) {
        this(context, null, 0);
    }

    public CommonSimpleDraweeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonSimpleDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(null == hierarchy) {
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
            hierarchy = builder
                    .setFadeDuration(300)
//                    .setPlaceholderImage(new MyCustomDrawable())
//                    .setBackgrounds(backgroundList)
//                    .setOverlays(overlaysList)
                    .setProgressBarImage(new ProgressBarDrawable())
                    .build();
        }
        setHierarchy(hierarchy);
    }
}
