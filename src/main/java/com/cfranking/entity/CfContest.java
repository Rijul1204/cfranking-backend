package com.cfranking.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.TreeSet;

@Entity
@Table(name = "Contest")
public class CfContest implements Serializable {

    @Id
    private int id;
    private String name;
    private String type;
    private String phase;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
        TreeSet<Integer> a = new TreeSet<>();
        a.ceiling(10);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CfContest{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", phase='").append(phase).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
