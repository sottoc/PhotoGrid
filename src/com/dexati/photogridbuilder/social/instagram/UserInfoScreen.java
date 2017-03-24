package com.dexati.photogridbuilder.social.instagram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.dexati.photogridbuilder.social.instagram.oauth.InstagramSession;
import com.dexati.photogridbuilder.social.instagram.servercall.CallType;
import com.dexati.photogridbuilder.social.instagram.servercall.CallWSTask;
import com.dexati.photogridbuilder.social.instagram.servercall.GetJSONListener;
import com.dexati.photogridbuilder.social.instagram.servercall.HttpUtils;
import com.dexati.photogridbuilder.social.instagram.servercall.ResponseParser;
import com.dexati.photogridbuilder.social.instagram.servercall.UrlConstant;
import com.dexati.photogridbuilder.social.instagram.servercall.UserInfoDto;
import com.km.photogridbuilder.PhotoSelectorScreen;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;

// Referenced classes of package com.dexati.photogridbuilder.social.instagram:
//            UserPhotoScreen

public class UserInfoScreen extends Activity
    implements GetJSONListener
{
    public class ImageAdapter extends BaseAdapter
    {

        private ArrayList instaDtos;
        private LayoutInflater mInflater;

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
            if(view == null)
            {
                view = mInflater.inflate(0x7f03000d, null);
            }
            ImageView imageview = (ImageView)view.findViewById(0x7f090083);
            imageLoader.displayImage(((UserInfoDto)instaDtos.get(i)).getPicUrl(), imageview);
            if(getScreenWidth() < 400)
            {
                android.widget.LinearLayout.LayoutParams layoutparams = new android.widget.LinearLayout.LayoutParams(-2, -2);
                layoutparams.width = 80;
                layoutparams.height = 80;
                imageview.setLayoutParams(layoutparams);
            }
            ((TextView)view.findViewById(0x7f090084)).setText(((UserInfoDto)instaDtos.get(i)).getUserName());
            return view;
        }

        public ImageAdapter(Context context, ArrayList arraylist)
        {
            super();
            mInflater = (LayoutInflater)getSystemService("layout_inflater");
            instaDtos = arraylist;
        }
    }


    private ImageAdapter imageAdapter;
    private ImageView imageDone;
    protected ImageLoader imageLoader;
    private InstagramSession insta;
    private GridView mGridView;

    public UserInfoScreen()
    {
    }

    private int getScreenWidth()
    {
        return getWindowManager().getDefaultDisplay().getWidth();
    }

    private void init()
    {
        imageDone = (ImageView)findViewById(0x7f090085);
        if(PhotoSelectorScreen.isMultipleMode)
        {
            imageDone.setVisibility(8);
        } else
        {
            imageDone.setVisibility(8);
        }
        mGridView = (GridView)findViewById(0x7f090131);
        imageLoader = ImageLoader.getInstance();
        mGridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                Intent intent = new Intent(UserInfoScreen.this, com.dexati.photogridbuilder.social.instagram.UserPhotoScreen.class);
                intent.putExtra("image", (UserInfoDto)imageAdapter.getItem(i));
                startActivityForResult(intent, 4);
            }
        });
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        if(i == 4 && j == -1)
        {
            ArrayList arraylist = intent.getStringArrayListExtra("image_list");
            Log.e("UserInfo", (new StringBuilder()).append(arraylist.size()).toString());
            Intent intent1 = new Intent();
            intent1.putStringArrayListExtra("image_list", arraylist);
            setResult(-1, intent1);
            finish();
        }
        super.onActivityResult(i, j, intent);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(0x7f03003c);
        init();
        insta = new InstagramSession(this);
        if(HttpUtils.isNetworkAvail(this))
        {
            CallWSTask callwstask = new CallWSTask(this, this, CallType.FETCH_FRNDS.toString());
            Object aobj[] = new Object[1];
            aobj[0] = UrlConstant.getUserFollowingUrl(insta.getId(), insta.getAccessToken());
            callwstask.execute(aobj);
        }
    }

    public void onRemoteCallComplete(String s, String s1)
    {
label0:
        {
            if(s1.equals(CallType.FETCH_FRNDS.toString()) && s != null)
            {
                ArrayList arraylist = (new ResponseParser()).parseUserInfos(s, this);
                if(arraylist == null || arraylist.size() <= 0)
                {
                    break label0;
                }
                imageAdapter = new ImageAdapter(getApplicationContext(), arraylist);
                mGridView.setAdapter(imageAdapter);
            }
            return;
        }
        Toast.makeText(this, "Album is empty,please check another album", 1).show();
        finish();
    }


}
