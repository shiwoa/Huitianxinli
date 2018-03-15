package com.yizhilu.baoli;


public class PolyvDownloadInfo{
    private String vid;
    private String duration;
    private long filesize;
    private int bitrate;
    private long percent;
    private String title;
    private long total;
    private String image;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total=total;
	}
    
    public long getPercent() {
		return percent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPercent(long percent) {
		this.percent = percent;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public int getBitrate() {
		return bitrate;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}

	public PolyvDownloadInfo(){
    	
    }
    
	public PolyvDownloadInfo(String vid, String duration, long filesize, int bitrate,String image) {
		this.vid = vid;
		this.duration = duration;
		this.filesize = filesize;
		this.bitrate = bitrate;
		this.image = image;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public long getFilesize() {
		return filesize;
	}
	public void setFilesize(int filesize) {
		this.filesize = filesize;
	}
	@Override
	public String toString() {
		return "DownloadInfo [vid=" + vid + ", duration=" + duration
				+ ", filesize=" + filesize + ", total=" + total+"]";
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
