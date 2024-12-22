package com.karzansoft.fastvmi.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;


/**
 * Created by Yasir on 3/24/2016.
 */
public class AlertDialogFragment extends AppCompatDialogFragment {

    DialogInterface.OnClickListener clickListener;

    public static AlertDialogFragment newInstance(String Title,String msg,String posButtontxt,boolean showNegButton) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title",Title);
        args.putString("msg",msg);
        args.putBoolean("neg",showNegButton);
        args.putString("btntxt",posButtontxt);

        frag.setArguments(args);
        return frag;
    }

    public static AlertDialogFragment newInstance(String Title,String msg,String posButtontxt,String negButtontxt) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title",Title);
        args.putString("msg",msg);
        args.putString("btntxt",posButtontxt);
        args.putBoolean("neg",true);
        args.putString("nbtntxt",negButtontxt);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title=this.getArguments().getString("title");
        String msg=this.getArguments().getString("msg");
        String btxt=this.getArguments().getString("btntxt");
        String nbtxt=this.getArguments().getString("nbtntxt","NO");
        boolean isNge=getArguments().getBoolean("neg");



        if(isNge)
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(msg)

                .setPositiveButton(btxt, clickListener)
                .setNegativeButton(nbtxt,
                        clickListener)
                .create();
        else
        {
          /*  LayoutInflater inflater = getActivity().getLayoutInflater();
            View view=inflater.inflate(R.layout.custom_dialog, null);
            TextView tv=(TextView)view.findViewById(R.id.info);
            tv.setText(msg);*/
            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(msg)

                    .setPositiveButton(btxt, clickListener)
                    .create();
        }
    }

    public void setButtonsClickListener( DialogInterface.OnClickListener clickListener)
    {
        this.clickListener=clickListener;
    }
}
