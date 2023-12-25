package com.training.socialnetwork.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "friend")
public class FriendEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int friendId;
	
	@ManyToOne
	@JoinColumn(name = "userId1")
	private UserEntity userEntity1;
	
	@ManyToOne
	@JoinColumn(name = "userId2")
	private UserEntity userEntity2;
	
	@Column
	private int status;
	
	@Column
	private Date createDate;
	
	@Column
	private Date updateDate;

	public int getFriendId() {
		return friendId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}
	
	public UserEntity getUserEntity1() {
		return userEntity1;
	}

	public void setUserEntity1(UserEntity userEntity1) {
		this.userEntity1 = userEntity1;
	}

	public UserEntity getUserEntity2() {
		return userEntity2;
	}

	public void setUserEntity2(UserEntity userEntity2) {
		this.userEntity2 = userEntity2;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
	
}
