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
@Table(name = "application")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Application.findAll", query = "SELECT a FROM Application a"),
    @NamedQuery(name = "Application.findById", query = "SELECT a FROM Application a WHERE a.applicationPK.id = :id"),
    @NamedQuery(name = "Application.findByApplicationType", query = "SELECT a FROM Application a WHERE a.applicationType = :applicationType"),
    @NamedQuery(name = "Application.findByName", query = "SELECT a FROM Application a WHERE a.name = :name"),
    @NamedQuery(name = "Application.findByApplicationPath", query = "SELECT a FROM Application a WHERE a.applicationPath = :path"),
    @NamedQuery(name = "Application.findByEnabled", query = "SELECT a FROM Application a WHERE a.enabled = :enabled"),
    @NamedQuery(name = "Application.findByVersion", query = "SELECT a FROM Application a WHERE a.version = :version")})
public class Application implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ApplicationPK applicationPK;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "applicationPath")
    private String applicationPath;
    @Column(name = "enabled")
    private Boolean enabled;
    @Basic(optional = false)
    @Column(name = "version")
    private String version;
    @JoinTable(name = "application_has_plugin", joinColumns = {
        @JoinColumn(name = "application_id", referencedColumnName = "id"),
        @JoinColumn(name = "application_application_type", referencedColumnName = "application_type")}, inverseJoinColumns = {
        @JoinColumn(name = "plugin_id", referencedColumnName = "id")})
    @ManyToMany
    private List<ServerPlugin> serverPluginList;
    @JoinColumn(name = "application_type", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ApplicationType applicationType;

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

    public Application(int id, int applicationType) {
        this.applicationPK = new ApplicationPK(applicationType);
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

    public String getApplicationPath() {
        return applicationPath;
    }

    public void setApplicationPath(String path) {
        this.applicationPath = path;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XmlTransient
    public List<ServerPlugin> getServerPluginList() {
        return serverPluginList;
    }

    public void setServerPluginList(List<ServerPlugin> serverPluginList) {
        this.serverPluginList = serverPluginList;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
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
