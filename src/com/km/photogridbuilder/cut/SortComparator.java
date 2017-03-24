package com.km.photogridbuilder.cut;

import java.util.Comparator;

// Referenced classes of package com.km.photogridbuilder.cut:
//            Paste

public class SortComparator
    implements Comparator
{

    public SortComparator()
    {
    }

    public int compare(Paste paste, Paste paste1)
    {
        return paste1.getModifiedDate().compareTo(paste.getModifiedDate());
    }

    public int compare(Object obj, Object obj1)
    {
        return compare((Paste)obj, (Paste)obj1);
    }
}
