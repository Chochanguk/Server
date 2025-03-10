package taba.tabaServer.tabaserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taba.tabaServer.tabaserver.domain.DrivingSession;
import taba.tabaServer.tabaserver.domain.SensorData;
import taba.tabaServer.tabaserver.dto.sensordatadto.SensorDataRequestDto;
import taba.tabaServer.tabaserver.dto.sensordatadto.SensorDataResponseDto;
import taba.tabaServer.tabaserver.enums.DrivingStatus;
import taba.tabaServer.tabaserver.enums.ErrorStatus;
import taba.tabaServer.tabaserver.exception.CommonException;
import taba.tabaServer.tabaserver.exception.ErrorCode;
import taba.tabaServer.tabaserver.repository.DrivingSessionRepository;
import taba.tabaServer.tabaserver.repository.SensorDataRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;
    private final DrivingSessionRepository drivingSessionRepository;

    @Transactional
    public SensorDataResponseDto createSensorData(SensorDataRequestDto sensorDataRequestDto){
        DrivingSession drivingSession = drivingSessionRepository.findById(sensorDataRequestDto.drivingSessionId())
                .orElseThrow(()-> new CommonException(ErrorCode.NOT_FOUND_DRIVING_SESSION));

        if(drivingSession.getDrivingStatus() != DrivingStatus.DRIVING){
            throw new CommonException(ErrorCode.DRIVING_STATUS_NONE);
        }

        SensorData save = sensorDataRepository.save(SensorData.builder()
                .drivingSession(drivingSession)
                .brakePressure(sensorDataRequestDto.brakePressure())
                .accelPressure(sensorDataRequestDto.accelPressure())
                .speed(sensorDataRequestDto.speed())
                .latitude(sensorDataRequestDto.latitude())
                .longitude(sensorDataRequestDto.longitude())
                .build()
        );

        return SensorDataResponseDto.builder()
                .sensorId(save.getId())
                .drivingSessionId(save.getDrivingSession().getId())
                .brakePressure(save.getBrakePressure())
                .accelPressure(save.getAccelPressure())
                .timeStamp(save.getTimestamp())
                .speed(save.getSpeed())
                .latitude(save.getLatitude())
                .longitude(save.getLongitude())
                .errorStatus(drivingSession.getErrorStatus())
                .build();
    }

    @Transactional
    public Boolean deleteSensorDataById(Long id){
        sensorDataRepository.deleteById(id);
        return Boolean.TRUE;
    }

    @Transactional
    public Boolean deleteSensorDataByDrivingSessionId(Long id){
        List<SensorData> sensorDataByDrivingSessionId = sensorDataRepository.findSensorDataByDrivingSessionId(id);
        sensorDataRepository.deleteAll(sensorDataByDrivingSessionId);
        return Boolean.TRUE;
    }

    @Transactional
    public List<SensorDataResponseDto> getAllSensorDataByDrivingSessionId(Long id){
        return sensorDataRepository.findSensorDataByDrivingSessionId(id).stream()
                .map(sensorData -> SensorDataResponseDto.of(
                        sensorData.getId(),
                        sensorData.getDrivingSession().getId(),
                        sensorData.getTimestamp(),
                        sensorData.getBrakePressure(),
                        sensorData.getAccelPressure(),
                        sensorData.getSpeed(),
                        sensorData.getLatitude(),
                        sensorData.getLongitude(),
                        sensorData.getDrivingSession().getErrorStatus()
                )).collect(Collectors.toList());
    }

    @Transactional
    public ByteArrayInputStream getSensorDataAsCsvForSession(Long sessionId){   //error상태이고, 세션id가 주어져야함
        Optional<DrivingSession> sessionOptional = drivingSessionRepository.findById(sessionId);
        if (sessionOptional.isPresent() && sessionOptional.get().getErrorStatus() == ErrorStatus.ERROR) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(out);
            writer.println("SessionId,Timestamp,BrakePressure,AccelPressure,Speed,Latitude,Longitude");

            List<SensorData> sensorDataList = sensorDataRepository.findAllByDrivingSession(sessionOptional.get());
            for (SensorData data : sensorDataList) {
                writer.printf("%d,%s,%f,%f,%f,%s,%s\n",
                        sessionOptional.get().getId(), data.getTimestamp(),
                        data.getBrakePressure(), data.getAccelPressure(),
                        data.getSpeed(), data.getLatitude(), data.getLongitude());
            }
            writer.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } else {
            return null; // Or handle this case appropriately based on your application needs
        }
    }
}
