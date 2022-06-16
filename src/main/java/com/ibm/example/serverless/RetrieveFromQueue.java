package com.ibm.example.serverless;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.google.gson.JsonObject;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class RetrieveFromQueue {
	public static JsonObject main(JsonObject args) {
		JsonObject reply = new JsonObject();
		
		// First get the MQ message
		Connection conn = null;
		MQConnectionFactory cf = new MQQueueConnectionFactory();

		try {
			cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
			cf.setHostName(args.getAsJsonPrimitive("qmgrHostName").getAsString());
			cf.setPort(args.getAsJsonPrimitive("qmgrPort").getAsInt());
			cf.setQueueManager(args.getAsJsonPrimitive("qmgrName").getAsString());
			cf.setChannel(args.getAsJsonPrimitive("qmgrChannelName").getAsString());
			
//			conn = cf.createConnection(args.getAsJsonPrimitive("username").getAsString(), 
//					args.getAsJsonPrimitive("password").getAsString());
			conn = cf.createConnection();
			
			Session session = conn.createSession(true, 0);
			Queue q = session.createQueue(args.getAsJsonPrimitive("queueName").getAsString());
			
			MessageConsumer consumer = session.createConsumer(q);
			
			conn.start();
			
			Message receivedMsg = null;

			JsonObject vote = new JsonObject();
			
			if ((receivedMsg = consumer.receiveNoWait()) != null) {
				if (receivedMsg != null) {
					String s = ((TextMessage) receivedMsg).getText();
					vote.addProperty("vote", s);
				}
				
			}

//			reply.addProperty("processed", processedMessages);
//			reply.add("messages",messages);
			

			reply.add("doc", vote);
			
			session.commit();
			session.close();
			conn.close();
			
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
		
		reply.addProperty("successful", true);
		return reply;
	}
}
