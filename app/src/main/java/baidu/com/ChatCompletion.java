package baidu.com;

public class ChatCompletion {
    String id;
    String object;
    long created;
    String result;
    boolean isTruncated;
    boolean needClearHistory;
    Usage usage;

    // 注意：Usage类定义应包含prompt_tokens, completion_tokens, total_tokens字段
    static class Usage {
        int prompt_tokens;
        int completion_tokens;
        int total_tokens;
    }

    public String getResult() {
        return result;
    }
}
