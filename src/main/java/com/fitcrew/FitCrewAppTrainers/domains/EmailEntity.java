package com.fitcrew.FitCrewAppTrainers.domains;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Email")
@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
public class EmailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long emailId;

	@Column(nullable = false, length = 20)
	private String sender;

	@Column(nullable = false, length = 50)
	private String recipient;

	@Column(nullable = false, length = 20)
	private String subject;

	@Column(nullable = false)
	private String bodyOfMessage;

	private String filePathToAttachment;
}
