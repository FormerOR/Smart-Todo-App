package com.example.emptyactivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import baidu.com.Sample;

public class AdviseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_advise);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 获取数据
        Intent i = getIntent();
        String previous_info = i.getStringExtra("data");

        //设置mediaPlayer
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.bling);
        // 获取控件
        TextView adv_text = (TextView) this.findViewById(R.id.textView2);
        Button button2 = (Button) this.findViewById(R.id.Button_get_adv);
        ProgressBar wait_bar = (ProgressBar) this.findViewById(R.id.progressBar);
        TextView previous_info_text = (TextView) this.findViewById(R.id.textView6);

        // 设置上一个页面传递的数据
        previous_info_text.setText(previous_info);

        // 添加点击事件——获取建议
        onClickGetAdvice(button2, wait_bar, previous_info_text, adv_text, mediaPlayer);


    }

    private void onClickGetAdvice(Button button2, ProgressBar wait_bar, TextView user_input_edit, TextView adv_text, MediaPlayer mediaPlayer) {
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击按钮后，显示进度条
                wait_bar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int times = 0;
                        // 执行5次请求，直到成功就停止
                        for (times = 0; times < 5; times++) {
                            try {
                                // 调用Sample.start方法，传入用户输入的内容
                                Sample Sample1 = new Sample();
                                String content = Sample1.start(user_input_edit.getText().toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adv_text.setText(content);
                                        Toast.makeText(AdviseActivity.this, "已获取到建议", Toast.LENGTH_SHORT).show();
                                        // 播放音效
                                        if (!mediaPlayer.isPlaying()) {
                                            mediaPlayer.start();
                                        }
                                        // 隐藏进度条
                                        wait_bar.setVisibility(View.GONE);
                                    }
                                });
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                                int finalI = times;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AdviseActivity.this, String.format("服务器开了点小差，正在重试...(%d)", finalI+1), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            //  如果失败，toast提示用户
                            if (times == 4) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AdviseActivity.this, "服务器开了点小差，请重试", Toast.LENGTH_SHORT).show();
                                        // 隐藏进度条
                                        wait_bar.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    }
                }).start();
            }
        });
    }
}