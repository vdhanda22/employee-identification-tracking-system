package com.karzansoft.fastvmi.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.karzansoft.fastvmi.MCCApplication;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Interfaces.SymbolStateListener;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.extended.MarkerViewInterior;
import com.karzansoft.fastvmi.extended.SymbolMarker;

/**
 * Created by Yasir on 2/17/2016.
 */
public class VehicleMarksInteriorActivity extends AppCompatActivity implements View.OnClickListener,SymbolStateListener {

    boolean IsCheckout = false;
    Button mNext = null;
    ImageView imgLdent;
    MarkerViewInterior markerView;
    int current_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_vehicle_marks_interior);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        IsCheckout = getIntent().getBooleanExtra("IsCheckout", true);
        current_mode=getIntent().getIntExtra(Constant.CURRENT_MODE, 1);

        if(current_mode== Constant.RA_CHECKOUT)
        {
            setTitle("Check Out - Interior");
        }else if(current_mode==Constant.RA_CHECKIN)
        {
            setTitle("Check In - Interior");
        }
        else if(current_mode==Constant.REPLACEMENT_CHECKIN)
        {
            setTitle("Replacement(In)- Interior");
        }
        else if(current_mode==Constant.REPLACEMENT_CHECKOUT)
        {
            setTitle("Replacement(Out)- Interior");
        }
        else if(current_mode==Constant.NRM_CHECKOUT)
        {
            setTitle("NRM Out - Interior");

        }else if(current_mode==Constant.NRM_CHECKIN)
        {
            setTitle("NRM In - Interior");
        }
        else if(current_mode==Constant.GARAGE_CHECKOUT)
        {
            setTitle("Garage Check Out - Interior");

        }else if(current_mode==Constant.GARAGE_CHECKIN)
        {
            setTitle("Garage Check In - Interior");

        }

        mNext = (Button) findViewById(R.id.vm_action_next);
        imgLdent=(ImageView)findViewById(R.id.img_large_dent);
        markerView=(MarkerViewInterior)findViewById(R.id.marker_view);
        markerView.setSymbolStateListener(this);


        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(current_mode==Constant.REPLACEMENT_CHECKIN)
                {
                    Intent intent = new Intent(VehicleMarksInteriorActivity.this, VehicleSelectActivity.class);
                    intent.putExtra("IsCheckout", true);
                    intent.putExtra(Constant.CURRENT_MODE,Constant.REPLACEMENT_CHECKOUT);
                    startActivityForResult(intent, 1);
                }
                else if(current_mode==Constant.NRM_CHECKIN||current_mode==Constant.NRM_CHECKOUT||current_mode==Constant.GARAGE_CHECKIN||current_mode==Constant.GARAGE_CHECKOUT)
                {
                    Intent intent = new Intent(VehicleMarksInteriorActivity.this, NotesActivity.class);
                    intent.putExtra("IsCheckout", IsCheckout);
                    intent.putExtra(Constant.CURRENT_MODE,current_mode);
                    startActivityForResult(intent, 1);
                }
                else{
                Intent intent = new Intent(VehicleMarksInteriorActivity.this, RANotesActivity.class);
                intent.putExtra("IsCheckout", IsCheckout);
                intent.putExtra(Constant.CURRENT_MODE, current_mode);
                startActivityForResult(intent, 1);
                }
                //finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                //String result=data.getStringExtra("result");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",IsCheckout);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.img_large_dent:
                markerView.addSymbolOntouch(Constant.SYMBOL_MARKER_LARGE_DENT);
                imgLdent.setSelected(true);
                // getSnackbar(Constant.SYMBOL_MARKER_LARGE_DENT).show();

                break;

            case R.id.img_info:
                SymbolMarker selected=markerView.getSelectedMarker();
                if(selected!=null)
                {
                    Intent intent=new Intent(this,SymbolDetailActivity.class);
                    intent.putExtra("ID",selected.getId());
                    intent.putExtra("IS_EDITABLE",selected.isEditable());
                    startActivity(intent);
                }else{

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(this);
                    builder.setTitle("Symbol detail");
                    builder.setMessage("Please select at least one symbol to view it's detail ");
                    builder.setPositiveButton("Ok", null);

                    builder.show();
                }
                break;
        }
    }


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
      if(id==android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private Snackbar getSnackbar(int markerSymbol)
    {
        String text="";
        if (markerSymbol == Constant.SYMBOL_MARKER_SMALL_DENT) {
            text = "Small Dent Selected";
        } else if (markerSymbol == Constant.SYMBOL_MARKER_LARGE_DENT) {
            text = "Large Dent Selected";
        }
        else if (markerSymbol == Constant.SYMBOL_MARKER_SCRATCH) {
            text = "Large Scratch Selected";
        }
        else if (markerSymbol == Constant.SYMBOL_MARKER_SCRATCH_THIN) {
            text = "Small Scratch Selected";
        }
        Snackbar snackbar=Snackbar.make(markerView, text, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        return snackbar;
    }

    @Override
    public void onSymbolAdded(SymbolMarker symbolMarker) {
        ((MCCApplication)getApplication()).addSymbol(symbolMarker.markerType, symbolMarker.getId());
    }

    @Override
    public void onRemoved(SymbolMarker symbolMarker) {
        ((MCCApplication)getApplication()).removeSymbol(symbolMarker.getId());
    }

    @Override
    public void onSymbolSelected(SymbolMarker symbolMarker) {
        resetSelection();
    }

    public void resetSelection()
    {

        imgLdent.setSelected(false);

    }
}
