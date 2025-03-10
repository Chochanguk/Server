package taba.tabaServer.tabaserver.dto.calibrationdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import taba.tabaServer.tabaserver.domain.Calibration;
import taba.tabaServer.tabaserver.domain.Car;
import taba.tabaServer.tabaserver.enums.SensorType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
public record CalibrationResponseDto(
        @JsonProperty("id") Long id,
        @JsonProperty("sensorType")SensorType sensorType,
        @JsonProperty("pressureMax") double pressureMax,
        @JsonProperty("pressureMin") double pressureMin,
        @JsonProperty("createdAt")LocalDateTime calibrationTime,
        @JsonProperty("carId")Long carId
        ) implements Serializable {
            public static CalibrationResponseDto of(
                    final Long id,
                    final SensorType sensorType,
                    final double pressureMax,
                    final double pressureMin,
                    final LocalDateTime calibrationTime,
                    final Long carId
            )    {
                return CalibrationResponseDto.builder()
                        .id(id)
                        .sensorType(sensorType)
                        .pressureMax(pressureMax)
                        .pressureMin(pressureMin)
                        .calibrationTime(calibrationTime)
                        .carId(carId)
                        .build();
            }
}
