package com.km.photogridbuilder.mirroreffects;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.km.photogridbuilder.Objects.EffectsView;

// Referenced classes of package com.km.photogridbuilder.mirroreffects:
//            MirrorEffect, AppUtils, MirrorEffects

public class MirrorTask extends AsyncTask
{

    private Context mContext;
    private ProgressDialog mDialog;
    private MirrorEffect mEffect;
    private EffectsView mImageView;

    public MirrorTask(EffectsView effectsview, Context context, MirrorEffect mirroreffect)
    {
        mImageView = effectsview;
        mContext = context;
        mEffect = mirroreffect;
    }

    protected Object doInBackground(Object aobj[])
    {
        return doInBackground((String[])aobj);
    }

    protected String doInBackground(String as[])
    {
        if(!mEffect.equals(MirrorEffect.LEFT)){
        	if(mEffect.equals(MirrorEffect.RIGHT))
            {
                AppUtils.mBmpImage = MirrorEffects.getRightMirrored(AppUtils.mBmpImage);
            } else
            if(mEffect.equals(MirrorEffect.TOP))
            {
                AppUtils.mBmpImage = MirrorEffects.getTopMirrored(AppUtils.mBmpImage);
            } else
            if(mEffect.equals(MirrorEffect.BOTTOM))
            {
                AppUtils.mBmpImage = MirrorEffects.getBottomMirrored(AppUtils.mBmpImage);
            }
        }else
        	AppUtils.mBmpImage = MirrorEffects.getLeftMirrored(AppUtils.mBmpImage);
        return null;
    }

    protected void onPostExecute(Object obj)
    {
        onPostExecute((String)obj);
    }

    protected void onPostExecute(String s)
    {
        mImageView.setBitmap(AppUtils.mBmpImage);
        mDialog.dismiss();
        super.onPostExecute(s);
    }

    protected void onPreExecute()
    {
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage(mContext.getString(0x7f060051));
        mDialog.setCancelable(false);
        mDialog.show();
        super.onPreExecute();
    }
}
