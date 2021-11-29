package model;

import androidx.annotation.StringRes;

public class User {

    private String userId;
    private String userName;
    private String avatarUrl;

    public User() {}

    public User (String userId, String userName, String avatarUrl) {
        this.userId = userId;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
    }

    public String getUserId() {return userId;}
    public String getUserName() {return userName;}
    public String getAvatarUrl() {return avatarUrl;}

}
