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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tasslegro.rest.MySQL.MySQL;
import tasslegro.rest.model.Images;

@Path("/images")
@Api(value = "images")
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
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get all images.")
	public Response getImages() throws SQLException {
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Problem with server! Please try again later!\n").build();
		}
		List<Images> ImageList = this.database.getImagesList();
		if (ImageList == null) {
			return Response.status(Response.Status.NO_CONTENT).entity("No content!").build();
		} else {
			return Response.status(Response.Status.OK).entity(ImageList).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Add image.")
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

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update image.")
	public Response UpdateImage() throws SQLException {
		return Response.status(Response.Status.NOT_IMPLEMENTED).entity("PUT IS NOT IMPLEMENTED!").build();
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete image with {id}.")
	public Response DeleteImage() throws SQLException {
		return Response.status(Response.Status.NOT_IMPLEMENTED).entity("DELETE IS NOT IMPLEMENTED!").build();
	}

	@Path("/{id}")
	@GET
	@ApiOperation(value = "Get image with {id}.")
	public Response getImage(@PathParam("id") final String id) throws ClassNotFoundException, SQLException {
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Problem with server! Please try again later!\n").build();
		}
		Images image = this.database.getImageById(id);
		if (image == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Image with id \"" + id + "\" not found!\n")
					.build();
		} else {
			return Response.status(Response.Status.FOUND).entity(image.getImage()).build();
		}
	}

	@Path("/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete image with {id}.")
	public Response DeleteImage(@PathParam("id") final String id) throws SQLException {
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Problem with server! Please try again later!\n").build();
		}
		int idInt = Integer.parseInt(id);
		if (idInt <= 0) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("ID is required!\n").build();
		} else if (!this.database.checkExistImageById(idInt)) {
			return Response.status(Response.Status.CONFLICT).entity("Image with id \"" + idInt + "\" not found!!\n")
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
}
