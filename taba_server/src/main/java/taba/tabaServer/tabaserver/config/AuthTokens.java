package taba.tabaServer.tabaserver.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokens {
    /**
     * 사용자에게 내려주는 서비스의 인증 토큰 값
     */
    private String accessToken;
    private String refreshToken;
    private String grantType;
    private Long expiresIn;

    public static AuthTokens of(String accessToken, String refreshToken, String grantType, Long expiresIn ){
        return new AuthTokens(accessToken, refreshToken, grantType, expiresIn);
    }
}
