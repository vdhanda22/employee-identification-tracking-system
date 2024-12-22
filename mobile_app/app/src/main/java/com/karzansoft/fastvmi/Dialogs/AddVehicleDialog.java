package com.karzansoft.fastvmi.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.karzansoft.fastvmi.Adapters.ModelSearchAdapter;
import com.karzansoft.fastvmi.Models.VehicleModel;
import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;

/**
 * Created by Yasir on 7/21/2016.
 */
public class AddVehicleDialog extends AppCompatDialogFragment {
    View.OnClickListener clickListener;
    public EditText edtPlateNo,edtYear;
    public AutoCompleteTextView edtModal;
    public VehicleModel selectedModel;
    TextInputLayout modelContainer;
    Button btn_cancel,btn_save;

    public static AddVehicleDialog newInstance() {
        AddVehicleDialog frag = new AddVehicleDialog();
        return frag;
    }

 /*   @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView= inflater.inflate(R.layout.add_vehicle, null);

        AlertDialog.Builder dialog;


            dialog= new AlertDialog.Builder(getActivity())

                    .setTitle("Add Vehicle")
                    .setView(dialogView)
                /*    .setPositiveButton("Save", clickListener)
                    .setNegativeButton("Cancel",null
                    )*/;
        edtPlateNo=(EditText)dialogView.findViewById(R.id.plateNumber);
        edtModal=(AutoCompleteTextView)dialogView.findViewById(R.id.model);
        edtModal.setAdapter(new ModelSearchAdapter(getActivity()));
        edtYear=(EditText)dialogView.findViewById(R.id.year);
        btn_cancel=(Button)dialogView.findViewById(R.id.cancel);
        btn_save=(Button)dialogView.findViewById(R.id.save);
        modelContainer=(TextInputLayout)dialogView.findViewById(R.id.modelContainer);
        btn_cancel.setOnClickListener(clickListener);
        btn_save.setOnClickListener(clickListener);

        edtModal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedModel = ((ModelSearchAdapter) parent.getAdapter()).getModelAt(position);
                if (edtModal.hasFocus())
                    AppUtils.hideKeyboardFrom(getActivity(), edtModal);
            }
        });

        edtModal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0)
                    modelContainer.setHint("Make Model");
                else
                    modelContainer.setHint("Search Make/Model");
            }
        });
        return dialog.create();
    }

   /* @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_vehicle,container, false);
        //getDialog().setTitle("Add Vehicle");
        return view;
    }*/

    public void setButtonsClickListener( View.OnClickListener clickListener)
    {
        this.clickListener=clickListener;
    }
}
