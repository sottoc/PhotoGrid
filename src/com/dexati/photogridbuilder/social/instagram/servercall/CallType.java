package com.dexati.photogridbuilder.social.instagram.servercall;


public enum CallType
{
	FETCH_FRNDS("FETCH_FRNDS", 0),
    FETCH_USERPHOTOS("FETCH_USERPHOTOS", 1);
	
    private CallType(String s, int i)
    {
    }
    
    static 
    {
        CallType acalltype[] = new CallType[2];
        acalltype[0] = FETCH_FRNDS;
        acalltype[1] = FETCH_USERPHOTOS;
    }
}
