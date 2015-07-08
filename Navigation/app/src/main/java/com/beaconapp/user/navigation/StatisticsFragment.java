package com.beaconapp.user.navigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class StatisticsFragment extends Fragment {

    private FragmentTabHost mTabHost;
    View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	
	((MainActivity) getActivity())
                .setActionBarTitle("Statistics");
        rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator("Day", null),
                DailyChartFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator("Week", null),
                WeeklyChartFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator("Month", null),
                MonthlyChartFragment.class, null);

        return rootView;
    }

}
