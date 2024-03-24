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


public class PetHospDialog extends DialogFragment implements View.OnClickListener {

    private OnSureCancelListener mListener;
    private View view;
    private String mName;
    private String mWhere;
    private String mLen;
    private String mLeftText;
    private String mRightText;
    private LinearLayout mLLEdit;
    private EditText mEditText;
    private boolean mIsVisible;

    public static PetHospDialog newInstance() {
        PetHospDialog fragment = new PetHospDialog();
        return fragment;
    }

    public static PetHospDialog newInstance(String name, String where, String len, String leftText, String rightText) {
        PetHospDialog fragment = new PetHospDialog();
        fragment.mName = name;
        fragment.mWhere = where;
        fragment.mLen = len;
        fragment.mLeftText = leftText;
        fragment.mRightText = rightText;
        return fragment;
    }

    public void setTitle(String name) {
        this.mName = name;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = View.inflate(getContext(), R.layout.dialog_pet_hosp, null);
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

        TextView tv_name = view.findViewById(R.id.tv_name);
        tv_name.setText(mName);

        TextView tv_where = view.findViewById(R.id.tv_where);
        tv_where.setText(mWhere);

        TextView tv_len = view.findViewById(R.id.tv_len);
        tv_len.setText(mLen);

        left.setText(mLeftText);
        right.setText(mRightText);
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
                    mListener.onSureListener("");
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
