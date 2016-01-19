package com.lazy.dropdownlistview.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.lazy.dropdownlistview.R;
import com.lazy.dropdownlistview.adapter.DropAadapter;
import com.lazy.dropdownlistview.view.DropDownListview;

public class MainActivity extends Activity {
    @Bind(R.id.drop_ls) DropDownListview mDropLs;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }
    private void init() {
        DropAadapter dropAadapter = new DropAadapter(getApplicationContext());
        mDropLs.setAdapter(dropAadapter);
    }
}
