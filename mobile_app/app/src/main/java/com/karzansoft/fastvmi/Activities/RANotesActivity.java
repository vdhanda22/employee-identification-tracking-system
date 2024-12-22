package com.karzansoft.fastvmi.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.Constant;

/**
 * Created by Yasir on 2/17/2016.
 */
public class RANotesActivity extends AppCompatActivity {

    private boolean IsCheckout;
    int current_mode;
    ScrollView mScroller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_ra);
        IsCheckout = getIntent().getBooleanExtra("IsCheckout", true);
        current_mode=getIntent().getIntExtra(Constant.CURRENT_MODE, 1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
        // getSupportActionBar().setTitle("Notes " + (IsCheckout ? " for Checkout" : " for Check-in"));
       /* if(IsCheckout)
            setTitle("Check Out");
        else
            setTitle("Check In");*/

        mScroller=(ScrollView)findViewById(R.id.scoller);
        mScroller.requestDisallowInterceptTouchEvent(true);

        if(current_mode== Constant.RA_CHECKOUT)
        {
            setTitle("Check Out");
        }else if(current_mode==Constant.RA_CHECKIN)
        {
            setTitle("Check In");
        }
        else if(current_mode==Constant.REPLACEMENT_CHECKOUT)
        {
            setTitle("Replacement");
        }


        final CaptureSignatureView capt = (CaptureSignatureView) findViewById(R.id.capture);

        Button finishButton = (Button) findViewById(R.id.ns_action_finish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",IsCheckout);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        Button clearButton = (Button) findViewById(R.id.ns_action_clearpaint);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capt.ClearCanvas();
            }
        });


        final CaptureSignatureView capt2 = (CaptureSignatureView) findViewById(R.id.capture2);


        Button clearButton2 = (Button) findViewById(R.id.ns_action_clearpaint2);
        clearButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capt2.ClearCanvas();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                //String result=data.getStringExtra("result");
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

    }//onActivityResult

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        } else if(id==android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
