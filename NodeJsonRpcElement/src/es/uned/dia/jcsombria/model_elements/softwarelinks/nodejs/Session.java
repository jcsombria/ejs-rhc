/**
 * Session
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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;

import javax.json.JsonValue;

import es.uned.jcsombria.model_elements.softwarelinks.protocol.LowLevelProtocol;

/**
 * Class to interact with a JIL Server
 * @author Jesús Chacón <jcsombria@gmail.com> 
 */
public class Session implements LowLevelProtocol {	
	// The XmlRpcClient object
	private Transport transport;
	private RpcClient client;	
	private Metadata metadata = new Metadata();

	private HashMap<String, Object> results = new HashMap<String, Object>();
	private Queue<Object> queue = new LinkedList<Object>();

	// The names of the methods
    private static final String METHODNAME_CONNECT = "connect";
    private static final String METHODNAME_AUTHENTICATE = "authenticate";
    private static final String METHODNAME_OPEN = "open"; 
    private static final String METHODNAME_RUN = "run"; 
    private static final String METHODNAME_SETVALUE = "setValue";
    private static final String METHODNAME_GETVALUE = "getValue";
    private static final String METHODNAME_SYNC = "sync"; 
    private static final String METHODNAME_STOP = "stop"; 
    private static final String METHODNAME_CLOSE = "close"; 
    private static final String METHODNAME_DISCONNECT = "disconnect"; 

    private enum State { IDLE, CONNECTED, OPENED, RUNNING };
    private State state = State.IDLE;
    
    /**
     * Create a new NodeJs session 
     * @param url The url of the server
     * @throws Exception 
     */
	public Session(Transport transport) throws Exception {
		this.transport = transport;
		client = new JsonRpcClient(transport);
	}
	
    /**
     * Create a new NodeJs session 
     * @param url The url of the server
     * @throws Exception 
     */
	public Session(String serverURL) throws Exception {
		this.transport = new HttpTransport(serverURL);
		client = new JsonRpcClient(transport);
	}

	//	public Session(Transport url) {
//    	setServerAddress(url);
//	}
	
	
//    /**
//     * Create a new Labview Object 
//     * @param url The url of the server
//     */
//	public Session(URL url) {
//    	setServerAddress(url);
//	}
//	
//    /**
//     * Set the <i>url</i> of the server 
//     * @param url The url of the server
//     */
//	public void setServerAddress(String url) {
//    	try {
//    		setServerAddress(new URL(url));
//    	} catch(MalformedURLException e) {
//    		System.out.println(e.getMessage());
//    	}
//	}
//
//    /**
//     * Set the <i>url</i> of the server 
//     * @param url The url of the server
//     */
//	public void setServerAddress(URL url) {
//		/* XmlRpcClient Configuration */
//	    XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
//		config.setServerURL(url);
//		config.setConnectionTimeout(10000);
//		config.setEnabledForExtensions(true);
////	    config.setGzipRequesting(true);
//		/* Create the XmlRpcClient */    
//	    client = new XmlRpcClient();
//	    client.setTransportFactory(new XmlRpcSunHttpTransportFactory(client));
//	    client.setConfig(config);
//	}
//
//    /**
//     * Enable or disable the Gzip compressing of the data   
//     * @param	url	The url of the server
//     * @return		<i>true</i> if the property has been correctly changed, false otherwise.	
//     */
//	public boolean setGzipCompressing(boolean enabled) {
//		XmlRpcClientConfigImpl config = (XmlRpcClientConfigImpl)client.getConfig();
//		if(config == null) {
//			return false;
//		}
//	    config.setEnabledForExtensions(enabled);
//		config.setGzipCompressing(enabled);		
//		return true;
//	}
	
    public boolean connect() {
    	if(state != State.IDLE) return false;
		try {
			Object[] params = new Object[]{};
			Object result = client.execute(METHODNAME_CONNECT, params);	
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}	
		return true;
    }
    

    public boolean open() {
    	return false;
    }
    
