package com.zenglb.framework.retrofit2.result;

import java.util.List;

/**
 * Created by zenglb on 2017/4/23.
 */

public class StaffMsg {
    /**
     * id : 233
     * fullname : 张三
     * nickname : 张三疯
     * sex : male
     * mobile : 15011112222
     * created : 2015-11-24 18:49:49
     * updated : 2015-11-24 18:49:49
     * identity_id : 44011119910101666X
     * is_keeper : true
     * avatar_url : http://static.4009515151.com/avatar_url.jpg
     * contact_phones : ["15011112222"]
     * job_can_edit : true
     * role_identity : IDT_PROJECT
     */

    private int id;
    private String fullname;
    private String nickname;
    private String sex;
    private String mobile;
    private String created;
    private String updated;
    private String identity_id;
    private boolean is_keeper;
    private String avatar_url;
    private boolean job_can_edit;
    private String role_identity;
    private List<String> contact_phones;

    public StaffMsg(String mobile, String created) {
        this.mobile = mobile;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getIdentity_id() {
        return identity_id;
    }

    public void setIdentity_id(String identity_id) {
        this.identity_id = identity_id;
    }

    public boolean isIs_keeper() {
        return is_keeper;
    }

    public void setIs_keeper(boolean is_keeper) {
        this.is_keeper = is_keeper;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public boolean isJob_can_edit() {
        return job_can_edit;
    }

    public void setJob_can_edit(boolean job_can_edit) {
        this.job_can_edit = job_can_edit;
    }

    public String getRole_identity() {
        return role_identity;
    }

    public void setRole_identity(String role_identity) {
        this.role_identity = role_identity;
    }

    public List<String> getContact_phones() {
        return contact_phones;
    }

    public void setContact_phones(List<String> contact_phones) {
        this.contact_phones = contact_phones;
    }
}
