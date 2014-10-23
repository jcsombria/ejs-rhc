/**
 * LowLevelProtocol
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
package es.uned.jcsombria.model_elements.softwarelinks.protocol;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
* Interface to implement the low-level communication protocol
*/
public interface LowLevelProtocol {
	// Connection methods
//	public boolean setServerAddress(String url);
//	public boolean setServerAddress(URL url);
	public boolean connect();
	public boolean disconnect();

	// Execution control methods
	public boolean open();
	public boolean run();
	public boolean stop();
	public boolean close();
	public boolean sync();

	public boolean getBoolean(String name);
	public int getInt(String name);
	public float getFloat(String name);
	public double getDouble(String name);
	public String getString(String name);
	public void setValue(String name, boolean value);
	public void setValue(String name, int value);
	public void setValue(String name, float value);
	public void setValue(String name, double value);
	public void setValue(String name, String value);
	public List<String> getReadable();
	public List<String> getWritable();
	// Status methods
//  public boolean isConnected();
//  public boolean isRunning();
//  public boolean isOpened();
}
