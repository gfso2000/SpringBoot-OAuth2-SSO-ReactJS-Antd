package com.gfso.oauth.controller;

import java.util.UUID;

public class QRBean {
	private UUID randomUUID;
	private String imgData;
	public UUID getRandomUUID() {
		return randomUUID;
	}
	public void setRandomUUID(UUID randomUUID) {
		this.randomUUID = randomUUID;
	}
	public String getImgData() {
		return imgData;
	}
	public void setImgData(String imgData) {
		this.imgData = imgData;
	}
}
