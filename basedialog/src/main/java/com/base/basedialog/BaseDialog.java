package com.base.basedialog;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaseDialog extends DialogFragment {
    private Button       mbtnOk;
    private Button       mbtnCancel;
    private LinearLayout linear_background;
    private TextView     text_title, text_content;
    private ImageView image_icon;

    private String title, content, ok, cancel;
    private int titleColor, contentColor, icon, LinearBackground, okBackground, okColor, cancelBackground, cancelColor;

    private int type;

    private static BaseDialog         baseDialog;
    private        BaseDialogListener mBaseDialogListener;
    private        boolean            hasTitle, hasContent, hasIcon, hasBackground, hasButtonOk, hasButtonCancel;
    private        View containerView;
    private View viewBg;

    public static interface BaseDialogListener {
        public void ok(int type, int resultCode);

        public void cancel(int type, int resultCode);
    }

    public static BaseDialog getInstance(int type, BaseDialogListener listener) {
        if (baseDialog == null) {
            baseDialog = new BaseDialog();
        }
        baseDialog.type = type;
        baseDialog.mBaseDialogListener = listener;
        return baseDialog;
    }

    public BaseDialog() {
    }



    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(true);
        // 设置背景透明，否则在对话框的后面会有白色背景
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        containerView = inflater.inflate(R.layout.base_dialog_1, null);

        initView(containerView);
        setUpView();
        setLisenter(linear_background);

        setEnterAnimation2(linear_background);
        Log.v("xf", "oncreate结果");
        return containerView;
    }

    private void setLisenter(final View viewInterface) {
        mbtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExitAnimation(viewInterface);
                if (mBaseDialogListener != null) {
                    mBaseDialogListener.ok(type, Activity.RESULT_OK);
                }
            }
        });
        if (mbtnCancel != null) {
            mbtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBaseDialogListener != null) {
                        mBaseDialogListener.cancel(type, Activity.RESULT_OK);
                    }
                }
            });
        }
    }

    private void initView(View viewInterface) {
        linear_background = (LinearLayout) viewInterface.findViewById(R.id.linear_background);
        image_icon = (ImageView) viewInterface.findViewById(R.id.image_icon);
        text_title = (TextView) viewInterface.findViewById(R.id.text_title);
        text_content = (TextView) viewInterface.findViewById(R.id.text_content);
        mbtnOk = (Button) viewInterface.findViewById(R.id.button_ok);
        viewBg = viewInterface.findViewById(R.id.view_bg);
        if (type == 2) {
            mbtnCancel = (Button) viewInterface.findViewById(R.id.button_cancel);
        }
    }

    private void setUpView() {
        if (hasTitle) {
            text_title.setText(title);
            text_title.setTextColor(titleColor);
        }
        if (hasContent) {
            text_content.setText(content);
            text_content.setTextColor(contentColor);
        }
        if (hasIcon) {
            image_icon.setImageResource(icon);
        }
        if (hasBackground) {
            GradientDrawable myGrad = (GradientDrawable) linear_background.getBackground();
            myGrad.setColor(LinearBackground);
        }
        if (hasButtonOk) {
            mbtnOk.setBackgroundColor(okBackground);
            mbtnOk.setText(ok);
            mbtnOk.setTextColor(okColor);
        }
        if (hasButtonCancel) {
            if (type == 1) {
                mbtnCancel.setBackgroundColor(cancelBackground);
                mbtnCancel.setText(cancel);
                mbtnCancel.setTextColor(cancelColor);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // 修改对话框的大小要在此事件中，不然有些机型会显示异常
        getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        //设置dialog背景为透明
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0f;
        window.setAttributes(windowParams);
    }


    private void setEnterAnimation(View view) {
        PropertyValuesHolder propertyValuesHolder1 = PropertyValuesHolder.ofFloat("translationY", -2000, 0);
        PropertyValuesHolder propertyValuesHolder2 = PropertyValuesHolder.ofFloat("rotation", -20, 0);
        ObjectAnimator animator1 = ObjectAnimator.
                ofPropertyValuesHolder(view, propertyValuesHolder1,
                        propertyValuesHolder2);
//        animator1.setInterpolator(new AccelerateInterpolator());
        animator1.setDuration(400);
//        animator1.start();

        PropertyValuesHolder propertyValuesHolder3 = PropertyValuesHolder.ofFloat("translationY", 0, -30);
        ObjectAnimator animator2 = ObjectAnimator.
                ofPropertyValuesHolder(view, propertyValuesHolder3);
        //   animator2.setInterpolator(new DecelerateInterpolator());
        animator2.setDuration(200);


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator2).after(animator1);
        animatorSet.setInterpolator(new DecelerateInterpolator());
//        animatorSet.setDuration(800);
        animatorSet.start();
    }

    private void setEnterAnimation2(View view) {
        PropertyValuesHolder propertyValuesHolder1 = PropertyValuesHolder.ofFloat("translationY", 600, 0);
        PropertyValuesHolder propertyValuesHolder2 = PropertyValuesHolder.ofFloat("translationX", 400, 0);
        PropertyValuesHolder propertyValuesHolder3 = PropertyValuesHolder.ofFloat("scaleX", 0.2f, 1);
        PropertyValuesHolder propertyValuesHolder4 = PropertyValuesHolder.ofFloat("scaleY", 0.2f, 1);
        PropertyValuesHolder propertyValuesHolder5 = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);

        ObjectAnimator animator1 = ObjectAnimator.
                ofPropertyValuesHolder(view, propertyValuesHolder1,
                        propertyValuesHolder2);
//        animator1.setInterpolator(new AnticipateInterpolator());//开始时，先向后再往前
        animator1.setDuration(500);

        ObjectAnimator animator2 = ObjectAnimator.
                ofPropertyValuesHolder(view, propertyValuesHolder3, propertyValuesHolder4);
//        animator2.setInterpolator(new OvershootInterpolator());//结束时，先扩充再恢复
        animator2.setDuration(500);

        ObjectAnimator animator3 = ObjectAnimator.
                ofPropertyValuesHolder(viewBg, propertyValuesHolder5);
//        animator2.setInterpolator(new OvershootInterpolator());//结束时，先扩充再恢复
        animator3.setDuration(800);



        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator2).with(animator1).with(animator3);
        animatorSet.setInterpolator(new AnticipateOvershootInterpolator());
