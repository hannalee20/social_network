package com.training.socialnetwork.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;

	@Column
	private String username;

	@Column
	private String password;

	@Column
	private String realName;

	@Column
	private Date birthDate;

	@Column
	private Integer gender;

	@Column
	private String email;

	@Column
	private String address;

	@Column
	private String university;

	@Column
	private String job;

	@Column
	private String status;

	@Column
	private String about;

	@Column
	private String avatarUrl;

//	@ManyToMany
//	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
//	private Set<Role> roles = new HashSet<>();
	@ManyToOne
	@JoinColumn(name = "roleId")
	private Role role;
	
	@Column
	private String token;
	
	@Column
	private Date tokenCreateDate;

	@Column
	private Date createDate;

	@Column
	private Date updateDate;

	@OneToMany(mappedBy = "user1")
	private List<Friend> userId1;

	@OneToMany(mappedBy = "user2")
	private List<Friend> userId2;

	@OneToMany(mappedBy = "user")
	private List<Post> postList;

	@OneToMany(mappedBy = "user")
	private List<Like> likeList;

	@OneToMany(mappedBy = "user")
	private List<Comment> commentList;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public List<Friend> getUserId1() {
		return userId1;
	}

	public void setUserId1(List<Friend> userId1) {
		this.userId1 = userId1;
	}

	public List<Friend> getUserId2() {
		return userId2;
	}

	public void setUserId2(List<Friend> userId2) {
		this.userId2 = userId2;
	}

	public List<Post> getPostList() {
		return postList;
	}

	public void setPostList(List<Post> postList) {
		this.postList = postList;
	}

	public List<Like> getLikeList() {
		return likeList;
	}

	public void setLikeList(List<Like> likeList) {
		this.likeList = likeList;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getTokenCreateDate() {
		return tokenCreateDate;
	}

	public void setTokenCreateDate(Date tokenCreateDate) {
		this.tokenCreateDate = tokenCreateDate;
	}

}
