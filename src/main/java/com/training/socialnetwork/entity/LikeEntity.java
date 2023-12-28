package com.training.socialnetwork.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "likes")
public class LikeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int likeId;
	
	@ManyToOne
	@JoinColumn(name = "postId")
	private PostEntity postEntity;
	
	@ManyToOne 
	@JoinColumn(name = "userId")
	private UserEntity userEntity;
	
	@Column
	private Date createDate;
	
	@Column
	private Date updateDate;
	
	@Column
	private int deleteFlg;

	public int getLikeId() {
		return likeId;
	}

	public void setLikeId(int likeId) {
		this.likeId = likeId;
	}

	public PostEntity getPostEntity() {
		return postEntity;
	}

	public void setPostEntity(PostEntity postEntity) {
		this.postEntity = postEntity;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
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

	public int getDeleteFlg() {
		return deleteFlg;
	}

	public void setDeleteFlg(int deleteFlg) {
		this.deleteFlg = deleteFlg;
	}
	
}
