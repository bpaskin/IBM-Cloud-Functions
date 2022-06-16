package com.ibm.example.serverless;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import com.google.gson.JsonObject;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class SendToQueue {
	
	public static JsonObject main(JsonObject args) {
		JsonObject reply = new JsonObject();
		
		String selection = new String(args.getAsJsonPrimitive("selection").getAsString());
		
		Connection conn = null;
		MQConnectionFactory cf = new MQQueueConnectionFactory();
		
		try {
			cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
			cf.setHostName(args.getAsJsonPrimitive("qmgrHostName").getAsString());
			cf.setPort(args.getAsJsonPrimitive("qmgrPort").getAsInt());
			cf.setQueueManager(args.getAsJsonPrimitive("qmgrName").getAsString());
			cf.setChannel(args.getAsJsonPrimitive("qmgrChannelName").getAsString());
			
//		conn = cf.createConnection(args.getAsJsonPrimitive("username").getAsString(), 
//					args.getAsJsonPrimitive("password").getAsString());
			conn = cf.createConnection();
			
			Session session = conn.createSession(true, 0);
			Queue q = session.createQueue(args.getAsJsonPrimitive("queueName").getAsString());
			
			MessageProducer producer = session.createProducer(q);
			conn.start();
			
			Message msg = session.createTextMessage(selection);
			producer.send(msg);
			session.commit();
			session.close();
			conn.close();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
		
		reply.addProperty("accepted", true);
		return reply;
	}
}