//        animatorSet.setDuration(800);
        animatorSet.start();
    }

    private PointF mPointF;

    public void setMPointF(PointF pointF) {
        this.mPointF = pointF;
        linear_background.setTranslationX(mPointF.x);
        linear_background.setTranslationY(mPointF.y);
    }
    private void setExitAnimation(View view) {
        PointF startP = new PointF();
        startP.x = 0;
        startP.y = 0;
        PointF endP = new PointF();
        endP.x = - 500;
        endP.y = - 800;
        PointF controlP = new PointF();
        controlP.x = startP.x;
        controlP.y = -1200;
        ObjectAnimator anim = ObjectAnimator.ofObject(this, "mPointF", new CubicBezierPointFTypeEvaluator(controlP),startP,endP );
        anim.setDuration(800);
//        PropertyValuesHolder propertyValuesHolder1 = PropertyValuesHolder.ofFloat("translationY", 0, 600);
//        PropertyValuesHolder propertyValuesHolder3 = PropertyValuesHolder.ofFloat("translationX", 0, 400);
        PropertyValuesHolder propertyValuesHolder4 = PropertyValuesHolder.ofFloat("scaleX", 1, 0.05f);
        PropertyValuesHolder propertyValuesHolder5 = PropertyValuesHolder.ofFloat("scaleY", 1, 0.05f);
//        PropertyValuesHolder propertyValuesHolder2 = PropertyValuesHolder.ofFloat("rotation", 0, 10);
        ObjectAnimator animator1 = ObjectAnimator.
                ofPropertyValuesHolder(view, propertyValuesHolder4, propertyValuesHolder5);
        animator1.setInterpolator(new AnticipateOvershootInterpolator());
        animator1.setDuration(800);
        animator1.start();
//        PropertyValuesHolder propertyValuesHolder6 = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
//        ObjectAnimator animator3 = ObjectAnimator.
//                ofPropertyValuesHolder(viewBg, propertyValuesHolder6);
////        animator2.setInterpolator(new OvershootInterpolator());//结束时，先扩充再恢复
//        animator3.setDuration(2000);

//		PropertyValuesHolder propertyValuesHolder3 = PropertyValuesHolder.ofFloat("translationY",0,-10);
//		ObjectAnimator animator2 = ObjectAnimator.
//				ofPropertyValuesHolder(view,propertyValuesHolder3);
//		//   animator2.setInterpolator(new DecelerateInterpolator());
//		animator2.setDuration(200);


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(animator1).with(anim);
//        animatorSet.setDuration(800);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (mBaseDialogListener != null) {
            mBaseDialogListener.cancel(type, Activity.RESULT_CANCELED);
        }
        super.onCancel(dialog);
    }

    public BaseDialog bulidTitle(String title, int textColor) {
        this.title = title;
        this.titleColor = textColor;
        hasTitle = true;
        return baseDialog;
    }


    public BaseDialog bulidContent(String content, int textColor) {
        this.content = content;
        this.contentColor = textColor;
        hasContent = true;
        return baseDialog;
    }

    public BaseDialog bulidIcon(int imageRes) {
        icon = imageRes;
        hasIcon = true;
        return baseDialog;
    }

    public BaseDialog bulidBackground(int color) {
        LinearBackground = color;
        hasBackground = true;
        return baseDialog;
    }

    public BaseDialog bulidButtonOk(int backgroundColor, String text, int textColor) {
        ok = text;
        okBackground = backgroundColor;
        okColor = textColor;
        hasButtonOk = true;
        return baseDialog;
    }

    public BaseDialog bulidButtonCancel(int backgroundColor, String text, int textColor) {
        cancel = text;
        cancelBackground = backgroundColor;
        cancelColor = textColor;
        hasButtonCancel = true;
        return baseDialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hasTitle = false;
        hasContent = false;
        hasIcon = false;
        hasBackground = false;
        hasButtonCancel = false;
        hasButtonOk = false;
    }
}
