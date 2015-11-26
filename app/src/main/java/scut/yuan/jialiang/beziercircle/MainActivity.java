package scut.yuan.jialiang.beziercircle;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabbarLayout mBezierCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBezierCircle = (TabbarLayout) findViewById(R.id.circle);
        mBezierCircle.setImageResource(new int[]{R.mipmap.message1, R.mipmap.setting, R.mipmap.ic_launcher});
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        mBezierCircle.setViewPager(mViewPager);
    }

   class MyViewPagerAdapter extends FragmentPagerAdapter{

       public MyViewPagerAdapter(FragmentManager fm) {
           super(fm);
       }

       @Override
       public Fragment getItem(int position) {
           return new TestFragment();
       }

       @Override
       public int getCount() {
           return 3;
       }
   }
}
