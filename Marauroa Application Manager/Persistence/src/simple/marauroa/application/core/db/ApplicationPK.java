/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.marauroa.application.core.db;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Embeddable
public class ApplicationPK implements Serializable {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private int id;
    @Basic(optional = false)
    @Column(name = "application_type")
    private int applicationType;

    public ApplicationPK() {
    }

    public ApplicationPK(int applicationType) {
        this.applicationType = applicationType;
    }

    public int getId() {
        return id;
    }

    public int getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(int applicationType) {
        this.applicationType = applicationType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) applicationType;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApplicationPK)) {
            return false;
        }
        ApplicationPK other = (ApplicationPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.applicationType != other.applicationType) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "simple.marauroa.application.core.db.ApplicationPK[ id=" + id + ", applicationType=" + applicationType + " ]";
    }
    
}
