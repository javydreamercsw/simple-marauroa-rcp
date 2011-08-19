/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simple.marauroa.application.core.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "application", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"}),
    @UniqueConstraint(columnNames = {"path"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Application.findAll", query = "SELECT a FROM Application a"),
    @NamedQuery(name = "Application.findById", query = "SELECT a FROM Application a WHERE a.applicationPK.id = :id"),
    @NamedQuery(name = "Application.findByApplicationType", query = "SELECT a FROM Application a WHERE a.applicationPK.applicationType = :applicationType"),
    @NamedQuery(name = "Application.findByName", query = "SELECT a FROM Application a WHERE a.name = :name"),
    @NamedQuery(name = "Application.findByPath", query = "SELECT a FROM Application a WHERE a.path = :path"),
    @NamedQuery(name = "Application.findByEnabled", query = "SELECT a FROM Application a WHERE a.enabled = :enabled"),
    @NamedQuery(name = "Application.findByVersion", query = "SELECT a FROM Application a WHERE a.version = :version")})
public class Application implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ApplicationPK applicationPK;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "path", length = 255)
    private String path;
    @Basic(optional = false)
    @Column(name = "enabled", nullable = false)
    private boolean enabled;
    @Basic(optional = false)
    @Column(name = "version", nullable = false, length = 45)
    private String version;
    @JoinColumn(name = "application_type", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ApplicationType applicationType1;

    public Application() {
    }

    public Application(ApplicationPK applicationPK) {
        this.applicationPK = applicationPK;
    }

    public Application(ApplicationPK applicationPK, boolean enabled, String version) {
        this.applicationPK = applicationPK;
        this.enabled = enabled;
        this.version = version;
    }

    public ApplicationPK getApplicationPK() {
        return applicationPK;
    }

    public void setApplicationPK(ApplicationPK applicationPK) {
        this.applicationPK = applicationPK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ApplicationType getApplicationType1() {
        return applicationType1;
    }

    public void setApplicationType1(ApplicationType applicationType1) {
        this.applicationType1 = applicationType1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (applicationPK != null ? applicationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Application)) {
            return false;
        }
        Application other = (Application) object;
        if ((this.applicationPK == null && other.applicationPK != null) || (this.applicationPK != null && !this.applicationPK.equals(other.applicationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "simple.marauroa.application.core.db.Application[ applicationPK=" + applicationPK + " ]";
    }
    
}
