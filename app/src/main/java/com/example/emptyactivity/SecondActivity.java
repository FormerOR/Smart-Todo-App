package com.example.emptyactivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button2 = (Button) findViewById(R.id.Button_page2);
        TextView textView = (TextView) findViewById(R.id.textView4);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.tap_wooden_fish);
        button2.setOnClickListener(v -> {
            // 点击按钮后文本框数字加一
            int num = Integer.parseInt(textView.getText().toString());
            textView.setText(String.valueOf(num + 1));
            // 播放音效
            mediaPlayer.start();

            // 切换图片,根据图片列表的顺序切换
            String[] imageList = {"cj", "jsj","yhq","lxf","cjz"};
            int index = num % imageList.length;
            int imageId = getResources().getIdentifier(imageList[index], "drawable", getPackageName());
            imageView.setImageResource(imageId);

        });
    }
}