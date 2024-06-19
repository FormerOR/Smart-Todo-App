package baidu.com;

import androidx.annotation.NonNull;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import com.google.gson.Gson;


public class Sample {
    public static final String API_KEY = "WlNEBsP7s9jsGH7RQlJJNmMN";
    public static final String SECRET_KEY = "TVSrrTOAzcxKsReuLUSjo7K4WOXMcFyi";

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    private static String Response_string = null;

    public static String start(String user_input) throws IOException{
        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{\"messages\":[{\"role\":\"user\",\"content\":\"7:30-10:30去教学楼303拍摄一场招聘会\"}],\"temperature\":0.95,\"top_p\":0.7,\"penalty_score\":1,\"system\":\"你是一个“时间管理大师”，根据待办事项可以立刻做出合理精细且简洁的执行计划，执行计划是指将待办事项作为一个父任务，给出将执行待办事项需要完成的子任务计划。计划中的每个事件的内容格式为“开始时间-结束时间：具体内容：准备：建议：”。除了计划内容以外不要输出其他内容。请将具体内容分为几个点具体列出，同时给出准备和建议。\"}");
        RequestBodyGenerator requestBodyGenerator = new RequestBodyGenerator();
        RequestBody body = RequestBodyGenerator.createRequestBody(user_input);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ai_apaas?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        Response_string = response.body().string();
        System.out.println(Response_string);

        String displayText = getDisplayText();

        return displayText;


    }

    @NonNull
    private static String getDisplayText() {
        Gson gson = new Gson();
        ChatCompletion chatCompletion = gson.fromJson(Response_string, ChatCompletion.class);

//        String displayText = "活动详情:\n"
//                + "开始时间：" + chatCompletion.result.split("\\n")[0] + "\n"
//                + "结束时间：" + chatCompletion.result.split("\\n")[1] + "\n"
//                + "具体内容：" + chatCompletion.result.split("\\n")[2].replaceFirst("具体内容：", "") + "\n"
//                + "准备：" + chatCompletion.result.split("\\n")[3].replaceFirst("准备：", "") + "\n"
//                + "建议：" + chatCompletion.result.split("\\n")[4].replaceFirst("建议：", "");
//

        // 提取result字段，并根据\n分隔内容
        String result = chatCompletion.getResult();
        String[] lines = result.split("\n");
        System.out.println(lines);

        // 构建显示的文本
        StringBuilder displayText = new StringBuilder();
        for (String line : lines) {
            // 添加到显示文本中
            displayText.append(line).append("\n");
        }

        return displayText.toString().trim();
    }


    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    static String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        try {
            return new JSONObject(response.body().string()).getString("access_token");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}