    public boolean open(String name) {
		try {
			Object[] params = new Object[]{name};
			Object result = client.execute(METHODNAME_OPEN, params);
	    	javax.json.JsonObject response = (javax.json.JsonObject)result;
			if(response != null) {
				metadata = Metadata.parseJsonObject(response);
			} else {
				metadata = new Metadata();
			}
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
		return true;
    }
    
    public boolean run() {
		try {
			Object[] params = new Object[]{};
			client.execute(METHODNAME_RUN, params);
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
		return true;
    }
    
    public boolean stop() {
		try {
			Object[] params = new Object[]{};
			client.execute(METHODNAME_STOP, params);
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
		return true;
    }
    
    public boolean close() {
		try {
			Object[] params = new Object[]{};
			client.execute(METHODNAME_CLOSE, params);
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
		return true;
    }
    
    public boolean sync() {
		try {			
//			Object[] params = new Object[]{queue.toArray()};
//			queue.clear();
//			Object[] result = (Object[])client.execute(METHODNAME_SYNCVI, params);
			Object[] result = client.sendBatch();
			
//			for(Object o : result) {
//				HashMap<String, Object> variable =  (HashMap<String, Object>) o;
//				if(variable != null) {
//					String var = (String)variable.get("name");
//					results.put(var, variable.get("value"));
//				}
//			}
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
		return true;
    }   
    
    /**
     * Disconnect from the server
     */
    public boolean disconnect() {
//		try {
//			Object[] params = new Object[]{};
//			String result = (String)client.execute(METHODNAME_DISCONNECT, params);
//		} catch (XmlRpcException e) {
//			System.err.println(e);
//			return false;
//		}
		return true;
    }

    public Object getVariable(String name) {
    	Object result = null;
		try {
			Object[] params = new Object[]{name};
			result = client.execute(METHODNAME_GETVALUE, params);
		} catch (Exception e) {
			System.err.println(e);
		}
		return result;
    }
  
    public void setVariable(String name, Object value) {
    	Object result = null;
		try {
			Object[] params = new Object[]{name, value};
			result = client.execute(METHODNAME_SETVALUE, params);
		} catch (Exception e) {
			return;
		}
    }

    public Object getVariableResult(String name) {
    	return results.get(name);
    }
    
    public boolean getVariableLater(String name) {
		try {
			Object[] params = new Object[]{name};
			client.execute(METHODNAME_GETVALUE, params);
		} catch (Exception e) {
			return false;
		}
//    	HashMap<String, Object> request = getHashMapForGetVariable(name);
//    	if(request == null) {
//    		return false;
//    	}
//		queue.add(request);
		return true;
    }
    
    public boolean setVariableLater(String name, Object value) {
		try {
			Object[] params = new Object[]{name, value};
			client.executeLater(METHODNAME_SETVALUE, params);
		} catch (Exception e) {
			return false;
		}
//		HashMap<String, Object> request = getHashMapForSetVariable(name, value);
//		if(request == null) {
//			return false;
//		}
//		queue.add(request);
		return true;
    }

    public List<String> getMethods() {
    	return metadata.getMethods();
    }
    
    public List<String> getReadable() {
    	return metadata.getReadable();
    }
    
    public List<String> getWritable() {
    	return metadata.getWritable();
    }
    
    public boolean isConnected() {
    	return (state != State.IDLE);
    }

    public boolean isRunning() {
    	return (state == State.RUNNING);
    }
    
    public boolean isOpened() {
    	return (state == State.OPENED) || (state == State.RUNNING);
    }

	@Override
	public boolean getBoolean(String name) {
		try {
			javax.json.JsonValue value = (javax.json.JsonValue)getVariable(name);
			if(value.getValueType() == JsonValue.ValueType.FALSE) {
				return false;
			}
			if(value.getValueType() == JsonValue.ValueType.TRUE) {
				return true;			
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public int getInt(String name) {
		try {
			javax.json.JsonNumber number = (javax.json.JsonNumber)getVariable(name);
			return number.intValue();
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public float getFloat(String name) {
		try {
			javax.json.JsonNumber number = (javax.json.JsonNumber)getVariable(name);
			return (float)number.doubleValue();
		} catch (Exception e) {
			return 0.0f;
		}
	}

	@Override
	public double getDouble(String name) {
		try {
			javax.json.JsonNumber number = (javax.json.JsonNumber)getVariable(name);
			return (double)number.doubleValue();
		} catch (Exception e) {
			return 0.0;
		}
	}

	@Override
	public String getString(String name) {
		try {
			javax.json.JsonString string = (javax.json.JsonString)getVariable(name);
			return (String)string.getString();
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public void setValue(String name, boolean value) {
		setVariable(name, value);
	}

	@Override
	public void setValue(String name, int value) {
		setVariable(name, value);
	}

	@Override
	public void setValue(String name, float value) {
		setVariable(name, value);
	}

	@Override
	public void setValue(String name, double value) {
		setVariable(name, value);
	}

	@Override
	public void setValue(String name, String value) {
		setVariable(name, value);
	}    
}


class Metadata {
	private static final String METHODS = "methods"; 
	private static final String READABLE = "readable"; 
	private static final String WRITABLE = "writable"; 
	
	private List<String> methods;
	private List<String> readable;
	private List<String> writable;

	public static Metadata parseJsonObject(javax.json.JsonObject jsonObject) {
		List<String> methods = getValues(jsonObject.getJsonArray(METHODS));
		List<String> readable = getValues(jsonObject.getJsonArray(READABLE));
		List<String> writable = getValues(jsonObject.getJsonArray(WRITABLE));
		return new Metadata(methods, readable, writable);
	}

	public Metadata() {
		this.methods = new ArrayList<String>();
		this.readable = new ArrayList<String>();
		this.writable = new ArrayList<String>();
	}

	public Metadata(List<String> methods, List<String> readable, List<String> writable) {
		this.methods = methods;
		this.readable = readable;
		this.writable = writable;
	}	

    private static List<String> getValues(javax.json.JsonArray variables) {
    	List<javax.json.JsonString> namesToParse = variables.getValuesAs(javax.json.JsonString.class);
    	List<String> namesToReturn = new ArrayList<String>();    	
		for(javax.json.JsonString name : namesToParse) {
			namesToReturn.add(name.getString());
		}
		return (List<String>)namesToReturn;
    }
    
    public List<String> getMethods() {
    	return this.methods;
    }
    
    public List<String> getReadable() {
    	return this.readable;
    }
    
    public List<String> getWritable() {
    	return this.writable;
    }
    
    
}