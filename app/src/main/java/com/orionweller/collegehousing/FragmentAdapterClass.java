package com.orionweller.collegehousing;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.orionweller.collegehousing.ApartmentTabView;


public class FragmentAdapterClass extends FragmentStatePagerAdapter {
    // This class enables the tabs to work

    int TabCount;

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
                Tab_1_Apartment_List tab1 = new Tab_1_Apartment_List();
                return tab1;

            case 1:
                // map view for apartments
                Tab_2_MapsActivity tab2 = new Tab_2_MapsActivity();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TabCount;
    }
}