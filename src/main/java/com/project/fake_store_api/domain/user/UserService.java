package com.project.fake_store_api.domain.user;

import com.project.fake_store_api.domain.role.Role;
import com.project.fake_store_api.domain.role.RoleRepository;
import com.project.fake_store_api.domain.role.RoleStatus;
import com.project.fake_store_api.domain.user.embeded_class.Address;
import com.project.fake_store_api.domain.user.embeded_class.Geolocation;
import com.project.fake_store_api.domain.user.embeded_class.Name;
import com.project.fake_store_api.domain.util.UserMapper;
import com.project.fake_store_api.global.error.BusinessLogicException;
import com.project.fake_store_api.global.error.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRepositoryImpl userRepositoryImpl;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public UserDto findOne(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        return UserMapper.toDto(user);
    }

    public List<UserDto> findWithLimit(Long limit) {
        List<User> findUser = userRepositoryImpl.findWithLimit(limit);
        return findUser.stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public UserDto save(UserDto userDto) {
        ValidateDuplicateEmail(userDto.getEmail());


        User user = createUserFromUserDto(new User(), userDto);
        setRole(user);

        User result = userRepository.save(user);
        return UserMapper.toDto(result);
    }

    private void setRole(User user) {
        Role role = roleRepository.findByRoleStatus(RoleStatus.ROLE_USER);
        user.getRoles().add(role);
    }

    private void ValidateDuplicateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BusinessLogicException(ExceptionCode.USER_DUPLICATE);
        }
    }

    public UserDto update(Long userId, UserDto userDto) {
        User user = createUserFromUserDto(userRepository.findById(userId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND)), userDto);

        return UserMapper.toDto(user);
    }

    public UserDto delete(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        userRepository.delete(user);

        return UserMapper.toDto(user);
    }

    public User login(UserLoginDto userLoginDto) {
        String email = userLoginDto.getEmail();
        String password = userLoginDto.getPassword();
        log.info("email = {}", email);
        log.info("password = {}", password);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessLogicException(ExceptionCode.PASSWORD_MISMATCH);
        }

        return user;
    }

    private User createUserFromUserDto(User user, UserDto userDto) {
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setName(Name.builder()
                .firstName(userDto.getNameDto().getFirstName())
                .lastName(userDto.getNameDto().getLastName())
                .build());
        user.setAddress(Address.builder()
                .city(userDto.getAddressDto().getCity())
                .street(userDto.getAddressDto().getStreet())
                .zipcode(userDto.getAddressDto().getZipcode())
                .number(userDto.getAddressDto().getNumber())
                .geolocation(Geolocation.builder()
                        .lat(userDto.getAddressDto().getGeolocationDto().getLat())
                        .lng(userDto.getAddressDto().getGeolocationDto().getLng())
                        .build())
                .build());
        user.setPhone(userDto.getPhone());
        return user;
    }
}
