package com.yilos.nailstar.topic.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.yilos.nailstar.R;

/**
 * Created by ganyue on 15/11/19.
 */
public class OrderFinishDialog extends Dialog {
    public OrderFinishDialog(Context context) {
        super(context);
        initDialog();
    }

    public OrderFinishDialog(Context context, int themeResId) {
        super(context, themeResId);
        initDialog();
    }

    private void initDialog() {
        setCancelable(false);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.product_order_finish_dialog);
    }
}
