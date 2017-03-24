package com.km.photogridbuilder.listener;


public interface ImageSaveListener
{

    public abstract void onImageSaveFailed(Exception exception);

    public abstract void onImageSaved(String s);
}
