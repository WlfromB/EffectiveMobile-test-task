package org.example.effectivemobiletesttask.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Ответ API по запросу на 2 токена.")
public class JwtResponse {
    private static final String EMPTY_TOKEN = "";

    @Schema(description = "Тип токена. Задан Bearer")
    private final String type = "Bearer";
    @Schema(description = "Строка заключающая кратковременный access token", example = "super.secret.token")
    private String accessToken;
    @Schema(description = "Строка заключающая долговременный refresh token", example = "super.secret.token")
    private String refreshToken;

    public JwtResponse() {
    }

    public static JwtResponse responseWithTwoToken(String accessToken, String refreshToken) {
        JwtResponse response = new JwtResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    public static JwtResponse responseOnlyAccessToken(String accessToken) {
        return responseWithTwoToken(accessToken, EMPTY_TOKEN);
    }

}
