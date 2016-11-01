package com.smartfarm.net.bean;

import java.io.Serializable;

import com.smartfarm.view.util.URLsUtils;

public class ImageUrl implements Serializable {

	private static final long serialVersionUID = 8743273185733582838L;
	
	private String imgUrl;
	private String imgThumbUrl;
	
	public ImageUrl(String img) {
		
		imgUrl = URLsUtils.getOriginalImageUrl(img);
		imgThumbUrl = URLsUtils.getThumbImageUrl(img);
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public String getImgThumbUrl() {
		return imgThumbUrl;
	}
}
