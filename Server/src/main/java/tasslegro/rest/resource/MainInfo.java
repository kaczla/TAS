package tasslegro.rest.resource;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import tasslegro.rest.MySQL.MySQL;

@Path("/")
@Produces("text/html; charset=utf-8")
public class MainInfo {

	MySQL database = null;

	public MainInfo() throws ClassNotFoundException, SQLException {
		this.database = new MySQL();
	}

	public void finaly() {
		this.database.finalize();
	}

	@GET
	public Response getMsg() {
		String msg = "<html><head><meta charset=\"UTF-8\"><title>REST API</title></head>";
		msg += "</body><b>Hello :)</b></br></br><h3>All path:</h3>";
		msg += "&emsp;<b>/users</b> - list all users</br>";
		msg += "&emsp;&emsp;<b>/{login}</b> - user <b>login</b></br>";
		msg += "&emsp;&emsp;<b>/ids/{id}</b> - details user <b>id</b></br>";
		msg += "&emsp;&emsp;<b>/pages/{id}</b> - page number <b>id</b></br>";
		msg += "</br>";

		msg += "&emsp;<b>/auctions</b> - list all auctions</br>";
		msg += "&emsp;&emsp;<b>/{id}</b> - auction <b>id</b></br>";
		msg += "&emsp;&emsp;<b>/offers</b> - offers for auction</br>";
		msg += "&emsp;&emsp;<b>/pages/{id}</b> - page number <b>id</b></br>";
		msg += "</br>";

		msg += "&emsp;<b>/images</b> - list all images</br>";
		msg += "&emsp;&emsp;<b>/{id}</b> - image by <b>id</b></br>";
		msg += "&emsp;&emsp;<b>/pages/{id}</b> - page number <b>id</b></br>";
		msg += "</br>";

		if (!this.database.IsConnected()) {
			msg += "<h2><font color=\"red\">Problem with server! Please try again later!</font></h2>";
		}

		msg += "</br></br>";
		msg += "<b>SOURCE CODE: <a href=\"https://github.com/kaczla/TAS\">github.com</a></b></br>";

		msg += "</body></html>";
		return Response.status(Response.Status.OK).entity(msg).build();
	}
}
