package dev.webServer.model;

import net.sf.mpxj.ResourceAssignment;

public class Assignment implements java.io.Serializable {

	private Integer taskUniqueId;
	private Integer taskId;
	private Integer resourceUniqueId;
	private Integer resourceId;
	
	public Assignment(){}
	public Assignment(ResourceAssignment assignment){
		this.setTaskUniqueid(assignment.getTaskUniqueID());
		this.setResourceUniqueid(assignment.getResourceUniqueID());
		this.setTaskId(assignment.getTask().getID());
		if (assignment.getResource() == null){
			this.setResourceId(null);
		} else
		this.setResourceId(assignment.getResource().getID());
	}


	public Integer getTaskUniqueid() {
		return taskUniqueId;
	}

	public void setTaskUniqueid(Integer taskid) {
		this.taskUniqueId = taskid;
	}

	public Integer getResourceUniqueid() {
		return resourceUniqueId;
	}

	public void setResourceUniqueid(Integer resourceid) {
		this.resourceUniqueId = resourceid;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
}
