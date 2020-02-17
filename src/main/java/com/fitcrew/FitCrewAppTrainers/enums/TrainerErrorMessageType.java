package com.fitcrew.FitCrewAppTrainers.enums;

public enum TrainerErrorMessageType {

	NO_CLIENT_BOUGHT_TRAINING("No client bought training"),
	NOT_SUCCESSFULLY_MAPPING("Trainer object not mapped successfully"),
	NO_CLIENT_FOUND("No client found"),
	NO_TRAINER_DELETED("No trainer deleted"),
	NO_TRAINER_UPDATED("No trainer updated"),
	NO_TRAINERS("No trainers found"),
	NO_TRAINER("No trainer found"),
	RATING_ERROR("Rating failed. Something goes wrong"),
	NO_TRAINERS_SORDER("No trainers sorted"),
	NO_TRAININGS("No trainings found"),
	NO_TRAINING_SELECTED("No trainings selected"),
	NO_TRAINING_UPDATED("No training updated"),
	NO_TRAINING_DELETED("No training deleted"),
	NO_TRAINING_CREATED("No training created"),
	BUY_TRAINING_ERROR("No client has bought trainings from trainer"),
	NO_EMAIL_SENT("Email sending failed"),
	NO_CLIENT_SAVED("Client save failed");

	TrainerErrorMessageType(String text) {
	}
}
