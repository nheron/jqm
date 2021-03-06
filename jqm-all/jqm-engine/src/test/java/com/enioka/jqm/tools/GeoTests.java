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

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hsqldb.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.enioka.jqm.api.Dispatcher;
import com.enioka.jqm.api.JobDefinition;
import com.enioka.jqm.jpamodel.History;
import com.enioka.jqm.jpamodel.JobDef;
import com.enioka.jqm.jpamodel.JobDefParameter;

public class GeoTests
{
	public static Logger jqmlogger = Logger.getLogger(GeoTests.class);
	public static Server s;

	@BeforeClass
	public static void testInit() throws InterruptedException
	{
		s = new Server();
		s.setDatabaseName(0, "testdbengine");
		s.setDatabasePath(0, "mem:testdbengine");
		s.setLogWriter(null);
		s.setSilent(true);
		s.start();

		Dispatcher.resetEM();
		Helpers.resetEmf();
	}

	@AfterClass
	public static void stop()
	{
		Dispatcher.resetEM();
		s.shutdown();
		s.stop();
	}

	@Test
	public void testGeo() throws Exception
	{
		jqmlogger.debug("**********************************************************");
		jqmlogger.debug("**********************************************************");
		jqmlogger.debug("Starting test testGeo");
		EntityManager em = Helpers.getNewEm();
		TestHelpers.cleanup(em);
		TestHelpers.createLocalNode(em);

		ArrayList<JobDefParameter> jdargs = new ArrayList<JobDefParameter>();
		JobDefParameter jdp = CreationTools.createJobDefParameter("arg", "POUPETTE", em);
		jdargs.add(jdp);

		@SuppressWarnings("unused")
		JobDef jd = CreationTools.createJobDef(null, true, "App", jdargs, "jqm-test-geo/", "jqm-test-geo/jqm-test-geo.jar",
				TestHelpers.qVip, 42, "Geo", null, "Franquin", "ModuleMachin", "other1", "other2", false, em);

		JobDefinition form = new JobDefinition("Geo", "MAG");
		form.addParameter("nbJob", "1");
		Dispatcher.enQueue(form);

		// Create JNDI connection to write inside the engine database
		em.getTransaction().begin();
		CreationTools.createDatabaseProp("jdbc/jqm", "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost/testdbengine", "SA", "", em);
		em.getTransaction().commit();

		// Start the engine
		JqmEngine engine1 = new JqmEngine();
		JqmEngine engine2 = new JqmEngine();
		JqmEngine engine3 = new JqmEngine();
		engine1.start(new String[] { "localhost" });
		engine2.start(new String[] { "localhost4" });
		engine3.start(new String[] { "localhost5" });

		Thread.sleep(15000);
		jqmlogger.debug("###############################################################");
		jqmlogger.debug("SHUTDOWN");
		jqmlogger.debug("###############################################################");
		engine1.stop();
		engine2.stop();
		engine3.stop();

		long i = (Long) em.createQuery("SELECT COUNT(h) FROM History h").getSingleResult();
		Assert.assertTrue(i > 3);
		TypedQuery<History> query = em.createQuery("SELECT j FROM History j ORDER BY j.enqueueDate ASC", History.class);
		ArrayList<History> res = (ArrayList<History>) query.getResultList();

		for (History history : res)
		{
			if (history.getState() == "CRASHED")
			{
				Assert.fail("No job should be crashed");
			}
		}

		Assert.assertEquals(31, res.size());
	}
}
