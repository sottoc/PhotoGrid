package com.km.photogridbuilder.mirroreffects;


public enum MirrorEffect
{
	LEFT("LEFT", 0),
    TOP("TOP", 1),
    BOTTOM("BOTTOM", 2),
    RIGHT("RIGHT", 3),
    FOUR_D("FOUR_D", 4),
    INVERT_4_D("INVERT_4_D", 5),
    LEFT_HALF("LEFT_HALF", 6),
    RIGHT_HALF("RIGHT_HALF", 7),
    SWIRL("SWIRL", 8),
    SPHERE("SPHERE", 9),
    WAVE("WAVE", 10),
    WARP("WARP", 11);
    
    static 
    {
        MirrorEffect amirroreffect[] = new MirrorEffect[12];
        amirroreffect[0] = LEFT;
        amirroreffect[1] = TOP;
        amirroreffect[2] = BOTTOM;
        amirroreffect[3] = RIGHT;
        amirroreffect[4] = FOUR_D;
        amirroreffect[5] = INVERT_4_D;
        amirroreffect[6] = LEFT_HALF;
        amirroreffect[7] = RIGHT_HALF;
        amirroreffect[8] = SWIRL;
        amirroreffect[9] = SPHERE;
        amirroreffect[10] = WAVE;
        amirroreffect[11] = WARP;
    }
    
    private MirrorEffect(String s, int i)
    {
    }
}
