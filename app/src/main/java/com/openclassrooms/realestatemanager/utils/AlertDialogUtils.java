package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AlertDialogUtils {

    private OnClickButtonAlertDialog onClickButtonAlertDialog;
    private OnClickButtonInputDialog onClickButtonInputDialog;
    private OnClickItemListAlertDialog onClickItemListAlertDialog;
    private OnClickItemSpinnerAlertDialog onClickItemSpinnerAlertDialog;

    public interface OnClickButtonAlertDialog {
        void positiveButtonDialogClicked(DialogInterface dialog, int dialogIdForSwitch);
        void negativeButtonDialogClicked(DialogInterface dialog, int dialogIdForSwitch) throws IOException;
    }
    public interface OnClickItemSpinnerAlertDialog {
        void positiveSpinnerButtonDialogClicked(DialogInterface dialog, Spinner spinner);
        void negativeSpinnerButtonDialogClicked(DialogInterface dialog);
    }

    public interface OnClickButtonInputDialog {
        void onClickedPositiveButtonInputDialog(DialogInterface dialog, TextInputEditText textInputEditText, int dialogIdForSwitch);
        void onClickedNegativeButtonInputDialog(DialogInterface dialog);
    }

    public interface OnClickItemListAlertDialog {
        void positiveButtonDialogClicked(DialogInterface dialog);
        void negativeButtonDialogClicked(DialogInterface dialog);
    }

    /** Construct **/
    public AlertDialogUtils(OnClickButtonAlertDialog onClickButtonAlertDialog) {
        this.onClickButtonAlertDialog = onClickButtonAlertDialog;
    }

    /** Construct **/
    public AlertDialogUtils(OnClickButtonInputDialog onClickButtonInputDialog, OnClickItemListAlertDialog onClickItemListAlertDialog,
                            OnClickItemSpinnerAlertDialog onClickItemSpinnerAlertDialog) {
        this.onClickButtonInputDialog = onClickButtonInputDialog;
        this.onClickItemListAlertDialog = onClickItemListAlertDialog;
        this.onClickItemSpinnerAlertDialog = onClickItemSpinnerAlertDialog;
    }

    /** Construct **/
    public AlertDialogUtils(OnClickButtonAlertDialog onClickButtonAlertDialog, OnClickButtonInputDialog onClickButtonInputDialog) {
        this.onClickButtonInputDialog = onClickButtonInputDialog;
        this.onClickButtonAlertDialog = onClickButtonAlertDialog;
    }

    /** *************************************** **/
    /** ******** Alert Dialog Method  ******** **/
    /** ************************************* **/

    public void showAlertDialog(Context context, String dialogTitle, String dialogMessage,
                                String positiveButtonText, String negativeButtonText,
                                int dialogDrawableBackground, int dialogDrawableIcon, int dialogIdForSwitch){

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        dialogBuilder.setTitle(dialogTitle);
        dialogBuilder.setMessage(dialogMessage);
        dialogBuilder.setPositiveButton(positiveButtonText, (dialog, which) -> onClickButtonAlertDialog.positiveButtonDialogClicked(dialog, dialogIdForSwitch));
        dialogBuilder.setNegativeButton(negativeButtonText, (dialog, which) -> {
            try {
                onClickButtonAlertDialog.negativeButtonDialogClicked(dialog, dialogIdForSwitch);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        alertBody(context, dialogDrawableBackground, dialogDrawableIcon, dialogBuilder);
    }

    public void showMessageDialog(Context context, String dialogTitle, String dialogMessage, String negativeButtonText,
                                  int dialogDrawableBackground, int dialogDrawableIcon, int dialogIdForSwitch){

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        dialogBuilder.setTitle(dialogTitle);
        dialogBuilder.setMessage(dialogMessage);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setPositiveButton(negativeButtonText, (dialog, which) -> {
            try {
                onClickButtonAlertDialog.negativeButtonDialogClicked(dialog, dialogIdForSwitch);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        alertBody(context, dialogDrawableBackground, dialogDrawableIcon, dialogBuilder);
    }

    public void showAlertInputDialog(Context context, String dialogTitle, String dialogMessage,
                                     String positiveButtonText, String negativeButtonText,String hint,int inputType,
                                     int dialogDrawableBackground, int dialogDrawableIcon, int dialogIdForSwitch){

        TextInputLayout textInputLayout = new TextInputLayout(context);
        textInputLayout.setHint(hint);
        textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
        textInputLayout.setBackgroundColor(Color.WHITE);
        textInputLayout.setBoxBackgroundColor(Color.WHITE);
        textInputLayout.setPadding(15,0,15,0);
        textInputLayout.setBoxCornerRadii(5, 5, 5, 5);
        TextInputEditText textInputEditText = new TextInputEditText(textInputLayout.getContext());
        textInputEditText.setInputType(inputType);
        textInputLayout.addView(textInputEditText);
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        dialogBuilder.setTitle(dialogTitle);
        dialogBuilder.setMessage(dialogMessage);
        dialogBuilder.setView(textInputLayout);
        dialogBuilder.setPositiveButton(positiveButtonText, (dialog, which) -> onClickButtonInputDialog.onClickedPositiveButtonInputDialog(dialog, textInputEditText, dialogIdForSwitch));
        dialogBuilder.setNegativeButton(negativeButtonText, (dialog, which) -> onClickButtonInputDialog.onClickedNegativeButtonInputDialog(dialog));
        alertBody(context, dialogDrawableBackground, dialogDrawableIcon, dialogBuilder);
    }

    public void showAlertSpinnerDialog(Context context, String dialogTitle, String dialogMessage,
                                     String positiveButtonText, String negativeButtonText,
                                     int dialogDrawableBackground, int dialogDrawableIcon){
        List<String> currency = Arrays.asList("USD", "EUR");
        Spinner spinner = new Spinner(context);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, currency);
        spinner.setAdapter(spinnerArrayAdapter);
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        dialogBuilder.setTitle(dialogTitle);
        dialogBuilder.setMessage(dialogMessage);
        dialogBuilder.setView(spinner);
        dialogBuilder.setPositiveButton(positiveButtonText, (dialog, which) -> onClickItemSpinnerAlertDialog.positiveSpinnerButtonDialogClicked(dialog, spinner));
        dialogBuilder.setNegativeButton(negativeButtonText, (dialog, which) -> onClickItemSpinnerAlertDialog.negativeSpinnerButtonDialogClicked(dialog));
        alertBody(context, dialogDrawableBackground, dialogDrawableIcon, dialogBuilder);
    }

    public void showAlertListDialog(Context context, String dialogTitle, int dialogDrawableBackground, int dialogDrawableIcon, ArrayAdapter arrayAdapter){

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        dialogBuilder.setTitle(dialogTitle);
        dialogBuilder.setIcon(dialogDrawableIcon);
        ContextCompat.getDrawable(context, dialogDrawableBackground);
        dialogBuilder.setBackground(ActivityCompat.getDrawable(context, dialogDrawableBackground));
        dialogBuilder.setView(R.layout.item_assignment_dialog_list_layout);

        dialogBuilder.setAdapter(arrayAdapter, null);
        dialogBuilder.setPositiveButton("Validate", (dialog, which) -> onClickItemListAlertDialog.positiveButtonDialogClicked(dialog));
        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> onClickItemListAlertDialog.negativeButtonDialogClicked(dialog));

        dialogBuilder.show();
    }


    private void alertBody(Context context, int dialogDrawableBackground, int dialogDrawableIcon, MaterialAlertDialogBuilder dialogBuilder) {
        dialogBuilder.setIcon(dialogDrawableIcon);
        dialogBuilder.setBackground(ActivityCompat.getDrawable(context, dialogDrawableBackground));
        dialogBuilder.show();
    }

}
