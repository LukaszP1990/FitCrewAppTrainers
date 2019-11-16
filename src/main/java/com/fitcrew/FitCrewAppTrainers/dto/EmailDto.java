package com.fitcrew.FitCrewAppTrainers.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(value = "Email")
@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
public class EmailDto implements Serializable {

    private static final long serialVersionUID = 4470120338928967887L;

    @NotNull(message = "Email sender name cannot be null")
    @Size(min = 2, max = 20, message = "Email sender name must be equal or grater than 2 characters and less than 20 character")
    @ApiModelProperty(value = "Email sender")
    private String sender;

    @NotNull(message = "Email recipient name cannot be null")
    @Size(min = 2, max = 50, message = "Email recipient name must be equal or grater than 2 characters and less than 20 character")
    @ApiModelProperty(value = "Email recipient")
    private String recipient;

    @NotNull(message = "Email subject cannot be null")
    @Size(min = 2, max = 20, message = "Email subject must be equal or grater than 2 characters and less than 20 character")
    @ApiModelProperty(value = "Subject of email message")
    private String subject;

    @NotNull(message = "Email message cannot be null")
    @ApiModelProperty(value = "Body of email message")
    private String bodyOfMessage;

    @ApiModelProperty(value = "Email attachment")
    private String filePathToAttachment;
}
