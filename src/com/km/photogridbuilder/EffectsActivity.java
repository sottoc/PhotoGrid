package com.km.photogridbuilder;

import afzkl.development.colorpickerview.dialog.ColorPickerDialog;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.*;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.km.photogridbuilder.Objects.EffectsView;
import com.km.photogridbuilder.bean.Constant;
import com.km.photogridbuilder.flip.AppUtils_Flip;
import com.km.photogridbuilder.flip.MirrorEffect_Flip;
import com.km.photogridbuilder.flip.MirrorTask_Flip;
import com.km.photogridbuilder.mirroreffects.AppUtils;
import com.km.photogridbuilder.mirroreffects.MirrorEffect;
import com.km.photogridbuilder.mirroreffects.MirrorTask;
import com.km.photogridbuilder.shapecut.CircularColorView;
import com.km.photogridbuilder.shapecut.CropperView;
import com.km.photogridbuilder.shapecut.Shape;
import com.km.photogridbuilder.util.BitmapUtil;
import com.km.photogridbuilder.util.PhotoUtil;
import java.io.*;

public class EffectsActivity extends Activity
    implements android.view.View.OnClickListener, android.widget.SeekBar.OnSeekBarChangeListener
{
    private class BackgroundSave extends AsyncTask
    {

        Bitmap bitmap;
        String pathToSave;

        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((Bitmap[])aobj);
        }

        protected Void doInBackground(Bitmap abitmap[])
        {
            bitmap = abitmap[0];
            saveOutput(bitmap, pathToSave);
            return null;
        }

        protected void onPostExecute(Object obj)
        {
            onPostExecute((Void)obj);
        }

        protected void onPostExecute(Void void1)
        {
            if(bitmap != null)
            {
                bitmap.recycle();
                bitmap = null;
            }
            super.onPostExecute(void1);
        }

        public BackgroundSave(String s)
        {
            super();
            pathToSave = s;
        }
    }

    class BackgroungTask extends AsyncTask
    {
        protected Bitmap doInBackground(Integer ainteger[])
        {
            int i = ainteger[0].intValue();
            Bitmap bitmap;
            if(i == 0)
            {
                bitmap = null;
            } else
            {
                bitmap = originalBitmap;
                if(bitmap != null)
                {
                    switch(i)
                    {
                    default:
                        return bitmap;

                    case 1: // '\001'
                        Bitmap bitmap5;
                        try
                        {
                            bitmap5 = PhotoUtil.changeToOld(bitmap);
                        }
                        catch(OutOfMemoryError outofmemoryerror4)
                        {
                            outofmemoryerror4.printStackTrace();
                            return null;
                        }
                        return bitmap5;

                    case 2: // '\002'
                        Bitmap bitmap4;
                        try
                        {
                            bitmap4 = PhotoUtil.toGray(bitmap);
                        }
                        catch(OutOfMemoryError outofmemoryerror3)
                        {
                            outofmemoryerror3.printStackTrace();
                            return null;
                        }
                        return bitmap4;

                    case 3: // '\003'
                        Bitmap bitmap3;
                        try
                        {
                            bitmap3 = PhotoUtil.toBlue(bitmap);
                        }
                        catch(OutOfMemoryError outofmemoryerror2)
                        {
                            outofmemoryerror2.printStackTrace();
                            return null;
                        }
                        return bitmap3;

                    case 4: // '\004'
                        Bitmap bitmap2;
                        try
                        {
                            bitmap2 = PhotoUtil.toGreen(bitmap);
                        }
                        catch(OutOfMemoryError outofmemoryerror1)
                        {
                            outofmemoryerror1.printStackTrace();
                            return null;
                        }
                        return bitmap2;

                    case 5: // '\005'
                        break;
                    }
                    Bitmap bitmap1;
                    try
                    {
                        bitmap1 = PhotoUtil.toRed(bitmap);
                    }
                    catch(OutOfMemoryError outofmemoryerror)
                    {
                        outofmemoryerror.printStackTrace();
                        return null;
                    }
                    return bitmap1;
                }
            }
            return bitmap;
        }

        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((Integer[])aobj);
        }

        protected void onPostExecute(Bitmap bitmap)
        {
            if(pd != null)
            {
                pd.dismiss();
            }
            if(bitmap != null)
            {
                view.setBitmap(bitmap);
                view.invalidate();
            }
        }

        protected void onPostExecute(Object obj)
        {
            onPostExecute((Bitmap)obj);
        }

        protected void onPreExecute()
        {
            pd.show();
        }

        BackgroungTask()
        {
            super();
        }
    }

    private class DoneTask extends AsyncTask
    {
        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((Bitmap[])aobj);
        }

        protected Void doInBackground(Bitmap abitmap[])
        {
            if(mCurrentView == 1)
            {
                Bitmap bitmap1 = view.getBitmap();
                saveOutput(bitmap1, outputPath);
                checkforOriginalandSave();
            } else
            {
                Bitmap bitmap = cropperView.getShapeBitmap();
                saveOutput(bitmap, outputPath);
                checkforOriginalandSave();
            }
            setResult(-1);
            finish();
            return null;
        }

        protected void onPostExecute(Object obj)
        {
            onPostExecute((Void)obj);
        }

        protected void onPostExecute(Void void1)
        {
            if(mProgressDoneTask != null)
            {
                mProgressDoneTask.dismiss();
            }
        }

        protected void onPreExecute()
        {
            mProgressDoneTask.show();
        }

        private DoneTask()
        {
            super();
        }

        DoneTask(DoneTask donetask)
        {
            this();
        }
    }


    private static File mFolder;
    private LinearLayout containerCategoryMirror;
    private LinearLayout containerCategoryPhotFilter;
    private LinearLayout containerCategoryShapeCut;
    private CropperView cropperView;
    private String galleryImageFilePath;
    int imageHeight;
    int imageWidth;
    private boolean isFlipClicked;
    boolean isFreeFlow;
    private boolean isMirrorClicked;
    private boolean isPhotoFilterClicked;
    LinearLayout layoutBottomBarEdit;
    private RelativeLayout layoutCategoryMirror;
    private RelativeLayout layoutCategoryPhotoFilter;
    private RelativeLayout layoutCategoryShapeCut;
    private CircularColorView mCircularColorView;
    private int mCurrentView;
    Uri mImageUri;
    String mOutputFilePath;
    ProgressDialog mProgressDoneTask;
    private View mViewShapeCut;
    private Bitmap originalBitmap;
    private String outputPath;
    ProgressDialog pd;
    private Point point;
    private SeekBar seekbar;
    LinearLayout shapCut_Option;
    private int shapeid;
    private EffectsView view;

    public EffectsActivity()
    {
        pd = null;
        mCurrentView = 1;
    }

    private void addMirrorEffect()
    {
        isPhotoFilterClicked = false;
        isMirrorClicked = true;
        isFlipClicked = false;
        if(containerCategoryMirror != null)
        {
            containerCategoryMirror.removeAllViews();
        }
        layoutCategoryMirror.setVisibility(0);
        layoutCategoryPhotoFilter.setVisibility(8);
        layoutCategoryShapeCut.setVisibility(8);
        LayoutInflater layoutinflater = (LayoutInflater)getSystemService("layout_inflater");
        int i = 0;
        do
        {
            if(i >= Constant.mirror_effects_resources.length)
            {
                return;
            }
            RelativeLayout relativelayout = (RelativeLayout)layoutinflater.inflate(0x7f03002a, null);
            relativelayout.setId(i + 2000);
            relativelayout.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(View view1)
                {
                    int j = Constant.mirror_effects_resources[-2000 + view1.getId()];
                    AppUtils.mBmpImage = originalBitmap;
                    switch(j)
                    {
                    default:
                        Toast.makeText(getBaseContext(), "Effect not Available!", 0).show();
                        return;

                    case 2130837948: 
                        Log.e("Blue", "Mirror Left");
                        (new MirrorTask(view, EffectsActivity.this, MirrorEffect.LEFT)).execute(new String[0]);
                        return;

                    case 2130837952: 
                        (new MirrorTask(view, EffectsActivity.this, MirrorEffect.RIGHT)).execute(new String[0]);
                        return;

                    case 2130837958: 
                        (new MirrorTask(view, EffectsActivity.this, MirrorEffect.TOP)).execute(new String[0]);
                        return;

                    case 2130837920: 
                        (new MirrorTask(view, EffectsActivity.this, MirrorEffect.BOTTOM)).execute(new String[0]);
                        return;
                    }
                }
            });
            ((TextView)relativelayout.findViewById(0x7f0900c6)).setText(Constant.mirror_effects_names[i]);
            ((ImageView)relativelayout.findViewById(0x7f0900bd)).setImageResource(Constant.mirror_effects_resources[i]);
            containerCategoryMirror.addView(relativelayout);
            if(i == -1 + Constant.mirror_effects_resources.length)
            {
                ((ImageView)relativelayout.findViewById(0x7f0900c7)).setVisibility(8);
            }
            i++;
        } while(true);
    }

    private void addPhotoFilterEffects()
    {
        isPhotoFilterClicked = true;
        isMirrorClicked = false;
        isFlipClicked = false;
        if(containerCategoryPhotFilter != null)
        {
            containerCategoryPhotFilter.removeAllViews();
        }
        layoutCategoryMirror.setVisibility(8);
        layoutCategoryPhotoFilter.setVisibility(0);
        layoutCategoryShapeCut.setVisibility(8);
        LayoutInflater layoutinflater = (LayoutInflater)getSystemService("layout_inflater");
        int i = 0;
        do
        {
            if(i >= Constant.effects_resources.length)
            {
                return;
            }
            RelativeLayout relativelayout = (RelativeLayout)layoutinflater.inflate(0x7f03002a, null);
            relativelayout.setId(i + 1000);
            relativelayout.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(View view1)
                {
                    switch(Constant.effects_resources[-1000 + view1.getId()])
                    {
                    default:
                        Toast.makeText(getBaseContext(), "Effect not Available!", 0).show();
                        return;

                    case 2130837941: 
                        BackgroungTask backgroungtask4 = new BackgroungTask();
                        Integer ainteger4[] = new Integer[1];
                        ainteger4[0] = Integer.valueOf(1);
                        backgroungtask4.execute(ainteger4);
                        return;

                    case 2130837937: 
                        BackgroungTask backgroungtask3 = new BackgroungTask();
                        Integer ainteger3[] = new Integer[1];
                        ainteger3[0] = Integer.valueOf(2);
                        backgroungtask3.execute(ainteger3);
                        return;

                    case 2130837938: 
                        BackgroungTask backgroungtask2 = new BackgroungTask();
                        Integer ainteger2[] = new Integer[1];
                        ainteger2[0] = Integer.valueOf(3);
                        backgroungtask2.execute(ainteger2);
                        return;

                    case 2130837939: 
                        BackgroungTask backgroungtask1 = new BackgroungTask();
                        Integer ainteger1[] = new Integer[1];
                        ainteger1[0] = Integer.valueOf(4);
                        backgroungtask1.execute(ainteger1);
                        return;

                    case 2130837940: 
                        BackgroungTask backgroungtask = new BackgroungTask();
                        Integer ainteger[] = new Integer[1];
                        ainteger[0] = Integer.valueOf(5);
                        backgroungtask.execute(ainteger);
                        return;
                    }
                }
            });
            ((TextView)relativelayout.findViewById(0x7f0900c6)).setText(Constant.effects_names[i]);
            ((ImageView)relativelayout.findViewById(0x7f0900bd)).setImageResource(Constant.effects_resources[i]);
            containerCategoryPhotFilter.addView(relativelayout);
            if(i == -1 + Constant.effects_resources.length)
            {
                ((ImageView)relativelayout.findViewById(0x7f0900c7)).setVisibility(8);
            }
            i++;
        } while(true);
    }

    private void addShapeCutEffects()
    {
        isPhotoFilterClicked = false;
        isMirrorClicked = false;
        isFlipClicked = false;
        if(containerCategoryShapeCut != null)
        {
            containerCategoryShapeCut.removeAllViews();
        }
        layoutCategoryMirror.setVisibility(8);
        layoutCategoryPhotoFilter.setVisibility(8);
        layoutCategoryShapeCut.setVisibility(0);
        LayoutInflater layoutinflater = (LayoutInflater)getSystemService("layout_inflater");
        int i = 0;
        do
        {
            if(i >= Shape.SHAPE_ARRAY_NORMAL.length)
            {
                return;
            }
            RelativeLayout relativelayout = (RelativeLayout)layoutinflater.inflate(0x7f03002a, null);
            relativelayout.setId(i + 1000);
            relativelayout.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(View view1)
                {
                    switch(Shape.SHAPE_ARRAY_NORMAL[-1000 + view1.getId()])
                    {
                    case 2130837964: 
                    default:
                        Toast.makeText(getBaseContext(), "Effect not Available!", 0).show();
                        return;

                    case 2130837962: 
                        cropperView.changeShape(0x7f0200ad);
                        shapeid = 0x7f0200ad;
                        return;

                    case 2130837963: 
                        cropperView.changeShape(0x7f0200fd);
                        shapeid = 0x7f0200fd;
                        return;

                    case 2130837965: 
                        cropperView.changeShape(0x7f02014a);
                        shapeid = 0x7f02014a;
                        return;

                    case 2130837966: 
                        cropperView.changeShape(0x7f02014c);
                        shapeid = 0x7f02014c;
                        return;

                    case 2130837967: 
                        cropperView.changeShape(0x7f020191);
                        shapeid = 0x7f020191;
                        return;

                    case 2130837968: 
                        cropperView.changeShape(0x7f020192);
                        shapeid = 0x7f020192;
                        return;

                    case 2130837969: 
                        cropperView.changeShape(0x7f02019d);
                        shapeid = 0x7f02019d;
                        return;

                    case 2130837970: 
                        cropperView.changeShape(0x7f0201f6);
                        shapeid = 0x7f0201f6;
                        return;

                    case 2130837971: 
                        cropperView.changeShape(0x7f0201f7);
                        shapeid = 0x7f0201f7;
                        return;

                    case 2130837961: 
                        cropperView.changeShape(0x7f0200a7);
                        shapeid = 0x7f0200a7;
                        return;
                    }
                }
            });
            ((TextView)relativelayout.findViewById(0x7f0900c6)).setText(Shape.TEXT_STRING[i]);
            ((ImageView)relativelayout.findViewById(0x7f0900bd)).setImageResource(Shape.SHAPE_ARRAY_NORMAL[i]);
            containerCategoryShapeCut.addView(relativelayout);
            if(i == -1 + Shape.SHAPE_ARRAY_NORMAL.length)
            {
                ((ImageView)relativelayout.findViewById(0x7f0900c7)).setVisibility(8);
            }
            i++;
        } while(true);
    }

    private void checkforOriginalandSave()
    {
        if(!(new File(outputPath)).getParentFile().exists())
        	return;
        File afile[];
        int i;
        afile = (new File(outputPath)).getParentFile().listFiles();
        i = 0;
        int j;
        boolean flag;
        j = afile.length;
        flag = false;
        while(i < j){
        	if(!afile[i].getName().contains("image_original"))
            {
            	i++;
            	continue;
            }
            flag = true;
            break;
        }
        if(!flag)
        {
            Bitmap bitmap = BitmapUtil.getBitmap(getBaseContext(), galleryImageFilePath, point.x / 2, point.y / 2);
            (new BackgroundSave((new StringBuilder(String.valueOf((new File(outputPath)).getParent()))).append(File.separator).append("image_original.png").toString())).execute(new Bitmap[] {
                bitmap
            });
        }
    }

    private static Point getDisplaySize(Display display)
    {
        Point point1 = new Point();
        try
        {
            display.getSize(point1);
        }
        catch(NoSuchMethodError nosuchmethoderror)
        {
            point1.x = display.getWidth();
            point1.y = display.getHeight();
            return point1;
        }
        return point1;
    }

    private void saveOutput(Bitmap bitmap, String s)
    {
        Uri uri;
        OutputStream outputstream;
        try{
	        File file = new File((new File(s)).getParent());
	        if(!file.exists())
	        {
	            file.mkdir();
	        }
	        uri = Uri.fromFile(new File(s));
	        outputstream = null;
	        if(uri != null)
	        {
	            outputstream = getContentResolver().openOutputStream(uri);
	        }
	        if(outputstream != null)
	        {
	        	bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, outputstream);
	            outputstream.close();
	        }
        }catch(Exception e){}
    }

    public void changeColor(View view1)
    {
        onClickColorPickerDialog();
    }

    public void onClick(View view1)
    {
        switch(view1.getId())
        {
        default:
            return;

        case 2131296312: 
            (new DoneTask(null)).execute(new Bitmap[0]);
            return;

        case 2131296327: 
            mCurrentView = 1;
            mViewShapeCut.setVisibility(8);
            ((RelativeLayout)findViewById(0x7f090043)).setVisibility(8);
            if(!isMirrorClicked)
            {
                view.setBitmap(originalBitmap);
            }
            isMirrorClicked = true;
            if(layoutCategoryMirror.isShown())
            {
                layoutCategoryMirror.setVisibility(8);
                layoutCategoryMirror.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040004));
                ((ImageView)findViewById(0x7f090047)).setImageResource(0x7f02006b);
                ((ImageView)findViewById(0x7f090049)).setImageResource(0x7f02004f);
                ((ImageView)findViewById(0x7f090046)).setImageResource(0x7f02006d);
                ((ImageView)findViewById(0x7f09004a)).setImageResource(0x7f02005d);
            } else
            {
                addMirrorEffect();
                layoutCategoryMirror.setVisibility(0);
                layoutCategoryMirror.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040000));
                ((ImageView)findViewById(0x7f090047)).setImageResource(0x7f02006c);
                ((ImageView)findViewById(0x7f090049)).setImageResource(0x7f02004f);
                ((ImageView)findViewById(0x7f090046)).setImageResource(0x7f02006d);
                ((ImageView)findViewById(0x7f09004a)).setImageResource(0x7f02005d);
            }
            view.setVisibility(0);
            return;

        case 2131296329: 
            mCurrentView = 3;
            mViewShapeCut.setVisibility(0);
            view.setVisibility(8);
            if(layoutCategoryShapeCut.isShown())
            {
                layoutCategoryShapeCut.setVisibility(8);
                layoutCategoryShapeCut.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040004));
                ((ImageView)findViewById(0x7f090049)).setImageResource(0x7f02004f);
                ((ImageView)findViewById(0x7f090046)).setImageResource(0x7f02006d);
                ((ImageView)findViewById(0x7f090047)).setImageResource(0x7f02006b);
                ((ImageView)findViewById(0x7f09004a)).setImageResource(0x7f02005d);
            } else
            {
                addShapeCutEffects();
                layoutCategoryShapeCut.setVisibility(0);
                layoutCategoryShapeCut.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040000));
                ((ImageView)findViewById(0x7f090049)).setImageResource(0x7f020050);
                ((ImageView)findViewById(0x7f090046)).setImageResource(0x7f02006d);
                ((ImageView)findViewById(0x7f090047)).setImageResource(0x7f02006b);
                ((ImageView)findViewById(0x7f09004a)).setImageResource(0x7f02005d);
            }
            ((RelativeLayout)findViewById(0x7f090043)).setVisibility(8);
            cropperView.getViewTreeObserver().addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout()
                {
                    cropperView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    if(originalBitmap != null && shapeid != 0)
                    {
                        cropperView.populateView(originalBitmap, shapeid, cropperView.getMeasuredWidth(), cropperView.getMeasuredHeight());
                    }
                }
            });
            return;

        case 2131296330: 
            mCurrentView = 1;
            if(!isFlipClicked)
            {
                view.setBitmap(originalBitmap);
            }
            isFlipClicked = true;
            layoutCategoryMirror.setVisibility(8);
            layoutCategoryPhotoFilter.setVisibility(8);
            layoutCategoryShapeCut.setVisibility(8);
            mViewShapeCut.setVisibility(8);
            view.setVisibility(0);
            if(((RelativeLayout)findViewById(0x7f090043)).isShown())
            {
                ((RelativeLayout)findViewById(0x7f090043)).setVisibility(8);
                ((RelativeLayout)findViewById(0x7f090043)).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040004));
                ((ImageView)findViewById(0x7f09004a)).setImageResource(0x7f02005d);
                ((ImageView)findViewById(0x7f090049)).setImageResource(0x7f02004f);
                ((ImageView)findViewById(0x7f090046)).setImageResource(0x7f02006d);
                ((ImageView)findViewById(0x7f090047)).setImageResource(0x7f02006b);
                return;
            } else
            {
                ((RelativeLayout)findViewById(0x7f090043)).setVisibility(0);
                ((RelativeLayout)findViewById(0x7f090043)).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040000));
                ((ImageView)findViewById(0x7f09004a)).setImageResource(0x7f02005e);
                ((ImageView)findViewById(0x7f090049)).setImageResource(0x7f02004f);
                ((ImageView)findViewById(0x7f090046)).setImageResource(0x7f02006d);
                ((ImageView)findViewById(0x7f090047)).setImageResource(0x7f02006b);
                return;
            }

        case 2131296326: 
            if(!isPhotoFilterClicked)
            {
                view.setBitmap(originalBitmap);
            }
            isPhotoFilterClicked = true;
            mCurrentView = 1;
            mViewShapeCut.setVisibility(8);
            view.setVisibility(0);
            ((RelativeLayout)findViewById(0x7f090043)).setVisibility(8);
            if(layoutCategoryPhotoFilter.isShown())
            {
                layoutCategoryPhotoFilter.setVisibility(8);
                layoutCategoryPhotoFilter.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040004));
                ((ImageView)findViewById(0x7f090046)).setImageResource(0x7f02006d);
                ((ImageView)findViewById(0x7f09004a)).setImageResource(0x7f02005d);
                ((ImageView)findViewById(0x7f090049)).setImageResource(0x7f02004f);
                ((ImageView)findViewById(0x7f090047)).setImageResource(0x7f02006b);
            } else
            {
                addPhotoFilterEffects();
                layoutCategoryPhotoFilter.setVisibility(0);
                layoutCategoryPhotoFilter.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040000));
                ((ImageView)findViewById(0x7f090046)).setImageResource(0x7f02006e);
                ((ImageView)findViewById(0x7f09004a)).setImageResource(0x7f02005d);
                ((ImageView)findViewById(0x7f090049)).setImageResource(0x7f02004f);
                ((ImageView)findViewById(0x7f090047)).setImageResource(0x7f02006b);
            }
            view.invalidate();
            return;

        case 2131296325: 
            mCurrentView = 1;
            isPhotoFilterClicked = false;
            isMirrorClicked = false;
            isFlipClicked = true;
            AppUtils_Flip.mBmpImage = originalBitmap;
            (new MirrorTask_Flip(view, this, MirrorEffect_Flip.TOP)).execute(new String[0]);
            return;

        case 2131296324: 
            mCurrentView = 1;
            isPhotoFilterClicked = false;
            isMirrorClicked = false;
            isFlipClicked = true;
            AppUtils_Flip.mBmpImage = originalBitmap;
            (new MirrorTask_Flip(view, this, MirrorEffect_Flip.LEFT)).execute(new String[0]);
            return;
        }
    }

    public void onClickColorPickerDialog()
    {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final ColorPickerDialog colorDialog = new ColorPickerDialog(this, prefs.getInt("color", -1));
        colorDialog.setTitle("Pick a Color!");
        colorDialog.setButton(-1, getString(0x104000a), new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
                android.content.SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("color", colorDialog.getColor());
                editor.commit();
                Shape.borderColor = colorDialog.getColor();
                cropperView.updateBorderColor(Shape.borderColor);
                mCircularColorView.setColor(Shape.borderColor);
                Log.v("color", (new StringBuilder()).append(Shape.borderColor).toString());
            }
        });
        colorDialog.setButton(-2, getString(0x1040000), new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
            }
        });
        colorDialog.show();
    }

    public void onConfigurationChanged(Configuration configuration)
    {
        super.onConfigurationChanged(configuration);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(0x7f030003);
        shapCut_Option = (LinearLayout)findViewById(0x7f090048);
        layoutBottomBarEdit = (LinearLayout)findViewById(0x7f09003a);
        mFolder = new File(Constant.EFFECTS);
        if(!mFolder.exists())
        {
            mFolder.mkdirs();
        }
        File file = new File(Constant.TEMP);
        if(!file.exists())
        {
            file.mkdirs();
        }
        Bundle bundle1 = getIntent().getExtras();
        if(bundle1 != null)
        {
            galleryImageFilePath = bundle1.getString("inputpath");
            outputPath = bundle1.getString("savepath");
            isFreeFlow = bundle1.getBoolean("isFreeFlow");
            if(galleryImageFilePath == null)
            {
                setResult(0);
                finish();
            }
        }
        if(isFreeFlow)
        {
            layoutBottomBarEdit.setWeightSum(4F);
            shapCut_Option.setVisibility(0);
        } else
        {
            layoutBottomBarEdit.setWeightSum(3F);
            shapCut_Option.setVisibility(8);
        }
        view = (EffectsView)findViewById(0x7f090032);
        layoutCategoryPhotoFilter = (RelativeLayout)findViewById(0x7f090039);
        layoutCategoryMirror = (RelativeLayout)findViewById(0x7f09003d);
        layoutCategoryShapeCut = (RelativeLayout)findViewById(0x7f090040);
        containerCategoryPhotFilter = (LinearLayout)findViewById(0x7f09003c);
        containerCategoryMirror = (LinearLayout)findViewById(0x7f09003f);
        containerCategoryShapeCut = (LinearLayout)findViewById(0x7f090042);
        point = getDisplaySize(((WindowManager)getSystemService("window")).getDefaultDisplay());
        originalBitmap = BitmapUtil.getBitmap(getBaseContext(), galleryImageFilePath, point.x / 2, point.y / 2);
        view.setBitmap(originalBitmap);
        view.invalidate();
        Log.e("Sticker", "onCreate");
        addPhotoFilterEffects();
        pd = new ProgressDialog(this);
        pd.setTitle("Please Wait!");
        pd.setCancelable(false);
        pd.setMessage("Applying effect!");
        mProgressDoneTask = new ProgressDialog(this);
        mProgressDoneTask.setCancelable(false);
        mProgressDoneTask.setTitle("Please Wait!");
        mProgressDoneTask.setMessage(getString(0x7f06006d));
        seekbar = (SeekBar)findViewById(0x7f09002c);
        mViewShapeCut = findViewById(0x7f090037);
        mCircularColorView = (CircularColorView)findViewById(0x7f09002e);
        seekbar.setProgress(Shape.borderWidth);
        seekbar.setOnSeekBarChangeListener(this);
        cropperView = (CropperView)findViewById(0x7f09002f);
        shapeid = 0x7f0200ad;
        ((ImageView)findViewById(0x7f090046)).setImageResource(0x7f02006e);
        ((ImageView)findViewById(0x7f09004a)).setImageResource(0x7f02005d);
        ((ImageView)findViewById(0x7f090049)).setImageResource(0x7f02004f);
        ((ImageView)findViewById(0x7f090047)).setImageResource(0x7f02006b);
    }

    protected void onDestroy()
    {
        if(cropperView != null)
        {
            cropperView.onDestroy();
        }
        super.onDestroy();
    }

    public void onPause()
    {
        super.onPause();
    }

    public void onProgressChanged(SeekBar seekbar1, int i, boolean flag)
    {
        Shape.borderWidth = i;
        cropperView.updateBorderSize(Shape.borderWidth);
    }

    public void onResume()
    {
        super.onResume();
    }

    public void onStartTrackingTouch(SeekBar seekbar1)
    {
    }

    public void onStopTrackingTouch(SeekBar seekbar1)
    {
    }
}
