package com.example.quizbstu.Konst;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private int id;
    private String Email;
    private String LoginName;
    private String FullName;
    private String Password1;
    public User(){

    }
    public User(int Id, String Email, String LoginName, String FullName, String Password1){
        this.id = Id;
        this.Email = Email;
        this.LoginName = LoginName;
        this.FullName = FullName;
        this.Password1 = Password1;
    }
    protected User(Parcel in) {
        id = in.readInt();
        Email = in.readString();
        LoginName = in.readString();
        FullName = in.readString();
        Password1 = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(Email);
        dest.writeString(LoginName);
        dest.writeString(FullName);
        dest.writeString(Password1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }

    public String getLoginName() {
        return LoginName;
    }

    public void setLoginName(String loginName) {
        LoginName = loginName;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPassword1() {
        return Password1;
    }

    public void setPassword1(String password1) {
        Password1 = password1;
    }
}
