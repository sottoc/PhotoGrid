package com.dexati.photogridbuilder.social.facebook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.*;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.facebook.*;
import com.facebook.model.GraphObject;
import com.km.photogridbuilder.PhotoSelectorScreen;
import com.km.photogridbuilder.listener.ImageLoadListener;
import com.km.photogridbuilder.listener.LoadImagetask;
import com.km.photogridbuilder.util.BitmapUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONObject;

// Referenced classes of package com.dexati.photogridbuilder.social.facebook:
//            EndLessListener, ImagesGridView, HttpUtils, FacebookDto, 
//            ResponseParser

public class AlbumImagesScreen extends Activity
    implements EndLessListener
{
    public class ImageListAdapter extends BaseAdapter
    {

        private ArrayList imageUrls;
        private LayoutInflater mInflater;
        private int mScreenWidth;

        public void addAll(ArrayList arraylist)
        {
            imageUrls.addAll(arraylist);
        }

        public int getCount()
        {
            return imageUrls.size();
        }

        public Object getItem(int i)
        {
            return imageUrls.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }

        public View getView(int i, View view, ViewGroup viewgroup)
        {
label0:
            {
                if(view == null)
                {
                    view = mInflater.inflate(0x7f030023, null);
                }
                ImageView imageview = (ImageView)view.findViewById(0x7f0900a9);
                imageLoader.displayImage(((FacebookDto)imageUrls.get(i)).getUrl(), imageview);
                imageViewCheckBox = (ImageView)view.findViewById(0x7f0900aa);
                if(mScreenWidth < 400)
                {
                    android.widget.FrameLayout.LayoutParams layoutparams = new android.widget.FrameLayout.LayoutParams(-2, -2);
                    layoutparams.width = 80;
                    layoutparams.height = 80;
                    imageview.setLayoutParams(layoutparams);
                    imageViewCheckBox.setLayoutParams(layoutparams);
                }
                if(!PhotoSelectorScreen.isMultipleMode)
                {
                    imageViewCheckBox.setBackgroundResource(0x7f0200a9);
                }
                FacebookDto facebookdto = (FacebookDto)imageUrlList.get(i);
                if(PhotoSelectorScreen.isMultipleMode)
                {
                    if(!facebookdto.isSelected())
                    {
                        break label0;
                    }
                    imageViewCheckBox.setBackgroundResource(0x7f0200aa);
                }
                return view;
            }
            imageViewCheckBox.setBackgroundResource(0x7f0200a8);
            return view;
        }

        public void setSelection(int i, FacebookDto facebookdto)
        {
            imageUrls.set(i, facebookdto);
            notifyDataSetChanged();
        }

        public ImageListAdapter(Context context, ArrayList arraylist)
        {
            super();
            mInflater = (LayoutInflater)getSystemService("layout_inflater");
            imageUrls = arraylist;
            mScreenWidth = BitmapUtil.getScreenWidth(AlbumImagesScreen.this);
        }
    }


    private static final String TAG = com.km.photogridbuilder.PhotoSelectorScreen.class.getSimpleName();
    private String albumId;
    private ProgressDialog dialog;
    private ProgressBar footerbar;
    private ImageListAdapter imageAdapter;
    private ImageView imageDone;
    protected ImageLoader imageLoader;
    protected ArrayList imageUrlList;
    private ImageView imageViewCheckBox;
    protected JSONObject jsonObject;
    private ContentResolver mContentResolver;
    private ImagesGridView mGridView;
    private android.graphics.Bitmap.CompressFormat mOutputFormat;
    private Response mResponse;
    private ArrayList mSavedImagesList;
    private ArrayList mSelectUrlList;
    Runnable nextRunnable;
    private ProgressDialog pd;
    Runnable runnable;

    public AlbumImagesScreen()
    {
        mOutputFormat = android.graphics.Bitmap.CompressFormat.JPEG;
        runnable = new Runnable() {
            public void run()
            {
                if(jsonObject != null)
                {
                    imageUrlList = ResponseParser.getAlbumImages(jsonObject);
                    imageAdapter = new ImageListAdapter(getApplicationContext(), imageUrlList);
                    mGridView.setListener(AlbumImagesScreen.this);
                    mGridView.setAdapter(imageAdapter);
                }
                dialog.dismiss();
            }
        };
        nextRunnable = new Runnable() {
            public void run()
            {
                mGridView.addNewData(ResponseParser.getAlbumImages(jsonObject));
            }
        };
    }

    private void init()
    {
        imageDone = (ImageView)findViewById(0x7f090085);
        if(PhotoSelectorScreen.isMultipleMode)
        {
            imageDone.setVisibility(0);
        } else
        {
            imageDone.setVisibility(8);
        }
        mGridView = (ImagesGridView)findViewById(0x7f090086);
        footerbar = (ProgressBar)findViewById(0x7f090087);
        imageLoader = ImageLoader.getInstance();
        mSelectUrlList = new ArrayList();
        mSavedImagesList = new ArrayList();
        mGridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                imageViewCheckBox = (ImageView)view.findViewById(0x7f0900aa);
                FacebookDto facebookdto = (FacebookDto)imageUrlList.get(i);
                if(!PhotoSelectorScreen.isMultipleMode)
                {
                    mGridView.setOnItemClickListener(null);
                    pd = new ProgressDialog(AlbumImagesScreen.this);
                    pd.setCancelable(false);
                    pd.setMessage("loading");
                    pd.show();
                    mSelectUrlList.add(((FacebookDto)imageUrlList.get(i)).getUrl());
                    LoadImagetask loadimagetask = new LoadImagetask(new ImageLoadListener() {
                        public void onImageLoad(Bitmap bitmap)
                        {
                            if(bitmap != null)
                            {
                                saveOutput(bitmap);
                            }
                        }
                    });
                    String as[] = new String[1];
                    as[0] = (String)mSelectUrlList.get(0);
                    loadimagetask.execute(as);
                } else
                if(mSelectUrlList.contains(((FacebookDto)imageUrlList.get(i)).getUrl()))
                {
                    facebookdto.setSelected(false);
                    mSelectUrlList.remove(((FacebookDto)imageUrlList.get(i)).getUrl());
                    imageViewCheckBox.setBackgroundResource(0x7f0200a8);
                } else
                if(mSelectUrlList.size() < 30)
                {
                    facebookdto.setSelected(true);
                    mSelectUrlList.add(((FacebookDto)imageUrlList.get(i)).getUrl());
                    imageViewCheckBox.setBackgroundResource(0x7f0200aa);
                } else
                {
                    facebookdto.setSelected(false);
                    Toast.makeText(AlbumImagesScreen.this, getString(0x7f06006e), 0).show();
                }
                imageAdapter.setSelection(i, facebookdto);
            }
        });
    }

    private void saveOutput(Bitmap bitmap)
    {
        String s1;
        Uri uri;
        String s = (new StringBuilder()).append(Environment.getExternalStorageDirectory()).append("/").append(getString(0x7f060023)).append("/").toString();
        File file = new File(s);
        if(!file.exists())
        {
            file.mkdirs();
        }
        s1 = (new StringBuilder(String.valueOf(s))).append(getString(0x7f060023)).append(System.currentTimeMillis()).append(".jpg").toString();
        uri = Uri.fromFile(new File(s1));
        if(uri == null)
        	Log.e(TAG, "Could not save image");
        else{
        	try
            {
		        java.io.OutputStream outputstream = mContentResolver.openOutputStream(uri);
		        if(outputstream != null)
		        {
	                bitmap.compress(mOutputFormat, 100, outputstream);
	                mSavedImagesList.add(s1);
	                Log.e("Size", (new StringBuilder()).append(mSavedImagesList.size()).toString());
	                if(mSavedImagesList.size() == mSelectUrlList.size())
	                {
	                    Intent intent = new Intent();
	                    intent.putStringArrayListExtra("image_list", mSavedImagesList);
	                    setResult(-1, intent);
	                    if(pd != null)
	                    {
	                        pd.dismiss();
	                    }
	                    finish();
	                }
	            }
	        }catch(IOException ioexception){
                Log.e(TAG, (new StringBuilder("Cannot open file: ")).append(uri).toString(), ioexception);
            }
        }
        if(bitmap != null)
        {
            bitmap.recycle();
        }
    }

    public void loadData()
    {
label0:
        {
            if(mResponse != null)
            {
                Request request = mResponse.getRequestForPagedResults(com.facebook.Response.PagingDirection.NEXT);
                if(request == null)
                {
                    break label0;
                }
                request.setCallback(new com.facebook.Request.Callback() {
                    public void onCompleted(Response response)
                    {
                        if(response != null)
                        {
                            mResponse = response;
                            jsonObject = response.getGraphObject().getInnerJSONObject();
                            runOnUiThread(nextRunnable);
                        }
                    }
                });
                Request.executeBatchAsync(new Request[] {
                    request
                });
            }
            return;
        }
        showFooter(false);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        mContentResolver = getContentResolver();
        requestWindowFeature(1);
        setContentView(0x7f03000e);
        init();
        imageLoader = ImageLoader.getInstance();
        if(getIntent().getExtras() != null)
        {
            albumId = getIntent().getStringExtra("albumId");
        }
        if(HttpUtils.isNetworkAvail(this))
        {
            dialog = new ProgressDialog(this);
            dialog.setMessage(getString(0x7f060056));
            dialog.setCancelable(false);
            dialog.show();
            Bundle bundle1 = new Bundle();
            bundle1.putString("fields", "source");
            bundle1.putString("limit", "10");
            (new Request(Session.getActiveSession(), (new StringBuilder("/")).append(albumId).append("/photos").toString(), bundle1, HttpMethod.GET, new com.facebook.Request.Callback() {
                public void onCompleted(Response response)
                {
                    if(response != null)
                    {
                        mResponse = response;
                        jsonObject = response.getGraphObject().getInnerJSONObject();
                        runOnUiThread(runnable);
                    }
                }
            })).executeAsync();
        }
    }

    protected void onDestroy()
    {
        super.onDestroy();
    }

    public void onDoneClickofImage(View view)
    {
        if(PhotoSelectorScreen.isMultipleMode)
        {
            if(mSelectUrlList.size() == 0)
            {
                Toast.makeText(getApplicationContext(), getString(0x7f060055), 1).show();
            } else
            {
                pd = new ProgressDialog(this);
                pd.setCancelable(false);
                pd.setMessage(getString(0x7f060054));
                pd.show();
                Iterator iterator = mSelectUrlList.iterator();
                while(iterator.hasNext()) 
                {
                    String s = (String)iterator.next();
                    if(s != null)
                    {
                        Log.e("MYURL", (new StringBuilder()).append(s).toString());
                        LoadImagetask loadimagetask = new LoadImagetask(new ImageLoadListener() {
                            public void onImageLoad(Bitmap bitmap)
                            {
                                if(bitmap != null)
                                {
                                    saveOutput(bitmap);
                                }
                            }
                        });
                        String as[] = new String[1];
                        as[0] = s.toString();
                        loadimagetask.execute(as);
                    }
                }
            }
        }
    }

    protected void onResume()
    {
        super.onResume();
    }

    public void showFooter(boolean flag)
    {
        if(flag)
        {
            footerbar.setVisibility(0);
            return;
        } else
        {
            footerbar.setVisibility(8);
            return;
        }
    }
}
