package tasslegro.base;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Http_Delete {

	@NotThreadSafe
	class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
		public static final String METHOD_NAME = "DELETE";

		public String getMethod() {
			return METHOD_NAME;
		}

		public HttpDeleteWithBody(final String uri) {
			super();
			setURI(URI.create(uri));
		}

		public HttpDeleteWithBody(final URI uri) {
			super();
			setURI(uri);
		}

		public HttpDeleteWithBody() {
			super();
		}
	}

	CloseableHttpClient httpClient = new DefaultHttpClient();
	HttpDeleteWithBody delete = null;
	HttpResponse response = null;

	public Http_Delete(String url, String entity) throws ClientProtocolException, IOException {
		this.delete = new HttpDeleteWithBody(url);
		this.delete.setHeader("Content-type", "application/json");
		StringEntity stringEntity = new StringEntity(entity.toString(), "UTF-8");
		this.delete.setEntity(stringEntity);
		this.response = this.httpClient.execute(this.delete);
	}

	public Http_Delete() {
	}

	public void setURL(String url, String entity) throws ClientProtocolException, IOException {
		this.delete = new HttpDeleteWithBody(url);
		this.delete.setHeader("Content-type", "application/json");
		StringEntity stringEntity = new StringEntity(entity.toString(), "UTF-8");
		this.delete.setEntity(stringEntity);
		this.response = this.httpClient.execute(this.delete);
	}

	public int getStatusCode() {
		if (response == null) {
			return 0;
		}
		return this.response.getStatusLine().getStatusCode();
	}

	public String getStrinResponse() throws ParseException, IOException {
		if (response == null) {
			return null;
		}
		HttpEntity entity = this.response.getEntity();
		return EntityUtils.toString(entity, "UTF-8");
	}
}
