package dev.webServer.model;

import java.util.ArrayList;
import java.util.List;

import net.sf.mpxj.ProjectFile;


public class Project {
	private String projectTitle;
	private String publisher;
	private List<Resource> resources;
	private List<Task> tasks;
	private List<Assignment> assignments;
	private String message;


	public Project(String message) {
		this.setMessage(message);
	}

	public Project(ProjectFile project) {
		// fill in all project information
		fillProjectInfo(project);

		// Retrieve Resources
		resources = new ArrayList<Resource>();
		for (net.sf.mpxj.Resource resource : project.getResources()) {
			Resource newResource= new Resource(resource);
			resources.add(newResource);
		}
		
		// Retrieve Task
		tasks = new ArrayList<Task>();
		for (net.sf.mpxj.Task task : project.getTasks()){
			Task newTask = new Task(task);
			tasks.add(newTask);
		}
		
		// Retrieve assignment
		assignments = new ArrayList<Assignment>();
		for (net.sf.mpxj.ResourceAssignment assign : project.getResourceAssignments()) {
			Assignment newAssignment= new Assignment(assign);
			assignments.add(newAssignment);
		}
	}

	private void fillProjectInfo(ProjectFile project) {
		this.setProjectTitle(project.getProjectProperties().getProjectTitle());
		this.setPublisher(project.getProjectProperties().getAuthor());
	}
	
	public String getProjectTitle() {
		return projectTitle;
	}

	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public List<Assignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<Assignment> assignments) {
		this.assignments = assignments;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
