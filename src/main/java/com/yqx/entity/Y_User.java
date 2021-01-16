package com.yqx.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 * 用户类
 */
@Table(name = "t_user")
@SuppressWarnings("all")
public class Y_User implements Serializable {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
    private String bz;
    
    private String password;
    
    @Column(name = "true_name")
    private String true_name;
    
    @Column(name = "user_name")
    private String user_name;
    
    private String remarks;
    
    @Transient
    private String roles;
    
    
    @Transient
    private String oldPassword;
    
    
    public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	@Override
    public String toString() {
    	
    	return "{\"id\":\""+id+"\",\"bz\":\""+bz+"\",\"password\":\""+password+"\",\"true_name\":\""+true_name+"\",\"user_name\":\""+user_name+"\",\"remarks\":\""+remarks+"\",}";
    }
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTrue_name() {
		return true_name;
	}
	public void setTrue_name(String true_name) {
		this.true_name = true_name;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
