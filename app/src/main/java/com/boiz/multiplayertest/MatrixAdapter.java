package com.boiz.multiplayertest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MatrixAdapter extends BaseAdapter {

    Context context;
    List<Integer> matrixList;

    public MatrixAdapter(Context context, List<Integer> matrixList) {
        this.context = context;
        this.matrixList = matrixList;
    }

    @Override
    public int getCount() {
        return matrixList.size();
    }

    @Override
    public Object getItem(int position) {
        return matrixList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("POSI", "getView: " + position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            // get layout from mobile.xml
            assert inflater != null;
            gridView = inflater.inflate(R.layout.itemgrid, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.tvInflate);
            textView.getLayoutParams().height=78;
            //textView.getLayoutParams().width=100;
            if (matrixList.get(position)==1)
            {
                textView.setBackgroundResource(R.drawable.redcircle);
            }
            else if(matrixList.get(position)==2)
            {
                textView.setBackgroundResource(R.drawable.yellowcircle);
            }
            else if(matrixList.get(position)==5)
            {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.animation);
                textView.setBackgroundResource(R.drawable.greencircle);
                textView.startAnimation(animation);
                textView.setBackgroundResource(R.drawable.redcircle);
            }
            else if (matrixList.get(position)==6)
            {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.animation);
                textView.setBackgroundResource(R.drawable.greencircle);
                textView.startAnimation(animation);
                textView.setBackgroundResource(R.drawable.yellowcircle);
            }
            //textView.setText(String.valueOf(matrixList.get(position)));
        } else {
            gridView = (View) convertView;
        }

        return gridView;

        //return inflate(context,R.layout.itemgrid,null);
    }

//    private void startAnimation()
//    {
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);
//
//    }
}
