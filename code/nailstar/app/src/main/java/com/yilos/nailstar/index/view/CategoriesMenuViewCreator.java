package com.yilos.nailstar.index.view;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.yilos.nailstar.R;
import com.yilos.nailstar.framework.view.BaseActivity;
import com.yilos.nailstar.index.entity.Category;
import com.yilos.widget.banner.Banner;
import com.yilos.widget.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangdan on 15/11/17.
 */
public class CategoriesMenuViewCreator implements Banner.ViewCreator<Category> {
    private BaseActivity baseActivity;

    private List<Category> categories;

    public CategoriesMenuViewCreator(BaseActivity baseActivity, List<Category> categories) {
        this.baseActivity = baseActivity;
        this.categories = categories;
    }
    @Override
    public List<View> createViews() {
        final List<View> gridViews = new ArrayList<>(4);
        for (int i = 0, count = categories.size(); i < count; i++) {
            if (i % 8 == 0) {
                TableLayout tableLayout = (TableLayout) baseActivity.getLayoutInflater().inflate(R.layout.category_gridview, null);
                TableRow upLayout = (TableRow) tableLayout.findViewById(R.id.upLayout);
                TableRow downLayout = (TableRow) tableLayout.findViewById(R.id.downLayout);
                gridViews.add(tableLayout);

                for (int j = i; j < i + 8; j++) {
                    View menuView = baseActivity.getLayoutInflater().inflate(R.layout.category_menu_item, null);
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
                    layoutParams.weight = 1;
                    layoutParams.gravity = Gravity.CENTER;
                    menuView.setLayoutParams(layoutParams);
                    CircleImageView circleImageView = ((CircleImageView) menuView.findViewById(R.id.category_circle_image_view));
                    if (j < count) {
                        circleImageView.setImageSrc(categories.get(j).getPicUrl());
                        ((TextView) menuView.findViewById(R.id.category_text_view)).setText(categories.get(j).getName());
                        circleImageView.setClickable(true);
                    }

                    if (j - i < 4) {
                        upLayout.addView(menuView);
                    } else {
                        downLayout.addView(menuView);
                    }

                    circleImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            }
        }

        return gridViews;
    }
}
