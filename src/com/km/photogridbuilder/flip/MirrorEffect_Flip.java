package com.km.photogridbuilder.flip;


public enum MirrorEffect_Flip
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

    private MirrorEffect_Flip(String s, int i)
    {
    }

    static 
    {
        MirrorEffect_Flip amirroreffect_flip[] = new MirrorEffect_Flip[12];
        amirroreffect_flip[0] = LEFT;
        amirroreffect_flip[1] = TOP;
        amirroreffect_flip[2] = BOTTOM;
        amirroreffect_flip[3] = RIGHT;
        amirroreffect_flip[4] = FOUR_D;
        amirroreffect_flip[5] = INVERT_4_D;
        amirroreffect_flip[6] = LEFT_HALF;
        amirroreffect_flip[7] = RIGHT_HALF;
        amirroreffect_flip[8] = SWIRL;
        amirroreffect_flip[9] = SPHERE;
        amirroreffect_flip[10] = WAVE;
        amirroreffect_flip[11] = WARP;
    }
}
