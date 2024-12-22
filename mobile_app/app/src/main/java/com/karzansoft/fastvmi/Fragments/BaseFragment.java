package com.karzansoft.fastvmi.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.karzansoft.fastvmi.Activities.FragmentMainActivity;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.Constant;


/**
 * Created by Yasir on 3/20/2016.
 */
public class BaseFragment extends Fragment {
    int current_mode;
    protected long mLastClickTime = 0;

    protected ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    protected void replaceFragment(Fragment fragment,String tag)
    {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainContent, fragment,tag);
        ft.commit();
    }

    protected void addFragment(Fragment fragment,String tag)
    {
        ((FragmentMainActivity)getActivity()).addFragment(fragment, tag, false);

    }
    protected void popFragment()
    {
        ((FragmentMainActivity)getActivity()).popFragment();
    }

    protected void popAllFragment()
    {
        ((FragmentMainActivity)getActivity()).popAllAddedFragment();
    }

    protected void showProgress()
    {
        ((FragmentMainActivity)getActivity()).showProgress();
    }


    protected void hideProgress()
    {
        ((FragmentMainActivity)getActivity()).hideProgress();
    }

    protected void showOperationStatusMsg(boolean status)
    {
        ((FragmentMainActivity)getActivity()).showOperationStatus(status);
    }
    protected void loadLocalizeStrings()
    {
        ((FragmentMainActivity)getActivity()).loadLanguageText();
    }

    protected String getlocalizeString(String key,String defVal)
    {
        return  ((FragmentMainActivity)getActivity()).getLocalizeString(key,defVal);
    }

    protected void setTitle()
    {

        getActivity().setTitle(getTitle(current_mode));
       /* if(current_mode== Constant.RA_CHECKOUT)
        {
            getActionBar().setTitle("Check Out");
        }else if(current_mode==Constant.RA_CHECKIN)
        {
            getActionBar().setTitle("Check In");
        }
        else if(current_mode==Constant.REPLACEMENT_CHECKIN)
        {
            getActionBar().setTitle("Replacement (In)");
        }
        else if(current_mode==Constant.REPLACEMENT_CHECKOUT)
        {
            getActionBar().setTitle("Replacement (Out)");
        }

        else if(current_mode==Constant.NRM_CHECKOUT)
        {
            getActionBar().setTitle("NRM Out");


        }else if(current_mode==Constant.NRM_CHECKIN)
        {
            getActionBar().setTitle("NRM In");

        }*/
    }

    protected String getTitle(int currentMode)
    {

        String title = "";
        if (currentMode == Constant.RA_CHECKOUT) {
            title = getlocalizeString("RentalAgreementCheckOut","Rental Agreement Check-Out");
        } else if (currentMode == Constant.RA_CHECKIN) {
            title = getlocalizeString("RentalAgreementCheckIn","Rental Agreement Check-In");
        } else if (currentMode == Constant.REPLACEMENT_CHECKIN) {
            title = getlocalizeString("ReplacementIn","Replacement (In)");
        } else if (currentMode == Constant.REPLACEMENT_CHECKOUT) {
            title = getlocalizeString("ReplacementOut","Replacement (Out)");
        } else if (currentMode == Constant.NRM_CHECKOUT) {
            title =getlocalizeString("StaffMovementCheckOut","Staff Movement Check-Out");

        } else if (currentMode == Constant.NRM_CHECKIN) {
            title = getlocalizeString("StaffMovementCheckIn","Staff Movement Check-In");
        } else if (currentMode == Constant.GARAGE_CHECKOUT) {
            title =getlocalizeString("WorkshopMovementCheckOut","Workshop Movement Check-Out");

        } else if (currentMode == Constant.GARAGE_CHECKIN) {
            title = getlocalizeString("WorkshopMovementCheckIn","Workshop Movement Check-In");
        }

        return title;
    }

    protected void reLogin()
    {
        ((FragmentMainActivity)getActivity()).relogin();
    }

    protected void syncMasters()
    {
        ((FragmentMainActivity)getActivity()).getMasterTables(true);
    }

    protected void emailCheckCard(long movid)
    {
        ((FragmentMainActivity)getActivity()).emailCheckCard(movid);
    }

    protected void isMovement(boolean flag)
    {
        if (flag)
            ((FragmentMainActivity)getActivity()).isSettings(false);
        else
            ((FragmentMainActivity)getActivity()).isSettings(true);
    }

    public boolean onBackPressed()
    {
        return false;
    }

    public void parentBackPress()
    {
        if(getActivity() instanceof FragmentMainActivity)
            ((FragmentMainActivity)getActivity()).parentBackPress();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof FragmentMainActivity)
            ((FragmentMainActivity)getActivity()).setRunningFragment(this);
    }

}
