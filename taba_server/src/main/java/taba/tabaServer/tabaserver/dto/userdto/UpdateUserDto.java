package taba.tabaServer.tabaserver.dto.userdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import taba.tabaServer.tabaserver.enums.GenderEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Builder
public record UpdateUserDto(
        @JsonProperty("email") String email,
        @JsonProperty("password") String password,
        @JsonProperty("gender") GenderEnum gender,
        @JsonProperty("userBirth") LocalDate userBirth
) implements Serializable {
    public static UpdateUserDto of(
            final String email,
            final String password,
            final GenderEnum gender,
            final LocalDate userBirth
    ) {
        return UpdateUserDto.builder()
                .email(email)
                .password(password)
                .gender(gender)
                .userBirth(userBirth)
                .build();
    }
}

