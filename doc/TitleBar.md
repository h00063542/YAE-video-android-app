用法
        <com.yilos.widget.titlebar.TitleBar
            android:id="@+id/about_me_header_nav"
            android:layout_height="wrap_content"
            android:layout_width="match_parent">
        </com.yilos.widget.titlebar.TitleBar>

函数调用

设置标题内容
    public void setTitleText(String titleText) 

获取标题内容
    public String getTitleText() 

设置返回按钮不可见
    public void notShowBackButton() 

设置确定按钮不可见
    public void notShowSureButton()

设置标题不可见
    public void notShowTitleView() 

获取返回按钮对象
    public Button getBackButton() 

获取确定按钮对象
    public Button getSureButton() 

获取标题对象
    public TextView getTitleView() 
    
结束该Activity
    public void backEvent(Activity activity)

使用举例
        (TitleBar)titleBar = (TitleBar)view.findViewById(R.id.about_me_header_nav);
        titleBar.notShowSureButton();