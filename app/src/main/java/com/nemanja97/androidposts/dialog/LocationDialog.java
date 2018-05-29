package com.nemanja97.androidposts.dialog;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.nemanja97.androidposts.R;

public class LocationDialog extends AlertDialog.Builder {

    public LocationDialog(Context context) {
        super(context);

        setUpDialog();
    }

    private void setUpDialog(){
        setTitle("Warning");
        setMessage("Your location is disabled. Enable it?");
        setCancelable(false);

        setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getContext().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                dialogInterface.dismiss();
            }
        });

        setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
    }

    public AlertDialog prepareDialog(){
        AlertDialog dialog = create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

}
