package com.km.photogridbuilder;

import afzkl.development.colorpickerview.dialog.ColorPickerDialog;
import android.app.*;
import android.content.*;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.os.*;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.dexati.adclient.AdMobManager;
import com.google.android.gms.analytics.Tracker;
import com.km.photogridbuilder.Objects.Image;
import com.km.photogridbuilder.Objects.StickerView;
import com.km.photogridbuilder.Objects.TextObject;
import com.km.photogridbuilder.adapter.CustomAdapter;
import com.km.photogridbuilder.bean.Constant;
import com.km.photogridbuilder.bean.Drawing;
import com.km.photogridbuilder.bean.Stickers;
import com.km.photogridbuilder.cut.CutActivity;
import com.km.photogridbuilder.listener.EffectSelectListener;
import com.km.photogridbuilder.util.BitmapUtil;
import com.km.photogridbuilder.util.FontUtils;
import com.km.photogridbuilder.util.ImageObject;
import com.km.photogridbuilder.util.UtilUIEffectMenu;
import java.io.*;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

// Referenced classes of package com.km.photogridbuilder:
//            PhotoSelectorScreen, EffectsActivity, HelpActivity, ApplicationController

public class StickerActivity extends Activity
    implements android.view.View.OnClickListener, com.km.photogridbuilder.Objects.StickerView.TapListener, com.km.photogridbuilder.Objects.StickerView.ClickListener, EffectSelectListener
{
    class BackgroungTask extends AsyncTask
    {
        protected Integer doInBackground(Void avoid[])
        {
        	if(demo == null)
            {
                return 0;
            }
            isFreeFlow = true;
            Resources resources;
            int i;
            int j;
            int k;
            Bitmap bitmap;
            Image image;
            try
            {
                resources = getResources();
            }
            catch(Exception exception)
            {
                Log.v("ERROR", exception.toString());
                return Integer.valueOf(0);
            }
            i = 0;
            j = demo.size();
            while(i < j)
            {
            	k = demo.size();
                bitmap = null;
                bitmap = BitmapUtil.getBitmap(getBaseContext(), (String)demo.get(i), 300, 300);
                image = new Image(bitmap, resources);
                image.setUrl((String)demo.get(i));
                image.setClipping(false);
                image.setBorder(true);
                view.init(image);
                view.loadImages(getBaseContext(), null);
                i++;
            }
            return Integer.valueOf(1);
        }

        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((Void[])aobj);
        }

        protected void onPostExecute(Integer integer)
        {
            if(pd != null)
            {
                pd.dismiss();
            }
            if(integer.intValue() == 0)
            {
                Toast.makeText(StickerActivity.this, "Unable to create collage", 0).show();
                finish();
                return;
            } else
            {
                view.invalidate();
                return;
            }
        }

        protected void onPostExecute(Object obj)
        {
            onPostExecute((Integer)obj);
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

    public class SaveOutputAsynchTask extends AsyncTask
    {
        Bitmap display;
        File file;
        String filePath;
        boolean isSaved;

        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((Void[])aobj);
        }

        protected Void doInBackground(Void avoid[])
        {
        	try{
	            if(display.isRecycled())
	            {
	                return null;
	            }
	            display = cropTransparentArea(display);
	            file.getParentFile().mkdirs();
	            FileOutputStream fileoutputstream = new FileOutputStream(file);
	            display.compress(android.graphics.Bitmap.CompressFormat.PNG, 90, fileoutputstream);
	            fileoutputstream.flush();
	            fileoutputstream.close();
	            isSaved = true;
        	}catch(Exception exception){
        		isSaved = false;
        	}
        	return null;
        }

        protected void onPostExecute(Object obj)
        {
            onPostExecute((Void)obj);
        }

        protected void onPostExecute(Void void1)
        {
            mProgressDialog.dismiss();
            if(isSaved)
            {
                Toast.makeText(StickerActivity.this, "Collage saved. Can be viewed anytime from \"Your Creations\" and shared with friends.", 1).show();
                if(AdMobManager.isReady(getApplication()))
                {
                    AdMobManager.show();
                }
            } else
            {
                Toast.makeText(StickerActivity.this, "Unable to save Collage. Please Check Disk Space.", 1).show();
            }
            if(display != null)
            {
                display.recycle();
                display = null;
            }
            super.onPostExecute(void1);
        }

        protected void onPreExecute()
        {
            mProgressDialog.show();
        }

        public SaveOutputAsynchTask(Bitmap bitmap)
        {
            super();
            filePath = (new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString()))).append(getString(0x7f060061)).append(StickerActivity.getCurrentImageName()).toString();
            file = new File(filePath);
            isSaved = false;
            display = bitmap;
        }
    }


    private static final int ACTION_REQUEST_EFFECTS = 200;
    private static final String LOG_TAG = "KM";
    protected static final int REQUEST_CUT_PHOTO = 10;
    private static final int REQUEST_IMAGE_GALLERY = 100;
    private static final String TAG = "KM";
    private static File mFolder;
    private Object CurrentObject;
    private String array_paths[];
    private ImageView buttonSelectMoreImages;
    private int choice;
    private LinearLayout containerTextures;
    ArrayList demo;
    private EditText edtText;
    private ImageView imageViewFreehand;
    private ImageView imageViewSticker1;
    private ImageView imageViewhelp;
    private ImageView imageViewtext;
    private ImageView imageViewtexture;
    private boolean isBabyClicked;
    boolean isBottom;
    private boolean isDoodleClicked;
    private boolean isFixedCollageClicked;
    private boolean isFreeFlow;
    private boolean isLoveClicked;
    private boolean isTextColorClicked;
    private View layoutStickers;
    private View layoutTextures;
    View layouttopBarFreeHand;
    private int mBrushSize;
    Context mContext;
    ImageView mImageViewBaby;
    ImageView mImageViewDoodle;
    ImageView mImageViewLove;
    private RelativeLayout mLayoutBottomExpanded;
    private LinearLayout mLayoutTopBar;
    private ArrayList mListFont;
    private String mOutputFilePath;
    private TextView mPreview;
    private ProgressDialog mProgressDialog;
    private LinearLayout mSeekbar_layout;
    HorizontalScrollView mStickerHorizontalView;
    TextView mTextViewBaby;
    TextView mTextViewDoodle;
    TextView mTextViewLove;
    ProgressDialog pd;
    private Point point;
    private int resourceID;
    private float scale;
    private SeekBar seekbarBrushSize;
    private Spinner spinner;
    private Spinner spinnerFontSize;
    private com.km.photogridbuilder.bean.CustomTouch.PointInfo tempPointInfo;
    private int textures_resources[] = {
        0x7f020217, 0x7f020222, 0x7f020224, 0x7f020225, 0x7f020226, 0x7f020227, 0x7f020228, 0x7f020229, 0x7f02022a, 0x7f020218, 
        0x7f020219, 0x7f02021a, 0x7f02021b, 0x7f02021c, 0x7f02021d, 0x7f02021e, 0x7f02021f, 0x7f020220, 0x7f020221, 0x7f020223
    };
    private StickerView view;

    public StickerActivity()
    {
        mListFont = new ArrayList();
        isBottom = true;
        pd = null;
        isFreeFlow = false;
    }

    private void addCategories()
    {
        LayoutInflater layoutinflater = (LayoutInflater)getSystemService("layout_inflater");
        int i = 0;
        do
        {
            if(i >= textures_resources.length)
            {
                return;
            }
            RelativeLayout relativelayout = (RelativeLayout)layoutinflater.inflate(0x7f030028, null);
            relativelayout.setId(i + 1000);
            relativelayout.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(View view1)
                {
                    int j = textures_resources[-1000 + view1.getId()];
                    Bitmap bitmap = doSomeTricks(j);
                    view.setTexture(bitmap);
                    view.invalidate();
                }
            });
            ((ImageView)relativelayout.findViewById(0x7f0900bd)).setBackgroundResource(textures_resources[i]);
            containerTextures.addView(relativelayout);
            i++;
        } while(true);
    }

    private void addSpinnerItemChangedListener()
    {
        spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterview, View view1, int i, long l)
            {
                generatePreview();
            }

            public void onNothingSelected(AdapterView adapterview)
            {
            }
        });
    }

    private void addTextWatcher()
    {
        edtText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable)
            {
            }

            public void beforeTextChanged(CharSequence charsequence, int i, int j, int k)
            {
            }

            public void onTextChanged(CharSequence charsequence, int i, int j, int k)
            {
                generatePreview();
            }
        });
    }

    private void askForEditConfirmation()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose your Edit Option");
        builder.setItems(getResources().getStringArray(0x7f0b0007), new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
                String s;
                String s1;
                File file1;
                if(i == 0)
                {
                    String s2 = ((Image)CurrentObject).getUrl();
                    mOutputFilePath = s2;
                    startFeather(s2);
                    return;
                }
                s = "";
                s1 = ((Image)CurrentObject).getUrl();
                File file = new File(s1);
                Log.d("ravi", file.getParent());
                file1 = new File(file.getParent());
                if(file1.exists()){
	                String as[] = file1.list();
	                if(as.length > 0){
		                int j = 0;
		                while(j < as.length){
		                	if(!as[j].contains("image_original"))
		                    {
		                    	j++;
		                    	continue;
		                    }
		                    s = as[j];
		                    break;
		                }
	                }
                }
                mOutputFilePath = s1;
                startFeather((new StringBuilder(String.valueOf(file1.getAbsolutePath()))).append(File.separator).append(s).toString());
            }
        });
        builder.show();
    }

    private void askForPasteConfirmation()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose your Cut Option");
        builder.setItems(getResources().getStringArray(0x7f0b0006), new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i)
            {
                String s;
                String s1;
                File file1;
                if(i == 0)
                {
                    String s2 = ((Image)CurrentObject).getUrl();
                    mOutputFilePath = s2;
                    startCutPhoto(s2);
                    return;
                }
                s = "";
                s1 = ((Image)CurrentObject).getUrl();
                File file = new File(s1);
                Log.d("ravi", file.getParent());
                file1 = new File(file.getParent());
                if(file1.exists()){
	                String as[] = file1.list();
	                if(as.length > 0){
		                int j = 0;
		                while(j < as.length){
		                	if(!as[j].contains("image_original"))
		                    {
		                    	j++;
		                    	continue;
		                    }
		                    s = as[j];
		                    break;
		                }
	                }
                }
                mOutputFilePath = s1;
                startCutPhoto((new StringBuilder(String.valueOf(file1.getAbsolutePath()))).append(File.separator).append(s).toString());
            }
        });
        builder.show();
    }

    private File checkforOriginalandSave(String s)
    {
        if(!(new File(s)).getParentFile().exists())
        	return null;
        File afile[];
        int i;
        afile = (new File(s)).getParentFile().listFiles();
        i = 0;
        while(i < afile.length){
        	if(afile[i].getName().contains("image_original"))
            {
                return afile[i].getAbsoluteFile();
            }
            i++;
        }
        return null;
    }

    private String colorToHexString(int i)
    {
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(i & -1);
        return String.format("#%06X", aobj);
    }

    private void copyFile(File file, File file1)
    {
        if(file.exists()){
	        FileChannel filechannel;
	        FileChannel filechannel1;
	        try
	        {
	            filechannel = (new FileInputStream(file)).getChannel();
	            filechannel1 = (new FileOutputStream(file1)).getChannel();
	            if(filechannel1 != null && filechannel != null)
	            	filechannel1.transferFrom(filechannel, 0L, filechannel.size());
	            if(filechannel != null)
	            	filechannel.close();
	            if(filechannel1 != null)
	            	filechannel1.close();
	        }
	        catch(Exception exception){}
        }
    }

    private Bitmap cropTransparentArea(Bitmap bitmap)
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

    private Bitmap getBitmap(int i, boolean flag)
    {
        android.graphics.BitmapFactory.Options options;
        try
        {
            options = new android.graphics.BitmapFactory.Options();
            options.inDither = true;
            options.inPreferredConfig = android.graphics.Bitmap.Config.RGB_565;
        }
        catch(Exception exception)
        {
            Log.v("KM", "Error Getting Bitmap ", exception);
            return null;
        }
        if(!flag)
        	options.inScaled = false;
        else
        	options.inScaled = true;
        return BitmapFactory.decodeResource(getResources(), i, options);
    }

    public static String getCurrentImageName()
    {
        return (new StringBuilder(String.valueOf((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())))).append(".png").toString();
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

    private Bitmap getFinalBitmap()
    {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static int getRandom(int ai[])
    {
        return ai[(new Random()).nextInt(ai.length)];
    }

    private void lenghtyAddTask(com.km.photogridbuilder.bean.CustomTouch.PointInfo pointinfo)
    {
        float af[][] = Constant.getArrayForLayout(resourceID);
        int i = 0;
        do
        {
            if(i >= af.length)
            {
                return;
            }
            float f = af[i][0];
            float f1 = af[i][1];
            float f2 = af[i][2];
            float f3 = af[i][3];
            RectF rectf = new RectF(f - f2 / 2.0F, f1 - f3 / 2.0F, f + f2 / 2.0F, f1 + f3 / 2.0F);
            if(mapPoints(point, rectf).contains(pointinfo.getX(), pointinfo.getY()))
            {
                view.addButton(i);
                return;
            }
            i++;
        } while(true);
    }

    private void lenghtyTask(String s)
    {
        float af[][] = Constant.getArrayForLayout(resourceID);
        float f = af[-1 + choice][0];
        float f1 = af[-1 + choice][1];
        float f2 = af[-1 + choice][2];
        float f3 = af[-1 + choice][3];
        RectF rectf = new RectF(f - f2 / 2.0F, f1 - f3 / 2.0F, f + f2 / 2.0F, f1 + f3 / 2.0F);
        Bitmap bitmap = BitmapUtil.getBitmap(this, s, point.x / 2, point.y / 2);
        RectF rectf1 = mapPoints(point, rectf);
        Image image = new Image(bitmap, getResources());
        image.setOriginalRect(rectf);
        image.setUrl(s);
        image.setBorder(false);
        view.init(image);
        view.loadImages(getBaseContext(), rectf1);
        view.removeButton(-1 + choice);
    }

    private void lenghtyTaskAfteEffectApplied(String s, Object obj)
    {
    	try{
	        float af[][] = Constant.getArrayForLayout(resourceID);
	        int i = 0;
	        while(i < af.length)
	        {
	            float f = af[i][0];
	            float f1 = af[i][1];
	            float f2 = af[i][2];
	            float f3 = af[i][3];
	            RectF rectf = new RectF(f - f2 / 2.0F, f1 - f3 / 2.0F, f + f2 / 2.0F, f1 + f3 / 2.0F);
	            if(mapPoints(point, rectf).contains(tempPointInfo.getX(), tempPointInfo.getY()))
	            {
	                Bitmap bitmap = BitmapUtil.getBitmap(this, s, point.x / 2, point.y / 2);
	                getResources();
	                ((Image)obj).setBitmap(bitmap);
	                return;
	            }
	            i++;
	        }
    	}catch(Exception e){}
    }

    private void saveOutput(Bitmap bitmap)
        throws FileNotFoundException
    {
        (new SaveOutputAsynchTask(bitmap)).execute(new Void[0]);
    }

    private void showPhotoSelectDialog()
    {
        Intent intent = new Intent(this, com.km.photogridbuilder.PhotoSelectorScreen.class);
        intent.putExtra("isFixedCollageClicked", isFixedCollageClicked);
        startActivityForResult(intent, 100);
    }

    private void startCutPhoto(String s)
    {
        Intent intent = new Intent();
        intent.putExtra("inputpath", s);
        intent.putExtra("savepath", mOutputFilePath);
        intent.setClass(this, com.km.photogridbuilder.cut.CutActivity.class);
        startActivityForResult(intent, 10);
    }

    private void startFeather(String s)
    {
        Intent intent = new Intent();
        intent.putExtra("isFreeFlow", isFreeFlow);
        intent.putExtra("inputpath", s);
        intent.putExtra("savepath", mOutputFilePath);
        intent.setClass(this, com.km.photogridbuilder.EffectsActivity.class);
        startActivityForResult(intent, 200);
    }

    void deleteRecursive(File file)
    {
        if(file.isDirectory()){
	        File afile[];
	        int i;
	        int j;
	        afile = file.listFiles();
	        i = afile.length;
	        j = 0;
	        while(j < i){
	        	deleteRecursive(afile[j]);
	            j++;
	        }
        }
        file.delete();
        return;
    }

    protected Bitmap doSomeTricks(int i)
    {
        int ai[];
        int ai1[];
        int i1;
        int j;
        int k;
        Bitmap bitmap;
        Rect rect;
        BitmapDrawable bitmapdrawable;
        Bitmap bitmap1;
        Canvas canvas;
        int l;
        if(!isFreeFlow)
        {
            bitmap = view.getBitmap();
            j = bitmap.getWidth();
            k = bitmap.getHeight();
        } else
        {
            j = point.x;
            k = point.y;
            bitmap = null;
        }
        rect = new Rect(0, 0, j, k);
        bitmapdrawable = (BitmapDrawable)getResources().getDrawable(i);
        bitmapdrawable.setTileModeXY(android.graphics.Shader.TileMode.REPEAT, android.graphics.Shader.TileMode.REPEAT);
        bitmap1 = Bitmap.createBitmap(rect.width(), rect.height(), android.graphics.Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap1);
        bitmapdrawable.setBounds(rect);
        bitmapdrawable.draw(canvas);
        if(isFreeFlow)
        	return bitmap1;
        l = rect.width() * rect.height();
        ai = new int[l];
        ai1 = new int[l];
        bitmap.getPixels(ai, 0, rect.width(), 0, 0, rect.width(), rect.height());
        bitmap1.getPixels(ai1, 0, rect.width(), 0, 0, rect.width(), rect.height());
        i1 = 0;
        while(i1 < l){
        	if(ai[i1] == 0)
            {
                ai1[i1] = 0;
            }
            i1++;
        }
        bitmap1.setPixels(ai1, 0, rect.width(), 0, 0, rect.width(), rect.height());
        return bitmap1;
    }

    protected void generatePreview()
    {
        String s;
        int i;
        String s1;
        int j;
        s = edtText.getText().toString();
        i = PreferenceManager.getDefaultSharedPreferences(this).getInt("color_2", -1);
        s1 = spinnerFontSize.getSelectedItem().toString();
        j = 20;
        try{
	        int k = Integer.parseInt(s1);
	        j = k;
        }catch(Exception e){}
        //(int)((float)j * scale);
        String s2 = "";
        if(array_paths != null)
        {
            s2 = array_paths[spinner.getSelectedItemPosition()];
        }
        FontUtils.setTypeface(getBaseContext(), mPreview, s2);
        mPreview.setText(s);
        mPreview.setTextColor(i);
    }

    public Bitmap getStickers()
    {
        ArrayList arraylist;
        ArrayList arraylist1;
        int i;
        Canvas canvas;
        float f2;
        float f3;
        Iterator iterator;
        Iterator iterator1;
        Iterator iterator2;
        Bitmap bitmap;
        int j;
        float f;
        float f1;
        int k;
        int l;
        int i1;
        int j1;
        Bitmap bitmap1;
        Rect rect;
        Bitmap bitmap3;
        if(!isFreeFlow)
        {
            bitmap = view.getBitmap().copy(android.graphics.Bitmap.Config.ARGB_8888, true);
        } else
        if(view.getTexture() != null)
        {
            bitmap = view.getTexture().copy(android.graphics.Bitmap.Config.ARGB_8888, true);
        } else
        {
            bitmap = Bitmap.createBitmap(point.x, point.y, android.graphics.Bitmap.Config.ARGB_8888);
        }
        arraylist = view.getImages();
        arraylist1 = new ArrayList();
        i = 0;
        j = arraylist.size();
        while(i < j){
        	Object obj2 = arraylist.get(i);
            if(!(obj2 instanceof Image))
            {
                if(obj2 instanceof TextObject)
                {
                    arraylist1.add(obj2);
                } else
                if(!(obj2 instanceof ImageObject));
            }
            i++;
        }
        canvas = new Canvas(bitmap);
        f = point.x;
        f1 = point.y;
        k = bitmap.getHeight();
        l = bitmap.getWidth();
        i1 = (int)(f1 - 2.0F * view.gapRect.top);
        j1 = (int)(f - 2.0F * view.gapRect.left);
        f2 = 1.0F * ((1.0F * (float)k) / (float)i1);
        f3 = 1.0F * ((1.0F * (float)l) / (float)j1);
        bitmap1 = view.getFrame();
        rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        if(bitmap1 != null)
        {
            canvas.drawBitmap(bitmap1, null, rect, null);
        }
        if(isFreeFlow && view.getTexture() == null)
        {
            canvas.drawBitmap(getBitmap(0x7f020010, false), null, rect, null);
        }
        iterator = arraylist.iterator();
        while(iterator.hasNext()){
        	Object obj = iterator.next();
            Float float1 = Float.valueOf(0.0F);
            boolean flag = obj instanceof Image;
            int k1 = 0;
            boolean flag1 = false;
            boolean flag2 = false;
            float f4 = 0.0F;
            float f5 = 0.0F;
            float f6 = 0.0F;
            float f7 = 0.0F;
            RectF rectf = null;
            Paint paint = null;
            Bitmap bitmap2 = null;
            if(flag)
            {
                Image image = (Image)obj;
                paint = image.getPaint();
                bitmap2 = image.getBitmap();
                flag2 = image.isClipping();
                rectf = image.getOriginalRect();
                float1 = Float.valueOf(image.getAngle());
                f6 = image.getMinX();
                f4 = image.getMaxX();
                f5 = image.getMaxY();
                f7 = image.getMinY();
                flag1 = image.isBorder();
                k1 = image.borderSize;
            }
            if(!(obj instanceof TextObject))
            {
                if(obj instanceof ImageObject)
                {
                    ImageObject imageobject = (ImageObject)obj;
                    paint = imageobject.getPaint();
                    bitmap2 = imageobject.getBitmap();
                    float1 = Float.valueOf(imageobject.getAngle());
                    f6 = imageobject.getMinX();
                    f4 = imageobject.getMaxX();
                    f5 = imageobject.getMaxY();
                    f7 = imageobject.getMinY();
                    flag1 = imageobject.isBorder();
                }
                float f8 = f6 - view.gapRect.left;
                float f9 = f4 - view.gapRect.right;
                float f10 = f7 - view.gapRect.top;
                float f11 = f5 - view.gapRect.bottom;
                float f12 = f8 * f3;
                float f13 = f9 * f3;
                float f14 = f10 * f2;
                float f15 = f11 * f2;
                RectF rectf1 = new RectF(f12, f14, f13, f15);
                canvas.save();
                float f16 = (f13 + f12) / 2.0F;
                float f17 = (f15 + f14) / 2.0F;
                if(flag2)
                {
                    canvas.clipRect(rectf);
                }
                canvas.translate(f16, f17);
                canvas.rotate((180F * float1.floatValue()) / 3.141593F);
                canvas.translate(-f16, -f17);
                if(flag1)
                {
                    paint.setColor(-1);
                    canvas.drawRect(new Rect((int)rectf1.left - k1, (int)rectf1.top - k1, k1 + (int)rectf1.right, k1 + (int)rectf1.bottom), paint);
                }
                if(bitmap2 != null)
                {
                    canvas.drawBitmap(bitmap2, null, rectf1, paint);
                } else
                {
                    canvas.drawText("", f12, f14, paint);
                }
                canvas.restore();
            }
        }
        if(!isFreeFlow)
        {
            canvas.drawBitmap(view.getBitmap(), 0.0F, 0.0F, null);
            bitmap3 = view.getTexture();
            if(bitmap3 != null)
            {
                canvas.drawBitmap(bitmap3, null, rect, null);
            }
        }
        iterator1 = arraylist1.iterator();
        while(iterator1.hasNext()){
        	Object obj1 = iterator1.next();
            Float float2 = Float.valueOf(0.0F);
            String s = "";
            boolean flag3 = obj1 instanceof TextObject;
            TextObject textobject = null;
            float f18 = 0.0F;
            float f19 = 0.0F;
            float f20 = 0.0F;
            float f21 = 0.0F;
            if(flag3)
            {
                textobject = (TextObject)obj1;
                float2 = Float.valueOf(textobject.getAngle());
                f20 = textobject.getMinX();
                f18 = textobject.getMaxX();
                f19 = textobject.getMaxY();
                f21 = textobject.getMinY();
                s = textobject.text;
            }
            float f22 = f20 - view.gapRect.left;
            float f23 = f18 - view.gapRect.right;
            float f24 = f21 - view.gapRect.top;
            float f25 = f19 - view.gapRect.bottom;
            float f26 = f22 * f3;
            float f27 = f23 * f3;
            float f28 = f24 * f2;
            float f29 = f25 * f2;
            canvas.save();
            float f30 = (f27 + f26) / 2.0F;
            float f31 = (f29 + f28) / 2.0F;
            canvas.translate(f30, f31);
            canvas.rotate((180F * float2.floatValue()) / 3.141593F);
            canvas.translate(-f30, -f31);
            Paint paint1 = new Paint(1);
            paint1.setColor(textobject.color);
            paint1.setTypeface(FontUtils.getTypeface(getBaseContext(), textobject.getFont()));
            Log.v("KM", (new StringBuilder("Font :")).append(textobject.getPaint().getTextSize()).toString());
            paint1.setTextSize(textobject.getPaint().getTextSize() / 3F);
            paint1.setShadowLayer(2.0F, 0.0F, 1.0F, 0xff000000);
            Log.v("KM", (new StringBuilder("Fonter :")).append(paint1.getTextSize()).toString());
            canvas.drawText(s, f26, f29, paint1);
            canvas.restore();
        }
        iterator2 = view.mDrawingList.iterator();
        while(true){
	        if(!iterator2.hasNext())
	        {
	            return bitmap;
	        }
	        Drawing drawing = (Drawing)iterator2.next();
	        canvas.drawPath(drawing.getPath(), drawing.getPaint());
        }
    }

    public RectF mapPoints(Point point1, RectF rectf)
    {
        RectF rectf1 = new RectF();
        Bitmap bitmap = view.getBitmap();
        float f = point1.x;
        float f1 = point1.y;
        int i = bitmap.getHeight();
        int j = bitmap.getWidth();
        int k = (int)(f1 - 2.0F * view.gapRect.top);
        int l = (int)(f - 2.0F * view.gapRect.left);
        float f2 = 1.0F * ((1.0F * (float)k) / (float)i);
        float f3 = 1.0F * ((1.0F * (float)l) / (float)j);
        float f4 = rectf.left;
        float f5 = rectf.right;
        float f6 = rectf.top;
        float f7 = rectf.bottom;
        float f8 = f4 * f3 + view.gapRect.left;
        float f9 = f5 * f3 + view.gapRect.right;
        rectf1.set(f8, f6 * f2 + view.gapRect.top, f9, f7 * f2 + view.gapRect.bottom);
        return rectf1;
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        if(j != -1){
        	setResult(0);
            return;
        }
        switch(i){
        default:
        	return;
        case 0:
        	demo = (ArrayList)intent.getSerializableExtra("list");
            (new BackgroungTask()).execute(new Void[0]);
            return;
        case 10:
        	int i1;
            int j1;
            Bitmap bitmap1;
            Iterator iterator1;
            if(isFreeFlow)
            {
                i1 = 300;
                j1 = 300;
            } else
            {
                try
                {
                    i1 = point.x / 2;
                    j1 = point.y / 2;
                }
                // Misplaced declaration of an exception variable
                catch(Exception exception)
                {
                    exception.printStackTrace();
                    return;
                }
            }
            bitmap1 = BitmapUtil.getBitmap(this, mOutputFilePath, i1, j1);
            iterator1 = view.getImages().iterator();
            while(true){
    	        if(!iterator1.hasNext())
    	        {
    	            view.invalidate();
    	            return;
    	        }
    	        Object obj1 = iterator1.next();
    	        if(obj1.equals(CurrentObject))
    	        {
    	            ((Image)obj1).setBitmap(bitmap1);
    	            ((Image)obj1).setClipping(false);
    	            ((Image)obj1).setBorder(false);
    	            ((Image)obj1).setFirstLoad(false);
    	            ((Image)obj1).load(getResources());
    	            ((Image)obj1).setUrl(mOutputFilePath);
    	        }
            }
        case 100:
        	if(!(j != -1 || intent == null)){
    	        if(!isFixedCollageClicked){
    	        	demo = (ArrayList)intent.getSerializableExtra("list");
    	            if(demo == null){
    	            	String s;
    	                demo = new ArrayList();
    	                s = intent.getStringExtra("path");
    	                if(s == null){
    	                	demo = intent.getStringArrayListExtra("image_list");
    	                    if(demo != null)
    	                    {
    	                        (new BackgroungTask()).execute(new Void[0]);
    	                    }
    	                }else{
    	        	        demo.add(s);
    	        	        if(demo != null)
    	        	        {
    	        	            (new BackgroungTask()).execute(new Void[0]);
    	        	        }
    	                }
    	            }else
    	            	(new BackgroungTask()).execute(new Void[0]);
    	        }else{
    		        String s1 = intent.getStringExtra("path");
    		        if(s1 != null){
    		        	if(s1 != null){
    		            	(new AsyncTask() {
    		
    		                    ProgressDialog pdDialog;
    		
    		                    protected Bitmap doInBackground(String as[])
    		                    {
    		                        try
    		                        {
    		                            lenghtyTask(as[0]);
    		                        }
    		                        catch(Throwable throwable)
    		                        {
    		                            Log.v("KM", "oops something wrong");
    		                        }
    		                        return null;
    		                    }
    		
    		                    protected Object doInBackground(Object aobj[])
    		                    {
    		                        return doInBackground((String[])aobj);
    		                    }
    		
    		                    protected void onPostExecute(Bitmap bitmap2)
    		                    {
    		                        pdDialog.dismiss();
    		                        view.invalidate();
    		                    }
    		
    		                    protected void onPostExecute(Object obj2)
    		                    {
    		                        onPostExecute((Bitmap)obj2);
    		                    }
    		
    		                    protected void onPreExecute()
    		                    {
    		                        pdDialog = new ProgressDialog(StickerActivity.this);
    		                        pdDialog.setMessage("Loading picture in frame");
    		                        pdDialog.setCancelable(false);
    		                        pdDialog.show();
    		                    }
    		                }).execute(new String[] {
    		                    s1
    		                });
    		            }
    		        }else{
    			        demo = intent.getStringArrayListExtra("image_list");
    			        if(demo != null)
    			        {
    			            s1 = (String)demo.get(0);
    			        }
    		        }
    	        }
            }
        	break;
        case 200:
        	break;
        }
        boolean flag;
        flag = true;
        if(intent == null)
        {
        	//break MISSING_BLOCK_LABEL_374;
            return;
        }
        Bundle bundle = intent.getExtras();
        if(bundle == null)
        {
            //break MISSING_BLOCK_LABEL_374;
        	return;
        }
        flag = bundle.getBoolean("bitmapchanged");
        if(!flag)
        	Log.w("KM", "User did not modify the image, but just clicked on 'Done' button");
        int k;
        int l;
        if(!isFreeFlow){
        	k = point.x / 2;
            l = point.y / 2;
        }else{
	        k = 300;
	        l = 300;
        }
        if(!isFreeFlow)
        {
            lenghtyTaskAfteEffectApplied(mOutputFilePath, CurrentObject);
            view.invalidate();
            mOutputFilePath = null;
            tempPointInfo = null;
            return;
        }
        Bitmap bitmap = BitmapUtil.getBitmap(this, mOutputFilePath, k, l);
        Iterator iterator = view.getImages().iterator();
        while(iterator.hasNext()) 
        {
            Object obj = iterator.next();
            if(obj.equals(CurrentObject))
            {
                ((Image)obj).setBitmap(bitmap);
                ((Image)obj).setClipping(false);
                ((Image)obj).setBorder(false);
                ((Image)obj).setFirstLoad(false);
                ((Image)obj).load(getResources());
                ((Image)obj).setUrl(mOutputFilePath);
            }
        }
    }

    public void onBackPressed()
    {
        view.setFreHandDrawMode(false);
        imageViewtext.setImageResource(0x7f02003c);
        imageViewFreehand.setImageResource(0x7f020061);
        imageViewtexture.setImageResource(0x7f02007f);
        imageViewSticker1.setImageResource(0x7f02007d);
        imageViewhelp.setImageResource(0x7f020065);
        view.setFreHandDrawMode(false);
        if(layoutStickers.isShown())
        {
            mStickerHorizontalView.setVisibility(8);
            layoutStickers.setVisibility(8);
            return;
        }
        if(mLayoutBottomExpanded.isShown())
        {
            mLayoutBottomExpanded.setVisibility(8);
            return;
        }
        if(layouttopBarFreeHand.isShown())
        {
            layouttopBarFreeHand.setVisibility(8);
            return;
        }
        if(layoutTextures.isShown())
        {
            layoutTextures.setVisibility(8);
            return;
        } else
        {
            deleteRecursive(new File(Constant.TEMP));
            finish();
            return;
        }
    }

    public void onClick(View view1)
    {
        switch(view1.getId()){
        default:
        	return;
        case 2131296365:
        	layouttopBarFreeHand.setVisibility(8);
            layoutStickers.setVisibility(8);
            layoutTextures.setVisibility(8);
            view.setFreHandDrawMode(false);
            if(mLayoutBottomExpanded.isShown())
            {
                mLayoutBottomExpanded.setVisibility(8);
                mLayoutBottomExpanded.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040004));
                imageViewtext.setImageResource(0x7f02003c);
                imageViewFreehand.setImageResource(0x7f020061);
                imageViewtexture.setImageResource(0x7f02007f);
                imageViewSticker1.setImageResource(0x7f02007d);
                imageViewhelp.setImageResource(0x7f020065);
                return;
            } else
            {
                mLayoutBottomExpanded.setVisibility(0);
                mLayoutBottomExpanded.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040000));
                imageViewtext.setImageResource(0x7f02003d);
                imageViewFreehand.setImageResource(0x7f020061);
                imageViewtexture.setImageResource(0x7f02007f);
                imageViewSticker1.setImageResource(0x7f02007d);
                imageViewhelp.setImageResource(0x7f020065);
                return;
            }
        case 2131296366:
        	mLayoutBottomExpanded.setVisibility(8);
            layoutStickers.setVisibility(8);
            layoutTextures.setVisibility(8);
            if(layouttopBarFreeHand.isShown())
            {
                layouttopBarFreeHand.setVisibility(8);
                layouttopBarFreeHand.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040004));
                imageViewtext.setImageResource(0x7f02003c);
                imageViewFreehand.setImageResource(0x7f020061);
                imageViewtexture.setImageResource(0x7f02007f);
                imageViewSticker1.setImageResource(0x7f02007d);
                imageViewhelp.setImageResource(0x7f020065);
            } else
            {
                layouttopBarFreeHand.setVisibility(0);
                layouttopBarFreeHand.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040000));
                imageViewtext.setImageResource(0x7f02003c);
                imageViewFreehand.setImageResource(0x7f020062);
                imageViewtexture.setImageResource(0x7f02007f);
                imageViewSticker1.setImageResource(0x7f02007d);
                imageViewhelp.setImageResource(0x7f020065);
            }
            view.setFreHandDrawMode(true);
            return;
        case 2131296367:
        	view.setFreHandDrawMode(false);
            layoutStickers.clearAnimation();
            layoutStickers.setVisibility(8);
            layouttopBarFreeHand.setVisibility(8);
            mLayoutBottomExpanded.setVisibility(8);
            layoutStickers.setVisibility(8);
            if(layoutTextures.isShown())
            {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040004);
                animation1.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
                    public void onAnimationEnd(Animation animation2)
                    {
                        layoutTextures.setVisibility(8);
                    }

                    public void onAnimationRepeat(Animation animation2)
                    {
                    }

                    public void onAnimationStart(Animation animation2)
                    {
                    }
                });
                layoutTextures.startAnimation(animation1);
                imageViewtext.setImageResource(0x7f02003c);
                imageViewFreehand.setImageResource(0x7f020061);
                imageViewtexture.setImageResource(0x7f02007f);
                imageViewSticker1.setImageResource(0x7f02007d);
                imageViewhelp.setImageResource(0x7f020065);
                return;
            } else
            {
                layoutTextures.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040000));
                layoutTextures.setVisibility(0);
                imageViewtext.setImageResource(0x7f02003c);
                imageViewFreehand.setImageResource(0x7f020061);
                imageViewtexture.setImageResource(0x7f020080);
                imageViewSticker1.setImageResource(0x7f02007d);
                imageViewhelp.setImageResource(0x7f020065);
                return;
            }
        case 2131296368:
        	view.setFreHandDrawMode(false);
            layoutTextures.clearAnimation();
            layoutTextures.setVisibility(8);
            layouttopBarFreeHand.setVisibility(8);
            mLayoutBottomExpanded.setVisibility(8);
            if(layoutStickers.isShown())
            {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040004);
                animation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
                    public void onAnimationEnd(Animation animation2)
                    {
                        layoutStickers.setVisibility(8);
                    }

                    public void onAnimationRepeat(Animation animation2)
                    {
                    }

                    public void onAnimationStart(Animation animation2)
                    {
                    }
                });
                layoutStickers.startAnimation(animation);
                imageViewtext.setImageResource(0x7f02003c);
                imageViewFreehand.setImageResource(0x7f020061);
                imageViewtexture.setImageResource(0x7f02007f);
                imageViewSticker1.setImageResource(0x7f02007d);
                imageViewhelp.setImageResource(0x7f020065);
                return;
            } else
            {
                imageViewtext.setImageResource(0x7f02003c);
                imageViewFreehand.setImageResource(0x7f020061);
                imageViewtexture.setImageResource(0x7f02007f);
                imageViewSticker1.setImageResource(0x7f02007e);
                imageViewhelp.setImageResource(0x7f020065);
                mImageViewBaby.setImageResource(0x7f020041);
                mImageViewLove.setImageResource(0x7f02006a);
                mImageViewDoodle.setImageResource(0x7f020055);
                mTextViewLove.setTextColor(getResources().getColor(0x7f07002f));
                mTextViewBaby.setTextColor(getResources().getColor(0x7f070030));
                mTextViewDoodle.setTextColor(getResources().getColor(0x7f070030));
                isLoveClicked = true;
                isBabyClicked = false;
                isDoodleClicked = false;
                mStickerHorizontalView.setVisibility(0);
                UtilUIEffectMenu.loadEffects(this, (LinearLayout)findViewById(0x7f090133), this, Stickers.STICKER_LOVE_ARRAY);
                layoutStickers.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040000));
                layoutStickers.setVisibility(0);
                return;
            }
        case 2131296369:
        	mLayoutBottomExpanded.setVisibility(8);
            layouttopBarFreeHand.setVisibility(8);
            layoutTextures.setVisibility(8);
            layoutStickers.setVisibility(8);
            view.setFreHandDrawMode(false);
            imageViewtext.setImageResource(0x7f02003c);
            imageViewFreehand.setImageResource(0x7f020061);
            imageViewtexture.setImageResource(0x7f02007f);
            imageViewSticker1.setImageResource(0x7f02007d);
            imageViewhelp.setImageResource(0x7f020066);
            Intent intent = new Intent(this, com.km.photogridbuilder.HelpActivity.class);
            startActivity(intent);
            return;
        case 2131296370:
        	layouttopBarFreeHand.setVisibility(8);
            layoutStickers.setVisibility(8);
            layoutTextures.setVisibility(8);
            mLayoutBottomExpanded.setVisibility(8);
            imageViewtext.setImageResource(0x7f02003c);
            imageViewFreehand.setImageResource(0x7f020061);
            imageViewtexture.setImageResource(0x7f02007f);
            imageViewSticker1.setImageResource(0x7f02007d);
            imageViewhelp.setImageResource(0x7f020065);
            if(view.getImages().size() > 0)
            {
                view.setFreHandDrawMode(false);
                view.isSaveClicked = true;
                Bitmap bitmap = getFinalBitmap();
                view.isSaveClicked = false;
                try
                {
                    saveOutput(bitmap);
                    return;
                }
                catch(FileNotFoundException filenotfoundexception)
                {
                    Toast.makeText(this, "Unable to save Collage. Please Check Disk Space.", 1).show();
                }
                return;
            } else
            {
                Toast.makeText(this, getString(0x7f06006c), 1).show();
                return;
            }
        case 2131296372:
        	if(!isFixedCollageClicked)
            {
                mLayoutBottomExpanded.setVisibility(8);
                layouttopBarFreeHand.setVisibility(8);
                layoutTextures.setVisibility(8);
                layoutStickers.setVisibility(8);
                view.setFreHandDrawMode(false);
                imageViewtext.setImageResource(0x7f02003c);
                imageViewFreehand.setImageResource(0x7f020061);
                imageViewtexture.setImageResource(0x7f02007f);
                imageViewSticker1.setImageResource(0x7f02007d);
                imageViewhelp.setImageResource(0x7f020065);
                showPhotoSelectDialog();
            }
        	return;
        case 2131296427:
        	view.setFreHandDrawMode(true);
            if(mSeekbar_layout.isShown())
            {
                mSeekbar_layout.setVisibility(4);
                return;
            } else
            {
                mSeekbar_layout.setVisibility(0);
                return;
            }
        case 2131296430:
        	view.setFreHandDrawMode(true);
            mSeekbar_layout.setVisibility(4);
            isTextColorClicked = false;
            onClickColorPickerDialog();
            return;
        case 2131296433:
        	view.setFreHandDrawMode(true);
            view.onClickUndo();
            mSeekbar_layout.setVisibility(4);
            return;
        case 2131296436:
        	view.setFreHandDrawMode(true);
            view.onClickRedo();
            mSeekbar_layout.setVisibility(4);
            return;
        case 2131296439:
        	view.setFreHandDrawMode(false);
            mSeekbar_layout.setVisibility(4);
            if(layouttopBarFreeHand.isShown())
            {
                mLayoutTopBar.setVisibility(0);
                layouttopBarFreeHand.setVisibility(8);
            }
            imageViewtext.setImageResource(0x7f02003c);
            imageViewFreehand.setImageResource(0x7f020061);
            imageViewtexture.setImageResource(0x7f02007f);
            imageViewSticker1.setImageResource(0x7f02007d);
            imageViewhelp.setImageResource(0x7f020065);
            return;
        case 2131296462:
        	view.setFreHandDrawMode(false);
            isTextColorClicked = true;
            onClickColorPickerDialog();
            return;
        case 2131296464:
        	int i1;
            int j1;
            String s;
            Resources resources;
            int k1;
            String s1;
            String s2;
            int l1;
            view.setFreHandDrawMode(false);
            Random random = new Random();
            int i = (int)(0.25D * (double)point.x);
            int j = (int)(0.25D * (double)point.y);
            int k = point.x;
            int l = point.y;
            i1 = i + random.nextInt(-100 + (k - i));
            j1 = j + random.nextInt(-100 + (l - j));
            s = edtText.getText().toString();
            resources = getResources();
            k1 = PreferenceManager.getDefaultSharedPreferences(this).getInt("color_2", -1);
            s1 = colorToHexString(k1);
            s2 = spinnerFontSize.getSelectedItem().toString();
            l1 = 20;
            try{
    	        int j2 = Integer.parseInt(s2);
    	        l1 = j2;
            }catch(Exception e1){}
            int i2 = (int)((float)l1 * scale);
            String s3 = "";
            if(array_paths != null)
            {
                s3 = array_paths[spinner.getSelectedItemPosition()];
            }
            TextObject textobject = new TextObject(s, s3, i2, k1, s1, resources, getBaseContext());
            textobject.setSticker(true);
            textobject.setText(true);
            view.init(textobject);
            RectF rectf = new RectF(i1, j1 - 50, i1 + 100, j1);
            view.loadImages(this, rectf);
            view.invalidate();
            mLayoutBottomExpanded.setVisibility(4);
            mLayoutBottomExpanded.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), 0x7f040004));
            imageViewtext.setImageResource(0x7f02003c);
            imageViewFreehand.setImageResource(0x7f020061);
            imageViewtexture.setImageResource(0x7f02007f);
            imageViewSticker1.setImageResource(0x7f02007d);
            imageViewhelp.setImageResource(0x7f020065);
            return;
        }
    }

    public void onClickBaby(View view1)
    {
        mImageViewBaby.setImageResource(0x7f020042);
        mImageViewLove.setImageResource(0x7f020069);
        mImageViewDoodle.setImageResource(0x7f020055);
        mTextViewLove.setTextColor(getResources().getColor(0x7f070030));
        mTextViewDoodle.setTextColor(getResources().getColor(0x7f070030));
        mTextViewBaby.setTextColor(getResources().getColor(0x7f07002f));
        mStickerHorizontalView.startAnimation(AnimationUtils.loadAnimation(this, 0x7f040002));
        isBabyClicked = true;
        isLoveClicked = false;
        isDoodleClicked = false;
        mStickerHorizontalView.setVisibility(0);
        UtilUIEffectMenu.loadEffects(this, (LinearLayout)findViewById(0x7f090133), this, Stickers.STICKER_BABY_ARRAY);
    }

    public void onClickColorPickerDialog()
    {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int i;
        final ColorPickerDialog colorDialog;
        if(isTextColorClicked)
        {
            i = prefs.getInt("color_2", -1);
        } else
        {
            i = prefs.getInt("color_1", -1);
        }
        colorDialog = new ColorPickerDialog(this, i);
        colorDialog.setTitle("Pick a Color!");
        colorDialog.setButton(-1, getString(0x104000a), new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int j)
            {
                if(isTextColorClicked)
                {
                    android.content.SharedPreferences.Editor editor1 = prefs.edit();
                    editor1.putInt("color_2", colorDialog.getColor());
                    editor1.commit();
                    generatePreview();
                    return;
                } else
                {
                    android.content.SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("color_1", colorDialog.getColor());
                    editor.commit();
                    view.setDrawColor(colorDialog.getColor());
                    return;
                }
            }
        });
        colorDialog.setButton(-2, getString(0x1040000), new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int j)
            {
            }
        });
        colorDialog.show();
    }

    public void onClickDoodles(View view1)
    {
        mImageViewBaby.setImageResource(0x7f020041);
        mImageViewLove.setImageResource(0x7f020069);
        mImageViewDoodle.setImageResource(0x7f020056);
        mTextViewLove.setTextColor(getResources().getColor(0x7f070030));
        mTextViewBaby.setTextColor(getResources().getColor(0x7f070030));
        mTextViewDoodle.setTextColor(getResources().getColor(0x7f07002f));
        mStickerHorizontalView.startAnimation(AnimationUtils.loadAnimation(this, 0x7f040002));
        isDoodleClicked = true;
        isLoveClicked = false;
        isBabyClicked = false;
        mStickerHorizontalView.setVisibility(0);
        UtilUIEffectMenu.loadEffects(this, (LinearLayout)findViewById(0x7f090133), this, Stickers.STICKER_Doddles_ARRAY);
    }

    public void onClickListener(int i, int j)
    {
        choice = j;
        mLayoutBottomExpanded.setVisibility(8);
        layouttopBarFreeHand.setVisibility(8);
        layoutTextures.setVisibility(8);
        layoutStickers.setVisibility(8);
        view.setFreHandDrawMode(false);
        imageViewtext.setImageResource(0x7f02003c);
        imageViewFreehand.setImageResource(0x7f020061);
        imageViewtexture.setImageResource(0x7f02007f);
        imageViewSticker1.setImageResource(0x7f02007d);
        imageViewhelp.setImageResource(0x7f020065);
        showPhotoSelectDialog();
    }

    public void onClickLove(View view1)
    {
        mImageViewBaby.setImageResource(0x7f020041);
        mImageViewLove.setImageResource(0x7f02006a);
        mImageViewDoodle.setImageResource(0x7f020055);
        mTextViewLove.setTextColor(getResources().getColor(0x7f07002f));
        mTextViewBaby.setTextColor(getResources().getColor(0x7f070030));
        mTextViewDoodle.setTextColor(getResources().getColor(0x7f070030));
        mStickerHorizontalView.startAnimation(AnimationUtils.loadAnimation(this, 0x7f040002));
        isLoveClicked = true;
        isBabyClicked = false;
        isDoodleClicked = false;
        mStickerHorizontalView.setVisibility(0);
        UtilUIEffectMenu.loadEffects(this, (LinearLayout)findViewById(0x7f090133), this, Stickers.STICKER_LOVE_ARRAY);
    }

    public void onCreate(Bundle bundle)
    {
        String as[];
        int i;
        ArrayList arraylist;
        String as1[];
        int j;
        super.onCreate(bundle);
        setContentView(0x7f03000a);
        mContext = this;
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Saving Image...");
        mProgressDialog.setCancelable(false);
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
        resourceID = 0;
        Bundle bundle1 = getIntent().getExtras();
        buttonSelectMoreImages = (ImageView)findViewById(0x7f090074);
        Bitmap bitmap;
        Bitmap bitmap1;
        CustomAdapter customadapter;
        CustomAdapter customadapter1;
        Tracker tracker;
        if(bundle1 != null && bundle1.containsKey("frame"))
        {
            resourceID = bundle1.getInt("frame");
            isFixedCollageClicked = true;
            isFreeFlow = false;
            buttonSelectMoreImages.setVisibility(8);
        } else
        {
            isFixedCollageClicked = false;
            isFreeFlow = true;
            buttonSelectMoreImages.setVisibility(0);
        }
        view = (StickerView)findViewById(0x7f090032);
        view.setOnTapListener(this);
        view.setOnButtonClickListener(this);
        seekbarBrushSize = (SeekBar)findViewById(0x7f0900ba);
        seekbarBrushSize.setMax(50);
        seekbarBrushSize.setProgress(10);
        seekbarBrushSize.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekbar, int k, boolean flag)
            {
                if(k < 10)
                {
                    k = 10;
                }
                mBrushSize = k;
                view.setBrushSize(mBrushSize);
            }

            public void onStartTrackingTouch(SeekBar seekbar)
            {
            }

            public void onStopTrackingTouch(SeekBar seekbar)
            {
            }
        });
        imageViewtext = (ImageView)findViewById(0x7f09006d);
        imageViewFreehand = (ImageView)findViewById(0x7f09006e);
        imageViewtexture = (ImageView)findViewById(0x7f09006f);
        imageViewSticker1 = (ImageView)findViewById(0x7f090070);
        imageViewhelp = (ImageView)findViewById(0x7f090071);
        layoutTextures = findViewById(0x7f090076);
        containerTextures = (LinearLayout)findViewById(0x7f090103);
        addCategories();
        layoutTextures.setVisibility(8);
        imageViewtexture.setImageResource(0x7f02007f);
        mStickerHorizontalView = (HorizontalScrollView)findViewById(0x7f090132);
        layoutStickers = findViewById(0x7f090077);
        mImageViewBaby = (ImageView)findViewById(0x7f0900fe);
        mImageViewDoodle = (ImageView)findViewById(0x7f0900fb);
        mImageViewLove = (ImageView)findViewById(0x7f0900f8);
        mTextViewLove = (TextView)findViewById(0x7f0900f9);
        mTextViewDoodle = (TextView)findViewById(0x7f0900fc);
        mTextViewBaby = (TextView)findViewById(0x7f0900ff);
        layoutStickers.setVisibility(8);
        mLayoutTopBar = (LinearLayout)findViewById(0x7f09006c);
        layouttopBarFreeHand = findViewById(0x7f090073);
        mSeekbar_layout = (LinearLayout)findViewById(0x7f09002b);
        point = getDisplaySize(((WindowManager)getSystemService("window")).getDefaultDisplay());
        bitmap = getBitmap(0x7f02014d, false);
        if(resourceID != 0)
        {
            Bitmap bitmap2 = getBitmap(resourceID, true);
            view.setBitmap(bitmap2);
            view.invalidate();
        }
        bitmap1 = doSomeTricks(getRandom(textures_resources));
        view.setTexture(bitmap1);
        view.invalidate();
        spinner = (Spinner)findViewById(0x7f0900cc);
        spinnerFontSize = (Spinner)findViewById(0x7f0900cd);
        edtText = (EditText)findViewById(0x7f0900c9);
        mPreview = (TextView)findViewById(0x7f0900ca);
        addTextWatcher();
        mListFont.clear();
        as = getResources().getStringArray(0x7f0b0001);
        array_paths = getResources().getStringArray(0x7f0b0000);
        i = 0;
        while(i < as.length){
        	mListFont.add(as[i]);
            i++;
        }
        arraylist = new ArrayList();
        as1 = getResources().getStringArray(0x7f0b0002);
        j = 0;
        while(true){
	        if(j >= as1.length)
	        {
	            customadapter = new CustomAdapter(this, 0x7f030034, mListFont, true, array_paths);
	            spinner.setAdapter(customadapter);
	            spinner.setSelection(0);
	            customadapter1 = new CustomAdapter(this, 0x7f030034, arraylist, false, null);
	            spinnerFontSize.setAdapter(customadapter1);
	            spinnerFontSize.setSelection(0);
	            addSpinnerItemChangedListener();
	            mLayoutBottomExpanded = (RelativeLayout)findViewById(0x7f090078);
	            mLayoutBottomExpanded.setVisibility(4);
	            view.addButtons(resourceID, bitmap, point);
	            pd = new ProgressDialog(this);
	            pd.setTitle("Please Wait!");
	            pd.setCancelable(false);
	            pd.setMessage("Creating Collage");
	            scale = getResources().getDisplayMetrics().density;
	            tracker = ((ApplicationController)getApplication()).getTracker();
	            tracker.setScreenName("StickerActivity");
	            tracker.send((new com.google.android.gms.analytics.HitBuilders.AppViewBuilder()).build());
	            if(AdMobManager.isReady(getApplication()))
	            {
	                AdMobManager.show();
	            }
	            return;
	        }
	        arraylist.add(as1[j]);
	        j++;
        }
    }

    public void onDoubleTapListener(final Object img, final com.km.photogridbuilder.bean.CustomTouch.PointInfo touchPoint)
    {
        if(img != null)
        {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Choose your option");
            if(img instanceof Image)
            {
                String[] _tmp = new String[3];
                String as[];
                if(!isFixedCollageClicked)
                {
                    as = getResources().getStringArray(0x7f0b0005);
                } else
                {
                    as = getResources().getStringArray(0x7f0b0003);
                }
                builder.setItems(as, new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i)
                    {
                        if(i != 0){
                        	if(i == 1)
                            {
                                view.delete(img);
                                if(!isFreeFlow && (img instanceof Image))
                                {
                                    lenghtyAddTask(touchPoint);
                                }
                                view.invalidate();
                                return;
                            }
                            if(i == 2)
                            {
                                CurrentObject = img;
                                File file = new File(((Image)CurrentObject).getUrl());
                                if(file.getParent().contains("CutPaste_") && file.getParentFile().exists())
                                {
                                    Log.e("ravi paste::", (new StringBuilder()).append(file.getParentFile().list().length).toString());
                                    if(file.getParentFile().list().length > 0)
                                    {
                                        askForPasteConfirmation();
                                        return;
                                    } else
                                    {
                                        String s1 = ((Image)img).getUrl();
                                        startCutPhoto(s1);
                                        return;
                                    }
                                }
                                File file1 = new File((new StringBuilder(String.valueOf(Constant.TEMP))).append(File.separator).append("CutPaste_").append(System.currentTimeMillis()).append(File.separator).append(System.currentTimeMillis()).append(".png").toString());
                                if(file1 != null)
                                {
                                    mOutputFilePath = file1.getAbsolutePath();
                                }
                                File file2 = new File(((Image)CurrentObject).getUrl());
                                if(file2.getParent().contains("Edit_") && file2.getParentFile().exists() && file2.getParentFile().list().length > 1)
                                {
                                    File file3 = checkforOriginalandSave(((Image)CurrentObject).getUrl());
                                    if(file3 != null)
                                    {
                                        File file4 = file1.getParentFile();
                                        if(!file4.exists())
                                        {
                                            file4.mkdir();
                                        }
                                        copyFile(file3, new File((new StringBuilder()).append(file1.getParentFile()).append(File.separator).append("image_original.png").toString()));
                                    }
                                }
                                String s = ((Image)img).getUrl();
                                startCutPhoto(s);
                            }
                            return;
                        }
                        File file5;
                        tempPointInfo = touchPoint;
                        CurrentObject = img;
                        file5 = new File(((Image)CurrentObject).getUrl());
                        if(!file5.getParent().contains("Edit_") && !file5.getParent().contains("CutPaste_") || !file5.getParentFile().exists()){
                        	File file6 = new File((new StringBuilder(String.valueOf(Constant.TEMP))).append(File.separator).append("Edit_").append(System.currentTimeMillis()).append(File.separator).append(System.currentTimeMillis()).append(".png").toString());
                            if(file6 != null)
                            {
                                mOutputFilePath = file6.getAbsolutePath();
                            }
                            File file7 = new File(((Image)CurrentObject).getUrl());
                            if((file5.getParent().contains("Edit_") || file5.getParent().contains("CutPaste_")) && file5.getParentFile().exists() && file7.getParentFile().list().length > 1)
                            {
                                File file8 = checkforOriginalandSave(((Image)CurrentObject).getUrl());
                                if(file8 != null)
                                {
                                    File file9 = file6.getParentFile();
                                    if(!file9.exists())
                                    {
                                        file9.mkdir();
                                    }
                                    copyFile(file8, new File((new StringBuilder()).append(file6.getParentFile()).append(File.separator).append("image_original.png").toString()));
                                }
                            }
                            String s2 = ((Image)img).getUrl();
                            startFeather(s2);
                            return;
                        }
                        Log.e("ravi::", (new StringBuilder()).append(file5.getParentFile().list().length).toString());
                        if(file5.getParentFile().list().length <= 0){
                        	String s3 = ((Image)img).getUrl();
                            startFeather(s3);
                            return;
                        }
                        askForEditConfirmation();
                    }
                });
            } else
            {
                builder.setItems(0x7f0b0004, new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i)
                    {
                        if(i == 0)
                        {
                            view.delete(img);
                            view.invalidate();
                        }
                    }
                });
            }
            builder.create().show();
        }
    }

    public void onStickerSelect(int i)
    {
        ImageObject imageobject = new ImageObject(BitmapFactory.decodeResource(getResources(), i), getResources());
        view.init(imageobject);
        if(!isFreeFlow)
        {
            int ai1[] = new int[2];
            ai1[0] = view.getBitmap().getWidth() / 2;
            ai1[1] = view.getBitmap().getHeight() / 2;
            view.loadImages(this, true, ai1);
        } else
        {
            int ai[] = new int[2];
            ai[0] = view.getWidth() / 2;
            ai[1] = view.getHeight() / 2;
            view.loadImages(this, true, ai);
        }
        view.invalidate();
    }
}
