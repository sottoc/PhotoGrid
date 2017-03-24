package com.km.photogridbuilder.cut;

import android.app.*;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.km.photogridbuilder.util.BitmapUtil;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

// Referenced classes of package com.km.photogridbuilder.cut:
//            CutView, Image, EditActivity

public class CutActivity extends Activity
    implements CutView.ActionListener, android.view.View.OnClickListener
{
    private class BackgroundSave extends AsyncTask
    {

        String pathToSave;

        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((Bitmap[])aobj);
        }

        protected Void doInBackground(Bitmap abitmap[])
        {
            saveOutput(abitmap[0], pathToSave);
            return null;
        }

        protected void onPostExecute(Object obj)
        {
            onPostExecute((Void)obj);
        }

        protected void onPostExecute(Void void1)
        {
            super.onPostExecute(void1);
        }

        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        public BackgroundSave(String s)
        {
            super();
            pathToSave = s;
        }
    }

    static enum CUTMODE
    {
    	FREE_FORM("FREE_FORM", 0),
        SQUARE("SQUARE", 1),
        CIRCLE("CIRCLE", 2),
        HEART("HEART", 3),
        STAR("STAR", 4);
        static 
        {
            CUTMODE acutmode[] = new CUTMODE[5];
            acutmode[0] = FREE_FORM;
            acutmode[1] = SQUARE;
            acutmode[2] = CIRCLE;
            acutmode[3] = HEART;
            acutmode[4] = STAR;
        }

        private CUTMODE(String s, int i)
        {
        }
    }


    protected static final int REQUEST_ADVANCED_EDIT = 11;
    private static final int STAR_ANGLE_HALF = 18;
    private static final int STAR_OPP_ANGLE = 72;
    public static CUTMODE current_cut_mode;
    private String filePath;
    private boolean isAdvanced;
    private boolean isAlreadyOpened;
    private boolean isCutToolListShown;
    private boolean iscut;
    private boolean isdone;
    private LinearLayout layoutCutToolModeList;
    LinearLayout mBottomBar;
    ImageView mChooseTexture;
    private Bitmap mCroppedBitmap;
    private int mImagesize;
    private ImageButton mImgBtnSave;
    RelativeLayout mLayoutPaste;
    private CutView mView;
    private String outputPath;
    public ProgressDialog pd;
    private RelativeLayout relativeLayoutActivityStickerInfo;
    private Point screen;
    private TextView textViewActivityStickerInfoSticker;

    public CutActivity()
    {
        isCutToolListShown = false;
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
            BackgroundSave backgroundsave = new BackgroundSave((new StringBuilder(String.valueOf((new File(outputPath)).getParent()))).append(File.separator).append("image_original.png").toString());
            Bitmap abitmap[] = new Bitmap[1];
            abitmap[0] = mView.getBitmap();
            backgroundsave.execute(abitmap);
        }
    }

    private void counvertToImageCordinate(ArrayList arraylist)
    {
        Iterator iterator = arraylist.iterator();
        do
        {
            if(!iterator.hasNext())
            {
                return;
            }
            PointF pointf = (PointF)iterator.next();
            Bitmap bitmap = mView.getBitmap();
            float f = screen.x;
            float f1 = screen.y;
            float _tmp = (float)bitmap.getHeight();
            float _tmp1 = (float)bitmap.getWidth();
            int i = bitmap.getHeight();
            int j = bitmap.getWidth();
            int k = (int)(f1 - 2.0F * mView.gapRect.top);
            int l = (int)(f - 2.0F * mView.gapRect.left);
            float f2 = 1.0F * ((1.0F * (float)i) / (float)k);
            float f3 = 1.0F * ((1.0F * (float)j) / (float)l);
            float f4 = pointf.x - mView.gapRect.left;
            float f5 = pointf.y - mView.gapRect.top;
            pointf.x = f4 * f3;
            pointf.y = f5 * f2;
        } while(true);
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

    private void cutAndSave(RectF rectf, Path path)
    {
        Bitmap bitmap = mView.getBitmap();
        Bitmap bitmap1 = bitmap.copy(android.graphics.Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap1);
        canvas.drawBitmap(bitmap, 0.0F, 0.0F, null);
        canvas.clipPath(path, android.graphics.Region.Op.DIFFERENCE);
        canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
        float f = screen.x;
        float f1 = screen.y;
        float _tmp = (float)bitmap.getHeight();
        float _tmp1 = (float)bitmap.getWidth();
        int i = (int)rectf.left;
        int j = (int)rectf.top;
        int k = (int)rectf.right;
        int l = (int)rectf.bottom;
        int i1 = bitmap.getHeight();
        int j1 = bitmap.getWidth();
        int k1 = (int)(f1 - 2.0F * mView.gapRect.top);
        int l1 = (int)(f - 2.0F * mView.gapRect.left);
        float f2 = 1.0F * ((1.0F * (float)i1) / (float)k1);
        float f3 = 1.0F * ((1.0F * (float)j1) / (float)l1);
        float f4 = (float)i - mView.gapRect.left;
        float f5 = (float)k - mView.gapRect.right;
        float f6 = (float)j - mView.gapRect.top;
        float f7 = (float)l - mView.gapRect.bottom;
        float f8 = f4 * f3;
        float f9 = f5 * f3;
        float f10 = f6 * f2;
        float f11 = f7 * f2;
        Rect rect = new Rect((int)f8, (int)f10, (int)f9, (int)f11);
        int i2 = rect.width();
        int j2 = rect.height();
        if(i2 > 0 && j2 > 0)
        {
            Bitmap bitmap2 = Bitmap.createBitmap(i2, j2, android.graphics.Bitmap.Config.ARGB_8888);
            mImagesize = 1;
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            (new Canvas(bitmap2)).drawBitmap(bitmap1, rect, new Rect(0, 0, i2, j2), paint);
            mCroppedBitmap = bitmap2;
            if(isdone)
            {
                saveOutput(bitmap2, outputPath);
                checkforOriginalandSave();
            }
            if(isAdvanced)
            {
                saveOutput(bitmap2, outputPath);
            }
            Toast.makeText(this, "Cut Photo Copied Successfully. You can edit it in Advanced Edit or Paste it on a" +
"nother photo using Paste Tool. "
, 1).show();
            return;
        } else
        {
            mCroppedBitmap = null;
            mImagesize = 0;
            Toast.makeText(this, "Too small area to Cut. Please Draw a loop over larger area to cut.", 1).show();
            return;
        }
    }

    private Path drawShapes(RectF rectf)
    {
        Path path = new Path();
        Rect rect = new Rect((int)rectf.left, (int)rectf.top, (int)rectf.right, (int)rectf.bottom);
        if(current_cut_mode == CUTMODE.HEART)
        {
            int i1 = rect.width();
            int j1 = rect.height();
            float f2 = 1.0F * ((1.0F * (float)i1) / 130F);
            float f3 = 1.0F * ((1.0F * (float)j1) / 120F);
            path.moveTo((float)rect.left + 65F * f2, (float)rect.top + 20F * f3);
            path.cubicTo((float)rect.left + 65F * f2, (float)rect.top + 17F * f3, (float)rect.left + 60F * f2, (float)rect.top + 5F * f3, (float)rect.left + 45F * f2, (float)rect.top + 5F * f3);
            path.cubicTo((float)rect.left + 0.0F * f2, (float)rect.top + 5F * f3, (float)rect.left + 0.0F * f2, (float)rect.top + 42.5F * f3, (float)rect.left + 0.0F * f2, (float)rect.top + 42.5F * f3);
            path.cubicTo((float)rect.left + 0.0F * f2, (float)rect.top + 80F * f3, (float)rect.left + 20F * f2, (float)rect.top + 102F * f3, (float)rect.left + 65F * f2, (float)rect.top + 120F * f3);
            path.cubicTo((float)rect.left + 110F * f2, (float)rect.top + 102F * f3, (float)rect.left + 130F * f2, (float)rect.top + 80F * f3, (float)rect.left + 130F * f2, (float)rect.top + 42.5F * f3);
            path.cubicTo((float)rect.left + 130F * f2, (float)rect.top + 42.5F * f3, (float)rect.left + 130F * f2, (float)rect.top + 5F * f3, (float)rect.left + 90F * f2, (float)rect.top + 5F * f3);
            path.cubicTo((float)rect.left + 75F * f2, (float)rect.top + 5F * f3, (float)rect.left + 65F * f2, (float)rect.top + 17F * f3, (float)rect.left + 65F * f2, (float)rect.top + 20F * f3);
            path.close();
        } else
        {
            if(current_cut_mode == CUTMODE.STAR)
            {
                int i = rect.width();
                int j = Math.min(i, rect.height());
                double d = (double)j / Math.cos(Math.toRadians(18D));
                double d1 = j;
                double d2 = d1 * Math.tan(Math.toRadians(18D));
                double d3 = d / (2D + Math.cos(Math.toRadians(72D)) + Math.cos(Math.toRadians(72D)));
                double d4 = d3 * Math.cos(Math.toRadians(72D));
                double d5 = d3 * Math.sin(Math.toRadians(72D));
                int k = rect.left + i / 2;
                int l = rect.top;
                path.moveTo(k, l);
                path.lineTo((int)(d2 + (double)k), (int)(d1 + (double)l));
                path.lineTo((int)((double)k - d4 - d5), (int)(d5 + (double)l));
                path.lineTo((int)(d5 + (d4 + (double)k)), (int)(d5 + (double)l));
                path.lineTo((int)((double)k - d2), (int)(d1 + (double)l));
                path.lineTo(k, l);
                path.close();
                return path;
            }
            if(current_cut_mode == CUTMODE.CIRCLE)
            {
                float f = rect.width();
                float f1 = rect.height();
                path.addCircle((float)rect.left + f / 2.0F, (float)rect.top + f1 / 2.0F, f / 2.0F, android.graphics.Path.Direction.CW);
                return path;
            }
            if(current_cut_mode == CUTMODE.SQUARE)
            {
                RectF rectf1 = new RectF(rect);
                path.addRect(rectf1, android.graphics.Path.Direction.CW);
                return path;
            }
        }
        return path;
    }

    public static String getCurrentImageName()
    {
        return (new StringBuilder(String.valueOf((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())))).append(".png").toString();
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
	        filePath = uri.getPath();
	        outputstream = null;
	        if(uri != null)
	        {
	        	outputstream = getContentResolver().openOutputStream(uri);
	        }
	        if(outputstream != null)
	        {
	        	bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, outputstream);
	            if(isdone)
	            {
	                setResult(-1);
	                finish();
	            }
	            outputstream.close();
	        }
        }catch(Exception e){}
    }

    private void showConfirmationDialog(final RectF bounds, final Path mPath)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        dialog.setContentView(0x7f030029);
        ImageView imageview = (ImageView)dialog.findViewById(0x7f0900c4);
        ImageView imageview1 = (ImageView)dialog.findViewById(0x7f0900c2);
        ImageView imageview2 = (ImageView)dialog.findViewById(0x7f0900bf);
        ImageView imageview3 = (ImageView)dialog.findViewById(0x7f0900c0);
        cutAndSave(bounds, mPath);
        if(mImagesize != 0 && !isAlreadyOpened)
        {
            dialog.show();
            isAlreadyOpened = true;
        }
        imageview2.setImageBitmap(mCroppedBitmap);
        imageview.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                isdone = true;
                isAdvanced = false;
                cutAndSave(bounds, mPath);
                dialog.dismiss();
                isAlreadyOpened = false;
                finish();
            }
        });
        imageview1.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                mView.clearPath();
                dialog.dismiss();
                isAlreadyOpened = false;
            }
        });
        imageview3.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view)
            {
                dialog.dismiss();
                isAdvanced = true;
                isdone = false;
                cutAndSave(bounds, mPath);
                Intent intent = new Intent(CutActivity.this, com.km.photogridbuilder.cut.EditActivity.class);
                intent.putExtra("editimagepath", filePath);
                intent.putExtra("savepath", outputPath);
                startActivityForResult(intent, 11);
            }
        });
    }

    public Bitmap AddStickers()
    {
        Bitmap bitmap = mView.getBitmap().copy(android.graphics.Bitmap.Config.ARGB_8888, true);
        ArrayList arraylist = mView.getImages();
        Canvas canvas = new Canvas(bitmap);
        float f = screen.x;
        float f1 = screen.y;
        float f2 = bitmap.getHeight();
        float f3 = bitmap.getWidth();
        float _tmp = f2 / f1;
        float _tmp1 = f3 / f;
        int i = bitmap.getHeight();
        int j = bitmap.getWidth();
        int k = (int)(f1 - 2.0F * mView.gapRect.top);
        int l = (int)(f - 2.0F * mView.gapRect.left);
        float f4 = 1.0F * ((1.0F * (float)i) / (float)k);
        float f5 = 1.0F * ((1.0F * (float)j) / (float)l);
        Iterator iterator = arraylist.iterator();
        do
        {
            if(!iterator.hasNext())
            {
                return bitmap;
            }
            Image image = (Image)iterator.next();
            float f6 = image.getMinX() - mView.gapRect.left;
            float f7 = image.getMaxX() - mView.gapRect.right;
            float f8 = image.getMinY() - mView.gapRect.top;
            float f9 = image.getMaxY() - mView.gapRect.bottom;
            float f10 = f6 * f5;
            float f11 = f7 * f5;
            float f12 = f8 * f4;
            float f13 = f9 * f4;
            RectF rectf = new RectF(f10, f12, f11, f13);
            canvas.save();
            float f14 = (f11 + f10) / 2.0F;
            float f15 = (f13 + f12) / 2.0F;
            canvas.translate(f14, f15);
            canvas.rotate((180F * image.getAngle()) / 3.141593F);
            canvas.translate(-f14, -f15);
            canvas.drawBitmap(image.getBitmap(), null, rectf, image.getPaint());
            canvas.restore();
        } while(true);
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        super.onActivityResult(i, j, intent);
        switch(i)
        {
        default:
            return;

        case 11: // '\013'
            break;
        }
        if(j == -1)
        {
            checkforOriginalandSave();
            setResult(-1);
            finish();
            return;
        } else
        {
            setResult(0);
            finish();
            return;
        }
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
            finish();
            break;
        }
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(0x7f030002);
        screen = getDisplaySize(((WindowManager)getSystemService("window")).getDefaultDisplay());
        mView = (CutView)findViewById(0x7f090032);
        mView.setCutMode(CUTMODE.FREE_FORM);
        mBottomBar = (LinearLayout)findViewById(0x7f0900f5);
        Bundle bundle1 = getIntent().getExtras();
        if(bundle1 != null)
        {
            try
            {
                String s = bundle1.getString("inputpath");
                outputPath = bundle1.getString("savepath");
                Log.d("ravi", outputPath);
                Bitmap bitmap = BitmapUtil.getBitmap(getBaseContext(), s, screen.x / 2, screen.y / 2);
                mView.setBitmap(bitmap);
                mView.setMode(true);
            }
            catch(Exception exception)
            {
                Toast.makeText(this, "Photo format not supported. Please select another Photo.", 1).show();
                finish();
                return;
            }
        }
        mView.setOnActionListener(this);
        relativeLayoutActivityStickerInfo = (RelativeLayout)findViewById(0x7f090034);
        textViewActivityStickerInfoSticker = (TextView)findViewById(0x7f090035);
        if(iscut)
        {
            mImgBtnSave.setVisibility(8);
            textViewActivityStickerInfoSticker.setText(getString(0x7f060065));
        }
    }

    public void onCutActionListener(RectF rectf, Path path, ArrayList arraylist)
    {
        Path path1;
        int i;
        path1 = new Path();
        if(mView.getCutMode() != CUTMODE.FREE_FORM)
        {
            arraylist.add(new PointF(rectf.left, rectf.top));
            arraylist.add(new PointF(rectf.right, rectf.bottom));
        }
        counvertToImageCordinate(arraylist);
        if(mView.getCutMode() != CUTMODE.FREE_FORM)
        {
        	path1 = drawShapes(new RectF(((PointF)arraylist.get(0)).x, ((PointF)arraylist.get(0)).y, ((PointF)arraylist.get(1)).x, ((PointF)arraylist.get(1)).y));
        }else{
	        i = 0;
	        while(i < arraylist.size()){
	        	if(i == 0)
	            {
	                path1.moveTo(((PointF)arraylist.get(i)).x, ((PointF)arraylist.get(i)).y);
	            } else
	            if(i == -1 + arraylist.size())
	            {
	                path1.lineTo(((PointF)arraylist.get(i - 1)).x, ((PointF)arraylist.get(i - 1)).y);
	            } else
	            {
	                path1.quadTo(((PointF)arraylist.get(i - 1)).x, ((PointF)arraylist.get(i - 1)).y, (((PointF)arraylist.get(i)).x + ((PointF)arraylist.get(i - 1)).x) / 2.0F, (((PointF)arraylist.get(i)).y + ((PointF)arraylist.get(i - 1)).y) / 2.0F);
	            }
	            i++;
	        }
        }
        showConfirmationDialog(rectf, path1);
    }

    public void onDestroy()
    {
        super.onDestroy();
    }

    public void onDoubleTapListener(Image image, CustomTouch.PointInfo pointinfo)
    {
    }

    public void onPause()
    {
        super.onPause();
    }

    public void onResume()
    {
        super.onResume();
    }

    static 
    {
        current_cut_mode = CUTMODE.FREE_FORM;
    }
}
