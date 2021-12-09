package com.gunduzonur.shovelant;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class Lore extends Fragment{
    TextView titleText,descriptionText;
    ImageView imageView;
    String title,description,stats;
    int index;
    public Lore(){}
    public static Lore newInstance(Bundle bundle){
        Lore fragment=new Lore();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        title=getArguments().getString("title");
        description=getArguments().getString("description");
        stats=getArguments().getString("stats");
        index=getArguments().getInt("index");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.lore_page,container,false);
    }
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        titleText=view.findViewById(R.id.title);
        descriptionText=view.findViewById(R.id.description);
        imageView=view.findViewById(R.id.image);
        titleText.setText(title);
        int statCount=index<=6?3:8;
        String[] statArray=new String[statCount];
        if(index!=7){
            for(int i=0;i<statArray.length;i++){
                statArray[i]=stats.substring(0,stats.indexOf(','));
                stats=stats.substring(stats.indexOf(',')+1);
            }
        }
        if(index<=6){
            descriptionText.setText(description+"\n\nArmor: "+statArray[0]+"\nHealth: "+statArray[1]+"\nSpeed: "+statArray[2]+"\nMarket Value: "+stats+"₿");
        }
        else if(index>=8){
            description=description+"\n\nAttack Speed: "+statArray[0]+"\nDamage: "+statArray[1]+"\nRange: "+statArray[2]+"\nArmor Penetration: "+statArray[3]+"\nCost: "+statArray[4]+"₿";
            description=description+"\n\nFirst Upgrade: "+statArray[5]+"₿ - "+statArray[6]+"\nSecond Upgrade: "+statArray[7]+"₿ - "+stats;
            descriptionText.setText(description);
        }
        else{
            descriptionText.setTextSize(30);
            descriptionText.setGravity(Gravity.CENTER);
            descriptionText.setText(description);
        }
        switch(index){
            case 0:
                imageView.setImageResource(R.drawable.butter);
                break;
            case 1:
                imageView.setImageResource(R.drawable.cat);
                break;
            case 2:
                imageView.setImageResource(R.drawable.narwhal);
                break;
            case 3:
                imageView.setImageResource(R.drawable.clown);
                break;
            case 4:
                imageView.setImageResource(R.drawable.jelly);
                break;
            case 5:
                imageView.setImageResource(R.drawable.puffer);
                break;
            case 6:
                imageView.setImageResource(R.drawable.blob);
                break;
            case 8:
                imageView.setImageResource(R.drawable.fishermanicon);
                break;
            case 9:
                imageView.setImageResource(R.drawable.lasericon);
                break;
            case 10:
                imageView.setImageResource(R.drawable.teslaicon);
                break;
            case 11:
                imageView.setImageResource(R.drawable.snipericon);
        }
    }
}
