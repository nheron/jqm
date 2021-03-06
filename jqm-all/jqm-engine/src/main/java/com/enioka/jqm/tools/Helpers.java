/**
 * Copyright © 2013 enioka. All rights reserved
 * Authors: Marc-Antoine GOUILLART (marc-antoine.gouillart@enioka.com)
 *          Pierre COPPEE (pierre.coppee@enioka.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.enioka.jqm.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.enioka.jqm.jpamodel.Deliverable;
import com.enioka.jqm.jpamodel.History;
import com.enioka.jqm.jpamodel.Message;

/**
 * This is a helper class for internal use only.
 * 
 */
public final class Helpers
{
	private static final String PERSISTENCE_UNIT = "jobqueue-api-pu";
	private static Logger jqmlogger = Logger.getLogger(Helpers.class);

	// The one and only EMF in the engine.
	private static EntityManagerFactory emf = createFactory();

	/**
	 * Get a fresh EM on the jobqueue-api-pu persistence Unit
	 * 
	 * @return an EntityManager
	 */
	public static EntityManager getNewEm()
	{
		return emf.createEntityManager();
	}

	private static EntityManagerFactory createFactory()
	{
		Properties p = new Properties();
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream("conf/db.properties");
			p.load(fis);
			IOUtils.closeQuietly(fis);
			return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, p);
		} catch (FileNotFoundException e)
		{
			// No properties file means we use the test HSQLDB (file, not in-memory) as specified in the persistence.xml
			IOUtils.closeQuietly(fis);
			return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		} catch (IOException e)
		{
			jqmlogger.fatal("conf/db.properties file is invalid", e);
			IOUtils.closeQuietly(fis);
			System.exit(1);
			// Stupid, just for Eclipse's parser and therefore avoid red lines...
			return null;
		} finally
		{
			IOUtils.closeQuietly(fis);
		}
	}

	/**
	 * For internal test use only <br/>
	 * <bold>WARNING</bold> This will invalidate all open EntityManagers!
	 */
	static void resetEmf()
	{
		if (emf != null)
		{
			emf.close();
		}
		emf = createFactory();
	}

	/**
	 * Create a text message that will be stored in the database. Must be called inside a JPA transaction.
	 * 
	 * @param textMessage
	 * @param history
	 * @param em
	 * @return the JPA message created
	 */
	static Message createMessage(String textMessage, History history, EntityManager em)
	{
		Message m = new Message();

		m.setTextMessage(textMessage);
		m.setHistory(history);

		em.persist(m);
		return m;
	}

	/**
	 * Create a Deliverable inside the datbase that will track a file created by a JobInstance Must be called from inside a JPA transaction
	 * 
	 * @param fp
	 *            FilePath (relative to a root directory - cf. Node)
	 * @param fn
	 *            FileName
	 * @param hp
	 *            HashPath
	 * @param ff
	 *            File family (may be null). E.g.: "daily report"
	 * @param jobId
	 *            Job Instance ID
	 * @param em
	 *            the EM to use.
	 * @return
	 */
	static Deliverable createDeliverable(String fp, String fn, String hp, String ff, Integer jobId, EntityManager em)
	{
		Deliverable j = new Deliverable();

		j.setFilePath(fp);
		j.setHashPath(hp);
		j.setFileFamily(ff);
		j.setJobId(jobId);
		j.setFileName(fn);

		em.persist(j);
		return j;
	}

}
