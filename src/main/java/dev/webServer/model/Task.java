package dev.webServer.model;


import net.sf.mpxj.Duration;
import net.sf.mpxj.Relation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task {

	private String name;
	private Integer id;
	private Integer uniqueId;
	private Duration duration;
	private Number cost;
	private Duration work;
	private Date start;
	private Date finish;
	private Number percentageComplete;
	private List<Predecessor> predecessors;
	private List<Integer> childTaskIds;
	private Integer parentTaskId;

	public Task() {}

	public Task(net.sf.mpxj.Task task) {
		this.setName(task.getName());
		this.setId(task.getID());
		this.setUniqueId(task.getUniqueID());
		this.setDuration(task.getDuration());
		this.setCost(task.getCost());
		this.setWork(task.getWork());
		this.setStart(task.getStart());
		this.setFinish(task.getFinish());
		this.setPercentageComplete(task.getPercentageComplete());

		predecessors = new ArrayList<Predecessor>();
		for (Relation relation : task.getPredecessors()) {
			Predecessor predecessor = new Predecessor(relation);
			predecessors.add(predecessor);
		}

		childTaskIds = new ArrayList<Integer>();
		for (net.sf.mpxj.Task child : task.getChildTasks()) {
			childTaskIds.add(child.getID());
		}

		if (task.getParentTask() != null) {
			parentTaskId = task.getParentTask().getID();
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(Integer uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Number getPercentageComplete() {
		return percentageComplete;
	}

	public void setPercentageComplete(Number percentageComplete) {
		this.percentageComplete = percentageComplete;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public Number getCost() {
		return cost;
	}

	public void setCost(Number cost) {
		this.cost = cost;
	}

	public Duration getWork() {
		return work;
	}

	public void setWork(Duration work) {
		this.work = work;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getFinish() {
		return finish;
	}

	public void setFinish(Date finish) {
		this.finish = finish;
	}

	public List<Predecessor> getPredecessors() {
		return predecessors;
	}

	public void setPredecessors(List<Predecessor> predecessors) {
		this.predecessors = predecessors;
	}

	public List<Integer> getChildTaskIds() {
		return childTaskIds;
	}

	public void setChildTaskIds(List<Integer> childTaskIds) {
		this.childTaskIds = childTaskIds;
	}

	public Integer getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(Integer parentTaskId) {
		this.parentTaskId = parentTaskId;
	}
}