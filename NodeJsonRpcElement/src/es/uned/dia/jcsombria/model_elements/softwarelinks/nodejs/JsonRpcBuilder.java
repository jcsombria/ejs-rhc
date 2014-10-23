/**
 * JSON-RPC Builder
 * author: Jesús Chacón <jcsombria@gmail.com>
 *
 * Copyright (C) 2013 Jesús Chacón
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.uned.dia.jcsombria.model_elements.softwarelinks.nodejs;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


/**
 * JSON-RPC Helper class 
 */
public class JsonRpcBuilder {
	/**
	 * Build a JSON-RPC request object
	 *
	 * @param {string} method - The method to call
	 * @param {array|object} params - The params either by position (array) or 
	 * 								  by name (object).
	 * @param {string|integer} id - The id of the call
	 */
	public static JsonObject request(String method, Object[] params, String id) {		
		JsonObjectBuilder request = Json.createObjectBuilder()
			.add("jsonrpc", "2.0")
			.add("method", method);		
		if(params != null) {
			JsonArrayBuilder paramsArray = Json.createArrayBuilder();
			for(Object item : params) {
				paramsArray.add(item.toString());
			}
			request.add("params", paramsArray);
		}
		if(id!=null) request.add("id", id);
		return request.build();
	}

	/**
	 * Build a JSON-RPC response object
	 *
	 * @param {object} result - The result to return
	 * @param {string|integer} id - The id of the call
	 */
	public static JsonObject response(Object result, String id) {
		JsonObject model = Json.createObjectBuilder()
		   .add("jsonrpc", "2.0")
		   .add("result", result.toString())
		   .add("id", id)
		   .build();	
 		return model;
	}
	
	
	/**
	 * Build a JSON-RPC error response object
	 *
	 * @param {object} error - The error object
	 * @param {string|integer} id - The id of the call
	 */
	public static JsonObject responseWithError(Object error, String id) {
		JsonObject model = Json.createObjectBuilder()
		   .add("jsonrpc", "2.0")
		   .add("error", error.toString())
		   .add("id", id)
		   .build();
 		return model;
	}

	/**
	 * Build a JSON-RPC error object
	 *
	 * @param {integer} code - The result to return
	 * @param {string} message - The error description message 
	 * @param {object} data - Additional data
	 */
	public static JsonObject error(int code, String message, String data) {
		JsonObject model = Json.createObjectBuilder()
		   .add("jsonrpc", "2.0")
		   .add("message", message)
		   .add("data", data)
		   .build();
 		return model;
	}
	
}