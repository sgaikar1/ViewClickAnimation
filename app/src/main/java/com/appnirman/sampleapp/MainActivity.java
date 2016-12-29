package com.appnirman.sampleapp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appnirman.viewclickanimation.FooterLayout;
import com.appnirman.viewclickanimation.SheetLayoutCustom;
import com.appnirman.viewclickanimation.SpringList;

public class MainActivity extends AppCompatActivity implements SpringList.SpringCompleteListsener {

    private FloatingActionButton fab,fabAdd;
    private SheetLayoutCustom mSheetLayout;
    private FooterLayout fabToolbar;
    private LinearLayout llLogout,llNewPatient;
    private FloatingActionButton fab2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSheetLayout = (SheetLayoutCustom) findViewById(R.id.bottom_sheet);
        final TextView tv =(TextView)findViewById(R.id.tv_hello);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabToolbar = (FooterLayout) findViewById(R.id.fabtoolbar);

        llLogout = (LinearLayout)findViewById(R.id.ll_logout);
        llNewPatient = (LinearLayout)findViewById(R.id.ll_new_patient);


        final com.appnirman.viewclickanimation.SpringList springList = new SpringList.Builder()
                .checkRam(true)
                .setSheet(mSheetLayout)
                .setContext(MainActivity.this)
                .registerSpringListener(MainActivity.this)
                .build();



        /*Fab click circular reveal animation with facebooks rebound animation sample*/
        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                springList.setView(fab);
                springList.useSpringAnim(true);
                springList.useFloatingSheetAnim(true);
                return springList.onTouch(motionEvent);
            }
        });

        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                springList.setView(tv);
                springList.useSpringAnim(true);
                springList.useFloatingSheetAnim(true);
                return springList.onTouch(motionEvent);
            }
        });

        fab2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                springList.setView(fab2);
                springList.useSpringAnim(true);
                springList.useFloatingSheetAnim(false);
                return springList.onTouch(motionEvent);
            }
        });


        /*fab click circular reveal toolbar animation*/
        fabToolbar.setFab(fabAdd);
        fabToolbar.setClosedOnTouchOutside(true);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabToolbar.expandFab();
                WindowManager.LayoutParams windowManager = getWindow().getAttributes();
                windowManager.dimAmount = 0.75f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });

        llNewPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconAnim(llNewPatient);
                Toast.makeText(MainActivity.this,"New Patient Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconAnim(llLogout);
                Toast.makeText(MainActivity.this,"Logout Clicked",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iconAnim(View icon) {
        Animator iconAnim = ObjectAnimator.ofPropertyValuesHolder(icon,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.5f, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.5f, 1f));
        iconAnim.start();
    }

    private void openSnackbar(View view) {
        Snackbar.make(fab, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void contractFabToolbar() {
        fabToolbar.contractFab();
    }


    @Override
    public void onSpringCompleteListener(View view) {
        if(view == fab) {
            openSnackbar(view);
        }
    }

    @Override
    public void onBackPressed() {
        if(mSheetLayout.isFabExpanded()||fabToolbar.isFabExpanded()){
            if(mSheetLayout.isFabExpanded()){
                mSheetLayout.contractFab();
            }
            if(fabToolbar.isFabExpanded()){
                contractFabToolbar();
            }
        }
        else{
            super.onBackPressed();
        }
    }
}
