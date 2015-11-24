package tasslegro.rest.resource;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tasslegro.rest.MySQL.MySQL;
import tasslegro.rest.model.Auctions;

@Path("/auctions")
@Api(value = "auctions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuctionsResource {

	MySQL database = null;

	public AuctionsResource() throws ClassNotFoundException, SQLException {
		this.database = new MySQL();
	}

	public void finaly() {
		this.database.finalize();
	}

	@GET
	@ApiOperation(value = "Get list all auctions.")
	public Response getAuctions() throws ClassNotFoundException, SQLException {
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Problem with server! Please try again later!\n").build();
		}
		List<Auctions> AuctionsList = this.database.getAuctions();
		if (AuctionsList == null) {
			return Response.status(Response.Status.NO_CONTENT).entity("No content!").build();
		} else {
			return Response.status(Response.Status.OK).entity(AuctionsList).build();
		}
	}

	@POST
	@ApiOperation(value = "Add auction.")
	public Response addAuction(Auctions auction) throws ClassNotFoundException, SQLException {
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Problem with server! Please try again later!\n").build();
		}
		if (auction.getTitle().isEmpty()) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Title is required!\n").build();
		} else if (auction.getDescription().isEmpty()) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Description is required!\n").build();
		} else if (auction.getPrice() <= 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Price must be greater than zero!\n").build();
		} else if (auction.getUser_ID() == 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("User ID is required!\n").build();
		} else if (this.database.checkExistUserById(auction.getUser_ID()) == false) {
			return Response.status(Response.Status.CONFLICT)
					.entity("User with id \"" + auction.getUser_ID() + "\" not found!\n").build();
		} else {
			Auctions tmp = this.database.addAuction(auction);
			if (tmp == null) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("Problem with server! Please try again later!\n").build();
			} else {
				return Response.status(Response.Status.CREATED).entity(tmp).build();
			}
		}
	}

	@PUT
	@ApiOperation(value = "Update auction.")
	public Response updateAuction(Auctions auction) {
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Problem with server! Please try again later!\n").build();
		}
		return null;
	}

	@DELETE
	@ApiOperation(value = "Delete auction.")
	public Response deleteAuction(Auctions auction) {
		return Response.status(Response.Status.NOT_IMPLEMENTED).entity("DELETE IS NOT IMPLEMENTED!").build();
	}

	@Path("/{id}")
	@GET
	@ApiOperation(value = "Get user {id} auctions.")
	public Response getUser(@PathParam("id") final String id) throws ClassNotFoundException, SQLException {
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Problem with server! Please try again later!\n").build();
		}
		return null;
	}
}