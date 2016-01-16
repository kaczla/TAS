package tasslegro.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class ImageUploader implements Receiver, SucceededListener {
	private InputStream fis = null;
	private OutputStream fos = null;
	private int imageId;

	public OutputStream receiveUpload(String filename, String mimeType) {
		File file = null;
		System.out.println("Add filename: " + filename + "\ntype: " + mimeType);
		try {
			file = new File("/tmp/" + filename);
			fos = new FileOutputStream(file);
			fis = new FileInputStream(file);
		} catch (IOException e) {
			Notification.show("PROBLEM WITH IMAGE", e.getMessage(), Notification.Type.ERROR_MESSAGE);
			System.err.println(e.getMessage());
			return null;
		}
		return fos;
	}

	public void uploadSucceeded(SucceededEvent event) {
		CloseableHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(BaseInformation.serverURL + "images");
		InputStreamEntity ent = new InputStreamEntity(fis);
		post.setEntity(ent);
		try {
			HttpResponse response = httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			System.out.println("resp: \"" + responseString + "\"");
			if (response.getStatusLine().getStatusCode() == 201) {
				Notification.show("OK", "Pomyślnie dodano zdjecie!", Notification.Type.WARNING_MESSAGE);
			} else {
				System.out.println("RESPONSE: " + responseString);
				Notification.show("Error sending!", responseString, Notification.Type.ERROR_MESSAGE);
			}
		} catch (IOException e) {
			Notification.show("Error connection!", "Problem z połączeniem!", Notification.Type.ERROR_MESSAGE);
			System.err.println(e.getMessage());
			return;
		}
	}

	public HttpResponse uploadImage() {
		if (this.imageId > 0) {
			return this.updateImage();
		} else {
			CloseableHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(BaseInformation.serverURL + "images/");
			InputStreamEntity ent = new InputStreamEntity(fis);
			post.setEntity(ent);
			try {
				HttpResponse response = httpClient.execute(post);
				return response;
			} catch (IOException e) {
				System.err.println(e.getMessage());
				return null;
			}
		}

	}

	public HttpResponse updateImage() {
		if (this.imageId > 0) {
			CloseableHttpClient httpClient = new DefaultHttpClient();
			HttpPut put = new HttpPut(BaseInformation.serverURL + "images/" + this.imageId);
			InputStreamEntity ent = new InputStreamEntity(fis);
			put.setEntity(ent);
			try {
				HttpResponse response = httpClient.execute(put);
				return response;
			} catch (IOException e) {
				System.err.println(e.getMessage());
				return null;
			}
		} else {
			return this.uploadImage();
		}
	}

	public int getImageId() {
		return this.imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public InputStream returnInputStream() {
		return this.fis;
	}
}
