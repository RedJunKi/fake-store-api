package com.project.fake_store_api.domain.user;

import com.project.fake_store_api.domain.user.embeded_class.Address;
import com.project.fake_store_api.domain.user.embeded_class.Geolocation;
import com.project.fake_store_api.domain.user.embeded_class.Name;
import com.project.fake_store_api.domain.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRepositoryImpl userRepositoryImpl;

    public List<UserDto> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public UserDto findOne(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("해당 유저가 존재하지 않습니다."));
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

        User result = userRepository.save(user);
        return UserMapper.toDto(result);
    }

    private void ValidateDuplicateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }
    }

    public UserDto update(Long userId, UserDto userDto) {
        User user = createUserFromUserDto(userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("해당 회원이 존재하지 않습니다.")), userDto);

        return UserMapper.toDto(user);
    }

    public UserDto delete(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("해당 회원이 존재하지 않습니다."));
        userRepository.delete(user);

        return UserMapper.toDto(user);
    }

    private static User createUserFromUserDto(User user, UserDto userDto) {
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
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
