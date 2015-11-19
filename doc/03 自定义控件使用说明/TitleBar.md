用法

        <com.yilos.widget.titlebar.TitleBar
            android:id="@+id/about_me_header_nav"
            android:layout_height="wrap_content"
            android:layout_width="match_parent"/>

函数调用

获取左边返回按钮

    public ImageView getBackButton()
    
获取左边返回按钮（已经写好了关闭Activity事件）

    public ImageView getBackButton(final Activity activity)

获取右边文字按钮

    public TextView getRightTextButton()

获取标题控件

    public TextView getTitleView()

获取靠左对齐标题控件

    public TextView getLeftTitleView()

获取图片标题控件
   
    public ImageView getImageTitleView()
   
获取最右边第一个按钮  
  
    public ImageView getRightImageButtonOne()

获取最右边倒数第二个按钮

    public ImageView getRightImageButtonTwo()
    
设置TitleBar的背景颜色（默认为橘色时候可以不用设置）
    
    参数：
        color: 比如 R.color.white
    
    public void setTitleBarBackgroundColor(int color) 

使用举例

        //在XML中使用该组件后通过id获取该组件
        (TitleBar)titleBar = (TitleBar)view.findViewById(R.id.about_me_header_nav);
        
        //设置TitleBar的背景颜色
        titleBar.setTitleBarBackgroundColor(R.color.white);
        
        //这里假设获取右边文字按钮
        TextView rightImageButtonOne = titleBar.getRightTextButton();
        
        //获取控件后可以使用Android原生方法
        rightImageButtonOne.setText("确定");