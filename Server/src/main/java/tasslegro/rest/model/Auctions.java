package tasslegro.rest.model;

public class Auctions {
	private int aucitonId;
	private int userId;
	private int imageId;
	private String title = "";
	private String description = "";
	private String startDate = "";
	private String endDate = "";
	private float price;

	public Auctions() {
	}

	public int getAucitonId() {
		return this.aucitonId;
	}

	public void setAucitonId(int aucitonId) {
		this.aucitonId = aucitonId;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getImageId() {
		return this.imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public float getPrice() {
		return this.price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
}
