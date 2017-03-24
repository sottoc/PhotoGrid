package com.km.photogridbuilder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.*;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.dexati.photogridbuilder.social.facebook.AlbumListScreen;
import com.dexati.photogridbuilder.social.facebook.HttpUtils;
import com.dexati.photogridbuilder.social.instagram.UserInfoScreen;
import com.dexati.photogridbuilder.social.instagram.oauth.InstagramApp;
import com.dexati.photogridbuilder.social.instagram.servercall.UrlConstant;
import com.facebook.*;
import com.km.photogridbuilder.listener.ImageLoadListener;
import com.km.photogridbuilder.listener.LoadImagetask;
import com.km.photogridbuilder.util.BitmapUtil2;
import com.km.photogridbuilder.util.PreferenceUtil;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

// Referenced classes of package com.km.photogridbuilder:
//            MultiPhotoSelectActivity

public class PhotoSelectorScreen extends Activity
{

    public static final int OPEN_CAMERA_REQUEST = 2;
    public static final int OPEN_INSTAGRAM_PIC = 4;
    private static final int REQUEST_CROP_IMAGE = 10;
    public static final int REQUEST_FACEBOOK_IMAGE = 3;
    public static final int REQUEST_GALLERY_IMAGE = 1;
    private static final String TAG = com.km.photogridbuilder.PhotoSelectorScreen.class.getSimpleName();
    public static boolean isFixedCollageClicked;
    public static boolean isMultipleMode;
    LinearLayout LinearLayout1;
    private com.facebook.Session.StatusCallback callback;
    ArrayList demo;
    protected boolean isCropCalled;
    private boolean isFbClicked;
    boolean iscamera;
    boolean isfacebook;
    boolean isgallary;
    boolean isinstagram;
    LinearLayout layoutCamera;
    LinearLayout layoutFacebook;
    LinearLayout layoutGallary;
    LinearLayout layoutInstagram;
    com.dexati.photogridbuilder.social.instagram.oauth.InstagramApp.OAuthAuthenticationListener listener;
    private InstagramApp mApp;
    private Bitmap mBmp;
    private ContentResolver mContentResolver;
    private String mCurrentPhotoPath;
    private android.graphics.Bitmap.CompressFormat mOutputFormat;
    private ArrayList mSavedImagesList;
    ProgressDialog progressDialog;
    private UiLifecycleHelper uiHelper;

    public PhotoSelectorScreen()
    {
        mOutputFormat = android.graphics.Bitmap.CompressFormat.PNG;
        callback = new com.facebook.Session.StatusCallback() {
            public void call(Session session, SessionState sessionstate, Exception exception)
            {
                Log.e(PhotoSelectorScreen.TAG, "session callback called");
                onSessionStateChange(session, sessionstate, exception);
            }
        };
        listener = new com.dexati.photogridbuilder.social.instagram.oauth.InstagramApp.OAuthAuthenticationListener() {
            public void onFail(String s)
            {
                Toast.makeText(PhotoSelectorScreen.this, s, 0).show();
            }

            public void onSuccess()
            {
                Intent intent = new Intent(PhotoSelectorScreen.this, com.dexati.photogridbuilder.social.instagram.UserInfoScreen.class);
                startActivityForResult(intent, 4);
            }
        };
    }

    private void CheckifSingleOption()
    {
        int i;
        i = PreferenceUtil.getcountSelected(this);
        Log.v("test", (new StringBuilder("countSelected")).append(i).toString());
        if(i != 1){
        	LinearLayout1.setVisibility(0);
            return;
        }
        if(!PreferenceUtil.getIsgallary(this)){
        	if(PreferenceUtil.getIscamera(this))
            {
                dispatchTakePictureIntent();
            } else
            if(PreferenceUtil.getIsfacebook(this))
            {
                isFbClicked = true;
                onFBLogin();
            } else
            if(PreferenceUtil.getIsinstagram(this))
            {
                onInstaClick();
            }
        }else{
	        if(isFixedCollageClicked)
	        {
	            dispatchGalleryIntent();
	        } else
	        {
	            Intent intent = new Intent();
	            intent.setClass(this, com.km.photogridbuilder.MultiPhotoSelectActivity.class);
	            startActivityForResult(intent, 0);
	        }
        }
        LinearLayout1.setVisibility(8);
        return;
    }

