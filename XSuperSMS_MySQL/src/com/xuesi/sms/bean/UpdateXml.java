package com.xuesi.sms.bean;

/**
 * 配置应用升级
 * 
 * @author XS-PC014
 * 
 */
public class UpdateXml {

	/** 应用文件名 */
	private String fileName;
	/** 服务器上的版本号 */
	private int versionCode;
	/** 服务器上的版本名 */
	private String versionName;
	/** 更新时间 */
	private String updateTime;
	/** 更新日志 */
	private String description;
	/** apk下载地址 */
	private String downloadUrl;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
