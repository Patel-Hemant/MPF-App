package Models;

public class NotificationData {
    String name;
    String profile_url;
    String msg;

    public NotificationData(String name, String profile_url, String msg) {
        this.name = name;
        this.profile_url = profile_url;
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
