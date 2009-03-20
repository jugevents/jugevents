/**
 * 
 */
package it.jugpadova.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * This class it's just a prototype of a custom implementation of the class {@link CommonsMultipartResolver}
 * We want to have the possibility to define more maxUploadSize properties depending on the
 * type of the file to upload.
 * @author Enrico Giurin
 *
 */
public class CustomCommonsMultipartResolver extends CommonsMultipartResolver {
	 

	@Override
	public MultipartParsingResult parseRequest(HttpServletRequest req)
			throws MultipartException {
		//TODO to complete
		return super.parseRequest(req);
	}

}
