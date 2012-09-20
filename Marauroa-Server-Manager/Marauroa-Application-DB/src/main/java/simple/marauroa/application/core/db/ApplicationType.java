/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.marauroa.application.core.db;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "application_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ApplicationType.findAll", query = "SELECT a FROM ApplicationType a"),
    @NamedQuery(name = "ApplicationType.findById", query = "SELECT a FROM ApplicationType a WHERE a.id = :id"),
    @NamedQuery(name = "ApplicationType.findByName", query = "SELECT a FROM ApplicationType a WHERE a.name = :name"),
    @NamedQuery(name = "ApplicationType.findByTypeClass", query = "SELECT a FROM ApplicationType a WHERE a.typeClass = :typeClass")})
public class ApplicationType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Basic(optional = false)
    @Column(name = "typeClass", nullable = false, length = 255)
    private String typeClass;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "applicationType")
    private List<Application> applicationList;

    public ApplicationType() {
    }

    public ApplicationType(String name, String class1) {
        this.name = name;
        this.typeClass = class1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(String typeClass) {
        this.typeClass = typeClass;
    }

    @XmlTransient
    public List<Application> getApplicationList() {
        return applicationList;
    }

    public void setApplicationList(List<Application> applicationList) {
        this.applicationList = applicationList;
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
