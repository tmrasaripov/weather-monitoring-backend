package com.weather.backend.dto;

import com.weather.backend.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Укажите логин")
    @Size(min = 3, max = 50, message = "Логин должен содержать от 3 до 50 символов")
    private String username;

    @NotBlank(message = "Укажите Email")
    @Email(message = "Некорректный Email — введите адрес с @ и доменом (например: user@mail.ru)")
    private String email;

    @NotBlank(message = "Укажите пароль")
    @Size(min = 6, max = 100, message = "Пароль должен содержать минимум 6 символов")
    private String password;

    private User.Role role;
}