package org.qtum.wallet.ui.fragment.fragment_input_password;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.fragment_input_fingerprint.InputFingerprintFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.OnClick;

public class InputPasswordFragment extends BaseFragment implements IInputPasswordView {

    private final static int MAX_INPUT_PIN_NUMBER = 6;
    private final static String ACTION = "action";

    private IInputPasswordPresenter mFragmentPresenter;

    @BindView(R.id.textViewPasswordDescription)
    TextView textViewPasswordDescription;

    @BindView(R.id.imageViewPassword_1)
    ImageView imageViewPassword_1;

    @BindView(R.id.imageViewPassword_2)
    ImageView imageViewPassword_2;

    @BindView(R.id.imageViewPassword_3)
    ImageView imageViewPassword_3;

    @BindView(R.id.imageViewPassword_4)
    ImageView imageViewPassword_4;

    @BindView(R.id.imageViewPassword_5)
    ImageView imageViewPassword_5;

    @BindView(R.id.imageViewPassword_6)
    ImageView imageViewPassword_6;

    @BindView(R.id.imageButtonKeypad_0)
    ImageButton imageButtonKeypad_0;

    @BindView(R.id.imageButtonKeypad_1)
    ImageButton imageButtonKeypad_1;

    @BindView(R.id.imageButtonKeypad_2)
    ImageButton imageButtonKeypad_2;

    @BindView(R.id.imageButtonKeypad_3)
    ImageButton imageButtonKeypad_3;

    @BindView(R.id.imageButtonKeypad_4)
    ImageButton imageButtonKeypad_4;

    @BindView(R.id.imageButtonKeypad_5)
    ImageButton imageButtonKeypad_5;

    @BindView(R.id.imageButtonKeypad_6)
    ImageButton imageButtonKeypad_6;

    @BindView(R.id.imageButtonKeypad_7)
    ImageButton imageButtonKeypad_7;

    @BindView(R.id.imageButtonKeypad_8)
    ImageButton imageButtonKeypad_8;

    @BindView(R.id.imageButtonKeypad_9)
    ImageButton imageButtonKeypad_9;

    @BindView(R.id.imageButtonKeypad_Del)
    ImageButton imageButtonKeypad_Del;

    // ImageView를 배열로 처리하기 위함
    private ImageView[] arrImageViewPassword;

