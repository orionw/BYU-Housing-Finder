package com.orionweller.collegehousing;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class FragmentAdapterClass extends FragmentStatePagerAdapter {
    // This class enables the tabs to work

    private int TabCount;

    public FragmentAdapterClass(FragmentManager fragmentManager, int CountTabs) {

        super(fragmentManager);

        this.TabCount = CountTabs;
    }

    @Override
    public Fragment getItem(int position) {
        // Two tabs, one for list, one for maps
        switch (position) {
            case 0:
                // List view of apartments
                return new Tab_1_Apartment_List();

            case 1:
                // map view for apartments
                return new Tab_2_MapsActivity();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TabCount;
    }
}