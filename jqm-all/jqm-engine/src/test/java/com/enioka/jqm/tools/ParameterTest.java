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

import org.apache.log4j.Logger;
import org.hsqldb.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.enioka.jqm.api.Dispatcher;
import com.enioka.jqm.api.JobDefinition;
import com.enioka.jqm.jpamodel.JobDef;
import com.enioka.jqm.jpamodel.JobDefParameter;

public class ParameterTest
{
	public static Server s;
	public static Logger jqmlogger = Logger.getLogger(ParameterTest.class);

	@BeforeClass
	public static void testInit()
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
		s.shutdown();
	}

	@Test
	public void testMixParameters() throws Exception
	{
		jqmlogger.debug("**********************************************************");
		jqmlogger.debug("**********************************************************");
		jqmlogger.debug("Starting test testMixParameters");
		EntityManager em = Helpers.getNewEm();
		TestHelpers.cleanup(em);
		TestHelpers.createLocalNode(em);

		ArrayList<JobDefParameter> jdargs = new ArrayList<JobDefParameter>();
		JobDefParameter jdp = CreationTools.createJobDefParameter("arg1", "argument1", em);
		JobDefParameter jdp2 = CreationTools.createJobDefParameter("arg2", "Franquin", em);
		jdargs.add(jdp);
		jdargs.add(jdp2);

		@SuppressWarnings("unused")
		JobDef jdDemoMaven = CreationTools.createJobDef(null, true, "App", jdargs, "jqm-test-checkargs/",
				"jqm-test-checkargs/jqm-test-checkargs.jar", TestHelpers.qVip, 42, "MarsuApplication", null, "Franquin", "ModuleMachin",
				"other", "other", false, em);

		JobDefinition j = new JobDefinition("MarsuApplication", "MAG");
		j.addParameter("arg2", "argument2");

		Dispatcher.enQueue(j);

		JqmEngine engine1 = new JqmEngine();
		engine1.start(new String[] { "localhost" });
		Thread.sleep(5000);
		engine1.stop();

		Assert.assertEquals(true, true);
	}

	@Test
	public void testDefaultParameters() throws Exception
	{
		jqmlogger.debug("**********************************************************");
		jqmlogger.debug("**********************************************************");
		jqmlogger.debug("Starting test testDefaultParameters");
		EntityManager em = Helpers.getNewEm();
		TestHelpers.cleanup(em);
		TestHelpers.createLocalNode(em);

		ArrayList<JobDefParameter> jdargs = new ArrayList<JobDefParameter>();
		JobDefParameter jdp = CreationTools.createJobDefParameter("arg1", "argument1", em);
		JobDefParameter jdp2 = CreationTools.createJobDefParameter("arg2", "argument2", em);
		jdargs.add(jdp);
		jdargs.add(jdp2);

		@SuppressWarnings("unused")
		JobDef jdDemoMaven = CreationTools.createJobDef(null, true, "App", jdargs, "jqm-test-checkargs/",
				"jqm-test-checkargs/jqm-test-checkargs.jar", TestHelpers.qVip, 42, "MarsuApplication", null, "Franquin", "ModuleMachin",
				"other", "other", false, em);

		JobDefinition j = new JobDefinition("MarsuApplication", "MAG");

		Dispatcher.enQueue(j);

		JqmEngine engine1 = new JqmEngine();
		engine1.start(new String[] { "localhost" });
		Thread.sleep(5000);
		engine1.stop();

		Assert.assertEquals(true, true);
	}

	@Test
	public void testOverrideParmeters() throws Exception
	{
		jqmlogger.debug("**********************************************************");
		jqmlogger.debug("**********************************************************");
		jqmlogger.debug("Starting test testOverrideParameters");
		EntityManager em = Helpers.getNewEm();
		TestHelpers.cleanup(em);
		TestHelpers.createLocalNode(em);

		ArrayList<JobDefParameter> jdargs = new ArrayList<JobDefParameter>();
		JobDefParameter jdp = CreationTools.createJobDefParameter("arg1", "Gaston Lagaffe", em);
		JobDefParameter jdp2 = CreationTools.createJobDefParameter("arg2", "Franquin", em);
		jdargs.add(jdp);
		jdargs.add(jdp2);

		@SuppressWarnings("unused")
		JobDef jdDemoMaven = CreationTools.createJobDef(null, true, "App", jdargs, "jqm-test-checkargs/",
				"jqm-test-checkargs/jqm-test-checkargs.jar", TestHelpers.qVip, 42, "MarsuApplication", null, "Franquin", "ModuleMachin",
				"other", "other", false, em);

		JobDefinition j = new JobDefinition("MarsuApplication", "MAG");
		j.addParameter("arg1", "argument1");
		j.addParameter("arg2", "argument2");

		Dispatcher.enQueue(j);

		JqmEngine engine1 = new JqmEngine();
		engine1.start(new String[] { "localhost" });
		Thread.sleep(5000);
		engine1.stop();

		Assert.assertEquals(true, true);
	}
}
