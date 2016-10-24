package com.leo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.leo.fragment.MyListFragment;
import com.leo.fragment.RecentMusicFragment;
import com.leo.fragment.AllMusicFragment;
import com.leo.musicinfo.FileSearch;

import java.io.File;
import java.util.ArrayList;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    ListView drawerList;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("TabbedActivity has been destoryed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tabbed2);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("天籁Player");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle=new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_closed){
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                        getSupportActionBar().setTitle("请选择");
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                        getSupportActionBar().setTitle("天籁Player");
                    }
                };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        arrayList=new ArrayList<String>();
        arrayList.add("导入本地歌曲");
        arrayList.add("关于");
        drawerList=(ListView)findViewById(R.id.list);
        adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,arrayList);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        Toast toast=Toast.makeText(MainActivity.this,"正在导入文件,请耐心等待",Toast.LENGTH_SHORT);
                        toast.show();
                        final Handler handler=new Handler();
                        Thread thread=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String root=getRoot();
                                FileSearch.traverseFolder2(root);
                                FileSearch.insertDataBase();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast toast1=Toast.makeText(MainActivity.this,"恭喜你,导入完成",Toast.LENGTH_SHORT);
                                        toast1.show();
                                        Intent i=new Intent(MainActivity.this,MainActivity.class);
                                        startActivity(i);
                                    }
                                });
                            }
                        });
                        thread.start();
                        return;
                    }
                    case 1: {
                        Intent i;
                        i=new Intent(MainActivity.this,AboatActivity.class);
                        startActivity(i);
                    }
                }
            }
        });
        System.out.println("TabbedActivity has been created");
    }

    String getRoot(){
        String root="/storage/emulated/0";
        File file=new File(root);
        if(file.exists()){
            return root;
        }
        else{
            root="/storage/sdcard/0";
            file=new File(root);
            if(file.exists()){
                return root;
            }
            else{
                root="/storage/emulated/legacy";
                file=new File(root);
                if(file.exists()){
                    return root;
                }
            }
        }
        return null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_tabbed_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0: return  new AllMusicFragment();
                case 1: return  new MyListFragment();
                case 2: return  new RecentMusicFragment();
            }
            return  new AllMusicFragment();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "所有音乐";
                case 1:
                    return "我的列表";
                case 2:
                    return "最近播放";
            }
            return null;
        }
    }
}
