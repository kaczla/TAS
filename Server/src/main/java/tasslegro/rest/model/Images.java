package tasslegro.rest.model;

import java.io.InputStream;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Zdjęcie")
public class Images {
	@ApiModelProperty(value = "Identyfikator zdjęcia")
	private int id;
	@ApiModelProperty(value = "zawartość zdjęcia")
	private InputStream image = null;

	public Images() {
	}

	@ApiModelProperty(value = "Zwraca identyfikator zdjęcia")
	public int getId() {
		return this.id;
	}

	@ApiModelProperty(value = "Ustala identyfikator zdjęcia")
	public void setId(int id) {
		this.id = id;
	}

	@ApiModelProperty(value = "Ustala zawartość zdjęcia")
	public InputStream getImage() {
		return this.image;
	}

	@ApiModelProperty(value = "Zwraca zawartość zdjęcia")
	public void setImage(InputStream inputStream) {
		this.image = inputStream;
	}
}
