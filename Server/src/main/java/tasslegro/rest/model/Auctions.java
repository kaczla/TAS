package tasslegro.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Aukcja")
public class Auctions {
	@ApiModelProperty(value = "Identyfikator aukcji")
	private int aucitonId;
	@ApiModelProperty(value = "Identyfikator założyciela aukcji")
	private int userId;
	@ApiModelProperty(value = "Identyfikator zdjęcia")
	private int imageId;
	@ApiModelProperty(value = "Tytuł aukcji")
	private String title = "";
	@ApiModelProperty(value = "Opis aukcji")
	private String description = "";
	@ApiModelProperty(value = "Data rozpoczęcia aukcji")
	private String startDate = "";
	@ApiModelProperty(value = "Data zakończenia aukcji")
	private String endDate = "";
	@ApiModelProperty(value = "Ustala cenę aukcji")
	private float price;
	@ApiModelProperty(value = "Identyfikator użytkownika licytującego aukcję")
	private int bindId;

	public Auctions() {
	}

	@ApiModelProperty(value = "Zwraca identyfikator aukcji")
	public int getAucitonId() {
		return this.aucitonId;
	}

	@ApiModelProperty(value = "Ustala identyfikator aukcji")
	public void setAucitonId(int aucitonId) {
		this.aucitonId = aucitonId;
	}

	@ApiModelProperty(value = "Zwraca identyfikator założyciela aukcji")
	public int getUserId() {
		return this.userId;
	}

	@ApiModelProperty(value = "Ustala identyfikator założyciela aukcji")
	public void setUserId(int userId) {
		this.userId = userId;
	}

	@ApiModelProperty(value = "Zwraca identyfikator zdjęcia")
	public int getImageId() {
		return this.imageId;
	}

	@ApiModelProperty(value = "Ustala identyfikator zdjęcia")
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	@ApiModelProperty(value = "Zwraca tytuł aukcji")
	public String getTitle() {
		return this.title;
	}

	@ApiModelProperty(value = "Ustala tytuł aukcji")
	public void setTitle(String title) {
		this.title = title;
	}

	@ApiModelProperty(value = "Zwraca opis aukcji")
	public String getDescription() {
		return this.description;
	}

	@ApiModelProperty(value = "Ustala opis aukcji")
	public void setDescription(String description) {
		this.description = description;
	}

	@ApiModelProperty(value = "Zwraca date rozpoczęcia aukcji")
	public String getStartDate() {
		return this.startDate;
	}

	@ApiModelProperty(value = "Ustala date rozpoczęcia aukcji")
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@ApiModelProperty(value = "Zwraca date zakończenia aukcji")
	public String getEndDate() {
		return this.endDate;
	}

	@ApiModelProperty(value = "Ustala date zakończenia aukcji")
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@ApiModelProperty(value = "Zwraca cenę aukcji")
	public float getPrice() {
		return this.price;
	}

	@ApiModelProperty(value = "Ustala cenę aukcji")
	public void setPrice(float price) {
		this.price = price;
	}

	@ApiModelProperty(value = "Zwraca identyfikator użytkownika licytującego aukcję")
	public int getBindId() {
		return this.bindId;
	}

	@ApiModelProperty(value = "Ustala identyfikator użytkownika licytującego aukcję")
	public void setBindId(int bindId) {
		this.bindId = bindId;
	}
}
