package kg.manurov.weathergridservice.services.impl;

import kg.manurov.weathergridservice.entities.User;
import kg.manurov.weathergridservice.repositories.UserRepository;
import kg.manurov.weathergridservice.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public Long createUser(String username) {
        log.info("Creating user with username={}", username);
        User user = new User();
        user.setUsername(username);
        return repository.save(user).getId();
    }
}
