package com.example.petproject.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.petproject.R;


public class SureCancelDialog extends DialogFragment implements View.OnClickListener {

    private OnSureCancelListener mListener;
    private View view;
    private String mTitle;
    private String mLeftText;
    private String mRightText;
    private LinearLayout mLLEdit;
    private EditText mEditText;
    private boolean mIsVisible;

    public static SureCancelDialog newInstance() {
        SureCancelDialog fragment = new SureCancelDialog();
        return fragment;
    }

    public static SureCancelDialog newInstance(String title, String leftText, String rightText) {
        SureCancelDialog fragment = new SureCancelDialog();
        fragment.mTitle = title;
        fragment.mLeftText = leftText;
        fragment.mRightText = rightText;
        return fragment;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = View.inflate(getContext(), R.layout.dialog_sure_cancel, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.TransBottomSheetDialogStyle);
        AlertDialog dialog = builder.create();
        initView();
        dialog.setView(view);
        return dialog;
    }

    private void initView() {
        TextView left = view.findViewById(R.id.tv_left);
        left.setOnClickListener(this);
        TextView right = view.findViewById(R.id.tv_right);
        right.setOnClickListener(this);
        left.setText(mLeftText);
        right.setText(mRightText);
        TextView title = view.findViewById(R.id.tv_title);
        title.setText(mTitle);
        mLLEdit = view.findViewById(R.id.ll_edit);
        mEditText = view.findViewById(R.id.edit_title);
        if (mIsVisible) {
            mLLEdit.setVisibility(View.VISIBLE);
        }
    }

    public void setLLEditVisible() {
        this.mIsVisible = true;
    }

    public String getEditString() {
        if (mEditText != null) {
            return mEditText.getText().toString();
        } else {
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                if (mListener != null) {
                    mListener.onCancel();
                }
                //dismiss();
                break;
            case R.id.tv_right:
                if (mListener != null) {
                    mListener.onSureListener(mEditText.getText().toString());
                }
                //dismiss();
                break;
            default:
                break;
        }
    }

    public void setOnCancelListener(OnSureCancelListener listener) {
        this.mListener = listener;
    }

    public interface OnSureCancelListener {
        void onCancel();

        void onSureListener(String text);
    }
}
