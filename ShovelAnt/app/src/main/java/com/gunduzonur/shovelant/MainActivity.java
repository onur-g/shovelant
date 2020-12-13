package com.gunduzonur.shovelant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity{
    final int SPLASHCOUNT=8;
    SharedPreferences sharedPreferences;
    Random random;
    TextView splash;
    Button startTower,startLore,exit;
    ImageButton fisherman,laser,tesla,sniper;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        random=new Random();
        splash=findViewById(R.id.splash);
        startTower=findViewById(R.id.startTower);
        startLore=findViewById(R.id.startLore);
        splash.setText(getResources().getStringArray(R.array.splash)[random.nextInt(SPLASHCOUNT)]);
        startTower.setOnClickListener(new ClickListener());
        startLore.setOnClickListener(new ClickListener());
    }
    public void createTower(){
        setContentView(R.layout.tower_defense);
        sharedPreferences=getSharedPreferences("selectTower",Context.MODE_PRIVATE);
        exit=findViewById(R.id.exit);
        fisherman=findViewById(R.id.fisherman);
        laser=findViewById(R.id.laser);
        tesla=findViewById(R.id.tesla);
        sniper=findViewById(R.id.sniper);
        exit.setOnClickListener(new ExitListener());
        fisherman.setOnClickListener(new TowerListener());
        laser.setOnClickListener(new TowerListener());
        tesla.setOnClickListener(new TowerListener());
        sniper.setOnClickListener(new TowerListener());
    }
    public void createLore(){
        setContentView(R.layout.lore);
        exit=findViewById(R.id.exit);
        viewPager=findViewById(R.id.viewPager);
        viewPagerAdapter=new ViewPagerAdapter(this);
        exit.setOnClickListener(new ExitListener());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(7,false);
    }
    public void reset(){
        setContentView(R.layout.activity_main);
        random=new Random();
        splash=findViewById(R.id.splash);
        startTower=findViewById(R.id.startTower);
        startLore=findViewById(R.id.startLore);
        splash.setText(getResources().getStringArray(R.array.splash)[random.nextInt(SPLASHCOUNT)]);
        startTower.setOnClickListener(new ClickListener());
        startLore.setOnClickListener(new ClickListener());
    }
    public class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            if(view.getId()==R.id.startTower){
                createTower();
            }
            else{
                createLore();
            }
        }
    }
    public class TowerListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            SharedPreferences.Editor editor=sharedPreferences.edit();
            switch(view.getId()){
                case R.id.fisherman:
                    editor.putInt("selectTower",0);
                    break;
                case R.id.laser:
                    editor.putInt("selectTower",1);
                    break;
                case R.id.tesla:
                    editor.putInt("selectTower",2);
                    break;
                case R.id.sniper:
                    editor.putInt("selectTower",3);
                    break;
                default:
                    reset();
            }
            editor.apply();
        }
    }
    public class ExitListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            reset();
        }
    }
    private class ViewPagerAdapter extends FragmentStateAdapter{
        public ViewPagerAdapter(MainActivity main){
            super(main);
        }
        @Override
        public Fragment createFragment(int index){
            Bundle bundle=new Bundle();
            bundle.putInt("index",index);
            if(index>=8){
                bundle.putString("title",getResources().getStringArray(R.array.towerNames)[index-8]);
                bundle.putString("description",getResources().getStringArray(R.array.towerDescriptions)[index-8]);
                bundle.putString("stats",getResources().getStringArray(R.array.towerStats)[index-8]);
            }
            else if(index<=6){
                bundle.putString("title",getResources().getStringArray(R.array.fishNames)[6-index]);
                bundle.putString("description",getResources().getStringArray(R.array.fishDescriptions)[6-index]);
                bundle.putString("stats",getResources().getStringArray(R.array.fishStats)[6-index]);
            }
            else{
                bundle.putString("title","Glossary");
                bundle.putString("description","\n\n←\nFish\n\nTowers\n→");
            }
            return Lore.newInstance(bundle);
        }
        @Override
        public int getItemCount(){
            return 12;
        }
    }
}