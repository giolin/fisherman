package com.tetrapods.fisherman.util;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.tetrapods.fisherman.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SanctuaryDialog extends Dialog {

    @BindView(R.id.msg)
    TextView textView;

    final private MediaPlayer mp = new MediaPlayer();
    private Context context;

    public SanctuaryDialog(@NonNull Context context, String name) {
        super(context);
        this.context = context;
        setContentView(R.layout.sanctuary_dialog);
        ButterKnife.bind(this);
        String str = context.getString(R.string.you_are_in_sanctuary, name);
        textView.setText(str);
    }

    @OnClick(R.id.btn_check_rule)
    void CheckRule() {
        // TODO go to rule
    }

    @OnClick(R.id.btn_back_to_map)
    void BackToMap() {
        dismiss();
    }

    @Override
    public void show() {
        super.show();
        if (mp.isPlaying()) {
            mp.stop();
        }
        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = context.getAssets().openFd("eng_warning.mp3");
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare();
            mp.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        if (mp.isPlaying()) {
            mp.stop();
        }
        context = null;
        super.dismiss();
    }
    
}
