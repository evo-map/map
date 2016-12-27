package com.evolutionaryenterprisesllc.map.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A HVisualizer.
 */
@Entity
@Table(name = "h_visualizer")
public class HVisualizer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private HType hType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public HVisualizer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HType getHType() {
        return hType;
    }

    public HVisualizer hType(HType hType) {
        this.hType = hType;
        return this;
    }

    public void setHType(HType hType) {
        this.hType = hType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HVisualizer hVisualizer = (HVisualizer) o;
        if (hVisualizer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, hVisualizer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HVisualizer{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
