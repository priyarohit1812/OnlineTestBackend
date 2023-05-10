package com.onlinetestapp.model;


import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "ota_result")
public class Result {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long resultId;
	@NotNull(message = "Quiz Id is mandatory")
	private Long quizId;
	@NotNull(message = "MarksObtained is mandatory")
	private int marksObtained;
	@NotNull(message = "Max Marks is mandatory")
	private int maxMarks;
	@NotNull(message = "isPass is mandatory")
	private boolean isPass;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false, updatable = false)
	private Date createdDate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_date", nullable = false, updatable = true)
	private Date modifiedDate;
	
	@ManyToOne
	@JoinColumn(name = "user_id_fk")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User user;
	
	public Long getResultId() {
		return resultId;
	}

	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}

	public Long getQuizId() {
		return quizId;
	}

	public void setQuizId(Long quizId) {
		this.quizId = quizId;
	}

	public int getMarksObtained() {
		return marksObtained;
	}

	public void setMarksObtained(int marksObtained) {
		this.marksObtained = marksObtained;
	}

	public int getMaxMarks() {
		return maxMarks;
	}

	public void setMaxMarks(int maxMarks) {
		this.maxMarks = maxMarks;
	}

	public boolean getIsPass() {
		return isPass;
	}

	public void setIsPass(boolean isPass) {
		this.isPass = isPass;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Result(Long resultId, int marksObtained, int maxMarks, boolean isPass) {
		this.resultId = resultId;
		this.marksObtained = marksObtained;
		this.maxMarks = maxMarks;
		this.isPass = isPass;
	}

	public Result() {
		this(0L,0,0,false);
	}

	@Override
	public String toString() {
		return "Result [resultId=" + resultId + ", quizId=" + quizId + ", marksObtained=" + marksObtained
				+ ", maxMarks=" + maxMarks + ", isPass=" + isPass + "]";
	}
}
