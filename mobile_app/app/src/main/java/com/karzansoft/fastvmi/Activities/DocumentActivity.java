package com.karzansoft.fastvmi.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.karzansoft.fastvmi.MCCApplication;
import com.karzansoft.fastvmi.Models.Document;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.Util;
import com.karzansoft.fastvmi.Adapters.DocumentListAdapter;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Yasir on 2/11/2016.
 */
public class DocumentActivity extends AppCompatActivity implements View.OnClickListener{

    static final int REQUEST_TAKE_PHOTO = 111;
    static final int REQUEST_CROP_PHOTO = 112;
    static final String Tag="DocumentActivity";
    ListView mList;
    List<Document> mDocuments;
    DocumentListAdapter mAdapter;
    int selectedIndex;
    Handler mUiHandler;
    ProgressDialog mProgress;
    String imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_document);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Documents");

        mList=(ListView)findViewById(R.id.listView);
        loadDefaultDocuments();
        mAdapter=new DocumentListAdapter(this,mDocuments,this);
        mList.setAdapter(mAdapter);

        mUiHandler=new Handler();
        mProgress=new ProgressDialog(this);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setMessage("Processing...");



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAdapter!=null)
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("path", imagePath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imagePath=savedInstanceState.getString("path");
    }

    public void loadDefaultDocuments()
    {
        mDocuments=((MCCApplication)getApplication()).getDocuments();
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(DocumentActivity.this, GalleryViewActivity.class);
        switch (v.getId()){

            case R.id.img3:
                selectedIndex=Integer.parseInt(v.getTag().toString());
                if(mDocuments.get(selectedIndex).getImages().size()<3)
                {
                    dispatchTakePictureIntent();
                }
                else
                {
                     intent.putExtra("Index",2);
                    intent.putExtra("documentIndex",selectedIndex);
                    intent.putExtra("isDocument",true);
                    startActivity(intent);
                }
                break;
            case R.id.img1:
                selectedIndex=Integer.parseInt(v.getTag().toString());
                intent.putExtra("Index",0);
                intent.putExtra("documentIndex",selectedIndex);
                intent.putExtra("isDocument",true);
                startActivity(intent);
                break;
            case R.id.img2:
                selectedIndex=Integer.parseInt(v.getTag().toString());
                intent.putExtra("Index",1);
                intent.putExtra("documentIndex",selectedIndex);
                intent.putExtra("isDocument",true);
                startActivity(intent);
                break;


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

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
            mAdapter.notifyDataSetChanged();
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
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                this.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void beginCrop(final String imagePath) {

try {


    mProgress.show();

    Thread imageCompression = new Thread(new Runnable() {
        @Override
        public void run() {
            Bitmap srcbt = Util.getScaledBitmap(512, 512, imagePath);
            final Uri src = Util.saveScaledBitmap(srcbt, imagePath);
            srcbt.recycle();
            System.gc();
            if (src != null) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgress.hide();
                        Uri destination = Uri.fromFile(new File(imagePath));
                        Crop.of(src, destination).start(DocumentActivity.this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("DocumentActivity","Ondestroying..");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(Tag,"Config Changes");
    }
}
