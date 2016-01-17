package tasslegro.rest.resource;

import java.io.InputStream;
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
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tasslegro.rest.MySQL.MySQL;
import tasslegro.rest.model.Images;

@Path("/images")
@Api(value = "Zdjęcia")
@Consumes("image/png")
@Produces("image/png")
public class ImagesResource {

	MySQL database = null;

	public ImagesResource() throws ClassNotFoundException, SQLException {
		this.database = new MySQL();
	}

	public void finalize() {
		this.database.finalize();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@ApiOperation(value = "Zwraca wszystkie zdjęcia")
	public Response getImages() throws SQLException {
		CacheControl cacheControl = new CacheControl();
		cacheControl.setMaxAge(10);
		cacheControl.setPrivate(false);
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).cacheControl(cacheControl)
					.entity("Problem with server! Please try again later!\n").build();
		}
		cacheControl.setMaxAge(120);
		List<Images> ImageList = this.database.getImages();
		if (ImageList == null) {
			return Response.status(Response.Status.NOT_FOUND).cacheControl(cacheControl).entity("No content!").build();
		} else {
			return Response.status(Response.Status.OK).cacheControl(cacheControl).entity(ImageList).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@ApiOperation(value = "Dodaje zdjęcie")
	public Response addImage(InputStream image) throws SQLException {
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Problem with server! Please try again later!\n").build();
		}
		Images tmp = new Images();
		tmp.setImage(image);
		tmp = this.database.addImage(tmp);
		if (tmp == null) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Problem with server! Please try again later!\n").build();
		} else {
			tmp.setImage(null);
			return Response.status(Response.Status.CREATED).entity(tmp).build();
		}
	}

	@Path("/{id}")
	@GET
	@ApiOperation(value = "Zwraca zdjęcie z identyfikatorem {id}")
	public Response getImage(@PathParam("id") final int id) throws ClassNotFoundException, SQLException {
		CacheControl cacheControl = new CacheControl();
		cacheControl.setMaxAge(10);
		cacheControl.setPrivate(false);
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).cacheControl(cacheControl)
					.entity("Problem with server! Please try again later!\n").build();
		}
		cacheControl.setMaxAge(360);
		Images image = this.database.getImageById(id);
		if (image == null) {
			return Response.status(Response.Status.CONFLICT).cacheControl(cacheControl)
					.entity("Image with id \"" + id + "\" not found!\n").build();
		} else {
			return Response.status(Response.Status.FOUND).cacheControl(cacheControl).entity(image.getImage()).build();
		}
	}

	@Path("/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@ApiOperation(value = "Usuwa zdjęcie z identyfikatorem {id}")
	public Response DeleteImage(@PathParam("id") final int id) throws SQLException {
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Problem with server! Please try again later!\n").build();
		}
		if (id <= 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("ID is required!\n").build();
		} else if (!this.database.checkExistImageById(id)) {
			return Response.status(Response.Status.CONFLICT).entity("Image with id \"" + id + "\" not found!!\n")
					.build();
		} else {
			Images tmp = this.database.deleteImageById(id);
			if (tmp == null) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("Problem with server! Please try again later!\n").build();
			} else {
				return Response.status(Response.Status.OK).entity(tmp).build();
			}
		}
	}

	@Path("/{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@ApiOperation(value = "Aktualizuje zdjęcie z identyfikatorem {id}")
	public Response UpdateImage(@PathParam("id") final int id, InputStream image) throws SQLException {
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Problem with server! Please try again later!\n").build();
		}
		if (id <= 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("ID is required!\n").build();
		} else if (!this.database.checkExistImageById(id)) {
			return Response.status(Response.Status.CONFLICT).entity("Image with id \"" + id + "\" not found!!\n")
					.build();
		} else {
			Images tmp = new Images();
			tmp.setImage(image);
			tmp = this.database.updateImage(id, tmp);
			if (tmp == null) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("Problem with server! Please try again later!\n").build();
			} else {
				tmp.setImage(null);
				return Response.status(Response.Status.CREATED).entity(tmp).build();
			}
		}
	}

	@Path("/pages/{page}")
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@ApiOperation(value = "Zwraca stronę o numerze {page} z zdjęciami")
	public Response getUser(@PathParam("page") final int page) throws ClassNotFoundException, SQLException {
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
		List<Images> ImageList = this.database.getImagesByPage(page);
		if (ImageList == null) {
			return Response.status(Response.Status.NOT_FOUND).cacheControl(cacheControl).entity("No content!").build();
		} else {
			return Response.status(Response.Status.OK).cacheControl(cacheControl).entity(ImageList).build();
		}
	}
}
