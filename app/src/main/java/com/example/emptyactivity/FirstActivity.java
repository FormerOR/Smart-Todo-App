package com.example.emptyactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import baidu.com.Sample;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);

        // 从布局文件中加载视图
        LayoutInflater inflater = LayoutInflater.from(this);
        //设置mediaPlayer
        final MediaPlayer mediaPlayer_bling = MediaPlayer.create(this, R.raw.bling);
        final MediaPlayer mediaPlayer_ding = MediaPlayer.create(this, R.raw.ding);
        // 获取控件
        TextView adv_text = (TextView) findViewById(R.id.textView2);
        EditText user_input_edit = (EditText) findViewById(R.id.editTextText);
        Button button1 = (Button) findViewById(R.id.Button_1);
        Button button2 = (Button) findViewById(R.id.Button_get_adv);
        ProgressBar wait_bar = (ProgressBar) findViewById(R.id.progressBar);
        Button button3 = (Button) findViewById(R.id.Button_add_todo);
        LinearLayout todo_list = (LinearLayout) findViewById(R.id.todo_linearlayout);
        TextView version_text = (TextView) findViewById(R.id.textView_version);

        // 添加点击事件——获取建议
//        onClickGetAdvice(button2, wait_bar, user_input_edit, adv_text, mediaPlayer);
        onClickGetAdvice(button2);
        // 添加点击事件——切换到活动2
        onClickNextPage(button1);
        // 添加点击事件——添加待办事项
        onClickAddTodoItem(button3, inflater, todo_list, user_input_edit, mediaPlayer_ding);
        // 添加点击事件——打开github
        onClickOpenGithub(version_text);

    }

    private void onClickOpenGithub(TextView versionText) {
        versionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FirstActivity.this, "正在跳转至Github仓库界面", Toast.LENGTH_SHORT).show();
                String url = "https://github.com/FormerOR/Smart-Todo-App";
                openWebPage(url);
            }
        });
    }

    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "没有可用的浏览器应用", Toast.LENGTH_SHORT).show();
        }
    }

    static private int todo_item_count = 0;

    public void vibrator(long milliseconds) {
        // 获取Vibrator服务
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // 检查是否存在Vibrator服务并且设备支持震动
        if (vibrator != null && vibrator.hasVibrator()) {
            // 设定震动模式，参数为震动持续时间和间隔时间，单位为毫秒
            // 例如，下面的代码会让设备震动500毫秒，然后停止200毫秒，再震动500毫秒
            long[] pattern = {0, milliseconds, 200, milliseconds};

            // 执行震动。第二个参数为重复次数，-1表示不重复
            vibrator.vibrate(pattern, -1);
        }
    }

    private void onClickAddTodoItem(Button button3, LayoutInflater inflater, LinearLayout todo_list, EditText editText, MediaPlayer mediaPlayer) {
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todo_item_count++;
                final CardView cardView = (CardView) inflater.inflate(R.layout.cardview_item, null, false);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 10, 10, 10);
                cardView.setCardBackgroundColor(0x254CABF3);
                cardView.setCardElevation(1);
                cardView.setRadius(12);
                cardView.setLayoutParams(layoutParams);

                TextView textView = cardView.findViewById(R.id.textView_todo);
                ImageView imageView = cardView.findViewById(R.id.imageView_todo);
                textView.setText(editText.getText().toString());
                String todo = editText.getText().toString().trim();

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(FirstActivity.this, "切换到建议活动", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FirstActivity.this, AdviseActivity.class);
                        intent.putExtra("data", todo);
                        startActivity(intent);
                    }
                });

                //为imageView设置点击事件
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 切换图片（点亮和熄灭）
                        // 先获取两张图片分别为@android:drawable/btn_star_big_off和@android:drawable/btn_star_big_on
                        // 然后判断当前图片是否为@android:drawable/btn_star_big_off，如果是则切换为@android:drawable/btn_star_big_on，否则切换为@android:drawable/btn_star_big_off
                        if (imageView.getDrawable().getConstantState().equals(getResources().getDrawable(android.R.drawable.btn_star_big_off).getConstantState())) {
                            imageView.setImageResource(android.R.drawable.btn_star_big_on);
                            vibrator(300);
                            Toast.makeText(FirstActivity.this, "已完成待办！", Toast.LENGTH_SHORT).show();
                            // 将文本划去
                            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            // 播放音效
                            mediaPlayer.start();

                        } else {
                            imageView.setImageResource(android.R.drawable.btn_star_big_off);
                            vibrator(100);
                            Toast.makeText(FirstActivity.this, "取消完成状态", Toast.LENGTH_SHORT).show();
                            // 取消文本划去
                            textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        }
                    }
                });

                // 设置长按监听器
                cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showDeleteConfirmationDialog(cardView, todo_list);
                        return true; // 返回true表示已经处理了长按事件
                    }
                });

                if (!todo.isEmpty()) {
                    editText.setText(""); // 清空输入框
                    Toast.makeText(FirstActivity.this, "待办事项已添加", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FirstActivity.this, "待办事项不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                todo_list.addView(cardView);
//                Toast.makeText(FirstActivity.this, "已添加待办事项", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 显示删除确认对话框的方法
    private void showDeleteConfirmationDialog(final CardView cardView , LinearLayout todo_list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除提醒");
        builder.setMessage("确定要删除这个待办事项吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                todo_list.removeView(cardView);
                Toast.makeText(FirstActivity.this, "待办事项已删除", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void onClickNextPage(Button button1) {
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FirstActivity.this, "切换到活动2", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                // 传递数据
                intent.putExtra("data", "Hello SecondActivity");
                startActivity(intent);
            }

        });
    }

    private void onClickGetAdvice(Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FirstActivity.this, "Tip：获取建议前，请先点击待办事项", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickGetAdvice(Button button2, ProgressBar wait_bar, EditText user_input_edit, TextView adv_text, MediaPlayer mediaPlayer) {
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
                                        Toast.makeText(FirstActivity.this, "已获取到建议", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(FirstActivity.this, String.format("服务器开了点小差，正在重试...(%d)", finalI+1), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            //  如果失败，toast提示用户
                            if (times == 4) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(FirstActivity.this, "服务器开了点小差，请重试", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.add_item){
            Toast.makeText(this, "你按到添加按钮了", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.remove_item) {
            Toast.makeText(this, "你按到删除按钮了", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}