    public static BaseFragment newInstance(Context context, InputPasswordAction action) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, InputPasswordFragment.class);
        args.putSerializable(ACTION, action);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new InputPasswordPresenterImpl(this, new InputPasswordInteractorImpl(getContext()));
    }

    @Override
    protected IInputPasswordPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_input_password;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        // 어떤 Action으로 Fragment를 실행할지 결정
        getPresenter().setAction((InputPasswordAction) getArguments().getSerializable(ACTION));

        // ImageView를 배열로 처리하기 위함
        arrImageViewPassword = new ImageView[MAX_INPUT_PIN_NUMBER];
        arrImageViewPassword[0] = imageViewPassword_1;
        arrImageViewPassword[1] = imageViewPassword_2;
        arrImageViewPassword[2] = imageViewPassword_3;
        arrImageViewPassword[3] = imageViewPassword_4;
        arrImageViewPassword[4] = imageViewPassword_5;
        arrImageViewPassword[5] = imageViewPassword_6;

    }

    @Override
    public void setInputNumberPinImage(int count) {
        setInputNumberPin(count);
    }

    @Override
    public void setDescriptionMessage(int strResourceID) {
        textViewPasswordDescription.setText(strResourceID);
    }

    @Override
    public void setKeypadNormalImage() {
        imageButtonKeypad_0.setImageResource(R.drawable.numericpad_pw_0);
        imageButtonKeypad_1.setImageResource(R.drawable.numericpad_pw_1);
        imageButtonKeypad_2.setImageResource(R.drawable.numericpad_pw_2);
        imageButtonKeypad_3.setImageResource(R.drawable.numericpad_pw_3);
        imageButtonKeypad_4.setImageResource(R.drawable.numericpad_pw_4);
        imageButtonKeypad_5.setImageResource(R.drawable.numericpad_pw_5);
        imageButtonKeypad_6.setImageResource(R.drawable.numericpad_pw_6);
        imageButtonKeypad_7.setImageResource(R.drawable.numericpad_pw_7);
        imageButtonKeypad_8.setImageResource(R.drawable.numericpad_pw_8);
        imageButtonKeypad_9.setImageResource(R.drawable.numericpad_pw_9);
        imageButtonKeypad_Del.setImageResource(R.drawable.numericpad_del);
    }

    @Override
    public void setKeypadWrongImage() {
        imageButtonKeypad_0.setImageResource(R.drawable.numericpad_pw_0_pressed);
        imageButtonKeypad_1.setImageResource(R.drawable.numericpad_pw_1_pressed);
        imageButtonKeypad_2.setImageResource(R.drawable.numericpad_pw_2_pressed);
        imageButtonKeypad_3.setImageResource(R.drawable.numericpad_pw_3_pressed);
        imageButtonKeypad_4.setImageResource(R.drawable.numericpad_pw_4_pressed);
        imageButtonKeypad_5.setImageResource(R.drawable.numericpad_pw_5_pressed);
        imageButtonKeypad_6.setImageResource(R.drawable.numericpad_pw_6_pressed);
        imageButtonKeypad_7.setImageResource(R.drawable.numericpad_pw_7_pressed);
        imageButtonKeypad_8.setImageResource(R.drawable.numericpad_pw_8_pressed);
        imageButtonKeypad_9.setImageResource(R.drawable.numericpad_pw_9_pressed);
        imageButtonKeypad_Del.setImageResource(R.drawable.numericpad_del_pressed);
    }

    @Override
    public void openInputFingerprintFragment()
    {
        Fragment fragment = InputFingerprintFragment.newInstance(getContext());
        openRootFragment(fragment);
    }

    private void setInputNumberPin(int count)
    {
        if (count < 0) return;
        if (count > MAX_INPUT_PIN_NUMBER) return;

        int i = 0;
        for (i = 0; i < count; i++) {
            arrImageViewPassword[i].setImageResource(R.drawable.pin_selected);
        }
        for (; i < MAX_INPUT_PIN_NUMBER; i++) {
            arrImageViewPassword[i].setImageResource(R.drawable.pin);
        }
    }

    private void onNumberKeyTouched(int num)
    {
        getPresenter().onNumberKeyTouched(num);
    }

    private void onDelKeyTouched()
    {
        getPresenter().onDelKeyTouched();
    }

    @OnClick({R.id.imageButtonKeypad_0, R.id.imageButtonKeypad_1, R.id.imageButtonKeypad_2, R.id.imageButtonKeypad_3, R.id.imageButtonKeypad_4, R.id.imageButtonKeypad_5, R.id.imageButtonKeypad_6, R.id.imageButtonKeypad_7, R.id.imageButtonKeypad_8, R.id.imageButtonKeypad_9, R.id.imageButtonKeypad_Del, })
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButtonKeypad_0: {
                onNumberKeyTouched(0);
                break;
            }
            case R.id.imageButtonKeypad_1: {
                onNumberKeyTouched(1);
                break;
            }
            case R.id.imageButtonKeypad_2: {
                onNumberKeyTouched(2);
                break;
            }
            case R.id.imageButtonKeypad_3: {
                onNumberKeyTouched(3);
                break;
            }
            case R.id.imageButtonKeypad_4: {
                onNumberKeyTouched(4);
                break;
            }
            case R.id.imageButtonKeypad_5: {
                onNumberKeyTouched(5);
                break;
            }
            case R.id.imageButtonKeypad_6: {
                onNumberKeyTouched(6);
                break;
            }
            case R.id.imageButtonKeypad_7: {
                onNumberKeyTouched(7);
                break;
            }
            case R.id.imageButtonKeypad_8: {
                onNumberKeyTouched(8);
                break;
            }
            case R.id.imageButtonKeypad_9: {
                onNumberKeyTouched(9);
                break;
            }
            case R.id.imageButtonKeypad_Del: {
                onDelKeyTouched();
                break;
            }
        }
    }
}
