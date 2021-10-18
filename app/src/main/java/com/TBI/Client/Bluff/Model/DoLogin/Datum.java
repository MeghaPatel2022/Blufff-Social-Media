package com.TBI.Client.Bluff.Model.DoLogin;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Datum implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_type")
    @Expose
    private Integer userType;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("profession_id")
    @Expose
    private Integer professionId;
    @SerializedName("birth_date")
    @Expose
    private String birthDate;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("mobile_code")
    @Expose
    private String mobileCode;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("is_verified")
    @Expose
    private Integer isVerified;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("account_privacy")
    @Expose
    private Integer accountPrivacy;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("otp_session")
    @Expose
    private String otpSession;
    @SerializedName("_token")
    @Expose
    private String token;
    @SerializedName("social_token")
    @Expose
    private String socialToken;
    @SerializedName("social_platform")
    @Expose
    private String socialPlatform;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("longt")
    @Expose
    private String longt;
    @SerializedName("chat_key")
    @Expose
    private String chatKey;
    @SerializedName("accept_agreement")
    @Expose
    private Integer acceptAgreement;
    @SerializedName("profile_completed")
    @Expose
    private Integer profileCompleted;
    @SerializedName("current_profile_step")
    @Expose
    private Integer currentProfileStep;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getProfessionId() {
        return professionId;
    }

    public void setProfessionId(Integer professionId) {
        this.professionId = professionId;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileCode() {
        return mobileCode;
    }

    public void setMobileCode(String mobileCode) {
        this.mobileCode = mobileCode;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Integer getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Integer isVerified) {
        this.isVerified = isVerified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAccountPrivacy() {
        return accountPrivacy;
    }

    public void setAccountPrivacy(Integer accountPrivacy) {
        this.accountPrivacy = accountPrivacy;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOtpSession() {
        return otpSession;
    }

    public void setOtpSession(String otpSession) {
        this.otpSession = otpSession;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSocialToken() {
        return socialToken;
    }

    public void setSocialToken(String socialToken) {
        this.socialToken = socialToken;
    }

    public String getSocialPlatform() {
        return socialPlatform;
    }

    public void setSocialPlatform(String socialPlatform) {
        this.socialPlatform = socialPlatform;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongt() {
        return longt;
    }

    public void setLongt(String longt) {
        this.longt = longt;
    }

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }

    public Integer getAcceptAgreement() {
        return acceptAgreement;
    }

    public void setAcceptAgreement(Integer acceptAgreement) {
        this.acceptAgreement = acceptAgreement;
    }

    public Integer getProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(Integer profileCompleted) {
        this.profileCompleted = profileCompleted;
    }

    public Integer getCurrentProfileStep() {
        return currentProfileStep;
    }

    public void setCurrentProfileStep(Integer currentProfileStep) {
        this.currentProfileStep = currentProfileStep;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
