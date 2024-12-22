package com.karzansoft.fastvmi.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.extended.MarkerView;

/**
 * Created by Yasir on 2/25/2016.
 */
public class MarkerActivity extends AppCompatActivity implements View.OnClickListener{

    MarkerView markerView;
   // ScrollView symbolScroller;
    ImageView toggle,imgDent,imgLdent,imgScratch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        markerView=(MarkerView)findViewById(R.id.marker_view);
     //   symbolScroller=(ScrollView)findViewById(R.id.symbolScroller);
        toggle=(ImageView)findViewById(R.id.toggle);
        toggle.setOnClickListener(this);
        imgDent=(ImageView)findViewById(R.id.img_small_dent);
        imgLdent=(ImageView)findViewById(R.id.img_large_dent);
        imgScratch=(ImageView)findViewById(R.id.img_scratch);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vehicle_accessories, menu);
      /*  if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
                }
            }
        }*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      int id = item.getItemId();
        Snackbar snackbar=getSnackbar();
        switch (id)
        {
            case android.R.id.home:
                this.finish();
                return true;
          /*  case R.id.action_mark_tick:

                markerView.addSymbol(Constant.MARKER_SYMBOL_TICK);
                snackbar.setText("Drag a symbol to adjust its position");
                snackbar.show();
            break;
            case R.id.action_mark_cross:
                markerView.addSymbol(Constant.MARKER_SYMBOL_CROSS);
                snackbar.setText("Drag a symbol to adjust its position");
                snackbar.show();
            break;*/
            case R.id.action_mark_edit:
              /*  markerView.setSelection(true);
                snackbar.setText("Drag a symbol to adjust its position");
                snackbar.show();*/
                break;
            case R.id.action_mark_delete:
               /* markerView.setDeletionFlag(true);
                snackbar.setText("Tab on a symbol to delete it");
                snackbar.show();*/
                break;
        }
        //noinspection SimplifiableIfStatement
        if(id==android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private Snackbar getSnackbar()
    {
       Snackbar snackbar=Snackbar.make(markerView, "Drag to adjust symbol position", Snackbar.LENGTH_INDEFINITE)
                .setAction("Done", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // markerView.setSelection(false);
                       // markerView.setDeletionFlag(false);

                    }
                });
        snackbar.setActionTextColor(Color.GREEN);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

    return snackbar;
    }

    @Override
    public void onClick(View v) {

        //Snackbar snackbar=getSnackbar();
        switch (v.getId())
        {
            case R.id.toggle:
             // toggleSymbolView();
                break;
            case R.id.img_small_dent:
                markerView.addSymbolOntouch(Constant.SYMBOL_MARKER_SMALL_DENT);
                imgDent.setSelected(true);
                imgLdent.setSelected(false);
                imgScratch.setSelected(false);
             /*   snackbar.setText("Touch on screen to add symbol");
                snackbar.show();*/
               // toggleSymbolView();
                break;
            case R.id.img_large_dent:
                markerView.addSymbolOntouch(Constant.SYMBOL_MARKER_LARGE_DENT);
                imgDent.setSelected(false);
                imgLdent.setSelected(true);
                imgScratch.setSelected(false);
              /*  snackbar.setText("Touch on screen to add symbol");
                snackbar.show();*/
               // toggleSymbolView();
                break;
            case R.id.img_scratch:
                markerView.addSymbolOntouch(Constant.SYMBOL_MARKER_SCRATCH);
                imgDent.setSelected(false);
                imgLdent.setSelected(false);
                imgScratch.setSelected(true);
                //toggleSymbolView();
                break;
        }
    }

    public void resetSelection()
    {
        imgDent.setSelected(false);
        imgLdent.setSelected(false);
        imgScratch.setSelected(false);
    }

    public void toggleSymbolView()
    {
       /* if(symbolScroller.getVisibility()==View.VISIBLE)
        {
            symbolScroller.setVisibility(View.GONE);
            toggle.setImageResource(R.drawable.right);
        }
        else
        {
            symbolScroller.setVisibility(View.VISIBLE);
            toggle.setImageResource(R.drawable.left);
        }*/
    }
}
