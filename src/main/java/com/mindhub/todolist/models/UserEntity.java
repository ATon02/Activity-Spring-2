package com.mindhub.todolist.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mindhub.todolist.models.enums.UserRoles;

import io.swagger.v3.oas.annotations.media.Schema;


@Entity
public class UserEntity {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true) 
    private long id;
    @Schema(required = true,example = "user test")
    private String username;
    @Schema(example = "passwordTest") 
    private String password;
    @Schema(example = "emailtest@email.com") 
    @Column(unique = true)
    private String email;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Schema(hidden = true) 
    private Set<Task> tasks = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private UserRoles role;
    public UserEntity() {
    }


    public UserEntity(long id) {
        this.id = id;
    }

    public UserEntity(Set<Task> tasks, String username, String password, String email) {
        this.tasks = tasks;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserEntity(String username, String password, String email, UserRoles role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role =role;
    }

    public UserEntity(String password, String email) {
        this.username = "This data is pending for update";
        this.password = password;
        this.email = email;
        this.role =UserRoles.USER;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public void addProduct(Task task) {
        task.setUser(this);
        this.tasks.add(task);
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserEntity other = (UserEntity) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    }

    public boolean validObject(){
        return !this.email.isBlank() && !this.password.isBlank() && !this.username.isBlank();
    }

    public boolean validEmail() {
		if (this.email != null) {
			Matcher matcher = pattern.matcher(this.email);
			return matcher.find();
		}
		return false;
	}
}
