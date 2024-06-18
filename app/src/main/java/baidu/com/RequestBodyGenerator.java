package baidu.com;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.IOException;

public class RequestBodyGenerator {

    private static final Moshi MOSHI = new Moshi.Builder().build();
    private static final JsonAdapter<RequestBodyModel> JSON_ADAPTER = MOSHI.adapter(RequestBodyModel.class);

    // 定义一个数据模型类，对应请求体的结构
    public static class RequestBodyModel {
        public Message[] messages;
        public double temperature;
        public double top_p;
        public int penalty_score;
        public String system;

        // 必要的构造方法、getter和setter省略
    }

    public static class Message {
        public String role;
        public String content;

        public Message(String user, String userInput) {
            this.role = user;
            this.content = userInput;
        }

        // 必要的构造方法、getter和setter


    }

    /**
     * 根据用户输入动态生成RequestBody
     * @param userInput 用户的具体输入内容
     * @return 返回根据用户输入创建的RequestBody实例
     */
    public static RequestBody createRequestBody(String userInput) throws IOException {
        // 创建请求体数据模型实例
        RequestBodyModel requestBodyModel = new RequestBodyModel();
        requestBodyModel.messages = new Message[]{new Message("user", userInput)};
        requestBodyModel.temperature = 0.95;
        requestBodyModel.top_p = 0.7;
        requestBodyModel.penalty_score = 1;
//        requestBodyModel.system = "你是一个“时间管理大师”，根据待办事项可以立刻做出合理精细且简洁的执行计划，执行计划是指将待办事项作为一个父任务，给出将执行待办事项需要完成的子任务计划。计划中的每个事件的内容格式为“开始时间-结束时间：具体内容：准备：建议：”。除了计划内容以外不要输出其他内容。请将具体内容分为几个点具体列出，同时给出准备和建议。";
        requestBodyModel.system = "请制定一个合理的、精细的、简洁的执行计划，将待办任务分配成子任务，并标明每个子任务的开始和结束时间，以及具体的任务内容、准备工作和建议。请将具体任务内容细分为多个点详细列出，并提供准备和建议。注意，除了计划内容外，不应包含任何其他信息。";
        // 使用Moshi将数据模型转换为JSON字符串
        String json = JSON_ADAPTER.toJson(requestBodyModel);
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    }

//    public static void main(String[] args) {
//        try {
//            String userInput = "7:30-10:30去教学楼303拍摄一场招聘会";
//            RequestBody body = createRequestBody(userInput);
//            // 现在你可以使用这个body发起网络请求
//            // ...
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}