package com.fitcrew.FitCrewAppTrainers.domains;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document
@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
public class EmailDocument {

	@Id
	private String id;

	@Field(value = "SENDER")
	@NotNull
	@Length(max = 20)
	private String sender;

	@Field(value = "RECIPIENT")
	@NotNull
	@Length(max = 50)
	private String recipient;

	@Field(value = "SUBJECT")
	@NotNull
	@Length(max = 20)
	private String subject;

	@Field(value = "BODY_OF_MESSAGE")
	@NotNull
	private String bodyOfMessage;

	@Field(value = "FILE_PATH_TO_ATTACHMENT")
	private String filePathToAttachment;
}
