package vn.iotstar.apiviewpager2;

import java.util.List;

public class MessageVideoModel {
    private boolean success;
    private String message;
    private List<VideoModel> result;

    public MessageVideoModel(boolean success, String message, List<VideoModel> result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<VideoModel> getResult() {
        return result;
    }

    public void setResult(List<VideoModel> result) {
        this.result = result;
    }
}
