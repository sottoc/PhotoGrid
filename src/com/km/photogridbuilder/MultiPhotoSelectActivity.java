package com.km.photogridbuilder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;

// Referenced classes of package com.km.photogridbuilder:
//            BaseActivity

public class MultiPhotoSelectActivity extends BaseActivity
{
    public class Album
    {

        public String albumId;
        private String albumName;
        private String imageUrl;

        public String getAlbumName()
        {
            return albumName;
        }

        public String getImageUrl()
        {
            return imageUrl;
        }

        public void setAlbumName(String s)
        {
            albumName = s;
        }

        public void setImageUrl(String s)
        {
            imageUrl = s;
        }



        public Album()
        {
            super();
        }
    }

    public class AlbumAdapter extends BaseAdapter
    {

        Context mContext;
        LayoutInflater mInflater;
        ArrayList mList;

        public int getCount()
        {
            return mList.size();
        }

        public Album getItem(int i)
        {
            return (Album)mList.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }

        public View getView(int i, View view, ViewGroup viewgroup)
        {
            if(view == null)
            {
                view = mInflater.inflate(0x7f030031, null);
            }
            final ImageView imageview = (ImageView)view.findViewById(0x7f0900d5);
            TextView textview = (TextView)view.findViewById(0x7f0900d6);
            imageLoader.displayImage((new StringBuilder("file://")).append(((Album)mList.get(i)).imageUrl).toString(), imageview, options, new SimpleImageLoadingListener() {
                public void onLoadingComplete(String s, View view, Bitmap bitmap)
                {
                    super.onLoadingComplete(s, view, bitmap);
                    Animation animation = AnimationUtils.loadAnimation(MultiPhotoSelectActivity.this, 0x7f040001);
                    imageview.setAnimation(animation);
                    animation.start();
                }
            });
            textview.setText(((Album)mList.get(i)).albumName);
            return view;
        }


        public AlbumAdapter(Context context, ArrayList arraylist)
        {
            super();
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mList = new ArrayList();
            mList = arraylist;
        }
    }

    public class ImageAdapter extends BaseAdapter
    {
        Context mContext;
        LayoutInflater mInflater;
        ArrayList mList;
        SparseBooleanArray mSparseBooleanArray;

        public ArrayList getCheckedItems()
        {
            ArrayList arraylist = new ArrayList();
            int i = 0;
            do
            {
                if(i >= mList.size())
                {
                    return arraylist;
                }
                if(mSparseBooleanArray.get(i))
                {
                    arraylist.add((String)mList.get(i));
                }
                i++;
            } while(true);
        }

        public int getCount()
        {
            return imageUrls.size();
        }

        public Object getItem(int i)
        {
            return null;
        }

        public long getItemId(int i)
        {
            return (long)i;
        }

