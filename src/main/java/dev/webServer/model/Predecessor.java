package dev.webServer.model;

import net.sf.mpxj.Relation;

public class Predecessor {
    private Integer id;

    public Predecessor() {}

    public Predecessor(Relation relation) {
        this.setId(relation.getTargetTask().getID());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
