package com.yqx.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @content 菜单
 * */
@Table(name = "t_menu")
public class Y_Menu {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String icon;//节点图标
	private String name;//节点名称
	private Integer state;//状态
	private String url;//访问路径
	private Integer p_id;//父节点
	
	@Override
	public String toString() {

		return "{\"id\":\""+id+"\",\"icon\":\""+icon+"\",\"name\":\""+name+"\",\"state\":\""+state+"\",\"url\":\""+url+"\",\"p_id\":\""+p_id+"\",}";
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getP_id() {
		return p_id;
	}
	public void setP_id(Integer p_id) {
		this.p_id = p_id;
	}
}
