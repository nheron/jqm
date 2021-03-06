package com.enioka.jqm.testpackages;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.spi.NamingManager;

import com.enioka.jqm.api.JobBase;

public class SuperTestPayload extends JobBase
{

	@Override
	public void start()
	{
		System.out.println("Thread context class loader is: " + Thread.currentThread().getContextClassLoader());
		System.out.println("Class class loader used for loading test class is: " + this.getClass().getClassLoader());
		int nb = 0;

		try
		{
			// Get the QCF
			Object o = NamingManager.getInitialContext(null).lookup("jms/qcf");
			System.out.println("Received a " + o.getClass());

			// Do as cast & see if no errors
			QueueConnectionFactory qcf = (QueueConnectionFactory) o;

			// Get the Queue
			Object p = NamingManager.getInitialContext(null).lookup("jms/testqueue");
			System.out.println("Received a " + p.getClass());
			Queue q = (Queue) p;

			// Now that we are sure that JNDI works, let's write a message
			System.out.println("Opening connection & session to the broker");
			Connection connection = qcf.createConnection();
			connection.start();
			Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

			System.out.println("Creating producer");
			MessageProducer producer = session.createProducer(q);
			TextMessage message = session.createTextMessage("HOUBA HOP. SIGNED: MARSUPILAMI");

			System.out.println("Sending message");
			producer.send(message);
			producer.close();
			session.commit();
			System.out.println("A message was sent to the broker");

			// Browse and check the message is there
			Connection connection2 = qcf.createConnection();
			connection2.start();
			Session session2 = connection2.createSession(false, Session.AUTO_ACKNOWLEDGE);
			QueueBrowser qb = session2.createBrowser(q);
			Enumeration<TextMessage> msgs = qb.getEnumeration();
			while (msgs.hasMoreElements())
			{
				TextMessage msg = msgs.nextElement();
				System.out.println("Message received: " + msg.getText());
				nb++;
			}
			System.out.println("Browsing will end here");
			qb.close();
			System.out.println("End of browsing. Nb of message read: " + nb);

			// We are done!
			connection.close();
			connection2.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		if (nb == 0)
			throw new RuntimeException("test has failed - no messages were received.");
	}
}
