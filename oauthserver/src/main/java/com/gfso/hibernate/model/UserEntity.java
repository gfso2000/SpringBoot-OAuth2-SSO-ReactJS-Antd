package com.gfso.hibernate.model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="users")
@NamedNativeQueries({
    @NamedNativeQuery(
            name    =   "getOneUser",
            query   =   "select t1.username,t1.password,t1.enabled " +
                        "from users t1 " +
                        "where t1.username=?",
                        resultClass=UserEntity.class
    )
})
public class UserEntity implements java.io.Serializable {

	private static final long serialVersionUID = -5847162755474624100L;

	@Id 
	//@GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="username", nullable=false, length=45)
	private String userName;
    
    @Column(name="password", nullable=false, length=100)
	private String password;
    
    @Column(name="enabled", nullable=false)
	private boolean enabled;

    @Column(name="firstname", nullable=false, length=100)
	private String firstName;
    
    @Column(name="lastname", nullable=false, length=100)
	private String lastName;
    
    @Column(name="createddate", nullable=false)
    private Date createdDate;
    
    @Column(name="activateddate", nullable=false)
    private Date activatedDate;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="username") 
    private Set<UserRoleEntity> roles = new HashSet<UserRoleEntity>(0);
    
	public UserEntity() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getActivatedDate() {
		return activatedDate;
	}

	public void setActivatedDate(Date activatedDate) {
		this.activatedDate = activatedDate;
	}

	public Set<UserRoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRoleEntity> roles) {
		this.roles = roles;
	}
	
}
