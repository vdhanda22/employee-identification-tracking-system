package org.chat21.android.ui.messages.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.chat21.android.R;
import org.chat21.android.ui.adapters.LanguageListAdapter;
import org.chat21.android.ui.models.LanguageItem;
import org.chat21.android.utils.IOUtils;
import org.chat21.android.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Yasir on 9/27/2018.
 */

public class LanguageListActivity extends AppCompatActivity {

    ArrayList<LanguageItem> languageItems;
    LanguageListAdapter languageListAdapter;
    ListView listView;
    private SearchView searchView;
    String languageCode = "en";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Translation Language");

        listView = findViewById(R.id.list);

        languageItems = loadLanguageList();

        languageCode = Utils.getLanguageCode(this,"en");



        languageListAdapter = new LanguageListAdapter(this,languageItems);
        languageListAdapter.setCheckCode(languageCode);
        listView.setAdapter(languageListAdapter);
        int lastIndex = getCurrentLanguageIndex(languageCode,languageItems);
        try{
        if (lastIndex !=-1)
        {
            listView.setSelection(lastIndex);
        }
        }catch (Exception ex){ex.printStackTrace();}

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                languageCode = languageListAdapter.getItem(pos).getCode();
                Utils.setLanguageCode(languageCode,LanguageListActivity.this);
                languageListAdapter.setCheckCode(languageCode);
                //languageListAdapter.notifyDataSetChanged();
                Utils.setLanguageSelected(true,LanguageListActivity.this);
                finish();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_contacts_list, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        searchView = (SearchView) item.getActionView();
        item.setShowAsAction(
                MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW |
                        MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                if (languageListAdapter != null) languageListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (languageListAdapter!= null) languageListAdapter.getFilter().filter(query);
//                Log.d(TAG, "ContactListActivity.OnQueryTextListener.onQueryTextChange:" +
//                        " query == " + query);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }



    public void onBackPressed() {
        // close search view on back button pressed
        if (searchView != null && !searchView.isIconified()) {
//            searchView.setIconified(true);
            searchView.onActionViewCollapsed();
            return;
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int getCurrentLanguageIndex(String code ,ArrayList<LanguageItem> items)
    {
        for (int i=0;i<items.size();i++)
        {
            if (items.get(i).getCode().equals(code))
                return i;
        }

        return -1;
    }

    private ArrayList<LanguageItem> loadLanguageList()
    {
        ArrayList<LanguageItem> languageItems = new ArrayList<>();
        
        String jsonString = IOUtils.loadJSONFromAsset(this,"Languages.json");
        
        if (jsonString!=null && jsonString.length()>0 )
        {
            JSONArray array = null;
           
            try{ 
                array = new JSONArray(jsonString);

                for (int i = 0 ;i < array.length(); i++)
                {
                    JSONObject row = array.getJSONObject(i);
                    String name = row.getString("Language");
                    String code = row.getString("code");
                    languageItems.add(new LanguageItem(name,code));
                }

            }catch (Exception ex){ex.printStackTrace();}
            
        }
        
        return languageItems;
    }
}
