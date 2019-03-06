package com.gfso.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

@Entity
@Table(name="user_roles")
@NamedNativeQueries({
    @NamedNativeQuery(
            name    =   "getUserRoles",
            query   =   "select t1.role " +
                        "from user_roles t1 " +
                        "where t1.username=?",
                        resultClass=UserRoleEntity.class
    )
})
public class UserRoleEntity implements java.io.Serializable {

	private static final long serialVersionUID = -1352329854358938261L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_role_id", nullable=false, length=11)
	private String id;
    
    @Column(name="username", nullable=false, length=45)
	private String username;
    
    @Column(name="role", nullable=false, length=45)
	private String role;

	public UserRoleEntity() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
