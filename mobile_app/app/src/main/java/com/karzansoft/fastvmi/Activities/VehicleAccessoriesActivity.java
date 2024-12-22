package com.karzansoft.fastvmi.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.Constant;
import com.karzansoft.fastvmi.Adapters.AccessoriesAdapter;

public class VehicleAccessoriesActivity extends AppCompatActivity {

    ListView mListView = null;
    boolean IsCheckout = false;
    Button mNext = null;
    int current_mode;
    private static final String[] DUMMY_ACCESSSORIES_OUT = new String[]{
            "Antenna", "Lighter","Jack","Tool Kit","Floor Mats","Wiper Operational","A.C Operational","Salik Tag"
    };

    private static final String[] DUMMY_ACCESSSORIES_IN = new String[]{
            "Antenna", "Lighter","Jack","Tool Kit","Floor Mats","Wiper Operational","A.C Operational","Salik Tag", "Antenna", "Lighter","Jack","Tool Kit","Floor Mats","Wiper Operational","A.C Operational","Salik Tag"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_accessories);
        IsCheckout = getIntent().getBooleanExtra("IsCheckout",true);
        current_mode=getIntent().getIntExtra(Constant.CURRENT_MODE, 1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // toolbar.setNavigationIcon(R.drawable.ic_menu);

        //getSupportActionBar().setTitle("Vehicle Accessories " + (IsCheckout ? " for Checkout" : " for Check-in"));
     /*   if(IsCheckout)
            setTitle("Check Out");
        else
            setTitle("Check In");*/
        if(current_mode==Constant.RA_CHECKOUT)
        {
            setTitle("Check Out");
        }else if(current_mode==Constant.RA_CHECKIN)
        {
            setTitle("Check In");
        }
        else if(current_mode==Constant.REPLACEMENT_CHECKIN)
        {
            setTitle("Replacement (In)");
        }
        else if(current_mode==Constant.REPLACEMENT_CHECKOUT)
        {
            setTitle("Replacement (Out)");
        }

        else if(current_mode==Constant.NRM_CHECKOUT)
        {
            setTitle("NRM Out");

        }else if(current_mode==Constant.NRM_CHECKIN)
        {
            setTitle("NRM In");
        }
        else if(current_mode==Constant.GARAGE_CHECKOUT)
        {
            setTitle("Garage Check Out");

        }else if(current_mode== Constant.GARAGE_CHECKIN)
        {
            setTitle("Garage Check In");

        }



        mNext = (Button) findViewById(R.id.va_action_next);

        mListView = (ListView) findViewById(R.id.listviewcheckboxes);

        mListView.setChoiceMode(mListView.CHOICE_MODE_MULTIPLE);


        mListView.setTextFilterEnabled(true);
        AccessoriesAdapter adapter;

      if(current_mode==Constant.RA_CHECKIN ||current_mode==Constant.REPLACEMENT_CHECKIN ||current_mode==Constant.NRM_CHECKIN )
      {
          adapter=new AccessoriesAdapter(this,
                  R.layout.list_item_accessories, DUMMY_ACCESSSORIES_OUT,Constant.ACCESSORIES_IN);
      }else
      {
          adapter=new AccessoriesAdapter(this,
                  R.layout.list_item_accessories, DUMMY_ACCESSSORIES_OUT,Constant.ACCESSORIES_OUT);
      }
       /* mListView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, Arrays.asList(DUMMY_ACCESSSORIES)));
*/
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView item = (CheckedTextView) view;

               /* if(current_mode==Constant.RA_CHECKIN ||current_mode==Constant.REPLACEMENT_CHECKIN ||current_mode==Constant.NRM_CHECKIN )

                {}
                else
                    Toast.makeText(VehicleAccessoriesActivity.this, DUMMY_ACCESSSORIES_OUT[position] + " checked : " +
                            item.isChecked(), Toast.LENGTH_SHORT).show();
*/


            }
        });


       /* ArrayList<SimpleSectionedListAdapter.Section> sections = new ArrayList<SimpleSectionedListAdapter.Section>();

       // for (int i = 0; i < mHeaderPositions.length; i++) {
            sections.add(new SimpleSectionedListAdapter.Section(0,"Check Out Accessories"));
        sections.add(new SimpleSectionedListAdapter.Section(8,"Check In Accessories"));
        //}
        SimpleSectionedListAdapter simpleSectionedGridAdapter = new SimpleSectionedListAdapter(this, adapter,
                R.layout.list_item_header, R.id.header);
        simpleSectionedGridAdapter.setSections(sections.toArray(new SimpleSectionedListAdapter.Section[0]));

        if(current_mode==Constant.RA_CHECKIN ||current_mode==Constant.REPLACEMENT_CHECKIN ||current_mode==Constant.NRM_CHECKIN )
        {
            mListView.setAdapter(simpleSectionedGridAdapter);
            mListView.setItemChecked(1, true);
            mListView.setItemChecked(4,true);
            mListView.setItemChecked(6,true);
            mListView.setItemChecked(7,true);


        }
        else*/
            mListView.setAdapter(adapter);



        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehicleAccessoriesActivity.this, VehicleMarksActivity.class);
                intent.putExtra("IsCheckout", IsCheckout);
                intent.putExtra(Constant.CURRENT_MODE,current_mode);
                startActivityForResult(intent, 1);
                //finish();
            }
        });


    }


    /*
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(getApplicationContext(),"Portrait",50);

        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(getApplicationContext(),"Landscape",50);

        }

//        Configuration c = getResources().getConfiguration();
//
//        if (c.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            // portrait
//
//        } else if (c.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            // landscape
//
//        }
    }*/

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
