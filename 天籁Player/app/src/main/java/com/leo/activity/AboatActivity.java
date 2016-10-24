package com.leo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;

/**
 * Created by Leo on 15/12/21.
 */
public class AboatActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_the_app);
        findViewById(R.id.btnReturnToTabbed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboatActivity.this.finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ArrayList<String> arrayList=new ArrayList<String>();
        ArrayAdapter<String> adapter;
        ListView listView;
        arrayList.add("软件名称:      天籁Player");
        arrayList.add("版本号:          1.0.0");
        arrayList.add("系统要求:      安卓4.4.4以上");
        arrayList.add("开发者:          Leo崔一鸣");
        arrayList.add("说明:         导入本地歌曲需要一定时间,请耐心等待");
        arrayList.add("版权所有,翻版不究");
        adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,arrayList);
        listView=(ListView)findViewById(R.id.list_about);
        listView.setAdapter(adapter);
    }
}
