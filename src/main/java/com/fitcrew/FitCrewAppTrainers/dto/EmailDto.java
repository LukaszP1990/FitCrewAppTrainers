package com.fitcrew.FitCrewAppTrainers.dto;

import com.fitcrew.FitCrewAppTrainers.common.ValidationErrorMessage;
import com.fitcrew.FitCrewAppTrainers.dto.validation.NotAllRequiredValueSetInEmailDto;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor(onConstructor = @__(@Builder))
@NoArgsConstructor
@ToString
@NotAllRequiredValueSetInEmailDto
public class EmailDto implements Serializable {

    private static final long serialVersionUID = 4470120338928967887L;

    @Size(min = 2, max = 20, message = ValidationErrorMessage.SENDER_ERROR_MESSAGE)
    private String sender;

    @Size(min = 2, max = 50, message = ValidationErrorMessage.RECIPIENT_ERROR_MESSAGE)
    private String recipient;

    @Size(min = 2, max = 20, message = ValidationErrorMessage.SUBJECT_ERROR_MESSAGE)
    private String subject;

    private String bodyOfMessage;
    private String filePathToAttachment;
}
