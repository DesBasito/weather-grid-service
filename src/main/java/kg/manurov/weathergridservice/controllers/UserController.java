package kg.manurov.weathergridservice.controllers;

import jakarta.validation.constraints.NotNull;
import kg.manurov.weathergridservice.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestParam @Validated @NotNull String username) {
        return ResponseEntity.ok(userService.createUser(username));
    }
}