        public View getView(final int position, View view, ViewGroup viewgroup)
        {
            if(view == null)
            {
                view = mInflater.inflate(0x7f030033, null);
            }
            final ImageView imageview = (ImageView)view.findViewById(0x7f0900d9);
            final ImageView imageview1 = (ImageView)view.findViewById(0x7f0900d5);
            imageLoader.displayImage((new StringBuilder("file://")).append((String)imageUrls.get(position)).toString(), imageview1, options, new SimpleImageLoadingListener() {
                public void onLoadingComplete(String s, View view, Bitmap bitmap)
                {
                    super.onLoadingComplete(s, view, bitmap);
                    Animation animation = AnimationUtils.loadAnimation(MultiPhotoSelectActivity.this, 0x7f040001);
                    imageview1.setAnimation(animation);
                    animation.start();
                }
            });
            imageview.setTag(Boolean.valueOf(mSparseBooleanArray.get(position)));
            if(mSparseBooleanArray.get(position))
            {
                imageview.setBackgroundResource(0x7f020194);
            } else
            {
                imageview.setBackgroundColor(0);
            }
            imageview.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(View view)
                {
                	int i;
                    if(imageAdapter.getCheckedItems().size() >= 30){
                    	mSparseBooleanArray.put(position, false);
                    	imageview.setBackgroundColor(0);
                        if(imageAdapter.getCheckedItems().size() > 29)
                        {
                            Toast.makeText(mContext, mContext.getString(0x7f06006e), 0).show();
                        }
                    }else{
	                    if(mSparseBooleanArray.get(position))
	                    {
	                    	imageview.setBackgroundColor(0);
	                        mSparseBooleanArray.put(position, false);
	                    } else
	                    {
	                    	imageview.setBackgroundResource(0x7f020194);
	                        mSparseBooleanArray.put(position, true);
	                    }
                    }
                    i = imageAdapter.getCheckedItems().size();
                    counter_txtView.setText((new StringBuilder()).append(i).toString());
                }
            });
            return view;
        }


        public ImageAdapter(Context context, ArrayList arraylist)
        {
            super();
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mSparseBooleanArray = new SparseBooleanArray();
            mList = new ArrayList();
            mList = arraylist;
        }
    }


    private AlbumAdapter albumAdapter;
    TextView counter_txtView;
    private GridView gridView;
    private ImageAdapter imageAdapter;
    private ArrayList imageUrls;
    private boolean isAlbumView;
    private ArrayList mAlbumsList;
    private DisplayImageOptions options;
    RelativeLayout topdoneButton_gallery;

    public MultiPhotoSelectActivity()
    {
    }

    private void fillAlbums()
    {
        isAlbumView = true;
        mAlbumsList = new ArrayList();
        String as[] = {
            "_data", "_id", "bucket_display_name", "bucket_id"
        };
        Cursor cursor = managedQuery(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, as, null, null, null);
        HashSet hashset = new HashSet();
        int i = 0;
        do
        {
            if(i >= cursor.getCount())
            {
                return;
            }
            Album album = new Album();
            cursor.moveToPosition(i);
            int j = cursor.getColumnIndex("_data");
            int k = cursor.getColumnIndex("bucket_display_name");
            int l = cursor.getColumnIndex("bucket_id");
            cursor.getColumnIndex("_id");
            String s = cursor.getString(k);
            album.setAlbumName(s);
            album.setImageUrl(cursor.getString(j));
            album.albumId = cursor.getString(l);
            if(hashset.add(s))
            {
                mAlbumsList.add(album);
            }
            System.out.println((new StringBuilder("=====> BUCKET_DISPLAY_NAME => ")).append(cursor.getString(cursor.getColumnIndex("bucket_display_name"))).toString());
            i++;
        } while(true);
    }

    private void fillGallery(String s)
    {
        String as[] = {
            "_data", "_id"
        };
        Cursor cursor = managedQuery(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, as, "bucket_id=?", new String[] {
            s
        }, "datetaken DESC");
        imageUrls = new ArrayList();
        int i = 0;
        do
        {
            if(i >= cursor.getCount())
            {
                imageAdapter = new ImageAdapter(this, imageUrls);
                counter_txtView.setText("0");
                gridView.setAdapter(imageAdapter);
                return;
            }
            cursor.moveToPosition(i);
            int j = cursor.getColumnIndex("_data");
            imageUrls.add(cursor.getString(j));
            i++;
        } while(true);
    }

    public void onBackPressed()
    {
        if(isAlbumView)
        {
            super.onBackPressed();
            return;
        }
        isAlbumView = true;
        if(isAlbumView)
        {
            topdoneButton_gallery.setVisibility(8);
        }
        gridView.setAdapter(albumAdapter);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f030000);
        topdoneButton_gallery = (RelativeLayout)findViewById(0x7f090025);
        counter_txtView = (TextView)findViewById(0x7f090026);
        topdoneButton_gallery.setVisibility(8);
        fillAlbums();
        options = (new com.nostra13.universalimageloader.core.DisplayImageOptions.Builder()).showStubImage(0x108003f).showImageForEmptyUri(0x108003f).cacheInMemory().cacheOnDisc().build();
        albumAdapter = new AlbumAdapter(this, mAlbumsList);
        gridView = (GridView)findViewById(0x7f090028);
        gridView.setAdapter(albumAdapter);
        gridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                if(isAlbumView)
                {
                    isAlbumView = false;
                    if(!isAlbumView)
                    {
                        topdoneButton_gallery.setVisibility(0);
                    }
                    fillGallery(((Album)mAlbumsList.get(i)).albumId);
                }
            }
        });
    }

    public void onDoneAlbum(View view)
    {
        if(imageAdapter == null)
        {
            Toast.makeText(this, "Select Photos from a Album first.", 1).show();
            return;
        } else
        {
            ArrayList arraylist = imageAdapter.getCheckedItems();
            Intent intent = new Intent();
            intent.putExtra("list", arraylist);
            setResult(-1, intent);
            finish();
            return;
        }
    }

    public void onDoneGallery(View view)
    {
        ArrayList arraylist = imageAdapter.getCheckedItems();
        if(arraylist.size() > 0)
        {
            Intent intent = new Intent();
            intent.putExtra("list", arraylist);
            setResult(-1, intent);
            finish();
            return;
        } else
        {
            Toast.makeText(this, "Select Photos first.", 1).show();
            return;
        }
    }

    protected void onStop()
    {
        imageLoader.stop();
        super.onStop();
    }







}
