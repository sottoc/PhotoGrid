package com.km.photogridbuilder.cut;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.km.photogridbuilder.util.BitmapUtil;
import java.io.*;

// Referenced classes of package com.km.photogridbuilder.cut:
//            OnViewLoadListener, EraseView

public class EditActivity extends Activity
    implements OnViewLoadListener, android.view.View.OnClickListener
{
    class BackgroundTask extends AsyncTask
    {
        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((Void[])aobj);
        }

        protected Void doInBackground(Void avoid[])
        {
            Bitmap bitmap = EditActivity.cropTransparentArea(mView.getErasedBitmap());
            saveOutput(bitmap);
            return null;
        }

        protected void onPostExecute(Object obj)
        {
            onPostExecute((Void)obj);
        }

        protected void onPostExecute(Void void1)
        {
            if(pd != null)
            {
                pd.dismiss();
            }
            finish();
        }

        protected void onPreExecute()
        {
            if(pd == null)
            {
                pd = new ProgressDialog(EditActivity.this);
                pd.setCancelable(false);
                pd.setTitle("Please Wait");
                pd.setMessage("Save process in progress....");
                pd.show();
            }
        }

        BackgroundTask()
        {
            super();
        }
    }


    protected String currentImageUrl;
    private boolean iscut;
    Bitmap mBmp;
    ImageView mImageViewBrushSize;
    ImageView mImageViewDelete;
    ImageView mImageViewRedo;
    ImageView mImageViewUndo;
    ImageView mImageViewZoom;
    private ImageButton mImgBtnDone;
    RelativeLayout mLayoutPaste;
    LinearLayout mLayoutTools;
    private EraseView mView;
    private String outputPath;
    public ProgressDialog pd;
    private RelativeLayout relativeLayoutActivityStickerInfo;
    private Point screen;
    SeekBar seekBarBrushSize;
    private TextView textViewActivityStickerInfoSticker;

    public EditActivity()
    {
        iscut = true;
    }

    private static Bitmap cropTransparentArea(Bitmap bitmap)
    {
        int i;
        int j;
        int k;
        int l;
        int i1;
        i = bitmap.getWidth();
        j = 0;
        k = bitmap.getHeight();
        l = 0;
        i1 = 0;
        while(true){
	        if(i1 >= bitmap.getWidth())
	        {
	            int k1 = j - i;
	            int l1 = l - k;
	            if(k1 > 0 && l1 > 0)
	            {
	                bitmap = Bitmap.createBitmap(bitmap, i, k, k1, l1);
	            }
	            return bitmap;
	        }
	        int j1 = 0;
	        while(j1 < bitmap.getHeight())
	        {
	        	if(bitmap.getPixel(i1, j1) != 0)
	            {
	                if(k > j1)
	                {
	                    k = j1;
	                }
	                if(l < j1)
	                {
	                    l = j1;
	                }
	                if(i > i1)
	                {
	                    i = i1;
	                }
	                if(j < i1)
	                {
	                    j = i1;
	                }
	            }
	            j1++;
	        }
	        i1++;
        }
    }

    private static Point getDisplaySize(Display display)
    {
        Point point = new Point();
        try
        {
            display.getSize(point);
        }
        catch(NoSuchMethodError nosuchmethoderror)
        {
            point.x = display.getWidth();
            point.y = display.getHeight();
            return point;
        }
        return point;
    }

    private void saveOutput(Bitmap bitmap)
    {
        Uri uri;
        OutputStream outputstream;
        try{
	        uri = Uri.fromFile(new File(outputPath));
	        outputstream = null;
	        if(uri != null)
	        	outputstream = getContentResolver().openOutputStream(uri);
	        if(outputstream != null)
	        {
	        	bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, outputstream);
	            setResult(-1);
	            finish();
	            outputstream.close();
	        }
        }catch(Exception e){}
    }

    public Bitmap getBitmapFromPath(String s)
    {
        return BitmapUtil.getBitmap(getBaseContext(), s, screen.x / 2, screen.y / 2);
    }

    public void onBackPressed()
    {
        setResult(0);
        finish();
    }

    public void onClick(View view)
    {
        switch(view.getId())
        {
        default:
            return;

        case 2131296310: 
            relativeLayoutActivityStickerInfo.setVisibility(4);
            return;

        case 2131296377: 
            (new BackgroundTask()).execute(new Void[0]);
            break;
        }
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        screen = getDisplaySize(((WindowManager)getSystemService("window")).getDefaultDisplay());
        setContentView(0x7f03000b);
        mView = (EraseView)findViewById(0x7f090032);
        mView.setLoadListener(this);
        mImgBtnDone = (ImageButton)findViewById(0x7f090079);
        seekBarBrushSize = (SeekBar)findViewById(0x7f09007b);
        relativeLayoutActivityStickerInfo = (RelativeLayout)findViewById(0x7f090034);
        textViewActivityStickerInfoSticker = (TextView)findViewById(0x7f090035);
        mImageViewZoom = (ImageView)findViewById(0x7f090082);
        mImageViewDelete = (ImageView)findViewById(0x7f090081);
        mImageViewBrushSize = (ImageView)findViewById(0x7f09007d);
        mImageViewRedo = (ImageView)findViewById(0x7f09007f);
        mImageViewUndo = (ImageView)findViewById(0x7f09007e);
        mLayoutTools = (LinearLayout)findViewById(0x7f09007a);
        Intent intent;
        if(iscut)
        {
            mImgBtnDone.setVisibility(0);
            textViewActivityStickerInfoSticker.setText(getString(0x7f060066));
        } else
        {
            textViewActivityStickerInfoSticker.setText(getString(0x7f060067));
            mImgBtnDone.setVisibility(8);
        }
        intent = getIntent();
        if(intent != null && intent.getStringExtra("editimagepath") != null)
        {
            String s = intent.getStringExtra("editimagepath");
            outputPath = intent.getStringExtra("savepath");
            mBmp = getBitmapFromPath(s);
            currentImageUrl = s;
        } else
        {
            Toast.makeText(this, "No Cut Photo to Edit. Please select a Cut Photo.", 1).show();
            finish();
        }
        seekBarBrushSize.setProgress(50);
        seekBarBrushSize.setMax(100);
        seekBarBrushSize.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekbar, int i, boolean flag)
            {
                if(i < 6)
                {
                    mView.updateStrokeWidth(5);
                    return;
                } else
                {
                    mView.updateStrokeWidth(i * 2);
                    return;
                }
            }

            public void onStartTrackingTouch(SeekBar seekbar)
            {
            }

            public void onStopTrackingTouch(SeekBar seekbar)
            {
            }
        });
    }

    public void onDeleteClick(View view)
    {
        mLayoutTools.setVisibility(0);
        mImageViewZoom.setImageResource(0x7f020085);
        mImageViewDelete.setImageResource(0x7f020054);
        mView.setZoom(false);
    }

    public void onDestroy()
    {
        if(mView != null)
        {
            mView.onDestroy();
        }
        super.onDestroy();
    }

    public void onPause()
    {
        super.onPause();
    }

    public void onRedoClick(View view)
    {
        mView.setZoom(false);
        mView.onClickRedo();
    }

    public void onResume()
    {
        super.onResume();
    }

    public void onSizeClick(View view)
    {
        mView.setZoom(false);
        if(seekBarBrushSize.getVisibility() == 0)
        {
            mImageViewBrushSize.setImageResource(0x7f020044);
            seekBarBrushSize.setVisibility(4);
            return;
        } else
        {
            mImageViewBrushSize.setImageResource(0x7f020043);
            seekBarBrushSize.setVisibility(0);
            return;
        }
    }

    public void onUndoClick(View view)
    {
        mView.setZoom(false);
        mView.onClickUndo();
    }

    public void onViewInflated()
    {
        if(mBmp != null)
        {
            mView.setPickedBitmap(mBmp);
            return;
        }
        try
        {
            Toast.makeText(this, "Failed to load", 0).show();
            return;
        }
        catch(OutOfMemoryError outofmemoryerror) { }
        if(mBmp != null)
        {
            mBmp.recycle();
            mBmp = null;
            System.gc();
        }
        Toast.makeText(this, "Failed to load", 0).show();
        return;
    }

    public void onZoomClick(View view)
    {
        mLayoutTools.setVisibility(4);
        mImageViewZoom.setImageResource(0x7f020086);
        mImageViewDelete.setImageResource(0x7f020053);
        mView.setZoom(true);
    }
}
