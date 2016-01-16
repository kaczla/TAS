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
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tasslegro.rest.MySQL.MySQL;
import tasslegro.rest.model.Users;

@Path("/users")
@Api(value = "users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

	MySQL database = null;

	public UsersResource() throws ClassNotFoundException, SQLException {
		this.database = new MySQL();
	}

	public void finalize() {
		this.database.finalize();
	}

	@GET
	@ApiOperation(value = "Get list all users.")
	public Response getUsers() throws ClassNotFoundException, SQLException {
		CacheControl cacheControl = new CacheControl();
		cacheControl.setMaxAge(10);
		cacheControl.setPrivate(false);
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).cacheControl(cacheControl)
					.entity("Problem with server! Please try again later!\n").build();
		}
		cacheControl.setMaxAge(120);
		List<Users> UserList = this.database.getUsers();
		if (UserList == null) {
			return Response.status(Response.Status.NOT_FOUND).cacheControl(cacheControl).entity("No content!").build();
		} else {
			return Response.status(Response.Status.OK).cacheControl(cacheControl).entity(UserList).build();
		}
	}

	@POST
	@ApiOperation(value = "Add user.")
	public Response addUser(Users user) throws ClassNotFoundException, SQLException {
		if (user == null) {
			return Response.status(Response.Status.CONFLICT).entity("Get user to add!").build();
		} else {
			if (!this.database.IsConnected()) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("Problem with server! Please try again later!\n").build();
			}
			if (user.getEmail().isEmpty()) {
				return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Email is required!\n").build();
			} else if (user.getLogin().isEmpty()) {
				return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Login is required!\n").build();
			} else if (user.getPass().isEmpty()) {
				return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Password is required!\n").build();
			} else if (this.database.checkExistUserByLogin(user.getLogin())) {
				return Response.status(Response.Status.CONFLICT)
						.entity("User with login \"" + user.getLogin() + "\" exists!\n").build();
			} else if (this.database.checkExistUserByEmail(user.getEmail())) {
				return Response.status(Response.Status.CONFLICT)
						.entity("User with email \"" + user.getEmail() + "\" exists!\n").build();
			} else {
				Users tmp = this.database.addUser(user);
				if (tmp == null) {
					return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
							.entity("Problem with server! Please try again later!\n").build();
				} else {
					return Response.status(Response.Status.CREATED).entity(tmp).build();
				}
			}
		}
	}

	@PUT
	@ApiOperation(value = "Update user.")
	public Response updateUser(Users user) {
		if (user == null) {
			return Response.status(Response.Status.CONFLICT).entity("Get user to add!").build();
		} else {
			if (!this.database.IsConnected()) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("Problem with server! Please try again later!\n").build();
			}
			if (user.getEmail().isEmpty()) {
				return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Email is required!\n").build();
			} else if (user.getLogin().isEmpty()) {
				return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Login is required!\n").build();
			} else if (user.getPass().isEmpty()) {
				return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Password is required!\n").build();
			} else if (this.database.checkExistUserByLogin(user.getLogin()) == false) {
				return Response.status(Response.Status.CONFLICT)
						.entity("User with login \"" + user.getLogin() + "\" exists!\n").build();
			} else if (this.database.checkExistUserByEmail(user.getEmail())
					&& this.database.checkExistUserByLoginEmail(user.getLogin(), user.getEmail()) == false) {
				return Response.status(Response.Status.CONFLICT)
						.entity("User with email \"" + user.getEmail() + "\" exists!\n").build();
			} else {
				Users tmp = this.database.updateUser(user);
				if (tmp == null) {
					return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
							.entity("Problem with server! Please try again later!\n").build();
				} else {
					return Response.status(Response.Status.CREATED).entity(tmp).build();
				}
			}
		}
	}

	@DELETE
	@ApiOperation(value = "Delete user.")
	public Response deleteUser(Users user) {
		if (user == null) {
			return Response.status(Response.Status.CONFLICT).entity("Get user to add!").build();
		} else {
			if (!this.database.IsConnected()) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("Problem with server! Please try again later!\n").build();
			}
			return null;
		}
	}

	@Path("/{login}")
	@GET
	@ApiOperation(value = "Get information about user with {login}.")
	public Response getUser(@PathParam("login") final String login) throws ClassNotFoundException, SQLException {
		CacheControl cacheControl = new CacheControl();
		cacheControl.setMaxAge(10);
		cacheControl.setPrivate(false);
		if (!this.database.IsConnected()) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).cacheControl(cacheControl)
					.entity("Problem with server! Please try again later!\n").build();
		}
		cacheControl.setMaxAge(120);
		Users User = this.database.getUserByLogin(login);
		if (User == null) {
			return Response.status(Response.Status.CONFLICT).cacheControl(cacheControl)
					.entity("User with login \"" + login + "\" not found!\n").build();
		} else {
			return Response.status(Response.Status.OK).cacheControl(cacheControl).entity(User).build();
		}
	}

	@Path("/pages/{page}")
	@GET
	@ApiOperation(value = "Get user page {page}.")
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
		List<Users> UserList = this.database.getUsersByPage(page);
		if (UserList == null) {
			return Response.status(Response.Status.NOT_FOUND).cacheControl(cacheControl).entity("No content!").build();
		} else {
			return Response.status(Response.Status.OK).cacheControl(cacheControl).entity(UserList).build();
		}
	}

	@Path("/ids/{id}")
	@POST
	@ApiOperation(value = "Get details about user {id}.")
	public Response getUserDetail(@PathParam("id") final int id, Users user)
			throws ClassNotFoundException, SQLException {
		if (user == null) {
			return Response.status(Response.Status.CONFLICT).entity("Get your id!").build();
		} else {
			if (!this.database.IsConnected()) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("Problem with server! Please try again later!\n").build();
			}
			if (this.database.checkAuthorization(user.getLogin(), user.getPass())) {
				if (id != user.getId()) {
					return Response.status(Response.Status.CONFLICT).entity("Wrong user id!").build();
				}
				Users User = this.database.getUserById(id);
				if (User == null) {
					return Response.status(Response.Status.CONFLICT).entity("User with id \"" + id + "\" not found!\n")
							.build();
				} else {
					return Response.status(Response.Status.OK).entity(User).build();
				}
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).entity("NOT_FOUND").build();
			}
		}
	}
}
