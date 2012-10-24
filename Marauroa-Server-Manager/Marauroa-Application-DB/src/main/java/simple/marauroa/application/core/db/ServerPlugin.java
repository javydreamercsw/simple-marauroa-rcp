/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.marauroa.application.core.db;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "server_plugin")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServerPlugin.findAll", query = "SELECT s FROM ServerPlugin s"),
    @NamedQuery(name = "ServerPlugin.findById", query = "SELECT s FROM ServerPlugin s WHERE s.id = :id"),
    @NamedQuery(name = "ServerPlugin.findByName", query = "SELECT s FROM ServerPlugin s WHERE s.name = :name"),
    @NamedQuery(name = "ServerPlugin.findByPluginPath", query = "SELECT s FROM ServerPlugin s WHERE s.pluginPath = :pluginPath")})
public class ServerPlugin implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "pluginPath")
    private String pluginPath;
    @ManyToMany(mappedBy = "serverPluginList")
    private List<Application> applicationList;

    public ServerPlugin() {
    }

    public ServerPlugin(Integer id) {
        this.id = id;
    }

    public ServerPlugin(Integer id, String name, String pluginPath) {
        this.id = id;
        this.name = name;
        this.pluginPath = pluginPath;
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

    public String getPluginPath() {
        return pluginPath;
    }

    public void setPluginPath(String pluginPath) {
        this.pluginPath = pluginPath;
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
        if (!(object instanceof ServerPlugin)) {
            return false;
        }
        ServerPlugin other = (ServerPlugin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "simple.marauroa.application.core.db.ServerPlugin[ id=" + id + " ]";
    }
    
}
