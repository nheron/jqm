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

package com.enioka.jqm.jpamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author pierre.coppee
 */
@Entity
@Table(name = "JobDef")
public class JobDef implements Serializable
{
	private static final long serialVersionUID = -3276834475433922990L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(name = "description")
	private String description;
	@Column(name = "canBeRestarted")
	private boolean canBeRestarted = true;
	@Column(nullable = false, length = 100, name = "javaClassName")
	private String javaClassName;
	@Column(length = 1000, name = "filePath")
	private String filePath;
	@ManyToOne(optional = false)
	@JoinColumn(name = "queue_id")
	private Queue queue;
	@Column(name = "maxTimeRunning")
	private Integer maxTimeRunning;
	@Column(nullable = false, name = "applicationName", unique = true)
	private String applicationName;
	@Column(length = 50, name = "application")
	private String application;
	@Column(length = 50, name = "module")
	private String module;
	@Column(length = 50, name = "keyword1")
	private String keyword1;
	@Column(length = 50, name = "keyword2")
	private String keyword2;
	@Column(length = 50, name = "keyword3")
	private String keyword3;
	@Column(name = "highlander", nullable = false)
	private boolean highlander = false;
	@Column(name = "jarPath")
	private String jarPath;
	@OneToMany(orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "JobDefId")
	private List<JobDefParameter> parameters = new ArrayList<JobDefParameter>();

	public Integer getId()
	{
		return id;
	}

	public boolean isCanBeRestarted()
	{
		return canBeRestarted;
	}

	public void setCanBeRestarted(final boolean canBeRestarted)
	{
		this.canBeRestarted = canBeRestarted;
	}

	public String getJavaClassName()
	{
		return javaClassName;
	}

	public void setJavaClassName(final String javaClassName)
	{
		this.javaClassName = javaClassName;
	}

	public Integer getMaxTimeRunning()
	{
		return maxTimeRunning;
	}

	public void setMaxTimeRunning(final Integer maxTimeRunning)
	{
		this.maxTimeRunning = maxTimeRunning;
	}

	public String getApplicationName()
	{
		return applicationName;
	}

	public void setApplicationName(final String applicationName)
	{
		this.applicationName = applicationName;
	}

	public String getApplication()
	{
		return application;
	}

	public void setApplication(final String application)
	{
		this.application = application;
	}

	public String getModule()
	{
		return module;
	}

	public void setModule(final String module)
	{
		this.module = module;
	}

	public String getKeyword1()
	{
		return keyword1;
	}

	public void setKeyword1(final String keyword1)
	{
		this.keyword1 = keyword1;
	}

	public String getKeyword2()
	{
		return keyword2;
	}

	public void setKeyword2(final String keyword2)
	{
		this.keyword2 = keyword2;
	}

	public String getKeyword3()
	{
		return keyword3;
	}

	public void setKeyword3(final String keyword3)
	{
		this.keyword3 = keyword3;
	}

	public boolean isHighlander()
	{
		return highlander;
	}

	public void setHighlander(final boolean highlander)
	{
		this.highlander = highlander;
	}

	public Queue getQueue()
	{
		return queue;
	}

	public void setQueue(final Queue queue)
	{
		this.queue = queue;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(final String filePath)
	{
		this.filePath = filePath;
	}

	public String getJarPath()
	{

		return jarPath;
	}

	public void setJarPath(final String jarPath)
	{
		this.jarPath = jarPath;
	}

	public List<JobDefParameter> getParameters()
	{
		return parameters;
	}

	public void setParameters(final List<JobDefParameter> parameters)
	{
		this.parameters = parameters;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
