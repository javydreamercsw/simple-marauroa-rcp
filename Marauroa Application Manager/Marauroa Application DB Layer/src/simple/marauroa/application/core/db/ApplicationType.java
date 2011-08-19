/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simple.marauroa.application.core.db;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "application_type", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"}),
    @UniqueConstraint(columnNames = {"class"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ApplicationType.findAll", query = "SELECT a FROM ApplicationType a"),
    @NamedQuery(name = "ApplicationType.findById", query = "SELECT a FROM ApplicationType a WHERE a.id = :id"),
    @NamedQuery(name = "ApplicationType.findByName", query = "SELECT a FROM ApplicationType a WHERE a.name = :name"),
    @NamedQuery(name = "ApplicationType.findByClass1", query = "SELECT a FROM ApplicationType a WHERE a.class1 = :class1")})
public class ApplicationType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Basic(optional = false)
    @Column(name = "class", nullable = false, length = 255)
    private String class1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "applicationType1")
    private Collection<Application> applicationCollection;

    public ApplicationType() {
    }

    public ApplicationType(String name, String class1) {
        this.name = name;
        this.class1 = class1;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    @XmlTransient
    public Collection<Application> getApplicationCollection() {
        return applicationCollection;
    }

    public void setApplicationCollection(Collection<Application> applicationCollection) {
        this.applicationCollection = applicationCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApplicationType)) {
            return false;
        }
        ApplicationType other = (ApplicationType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "simple.marauroa.application.core.db.ApplicationType[ id=" + id + " ]";
    }
    
}
