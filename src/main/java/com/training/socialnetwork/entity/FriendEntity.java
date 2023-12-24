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
	@JoinColumn(name = "accountId")
	private AccountEntity accountEntity1;
	
	@ManyToOne
	@JoinColumn(name = "accountId")
	private AccountEntity accountEntity2;
	
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

	public AccountEntity getAccountEntity1() {
		return accountEntity1;
	}

	public void setAccountEntity1(AccountEntity accountEntity1) {
		this.accountEntity1 = accountEntity1;
	}

	public AccountEntity getAccountEntity2() {
		return accountEntity2;
	}

	public void setAccountEntity2(AccountEntity accountEntity2) {
		this.accountEntity2 = accountEntity2;
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
