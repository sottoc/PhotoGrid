package com.dexati.photogridbuilder.social.facebook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.facebook.*;
import com.facebook.model.GraphObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import org.json.JSONObject;

// Referenced classes of package com.dexati.photogridbuilder.social.facebook:
//            EndLessListener, AlbumGridview, HttpUtils, AlbumsInfo, 
//            ResponseParser, ServerCalls, AlbumImagesScreen

public class AlbumListScreen extends Activity
    implements EndLessListener
{
    public class AlbumListAdapter extends BaseAdapter
    {

        private ArrayList albumInfos;
        private LayoutInflater mInflater;

        public void addAll(ArrayList arraylist)
        {
            albumInfos.addAll(arraylist);
        }

        public int getCount()
        {
            return albumInfos.size();
        }

        public Object getItem(int i)
        {
            return albumInfos.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }

        public View getView(int i, View view, ViewGroup viewgroup)
        {
            if(view == null)
            {
                view = mInflater.inflate(0x7f030027, null);
            }
            ImageView imageview = (ImageView)view.findViewById(0x7f090083);
            AlbumsInfo albumsinfo = (AlbumsInfo)albumInfos.get(i);
            if(albumsinfo.getCoverPicUrl() != null)
            {
                imageLoader.displayImage(albumsinfo.getCoverPicUrl(), imageview);
            }
            ((TextView)view.findViewById(0x7f090084)).setText(albumsinfo.getAlbumName());
            return view;
        }

        public AlbumListAdapter(Context context, ArrayList arraylist)
        {
            super();
            mInflater = (LayoutInflater)getSystemService("layout_inflater");
            albumInfos = arraylist;
        }
    }


    protected ArrayList albumList;
    private ProgressDialog dialog;
    private ProgressBar footerbar;
    private AlbumListAdapter imageAdapter;
    protected ImageLoader imageLoader;
    protected JSONObject jsonObject;
    private AlbumGridview mGridView;
    private Response mResponse;
    Runnable nextRunnable;
    Runnable runnable;

    public AlbumListScreen()
    {
        runnable = new Runnable() {
            public void run()
            {
                if(jsonObject != null)
                {
                    albumList = ResponseParser.parseAlbumData(jsonObject);
                    if(albumList != null && albumList.size() > 0)
                    {
                        imageAdapter = new AlbumListAdapter(getApplicationContext(), albumList);
                        mGridView.setListener(AlbumListScreen.this);
                        mGridView.setAdapter(imageAdapter);
                        ServerCalls.loadAlbumCovers(albumList, imageAdapter);
                    } else
                    {
                        Toast.makeText(AlbumListScreen.this, "Album is empty,please check another album", 1).show();
                        finish();
                    }
                }
                dialog.dismiss();
            }
        };
        nextRunnable = new Runnable() {
            public void run()
            {
                mGridView.addNewData(ResponseParser.parseAlbumData(jsonObject));
                ServerCalls.loadAlbumCovers(albumList, imageAdapter);
            }
        };
    }

    private void init()
    {
        mGridView = (AlbumGridview)findViewById(0x7f090086);
        footerbar = (ProgressBar)findViewById(0x7f090087);
        imageLoader = ImageLoader.getInstance();
        mGridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                Intent intent = new Intent(AlbumListScreen.this, com.dexati.photogridbuilder.social.facebook.AlbumImagesScreen.class);
                intent.putExtra("albumId", ((AlbumsInfo)albumList.get(i)).getFbId());
                startActivityForResult(intent, 3);
            }
        });
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

    protected void onActivityResult(int i, int j, Intent intent)
    {
        if(j == -1)
        {
            ArrayList arraylist = intent.getStringArrayListExtra("image_list");
            Log.e("Albumlist", (new StringBuilder()).append(arraylist.size()).toString());
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
        setContentView(0x7f03000f);
        init();
        if(HttpUtils.isNetworkAvail(this))
        {
            dialog = new ProgressDialog(this);
            dialog.setMessage(getString(0x7f060057));
            dialog.show();
            Bundle bundle1 = new Bundle();
            bundle1.putString("fields", "id,name,cover_photo,count");
            bundle1.putString("limit", "6");
            (new Request(Session.getActiveSession(), "/me/albums", bundle1, HttpMethod.GET, new com.facebook.Request.Callback() {
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