    private File createImageFile()
        throws IOException
    {
        String s = (new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)).format(new Date());
        File file = File.createTempFile((new StringBuilder("Photo_")).append(s).append("_").toString(), ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
        mCurrentPhotoPath = file.getAbsolutePath();
        return file;
    }

    private void dispatchGalleryIntent()
    {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    private void dispatchTakePictureIntent()
    {
    	Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if(intent.resolveActivity(getPackageManager()) == null)
        	return;
        File file;
    	try{
	        File file1 = createImageFile();
	        file = file1;
    	}catch(Exception e){
    		file = null;
    	}
        if(file != null)
        {
            intent.putExtra("output", Uri.fromFile(file));
            startActivityForResult(intent, 2);
        }
        return;
    }

    private void galleryAddPic()
    {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(mCurrentPhotoPath)));
        sendBroadcast(intent);
    }

    public static String getPath(Context context, Uri uri)
    {
        Cursor cursor = null;
        String s;
        try{
	        String as[] = {
	            "_data"
	        };
	        cursor = context.getContentResolver().query(uri, as, null, null, null);
	        int i = cursor.getColumnIndexOrThrow("_data");
	        cursor.moveToFirst();
	        Log.e("P", (new StringBuilder()).append(cursor.getString(i)).toString());
	        s = cursor.getString(i);
	        if(cursor != null)
	        {
	            cursor.close();
	        }
	        return s;
        }catch(Exception e){
        	if(cursor != null)
            {
                cursor.close();
            }
        	return null;
        }
    }

    private void initFB(Bundle bundle)
    {
    	Settings.addLoggingBehavior( LoggingBehavior.INCLUDE_ACCESS_TOKENS );
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(bundle);
    }

    private void onFBLogin()
    {
        if(HttpUtils.isNetworkAvail(this))
        {
            Session session = Session.getActiveSession();
            if(!session.isOpened() && !session.isClosed())
            {
                session.openForRead((new com.facebook.Session.OpenRequest(this)).setPermissions(Arrays.asList(new String[] {
                    "public_profile", "user_photos"
                })).setCallback(callback));
                Log.e(TAG, "opening new session");
                return;
            } else
            {
                Log.e(TAG, "opening active session");
                Session.openActiveSession(this, true, callback);
                return;
            }
        } else
        {
            Toast.makeText(this, "Check internet connection", 1).show();
            return;
        }
    }

    private void onInstaClick()
    {
        if(HttpUtils.isNetworkAvail(this))
        {
            mApp = new InstagramApp(this, UrlConstant.CLIENT_ID, UrlConstant.CLIENT_SECRET, UrlConstant.CALLBACK_URL);
            mApp.setListener(listener);
            if(!mApp.hasAccessToken())
            {
                mApp.authorize();
                return;
            } else
            {
                startActivityForResult(new Intent(this, com.dexati.photogridbuilder.social.instagram.UserInfoScreen.class), 4);
                return;
            }
        } else
        {
            Toast.makeText(this, "Check internet connection", 1).show();
            return;
        }
    }

    private void onSessionStateChange(Session session, SessionState sessionstate, Exception exception)
    {
        if(sessionstate.isOpened())
        {
            Log.e(TAG, "Logged in...");
            if(isFbClicked)
            {
                startActivityForResult(new Intent(getApplicationContext(), com.dexati.photogridbuilder.social.facebook.AlbumListScreen.class), 3);
            }
        } else
        if(sessionstate.isClosed())
        {
            Log.e(TAG, "Logged out...");
            return;
        }
    }

    private void saveOutput(Bitmap bitmap)
    {
        Uri uri;
        mCurrentPhotoPath = (new StringBuilder()).append(Environment.getExternalStorageDirectory()).toString();
        mCurrentPhotoPath = (new StringBuilder(String.valueOf(mCurrentPhotoPath))).append(File.separator).append("temp_img_forcrop.jpg").toString();
        uri = Uri.fromFile(new File(mCurrentPhotoPath));
        if(uri == null)
        	Log.e(TAG, "Could not save image");
        else{
        	try{
		        java.io.OutputStream outputstream = mContentResolver.openOutputStream(uri);
		        if(outputstream != null)
		        {
	                bitmap.compress(mOutputFormat, 100, outputstream);
	                Intent intent = new Intent();
	                intent.putExtra("path", mCurrentPhotoPath);
	                setResult(-1, intent);
	                finish();
	            }
	        }catch(IOException ioexception)
            {
                Log.e(TAG, (new StringBuilder("Cannot open file: ")).append(uri).toString(), ioexception);
            }
        }
        if(bitmap != null)
        {
            bitmap.recycle();
        }
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        switch(i){
        default:case 5:case 6:case 7:case 8:case 9:
        	break;
        case 0:
        	if(j == 0)
            {
                finish();
            }
            String s;
            Intent intent4;
            if(intent != null)
            {
                demo = (ArrayList)intent.getSerializableExtra("list");
                if(demo != null)
                {
                    Intent intent5 = new Intent();
                    intent5.putExtra("list", demo);
                    setResult(-1, intent5);
                    finish();
                }
            } else
            {
                finish();
            }
            if(j == -1)
            {
                s = (new StringBuilder(String.valueOf((new StringBuilder()).append(Environment.getExternalStorageDirectory()).toString()))).append(File.separator).append("temp_img.jpg").toString();
                intent4 = new Intent();
                intent4.putExtra("path", s);
                setResult(-1, intent4);
                finish();
            } else
            {
                finish();
            }
            break;
        case 1:
        	if(j == 0)
            {
                finish();
            }
            if(j == -1 && intent.getData() != null)
            {
                String s1 = getPath(this, intent.getData());
                if(s1 != null)
                {
                    mBmp = BitmapUtil2.getBitmapFromPath(s1, getResources().getDisplayMetrics());
                    Intent intent6 = new Intent();
                    intent6.putExtra("path", s1);
                    setResult(-1, intent6);
                    finish();
                } else
                {
                    Toast.makeText(getApplicationContext(), "Unable to load!!!!!!", 1).show();
                }
            } else
            {
                finish();
            }
            break;
        case 2:
        	if(j == 0)
            {
                finish();
            }
            if(j == -1 && !TextUtils.isEmpty(mCurrentPhotoPath))
            {
                galleryAddPic();
                if(mCurrentPhotoPath != null)
                {
                    mBmp = BitmapUtil2.getBitmapFromPath(mCurrentPhotoPath, getResources().getDisplayMetrics());
                    Intent intent2 = new Intent();
                    intent2.putExtra("path", mCurrentPhotoPath);
                    setResult(-1, intent2);
                    finish();
                }
            } else
            {
                finish();
            }
            break;
        case 3:
        	if(j == 0)
            {
                finish();
            }
            if(j == -1)
            {
                if(isFixedCollageClicked)
                {
                    if(HttpUtils.isNetworkAvail(this))
                    {
                        final ProgressDialog pd = new ProgressDialog(this);
                        pd.setMessage("Loading...");
                        pd.setCancelable(false);
                        pd.show();
                        Uri uri = intent.getData();
                        if(uri != null)
                        {
                            Log.e(getClass().getSimpleName(), (new StringBuilder()).append(uri).toString());
                            LoadImagetask loadimagetask = new LoadImagetask(new ImageLoadListener() {
                                public void onImageLoad(Bitmap bitmap)
                                {
                                    saveOutput(bitmap);
                                    pd.dismiss();
                                }
                            });
                            String as[] = new String[1];
                            as[0] = uri.toString();
                            loadimagetask.execute(as);
                        }
                    } else
                    {
                        Toast.makeText(this, "Check internet connection", 1).show();
                    }
                } else
                {
                    mSavedImagesList = intent.getStringArrayListExtra("image_list");
                    Log.e("PhotoSelector", (new StringBuilder()).append(mSavedImagesList.size()).toString());
                    Intent intent3 = new Intent();
                    intent3.putStringArrayListExtra("image_list", mSavedImagesList);
                    setResult(-1, intent3);
                    finish();
                }
            } else
            {
                finish();
            }
            break;
        case 4:
        	if(j == -1)
            {
                mSavedImagesList = intent.getStringArrayListExtra("image_list");
                Log.e("PhotoSelector", (new StringBuilder()).append(mSavedImagesList.size()).toString());
                Intent intent1 = new Intent();
                intent1.putStringArrayListExtra("image_list", mSavedImagesList);
                setResult(-1, intent1);
                finish();
            } else
            {
                isCropCalled = false;
                finish();
            }
        	break;
        case 10:
        	if(j == -1)
            {
                s = (new StringBuilder(String.valueOf((new StringBuilder()).append(Environment.getExternalStorageDirectory()).toString()))).append(File.separator).append("temp_img.jpg").toString();
                intent4 = new Intent();
                intent4.putExtra("path", s);
                setResult(-1, intent4);
                finish();
            } else
            {
                finish();
            }
        	break;
        }
        super.onActivityResult(i, j, intent);
        uiHelper.onActivityResult(i, j, intent);
    }

    public void onBackPressed()
    {
        if(!isCropCalled)
        {
            Log.e("PhotoSelector", (new StringBuilder("isCropCalled =")).append(isCropCalled).toString());
            super.onBackPressed();
        }
    }

    public void onCamera(View view)
    {
        dispatchTakePictureIntent();
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        mContentResolver = getContentResolver();
        setContentView(0x7f030009);
        isFixedCollageClicked = getIntent().getBooleanExtra("isFixedCollageClicked", true);
        if(isFixedCollageClicked)
        {
            isMultipleMode = false;
        } else
        {
            isMultipleMode = true;
        }
        initFB(new Bundle());
        progressDialog = new ProgressDialog(getApplicationContext());
        LinearLayout1 = (LinearLayout)findViewById(0x7f090062);
        isgallary = PreferenceUtil.getIsgallary(this);
        iscamera = PreferenceUtil.getIscamera(this);
        isfacebook = PreferenceUtil.getIsfacebook(this);
        isinstagram = PreferenceUtil.getIsinstagram(this);
        CheckifSingleOption();
        layoutGallary = (LinearLayout)findViewById(0x7f090063);
        layoutCamera = (LinearLayout)findViewById(0x7f090065);
        layoutFacebook = (LinearLayout)findViewById(0x7f090067);
        layoutInstagram = (LinearLayout)findViewById(0x7f090069);
        if(isgallary)
        {
            layoutGallary.setVisibility(0);
        } else
        {
            layoutGallary.setVisibility(8);
        }
        if(iscamera)
        {
            layoutCamera.setVisibility(0);
        } else
        {
            layoutCamera.setVisibility(8);
        }
        if(isfacebook)
        {
            layoutFacebook.setVisibility(0);
        } else
        {
            layoutFacebook.setVisibility(8);
        }
        if(isinstagram)
        {
            layoutInstagram.setVisibility(0);
            return;
        } else
        {
            layoutInstagram.setVisibility(8);
            return;
        }
    }

    protected void onDestroy()
    {
        if(mBmp != null)
        {
            mBmp.recycle();
            mBmp = null;
        }
        super.onDestroy();
        uiHelper.onDestroy();
    }

    public void onFacebook(View view)
    {
        isFbClicked = true;
        onFBLogin();
    }

    public void onGallery(View view)
    {
        if(isFixedCollageClicked)
        {
            dispatchGalleryIntent();
            return;
        } else
        {
            Intent intent = new Intent();
            intent.setClass(this, com.km.photogridbuilder.MultiPhotoSelectActivity.class);
            startActivityForResult(intent, 0);
            return;
        }
    }

    public void onInstagram(View view)
    {
        onInstaClick();
    }

    public void onPause()
    {
        super.onPause();
        uiHelper.onPause();
    }

    public void onResume()
    {
        super.onResume();
        uiHelper.onResume();
    }

    public void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        uiHelper.onSaveInstanceState(bundle);
    }




}
