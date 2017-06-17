package ichou.domotically;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private ViewPager viewPager;
    private int[] layouts;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private Button next,skip;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences myPrefs = getPreferences(MODE_PRIVATE);
        boolean first = myPrefs.getBoolean("first",false);
        if(first){
            Intent intro = new Intent(MainActivity.this, IntroWithDrawer.class);
            startActivity(intro);
            finish();
        }
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT>=21){

            getWindow().getDecorView().setSystemUiVisibility(View.GONE|View.SYSTEM_UI_FLAG_FULLSCREEN); //SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout)findViewById(R.id.layoutDots);
        skip = (Button)findViewById(R.id.btn_skip);
        next = (Button)findViewById(R.id.btn_next);
        layouts = new int[]{R.layout.screen1,R.layout.screen2,R.layout.screen3,R.layout.screen4};
        addBottomDots(0);
        changeStatusBarColor();
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewListner);
        skip.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intro = new Intent(MainActivity.this, IntroWithDrawer.class);   //-------------------------------
                startActivity(intro);
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int current = getItem(+1);
                if(current<layouts.length){

                    viewPager.setCurrentItem(current);
                }
                else{
                    Intent intro = new Intent(MainActivity.this, IntroWithDrawer.class); //------------------
                    startActivity(intro);
                    finish();
                }
            }
        });
    }



    private void addBottomDots(int position){

        dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.Active);
        int[] colorInactive = getResources().getIntArray(R.array.Inactive);
        dotsLayout.removeAllViews();
        for(int i=0;i<dots.length;i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive[position]);
            dotsLayout.addView(dots[i]);
        }
        if(dots.length>0){
            dots[position].setTextColor(colorActive[position]);

        }
    }
    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addBottomDots(position);
            if(position==layouts.length-1){
                next.setText("COMMENCER");
                next.setVisibility(View.VISIBLE);  //Gone
            }
            else{
                next.setText("SUIVANT");
                skip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    public void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private int getItem(int i){
        return viewPager.getCurrentItem()+i;
    }
    public class ViewPagerAdapter extends PagerAdapter{
        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(layouts[position],container,false);
            container.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View)object;
            container.removeView(v);
        }
    }

    @Override
    protected void onDestroy() {
        SharedPreferences myPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor myEditor = myPrefs.edit();
        myEditor.putBoolean("first",true);
        myEditor.commit();
        super.onDestroy();
    }
}
