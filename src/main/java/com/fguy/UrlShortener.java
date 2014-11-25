package com.fguy;

import java.io.InputStream;
import java.net.URI;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import redis.clients.jedis.Jedis;

import com.fiftyonred.mock_jedis.MockJedis;

/**
 * HTTP REST-based URL shortener service in JAX-RS.
 * 
 * @author fguy
 *
 */
@Path("/")
@Singleton
public class UrlShortener {
	Jedis redis = new MockJedis("prototype");

	@GET
	@Produces(MediaType.TEXT_HTML)
	public InputStream form() {
		return getClass().getResourceAsStream(
				String.format("/templates/form.html"));
	}

	/**
	 * You can submit a URL and get back a "shortened" version. E.g. submit
	 * "http://www.apple.com"and get back "http://a.pl/abcdef".
	 * 
	 * This method returns only the id part from the shorten url, it is "abcdef"
	 * in "http://a.pl/abcdef" case.
	 * 
	 * @param url
	 *            the original URL.
	 * @return id part of the shortened url.
	 */
	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	public String shorten(String url) {
		// may need url canonicalization here
		String lookupKey = String.format("lookup-%s", url);
		// find existing with lookupKey
		String result = redis.get(lookupKey);
		if (result != null) {
			return result;
		}
		String id = Base62.encode(redis.incr("id"));
		redis.mset(new String[] { String.format("url-%s", id), url, lookupKey,
				id });

		// return Response.created(new URI(String.format("http://a.pl/%s",
		// id)));
		return id;
	}

	/**
	 * You can resolve a shortened URL to get back the original URL. E.g. submit
	 * "http://a.pl/abcdef" and get back "http://www.apple.com".
	 * 
	 * @param id
	 *            id part of the shortened url.
	 * @return the original URL.
	 */
	@GET
	@Path("{id}")
	public Response resolve(@PathParam("id") String id) {
		String lookupKey = String.format("url-%s", id);
		String result = redis.get(lookupKey);
		if (result != null) {
			return Response.status(Status.MOVED_PERMANENTLY)
					.location(URI.create(result)).build();
		}
		throw new WebApplicationException(Status.NOT_FOUND);
	}
}
