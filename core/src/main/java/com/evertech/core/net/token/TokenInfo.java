package com.evertech.core.net.token;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @Author Shuo
 * @Create 2019-09-17
 * <p>
 * @Desc
 */
public class TokenInfo implements Parcelable {

    @SerializedName("access_token")
    public String accessToken;

    @SerializedName("refresh_token")
    public String refreshToken;

    @SerializedName("expires_in")
    public int accessTokenExpiringTime;

    @SerializedName("refresh_expires_in")
    public int refreshTokenExpiringTime;

    @Override
    public String toString() {
        return "TokenInfo{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", accessTokenExpiringTime=" + accessTokenExpiringTime +
                ", refreshTokenExpiringTime=" + refreshTokenExpiringTime +
                '}';
    }

    protected TokenInfo(Parcel in) {
        accessToken = in.readString();
        refreshToken = in.readString();
        accessTokenExpiringTime = in.readInt();
        refreshTokenExpiringTime = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accessToken);
        dest.writeString(refreshToken);
        dest.writeInt(accessTokenExpiringTime);
        dest.writeInt(refreshTokenExpiringTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TokenInfo> CREATOR = new Creator<TokenInfo>() {
        @Override
        public TokenInfo createFromParcel(Parcel in) {
            return new TokenInfo(in);
        }

        @Override
        public TokenInfo[] newArray(int size) {
            return new TokenInfo[size];
        }
    };

}
