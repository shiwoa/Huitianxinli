package com.yizhilu.entity;

import java.io.Serializable;

/**
 * @author bin
 * 修改人:
 * 时间:2015-10-12 上午10:02:31
 * 类说明:广告图的实体
 */
public class BannerEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String imagesUrl;
	private int courseId;
	private String title;
	private String keyWord;
	private int seriesNumber;
	private String color;
	private String sellType;
	private String previewUrl;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImagesUrl() {
		return imagesUrl;
	}
	public void setImagesUrl(String imagesUrl) {
		this.imagesUrl = imagesUrl;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public int getSeriesNumber() {
		return seriesNumber;
	}
	public void setSeriesNumber(int seriesNumber) {
		this.seriesNumber = seriesNumber;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getPreviewUrl() {
		return previewUrl;
	}
	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}
	public String getSellType() {
		return sellType;
	}
	public void setSellType(String sellType) {
		this.sellType = sellType;
	}
	
}
