package ca.uvic.ecg.model;

public class resultJson {
    private String Message;

    public resultJson(String error) {
        this.Message = error;
    }


    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

}
