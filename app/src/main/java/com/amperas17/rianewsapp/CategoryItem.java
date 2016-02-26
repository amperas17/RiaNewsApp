package com.amperas17.rianewsapp;

/**
 * Created by Вова on 26.02.2016.
 */
public class CategoryItem {
    String mName;
    String mLink;

    CategoryItem(String name,String link){
        mName = name;
        mLink = link;
    }

    @Override
    public String toString() {
        return mName+" "+mLink;
    }

    @Override
    public boolean equals(Object object)
    {
        boolean isSame = false;

        if (object != null && object instanceof CategoryItem)
        {
            isSame = this.mName.equals(((CategoryItem) object).mName);
        }

        return isSame;
    }
}
