/**
 * Test
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
import javax.json.JsonNumber;

public class Test {
	public static void main(String[] args) throws Exception {
		HttpTransport transport = new HttpTransport("http://localhost:2055/");
		Session session = new Session(transport);
		
        System.out.println("---------- Connecting people");
        session.connect();
        System.out.println("---------- Opening");
        session.open("");
        System.out.println(session.getReadable());
        System.out.println(session.getWritable());
        
        System.out.println("---------- Setting Value");
        session.setValue("motor1_control", 0.5);
        System.out.println("---------- Getting Value");
        System.out.println(session.getDouble("motor1_speed"));
        
//        System.out.println("---------- Getting&Setting Value in batch mode");
//        session.getVariableLater("input1");
//        session.setVariableLater("output1", 1.0);
//        System.out.println(session.sync());
        
        
        
//		JsonRpcClient client = new JsonRpcClient(transport);
//		client.executeLater("setValue", new Object[]{"input1", 1.0});
//		client.executeLater("getValue", new Object[]{"output2"});
//		client.executeLater("getValue", new Object[]{"output1"});
//		client.executeLater("setValue", new Object[]{"input2", 1.0});
//		Object[] response = client.sendBatch();
//		for(int i=0; i<response.length; i++) {
//			System.out.println(response[i]);
//		}
	}
	
	
}
