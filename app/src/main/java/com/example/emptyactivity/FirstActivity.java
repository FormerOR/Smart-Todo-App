package com.example.emptyactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import baidu.com.Sample;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        Button button1 = (Button) findViewById(R.id.Button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FirstActivity.this, "切换到活动2", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                startActivity(intent);
            }

        });

        TextView adv_text = (TextView) findViewById(R.id.textView2);
        EditText user_input_edit = (EditText) findViewById(R.id.editTextText);
        Button button2 = (Button) findViewById(R.id.Button_get_adv);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                        Toast.makeText(FirstActivity.this, content, Toast.LENGTH_SHORT).show();
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
                                    }
                                });
                            }
                        }
//                        Sample Sample1 = new Sample();
//                        try {
//                            String content = Sample1.start(user_input_edit.getText().toString());
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    adv_text.setText(content);
//                                    Toast.makeText(FirstActivity.this, content, Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(FirstActivity.this, "服务器开了点小差，请重试", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
                    }
                }).start();
//                Toast.makeText(FirstActivity.this, "你按到获取建议按钮了", Toast.LENGTH_SHORT).show();
//                Sample Sample1 = new Sample();
//                try {
//                    String content = Sample1.start(null);
//                    Toast.makeText(FirstActivity.this, content, Toast.LENGTH_SHORT).show();
//                    adv_text.setText(content);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(FirstActivity.this, "Exception!", Toast.LENGTH_SHORT).show();
//                }
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