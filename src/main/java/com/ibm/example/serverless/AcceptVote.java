package com.ibm.example.serverless;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class AcceptVote {
	public static JsonObject main(JsonObject args) {		
		JsonPrimitive primative = args.getAsJsonPrimitive("selection");
		
		if (primative == null) {
			throw new RuntimeException("field 'selection' is missing");
		}
		
		String selection = new String(args.getAsJsonPrimitive("selection").getAsString());

		boolean found = false;
		for (Countries country : Countries.values()) {
			if (selection.equalsIgnoreCase(country.toString())) {
				found = true;
			}
		}
		
		if (!found) {
			throw new RuntimeException("Country '" + selection + "' is not found");
		}
		
		args.addProperty("selection", selection.toLowerCase());
		
		return args;
	}
}
