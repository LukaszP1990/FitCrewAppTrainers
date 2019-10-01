package com.fitcrew.FitCrewAppTrainers.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fitcrew.FitCrewAppTrainers.dto.TrainingDto;

public class TrainingResourceMockUtil {

	public static List<TrainingDto> getListOfTrainings() {
		return IntStream.rangeClosed(1, 3)
				.mapToObj(value -> prepareTrainingData(value))
				.collect(Collectors.toList());
	}

	public static TrainingDto getTraining(int value) {
		return prepareTrainingData(value);
	}

	public static TrainingDto updateTrainingDto(int value) {
		return prepareTrainingData(value);
	}

	private static TrainingDto prepareTrainingData(int value) {
		return TrainingDto.builder()
				.description("default description")
				.trainerEmail("mockedTrainer@gmail.com")
				.trainingName("default name" + " " + value)
				.training("some training")
				.build();
	}
}
