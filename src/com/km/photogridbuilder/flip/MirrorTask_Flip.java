package com.km.photogridbuilder.flip;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.km.photogridbuilder.Objects.EffectsView;

// Referenced classes of package com.km.photogridbuilder.flip:
//            MirrorEffect_Flip, AppUtils_Flip, MirrorEffects_Flip

public class MirrorTask_Flip extends AsyncTask
{

    private Context mContext;
    private ProgressDialog mDialog;
    private MirrorEffect_Flip mEffect;
    private EffectsView mImageView;

    public MirrorTask_Flip(EffectsView effectsview, Context context, MirrorEffect_Flip mirroreffect_flip)
    {
        mImageView = effectsview;
        mContext = context;
        mEffect = mirroreffect_flip;
    }

    protected Object doInBackground(Object aobj[])
    {
        return doInBackground((String[])aobj);
    }

    protected String doInBackground(String as[])
    {
        if(!mEffect.equals(MirrorEffect_Flip.LEFT)){
        	if(mEffect.equals(MirrorEffect_Flip.RIGHT))
            {
                AppUtils_Flip.mBmpImage = MirrorEffects_Flip.getRightMirrored(AppUtils_Flip.mBmpImage);
            } else
            if(mEffect.equals(MirrorEffect_Flip.TOP))
            {
                AppUtils_Flip.mBmpImage = MirrorEffects_Flip.getTopMirrored(AppUtils_Flip.mBmpImage);
            } else
            if(mEffect.equals(MirrorEffect_Flip.BOTTOM))
            {
                AppUtils_Flip.mBmpImage = MirrorEffects_Flip.getBottomMirrored(AppUtils_Flip.mBmpImage);
            }
        }else
        	AppUtils_Flip.mBmpImage = MirrorEffects_Flip.getLeftMirrored(AppUtils_Flip.mBmpImage);
        return null;
    }

    protected void onPostExecute(Object obj)
    {
        onPostExecute((String)obj);
    }

    protected void onPostExecute(String s)
    {
        mImageView.setBitmap(AppUtils_Flip.mBmpImage);
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
