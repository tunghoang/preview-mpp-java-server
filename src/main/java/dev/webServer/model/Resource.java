package dev.webServer.model;

public class Resource implements java.io.Serializable {

	private static final long serialVersionUID = -5128066412328710548L;
	private String name;
	private String type;
	private Double stdRate;
	private Integer id;
	private Integer uniqueId;
	
	public Resource(){}
	
	public Resource(net.sf.mpxj.Resource resource){
		this.setId(resource.getID());
		if (resource.getName() == null){
			this.setName("Unassigned Resource");
		} else {
			this.setName(resource.getName());
		}
		this.setUniqueId(resource.getUniqueID());
		this.setType(resource.getType().name());
		this.setStdRate(resource.getStandardRate().getAmount());
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getStdRate() {
		return stdRate;
	}

	public void setStdRate(Double stdRate) {
		this.stdRate = stdRate;
	}
}
