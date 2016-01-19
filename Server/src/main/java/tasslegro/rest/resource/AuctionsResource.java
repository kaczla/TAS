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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tasslegro.rest.MySQL.MySQL;
import tasslegro.rest.model.Auctions;
import tasslegro.rest.model.AuctionsAuth;

@Path("/auctions")
@Api(value = "Aukcje")
@Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class AuctionsResource {
	MySQL database = null;

	public AuctionsResource() throws ClassNotFoundException, SQLException {
		this.database = new MySQL();
	}

	public void finalize() {
		this.database.finalize();
	}

	@GET
	@ApiOperation(value = "Zwraca wszystkie aukcję")
	public Response getAuctions(@QueryParam("title") String title, @QueryParam("description") String description,
			@QueryParam("price") float price, @QueryParam("userId") int userId)
					throws ClassNotFoundException, SQLException {
		CacheControl cacheControl = new CacheControl();
		cacheControl.setMaxAge(10);
		cacheControl.setPrivate(false);
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).cacheControl(cacheControl)
					.entity("Problem with server! Please try again later!\n").build();
		}
		cacheControl.setMaxAge(120);
		List<Auctions> AuctionsList = null;
		if ((title == null && description == null) || price < 0 || userId < 0) {
			AuctionsList = this.database.getAuctions();
		} else {
			AuctionsList = this.database.getAuctionBySearch(title, description, price, userId);
		}
		if (AuctionsList == null) {
			return Response.status(Response.Status.NOT_FOUND).cacheControl(cacheControl).entity("No content!").build();
		} else {
			return Response.status(Response.Status.OK).cacheControl(cacheControl).entity(AuctionsList).build();
		}
	}

	@POST
	@ApiOperation(value = "Dodanie aukcji")
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
		} else if (auction.getUserId() == 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("User ID is required!\n").build();
		} else if (this.database.checkExistUserById(auction.getUserId()) == false) {
			return Response.status(Response.Status.CONFLICT)
					.entity("User with id \"" + auction.getUserId() + "\" not found!\n").build();
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
	@ApiOperation(value = "Aktualizacja aukcji")
	public Response updateAuction(AuctionsAuth auction) {
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
		} else if (auction.getUserId() == 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("User ID is required!\n").build();
		} else if (this.database.checkExistUserById(auction.getUserId()) == false) {
			return Response.status(Response.Status.CONFLICT)
					.entity("User with id \"" + auction.getUserId() + "\" not found!\n").build();
		} else if (this.database.checkAuthorization(auction.getLogin(), auction.getPass()) == false) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("NOT_FOUND").build();
		} else if (this.database.checkExistAuctionByIds(auction.getAucitonId(), auction.getUserId()) == false) {
			return Response.status(Response.Status.CONFLICT).entity("Wrong data!\n").build();
		} else {
			Auctions tmp = this.database.updateAuction(auction);
			if (tmp == null) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("Problem with server! Please try again later!\n").build();
			} else {
				return Response.status(Response.Status.CREATED).entity(tmp).build();
			}
		}
	}

	@DELETE
	@ApiOperation(value = "Usunięcie aukcji")
	public Response deleteAuction(AuctionsAuth auction) {
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Problem with server! Please try again later!\n").build();
		}
		if (auction.getAucitonId() <= 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Auction ID is required!\n").build();
		} else if (auction.getUserId() <= 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("User ID is required!\n").build();
		} else if (auction.getImageId() < 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Image ID is required!\n").build();
		} else if (this.database.checkExistUserById(auction.getUserId()) == false) {
			return Response.status(Response.Status.CONFLICT)
					.entity("User with id \"" + auction.getUserId() + "\" not found!\n").build();
		} else if (this.database.checkAuthorization(auction.getLogin(), auction.getPass()) == false) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("NOT_FOUND").build();
		} else if (this.database.checkExistAuctionByIds(auction.getAucitonId(), auction.getUserId()) == false) {
			return Response.status(Response.Status.CONFLICT).entity("Wrong data!\n").build();
		} else {
			if (auction.getImageId() > 0) {
				this.database.deleteImageById(auction.getImageId());
			}
			Auctions tmp = this.database.deleteAuctionById(auction);
			if (tmp == null) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("Problem with server! Please try again later!\n").build();
			} else {
				return Response.status(Response.Status.CREATED).entity(tmp).build();
			}
		}
	}

	@Path("/{id}")
	@GET
	@ApiOperation(value = "Zwraca aukcję z identyfikatorem {id}")
	public Response getAuction(@PathParam("id") final int id) {
		CacheControl cacheControl = new CacheControl();
		cacheControl.setMaxAge(10);
		cacheControl.setPrivate(false);
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).cacheControl(cacheControl)
					.entity("Problem with server! Please try again later!\n").build();
		}
		cacheControl.setMaxAge(120);
		Auctions Auction = this.database.getAuctionById(id);
		if (Auction == null) {
			return Response.status(Response.Status.CONFLICT).cacheControl(cacheControl)
					.entity("Auction with id \"" + id + "\" not found!\n").build();
		} else {
			return Response.status(Response.Status.OK).cacheControl(cacheControl).entity(Auction).build();
		}
	}

	@Path("/pages/{page}")
	@GET
	@ApiOperation(value = "Zwraca stronę o numerze {page} z aukcjami")
	public Response getAuctions(@PathParam("page") final int page) throws ClassNotFoundException, SQLException {
		CacheControl cacheControl = new CacheControl();
		cacheControl.setMaxAge(10);
		cacheControl.setPrivate(false);
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).cacheControl(cacheControl)
					.entity("Problem with server! Please try again later!\n").build();
		}
		if (page < 1) {
			return Response.status(Response.Status.CONFLICT).cacheControl(cacheControl)
					.entity("Page " + page + " doesn't exist!").build();
		}
		cacheControl.setMaxAge(120);
		List<Auctions> AuctionsList = this.database.getAuctionsByPage(page);
		if (AuctionsList == null) {
			return Response.status(Response.Status.NOT_FOUND).cacheControl(cacheControl).entity("No content!").build();
		} else {
			return Response.status(Response.Status.OK).cacheControl(cacheControl).entity(AuctionsList).build();
		}
	}

	@Path("/offers")
	@PUT
	@ApiOperation(value = "Aktualizuje ofertę licytacji")
	public Response updateOffers(AuctionsAuth auction) {
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Problem with server! Please try again later!\n").build();
		}
		if (auction.getAucitonId() <= 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Auction ID is required!\n").build();
		} else if (auction.getBindId() <= 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("User ID is required!\n").build();
		} else if (this.database.checkExistUserById(auction.getBindId()) == false) {
			return Response.status(Response.Status.CONFLICT)
					.entity("User with id \"" + auction.getBindId() + "\" not found!\n").build();
		} else if (this.database.checkAuthorization(auction.getLogin(), auction.getPass()) == false) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("NOT_FOUND").build();
		} else if (this.database.checkAuctionOfferPrice(auction) == false) {
			return Response.status(Response.Status.CONFLICT).entity("Offer price must by greater!\n").build();
		} else {
			Auctions tmp = this.database.updateAuctionOffer(auction);
			if (tmp == null) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("Problem with server! Please try again later!\n").build();
			} else {
				return Response.status(Response.Status.CREATED).entity(tmp).build();
			}
		}
	}
}
