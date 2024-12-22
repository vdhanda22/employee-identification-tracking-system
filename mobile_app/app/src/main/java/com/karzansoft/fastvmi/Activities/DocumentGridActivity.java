package com.karzansoft.fastvmi.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.karzansoft.fastvmi.MCCApplication;
import com.karzansoft.fastvmi.Models.Document;
import com.karzansoft.fastvmi.Models.DocumentItem;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;
import com.karzansoft.fastvmi.Utils.Util;
import com.karzansoft.fastvmi.Adapters.DocumentGridAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.soundcloud.android.crop.Crop;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.dworks.libs.astickyheader.SimpleSectionedGridAdapter;
import dev.dworks.libs.astickyheader.SimpleSectionedGridAdapter.Section;
/**
 * Created by Yasir on 2/15/2016.
 */
public class DocumentGridActivity extends AppCompatActivity implements View.OnClickListener {

    public final int PERMISSION_REQUEST_CODE=111;
    private ArrayList<Section> sections ;
    private GridView grid;
    private DocumentGridAdapter mAdapter;
    static final int REQUEST_TAKE_PHOTO = 111;
    static final int REQUEST_CROP_PHOTO = 112;
    static final String Tag="DocumentGridActivity";
    List<Document> mDocuments;
    List<DocumentItem> mDocumentsItems;
    int selectedIndex;
    Handler mUiHandler;
    ProgressDialog mProgress;
    String imagePath;
    SimpleSectionedGridAdapter simpleSectionedGridAdapter;
    boolean isReadOnly;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppUtils.isRTLLanguageSelected(this))
            AppUtils.forceRTLIfSupported(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_document_grid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(AppUtils.isRTLLanguageSelected(this))
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(AppUtils.getLocalizeString(this,"Documents","Documents"));
        if(savedInstanceState!=null)
        {
            try{
                imagePath=savedInstanceState.getString("path");
                isReadOnly=savedInstanceState.getBoolean("IS_READ_ONLY",false);
                if(isReadOnly)
                    mDocuments= AppUtils.deSerializeDoc(savedInstanceState.getString("DOCUMENTS"));
                ((MCCApplication)getApplication()).loadSerializeDoc(savedInstanceState.getString("Docs"));
                selectedIndex=savedInstanceState.getInt("index",0);
            }catch (Exception e){}
        }else
        {
            isReadOnly= getIntent().getBooleanExtra("IS_READ_ONLY", false);
            if(isReadOnly)
                mDocuments= AppUtils.deSerializeDoc(getIntent().getStringExtra("DOCUMENTS"));
        }


        grid = (GridView)findViewById(R.id.grid);
        loadDocs();

        mUiHandler=new Handler();
        mProgress=new ProgressDialog(this);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setMessage(AppUtils.getLocalizeString(this,"Processing","Processing..."));


        if(isReadOnly && (mDocuments==null || mDocuments.size()<1) )
            AppUtils.showMessage("Documents not found", grid, Snackbar.LENGTH_LONG);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("path", imagePath);
        outState.putString("Docs", ((MCCApplication) getApplication()).getSerializedDoc());
        outState.putInt("index", selectedIndex);
        outState.putBoolean("IS_READ_ONLY", isReadOnly);
        if(isReadOnly)
        outState.putString("DOCUMENTS", AppUtils.serializeDoc(mDocuments));
        super.onSaveInstanceState(outState);
        /*
        isReadOnly=savedInstanceState.getBoolean("IS_READ_ONLY",false);
                if(isReadOnly)
                    mDocuments= AppUtils.deSerializeDoc(savedInstanceState.getString("DOCUMENTS"));
         */
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try{
            imagePath=savedInstanceState.getString("path");
        }catch (Exception e){}
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadSections();
        if(mAdapter!=null)
            mAdapter.notifyDataSetChanged();
        if(simpleSectionedGridAdapter!=null)
        simpleSectionedGridAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        ImageLoader.getInstance().stop();
        super.onDestroy();
    }

    public void loadDocs()
    {
       if(!isReadOnly)
        mDocuments=((MCCApplication)getApplication()).getDocuments();

        mDocumentsItems=new ArrayList<DocumentItem>() ;
        sections=new ArrayList<Section>();

        loadSections();

        mAdapter=new DocumentGridAdapter(this,this,mDocumentsItems);

        simpleSectionedGridAdapter = new SimpleSectionedGridAdapter(this, mAdapter,
                R.layout.grid_item_header, R.id.header_layout, R.id.header);
        simpleSectionedGridAdapter.setGridView(grid);
        simpleSectionedGridAdapter.setSections(sections.toArray(new Section[0]));
        grid.setAdapter(simpleSectionedGridAdapter);


    }


    public void loadSections()
    {

        mDocumentsItems.clear();
        sections.clear();
        
        for (int i=0;i<mDocuments.size();i++)
        {
            Document doc=mDocuments.get(i);
            sections.add(new Section(mDocumentsItems.size(),doc.getTitle()));

            for (int j=0;j<doc.getImages().size();j++)
            {
                DocumentItem item=new DocumentItem(i,doc.getImages().get(j));
                mDocumentsItems.add(item);
            }

          if(!isReadOnly){
            DocumentItem item=new DocumentItem(i,"_action");
            mDocumentsItems.add(item);
          }

        }

        if(simpleSectionedGridAdapter!=null)
            simpleSectionedGridAdapter.setSections(sections.toArray(new Section[0]));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(DocumentGridActivity.this, GalleryViewActivity.class);
        switch (v.getId()){

            case R.id.image:
                DocumentItem item=(DocumentItem)v.getTag();
                Util.LogE("Item Index",""+item.getItemIndex());
                selectedIndex=item.getSectionIndex();
                if (item.getImagePath().startsWith("_action"))
                {
                    if(RequestPermission())
                    dispatchTakePictureIntent();
                }
                else
                {
                    int index=0;
                    int offsetindex=1; //
                    if (isReadOnly)
                        offsetindex=0;
                    if(selectedIndex==1)
                        index=item.getItemIndex()-(mDocuments.get(0).getImages().size()+offsetindex);
                    else if (selectedIndex==2)
                    {
                        index=item.getItemIndex()-(mDocuments.get(0).getImages().size()+offsetindex);
                        index=index-(mDocuments.get(1).getImages().size()+offsetindex);
                    }
                    else {
                        index=item.getItemIndex();
                    }
                    if (index<0)
                        index=0;

                    if(isReadOnly && selectedIndex<mDocuments.size())
                    {
                        intent.putExtra("Title",""+mDocuments.get(selectedIndex).getTitle());
                        intent.putStringArrayListExtra("Images", (ArrayList) mDocuments.get(selectedIndex).getImages());
                    }
                    intent.putExtra("Index",index);
                    intent.putExtra("documentIndex",selectedIndex);
                    intent.putExtra("isReadOnly",isReadOnly);//Images Title isReadOnly
                    startActivity(intent);
                }


                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            beginCrop(imagePath);

        }

        else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }


    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri uri=Crop.getOutput(result);
            mDocuments.get(selectedIndex).getImages().add("file://"+uri.getPath());//"file://"
            loadSections();
            mAdapter.notifyDataSetChanged();
            simpleSectionedGridAdapter.notifyDataSetChanged();
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = Util.createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                imagePath=photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(this,
                        getString(R.string.file_provider_authority),
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                this.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void beginCrop(final String imagePath) {

        try {
            if(imagePath==null)
                return;
            mProgress.show();

            Thread imageCompression = new Thread(new Runnable() {
                @Override
                public void run() {

                    Bitmap srcbt = Util.getScaledBitmap(800, 800, imagePath);
                    final Uri src = Util.saveScaledBitmap(srcbt, imagePath);
                    srcbt.recycle();
                    System.gc();
                    if (src != null) {
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mProgress.dismiss();
                                Uri destination = Uri.fromFile(new File(imagePath));
                                Crop.of(src, destination).start(DocumentGridActivity.this);
                            }
                        });

                    }
                }
            });

            imageCompression.start();

        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private boolean RequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int locationExtStorage = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (locationExtStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions( listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;

    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case PERMISSION_REQUEST_CODE:
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if(perms.get(Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
                    {
                        dispatchTakePictureIntent();
                    }
                    else  Toast.makeText(this, AppUtils.getLocalizeString(this,"PermissionError","Some permissions are not granted"), Toast.LENGTH_LONG)
                            .show();
                }else {
                    Toast.makeText(this, AppUtils.getLocalizeString(this,"PermissionsEnableMsg","Go to settings and enable permissions"), Toast.LENGTH_LONG)
                            .show();
                }

        }


    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }
}
