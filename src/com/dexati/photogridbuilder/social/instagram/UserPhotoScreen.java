package com.dexati.photogridbuilder.social.instagram;

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
import com.dexati.photogridbuilder.social.instagram.oauth.InstagramSession;
import com.dexati.photogridbuilder.social.instagram.servercall.CallType;
import com.dexati.photogridbuilder.social.instagram.servercall.CallWSTask;
import com.dexati.photogridbuilder.social.instagram.servercall.GetJSONListener;
import com.dexati.photogridbuilder.social.instagram.servercall.HttpUtils;
import com.dexati.photogridbuilder.social.instagram.servercall.InstaDto;
import com.dexati.photogridbuilder.social.instagram.servercall.ResponseParser;
import com.dexati.photogridbuilder.social.instagram.servercall.UrlConstant;
import com.dexati.photogridbuilder.social.instagram.servercall.UserInfoDto;
import com.km.photogridbuilder.PhotoSelectorScreen;
import com.km.photogridbuilder.listener.ImageLoadListener;
import com.km.photogridbuilder.listener.LoadImagetask;
import com.km.photogridbuilder.util.BitmapUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class UserPhotoScreen extends Activity
    implements GetJSONListener
{
    public class ImageAdapter extends BaseAdapter
    {
        private ArrayList instaDtos;
        private LayoutInflater mInflater;
        private int mScreenWidth;

        public void addAll(ArrayList arraylist)
        {
            instaDtos.addAll(arraylist);
        }

        public int getCount()
        {
            return instaDtos.size();
        }

        public Object getItem(int i)
        {
            return instaDtos.get(i);
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
                    view = mInflater.inflate(0x7f030026, null);
                }
                imageViewCheckBox = (ImageView)view.findViewById(0x7f0900aa);
                ImageView imageview = (ImageView)view.findViewById(0x7f0900a9);
                if(mScreenWidth < 400)
                {
                    android.widget.FrameLayout.LayoutParams layoutparams = new android.widget.FrameLayout.LayoutParams(-2, -2);
                    layoutparams.width = 80;
                    layoutparams.height = 80;
                    imageview.setLayoutParams(layoutparams);
                    imageViewCheckBox.setLayoutParams(layoutparams);
                }
                imageLoader.displayImage(((InstaDto)instaDtos.get(i)).getInstaThumbPhotos(), imageview);
                if(!PhotoSelectorScreen.isMultipleMode)
                {
                    imageViewCheckBox.setBackgroundResource(0x7f0200a9);
                }
                InstaDto instadto = (InstaDto)instaDtos.get(i);
                if(PhotoSelectorScreen.isMultipleMode)
                {
                    if(!instadto.isSelected())
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

        public void setSelection(int i, InstaDto instadto)
        {
            instaDtos.set(i, instadto);
            notifyDataSetChanged();
        }

        public ImageAdapter(Context context, ArrayList arraylist)
        {
            super();
            mInflater = (LayoutInflater)getSystemService("layout_inflater");
            instaDtos = arraylist;
            mScreenWidth = BitmapUtil.getScreenWidth(UserPhotoScreen.this);
        }
    }


    ArrayList arrayList;
    private ImageAdapter imageAdapter;
    private ImageView imageDone;
    protected ImageLoader imageLoader;
    private ImageView imageViewCheckBox;
    InstagramSession insta;
    private ContentResolver mContentResolver;
    private GridView mGridView;
    private android.graphics.Bitmap.CompressFormat mOutputFormat;
    private ArrayList mSavedImagesList;
    private ArrayList mSelectUrlList;
    private ProgressDialog pd;
    private UserInfoDto userInfoDto;

    public UserPhotoScreen()
    {
        mOutputFormat = android.graphics.Bitmap.CompressFormat.JPEG;
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
        mGridView = (GridView)findViewById(0x7f090131);
        imageLoader = ImageLoader.getInstance();
        arrayList = new ArrayList();
        mSelectUrlList = new ArrayList();
        mSavedImagesList = new ArrayList();
        imageAdapter = new ImageAdapter(getApplicationContext(), arrayList);
        mGridView.setAdapter(imageAdapter);
        mGridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                imageViewCheckBox = (ImageView)view.findViewById(0x7f0900aa);
                InstaDto instadto = (InstaDto)imageAdapter.getItem(i);
                ImageView imageview = (ImageView)view.findViewById(0x7f0900aa);
                String s = instadto.getInstaStandardResoPhotos();
                if(!PhotoSelectorScreen.isMultipleMode)
                {
                    mGridView.setOnItemClickListener(null);
                    pd = new ProgressDialog(UserPhotoScreen.this);
                    pd.setCancelable(false);
                    pd.setMessage("loading");
                    pd.show();
                    mSelectUrlList.add(s);
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
                if(mSelectUrlList.contains(s))
                {
                    instadto.setSelected(false);
                    mSelectUrlList.remove(s);
                    imageview.setBackgroundResource(0x7f0200a8);
                } else
                if(mSelectUrlList.size() < 30)
                {
                    mSelectUrlList.add(s);
                    instadto.setSelected(true);
                    imageview.setBackgroundResource(0x7f0200aa);
                } else
                {
                    instadto.setSelected(false);
                    imageview.setBackgroundResource(0x7f0200a8);
                    Toast.makeText(UserPhotoScreen.this, getString(0x7f06006e), 0).show();
                }
                Log.v("clicked", (new StringBuilder("size ")).append(mSelectUrlList.size()).toString());
                imageAdapter.setSelection(i, instadto);
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
        {
            //break MISSING_BLOCK_LABEL_240;
        	return;
        }
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
        }catch(IOException ioexception) { }
        if(bitmap != null)
        {
            bitmap.recycle();
        }
        return;
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        mContentResolver = getContentResolver();
        requestWindowFeature(1);
        setContentView(0x7f03003c);
        init();
        imageLoader = ImageLoader.getInstance();
        if(getIntent().getExtras() != null)
        {
            userInfoDto = (UserInfoDto)getIntent().getExtras().get("image");
        }
        insta = new InstagramSession(this);
        if(HttpUtils.isNetworkAvail(this))
        {
            CallWSTask callwstask = new CallWSTask(this, this, CallType.FETCH_USERPHOTOS.toString());
            Object aobj[] = new Object[1];
            aobj[0] = UrlConstant.getUserPhotoUrl(userInfoDto.getId(), insta.getAccessToken());
            callwstask.execute(aobj);
        }
    }

    public void onDoneClickofImage(View view)
    {
        if(PhotoSelectorScreen.isMultipleMode)
        {
            if(mSelectUrlList.size() == 0)
            {
                Toast.makeText(getApplicationContext(), "Please select image!!!", 1).show();
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

    public void onRemoteCallComplete(String s, String s1)
    {
label0:
        {
            if(s1.equals(CallType.FETCH_USERPHOTOS.toString()) && s != null)
            {
                arrayList = (new ResponseParser()).parseInstaDatas(s);
                if(arrayList == null || arrayList.size() <= 0)
                {
                    break label0;
                }
                imageAdapter = new ImageAdapter(getApplicationContext(), arrayList);
                mGridView.setAdapter(imageAdapter);
            }
            return;
        }
        Toast.makeText(this, "No data found", 1).show();
    }








}
