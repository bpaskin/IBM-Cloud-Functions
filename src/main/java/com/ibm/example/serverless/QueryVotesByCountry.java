package com.ibm.example.serverless;

import java.util.Collections;
import java.util.Map;

import com.google.gson.JsonObject;
import com.ibm.cloud.cloudant.v1.Cloudant;
import com.ibm.cloud.cloudant.v1.model.FindResult;
import com.ibm.cloud.cloudant.v1.model.PostFindOptions;
import com.ibm.cloud.cloudant.v1.model.ServerInformation;
import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;

public class QueryVotesByCountry {
	
	public static JsonObject main(JsonObject args) {
		IamAuthenticator authenticator = new IamAuthenticator.Builder().apikey(args.getAsJsonPrimitive("iamApiKey").getAsString()).build();

		Cloudant service = new Cloudant(args.getAsJsonPrimitive("serviceName").getAsString(), authenticator);
		service.setServiceUrl(args.getAsJsonPrimitive("url").getAsString());

		HttpConfigOptions options =  new HttpConfigOptions.Builder().loggingLevel(HttpConfigOptions.LoggingLevel.BASIC).build();
		service.configureClient(options);
			
		Map<String, Object> selector = Collections.singletonMap("vote",args.getAsJsonPrimitive("country"));
		PostFindOptions pfo = new PostFindOptions.Builder().db(args.getAsJsonPrimitive("dbname").getAsString()).selector(selector).build();
		FindResult response = service.postFind(pfo).execute().getResult();
		
		JsonObject result = new JsonObject();
		result.addProperty("body", response.getDocs().size());
		return result;
	}

	public static void main(String[] args) {
		IamAuthenticator authenticator = new IamAuthenticator.Builder().apikey("g0Ki7iHk4eP4RTcE02UKa4k-4kItvJdnJ5GpoGhyYyrC").build();

		Cloudant service = new Cloudant("cloudant-serverless", authenticator);
		service.setServiceUrl("https://5de7608c-fce3-420d-b007-43f46e05d996-bluemix.cloudantnosqldb.appdomain.cloud");
		
		HttpConfigOptions options =  new HttpConfigOptions.Builder().loggingLevel(HttpConfigOptions.LoggingLevel.BASIC).build();
		service.configureClient(options);
			
		ServerInformation serverInfo = service.getServerInformation().execute().getResult();
		System.out.println("Cloudant version: " + serverInfo.getVersion());
		
		Map<String, Object> selector = Collections.singletonMap(
			    "vote","ireland");
		
		PostFindOptions pfo = new PostFindOptions.Builder().db("eurovision").selector(selector).build();
		FindResult response =
			    service.postFind(pfo).execute()
			        .getResult();
		System.out.println(response.getDocs().size());
	}

}
