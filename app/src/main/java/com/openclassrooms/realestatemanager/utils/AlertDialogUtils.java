package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import androidx.core.app.ActivityCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AlertDialogUtils {

    private OnClickButtonAlertDialog onClickButtonAlertDialog;
    private OnClickButtonInpuDialog onClickButtonInputDialog;

    public interface OnClickButtonAlertDialog {
        void positiveButtonDialogClicked(DialogInterface dialog, int dialogIdForSwitch);
        void negativeButtonDialogClicked(DialogInterface dialog, int dialogIdForSwitch);
    }

    public interface OnClickButtonInpuDialog {
        void onClickedPositiveButtonInputDialog(DialogInterface dialog, TextInputEditText textInputEditText, int dialogIdForSwitch);
        void onClickedNegativeButtonInputDialog(DialogInterface dialog);
    }

    /** Construct **/
    public AlertDialogUtils(OnClickButtonAlertDialog onClickButtonAlertDialog) {
        this.onClickButtonAlertDialog = onClickButtonAlertDialog;
    }

    /** Construct **/
    public AlertDialogUtils(OnClickButtonInpuDialog onClickButtonInputDialog) {
        this.onClickButtonInputDialog = onClickButtonInputDialog;
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
        dialogBuilder.setNegativeButton(negativeButtonText, (dialog, which) -> onClickButtonAlertDialog.negativeButtonDialogClicked(dialog, dialogIdForSwitch));
        alertBody(context, dialogDrawableBackground, dialogDrawableIcon, dialogBuilder);
    }

    public void showMessageDialog(Context context, String dialogTitle, String dialogMessage, String negativeButtonText,
                                  int dialogDrawableBackground, int dialogDrawableIcon, int dialogIdForSwitch){

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        dialogBuilder.setTitle(dialogTitle);
        dialogBuilder.setMessage(dialogMessage);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setPositiveButton(negativeButtonText, (dialog, which) -> onClickButtonAlertDialog.negativeButtonDialogClicked(dialog, dialogIdForSwitch));
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

    private void alertBody(Context context, int dialogDrawableBackground, int dialogDrawableIcon, MaterialAlertDialogBuilder dialogBuilder) {
        dialogBuilder.setIcon(dialogDrawableIcon);
        dialogBuilder.setBackground(ActivityCompat.getDrawable(context, dialogDrawableBackground));
        dialogBuilder.show();
    }

}
