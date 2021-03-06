package com.yhkj.yymall.view.popwindows;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.LoginActivity;
import com.yhkj.yymall.activity.WebActivity;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 */
public class FullScreenPopupView extends BasePopupWindow  {

    @Bind(R.id.vpa_img_adimg)
    ImageView mImgAd;

    @Bind(R.id.vpa_img_close)
    ImageView mImgClose;

    public FullScreenPopupView(Activity context, File file, String url,String title) {
        super(context);
        init(file,url,title);
    }

    private void init(File file,final String url,final String title){
        mImgAd.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissWithOutAnima();
            }
        });
        mImgAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    getContext().startActivity(new Intent(getContext(),LoginActivity.class));
                }else{
                    WebActivity.loadUrl(getContext(),url,title);
                }
                dismissWithOutAnima();
            }
        });
    }

    @Override
    protected Animation initShowAnimation() {
//        Animation animation = getTranslateAnimation(250 * 2, 0, 300);
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }

    @Override
    public View onCreatePopupView() {
        View view = createPopupById(R.layout.view_pop_ad);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_animas);
    }
}
