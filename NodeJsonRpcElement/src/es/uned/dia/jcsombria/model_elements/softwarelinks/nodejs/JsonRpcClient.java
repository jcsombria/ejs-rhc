/**
 * JSON-RPC Client
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;

public class JsonRpcClient implements RpcClient {
	Transport transport = null;
	LinkedList<String> queue = new LinkedList<String>();

	public JsonRpcClient(Transport transport) throws Exception {
		if(transport == null) throw new Exception("Invalid Transport");
		this.transport = transport;
	}
	
	/**
	 * Execute a JSON-RPC method call
	 *
	 * @param {string} method - The method to call
	 * @param {array|object} params - The params either by position (array) or 
	 * 								  by name (object).
	 */
	@Override
	public Object execute(String method, Object[] params) {
		Object response = null;
		try {
			JsonObject request = JsonRpcBuilder.request(method, params, UUID.randomUUID().toString()); 
			response = parseResponse((String)transport.send(request.toString()))[0];
		} catch(Exception e) {
			System.err.println(e);
		}
		return response;
	}

//	@Override
	/**
	 * Add a JSON-RPC method call to the queue
	 *
	 * @param {string} method - The method to call
	 * @param {array|object} params - The params either by position (array) or 
	 * 								  by name (object).
	 */
	public void executeLater(String method, Object[] params) {
		String id = UUID.randomUUID().toString();
		JsonObject request = JsonRpcBuilder.request(method, params, id); 
		queue.add(request.toString());
//		while(queue.containsKey(id)) {
//			id = UUID.randomUUID().toString();
//		}
//		queue.put(id, request(method, params, id));
	}
	
	/**
	 * Add a JSON-RPC method call to the queue
	 *
	 * @param {string} method - The method to call
	 * @param {array|object} params - The params either by position (array) or 
	 * 								  by name (object).
	 */
	public Object[] sendBatch() {
		StringBuilder request = new StringBuilder();
		Iterator<String> iter = queue.iterator();
//		Iterator<String> iter = queue.values().iterator();

		request.append("[");
		if(iter.hasNext()){
			request.append(iter.next());
		}
		while(iter.hasNext()) {
			request.append(",");
			request.append(iter.next());
		}
		request.append("]");

		
		Object[] response = null;
		try {
			response = parseResponse((String)transport.send(request.toString()));
		} catch(Exception e) {
			System.err.println(e);
		}
		return response;
	}

	/**
	 * Send method calls in batch list
	 *
	 * @param {string} resString - The response in JSON-RPC format
	 */
	private Object[] parseResponse(String responseString) {
		JsonStructure response = null;
		Object[] result = null;
		try {
            InputStream stream = new ByteArrayInputStream(responseString.getBytes("UTF-8"));
            JsonReader reader = Json.createReader(stream);
            try {
            	response = reader.read();
            	switch(response.getValueType()) {
                case OBJECT:
                   JsonObject object = (JsonObject) response;
                   result = new Object[]{object.get("result")};
                   break;
                case ARRAY:
                   JsonArray array = (JsonArray) response;
                   result = new String[array.size()];
                   Iterator<JsonValue> iter = array.iterator();
                   LinkedList<Object> list = new LinkedList<Object>();
                   while(iter.hasNext()) {
                	   object = (JsonObject)iter.next();
                	   list.add(object.get("result"));
                   }
                   result = list.toArray();
                   break;
                default:
                   System.out.println("ups..."+response.getValueType());
                   break;
             }
            } catch(JsonException e) {
            	
            }
            //System.out.println(response.keySet());
//			if(this.sent[response.id]) {
//				var method = this.sent[response.id].method;
//				this.response[method] = response;
//			}
		} catch(Exception e) {
			System.out.println(e);
		}
		
		return result;
	}
}