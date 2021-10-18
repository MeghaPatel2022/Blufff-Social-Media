package com.TBI.Client.Bluff.Model.GetOtherUser;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Datum implements Serializable {

    private final static long serialVersionUID = 900124758168908148L;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_type")
    @Expose
    private Integer userType;
    @SerializedName("cover_photo")
    @Expose
    private String coverPhoto;
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
    @SerializedName("location")
    @Expose
    private String location;
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
    @SerializedName("rate_avg")
    @Expose
    private Integer rateAvg;
    @SerializedName("has_rated")
    @Expose
    private Integer hasRated;
    @SerializedName("following_by_you")
    @Expose
    private Integer followingByYou;
    @SerializedName("follow_by_user")
    @Expose
    private Integer followByUser;
    @SerializedName("blocked")
    @Expose
    private Integer blocked;
    @SerializedName("reported")
    @Expose
    private Integer reported;

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

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public Integer getRateAvg() {
        return rateAvg;
    }

    public void setRateAvg(Integer rateAvg) {
        this.rateAvg = rateAvg;
    }

    public Integer getHasRated() {
        return hasRated;
    }

    public void setHasRated(Integer hasRated) {
        this.hasRated = hasRated;
    }

    public Integer getFollowingByYou() {
        return followingByYou;
    }

    public void setFollowingByYou(Integer followingByYou) {
        this.followingByYou = followingByYou;
    }

    public Integer getFollowByUser() {
        return followByUser;
    }

    public void setFollowByUser(Integer followByUser) {
        this.followByUser = followByUser;
    }

    public Integer getBlocked() {
        return blocked;
    }

    public void setBlocked(Integer blocked) {
        this.blocked = blocked;
    }

    public Integer getReported() {
        return reported;
    }

    public void setReported(Integer reported) {
        this.reported = reported;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("userType", userType).append("photo", photo).append("username", username).append("fullName", fullName).append("professionId", professionId).append("bio", bio).append("email", email).append("gender", gender).append("mobileCode", mobileCode).append("mobileNo", mobileNo).append("password", password).append("status", status).append("accountPrivacy", accountPrivacy).append("otp", otp).append("otpSession", otpSession).append("token", token).append("createdAt", createdAt).append("updatedAt", updatedAt).append("rateAvg", rateAvg).append("hasRated", hasRated).append("followingByYou", followingByYou).append("followByUser", followByUser).append("blocked", blocked).toString();
    }

}
