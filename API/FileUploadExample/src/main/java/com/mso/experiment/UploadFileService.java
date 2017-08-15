package com.mso.experiment;

import java.io.InputStream;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/upload")
public class UploadFileService {

	@POST
	@Path("/multipart")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		String output = "File "+fileDetail.getFileName()+" uploaded ! ";
		System.out.println("chegou multipart: "+ new Date().toString());

		return Response.status(200).entity(output).build();

	}
	
	@POST
	@Path("/encoded")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadFile(FileDTO fileEncoded) {

		String output = "File "+fileEncoded.getName()+" uploaded ! ";
		System.out.println("chegou base64: "+ new Date().toString());

		return Response.status(200).entity(output).build();

	}

}
