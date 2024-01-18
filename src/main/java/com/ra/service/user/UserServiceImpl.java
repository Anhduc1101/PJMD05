package com.ra.service.user;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.UserRequestDTO;
import com.ra.model.dto.response.UserInfoResponseDTO;
import com.ra.model.dto.response.UserLoginResponseDTO;
import com.ra.model.dto.response.UserResponseDTO;
import com.ra.model.entity.Cart;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import com.ra.model.entity.WishList;
import com.ra.repository.UserRepository;
import com.ra.security.jwt.JWTProvider;
import com.ra.security.user_principle.UserPrinciple;
import com.ra.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private RoleService roleService;

    @Override
    public List<UserResponseDTO> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserResponseDTO::new).toList();
    }

    @Override
    public User register(User user) {
//        ma hoa mat khau
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        role
        Set<Role> roles = new HashSet<>();
//        register cua user thi coi no la USER
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            roles.add(roleService.findByRoleName("USER"));
        } else {
//        tao tai khoan va phan quyen thi phai co quyen ADMIN
            user.getRoles().forEach(role -> roles.add(roleService.findByRoleName(role.getName())));
        }
        User newUser = new User();
        newUser.setUserName(user.getUserName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setStatus(user.getStatus());
        newUser.setGender(user.getGender());
        newUser.setPhone(user.getPhone());
        newUser.setAge(user.getAge());
        newUser.setRoles(roles);
        newUser.setAddress(user.getAddress());
        newUser.setOrders(user.getOrders());
        newUser.setCart(user.getCart());
        newUser.setWishList(user.getWishList());
        return userRepository.save(newUser);
    }

    @Override
    public UserLoginResponseDTO login(UserRequestDTO userRequestDTO) {
        Authentication authentication;
        authentication = authenticationProvider
                .authenticate(new UsernamePasswordAuthenticationToken(userRequestDTO.getUserName(), userRequestDTO.getPassword()));
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        return UserLoginResponseDTO.builder()
                .token(jwtProvider.generateToken(userPrinciple))
                .userName(userPrinciple.getUsername())
                .roles(userPrinciple.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .status(userPrinciple.getStatus())
                .build();
    }

    @Override
    public Page<UserResponseDTO> getAll(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(UserResponseDTO::new);
    }

    @Override
    public Page<UserResponseDTO> searchByName(Pageable pageable, String name) {
        Page<User> userPage = userRepository.findByUserNameContainingIgnoreCase(pageable, name);
        return userPage.map(UserResponseDTO::new);
    }

    @Override
    public void changeStatus(Long id) throws CustomException {
        UserResponseDTO userResponseDTO = findById(id);
        if (userResponseDTO.getRoles().contains("ADMIN")){
            throw new CustomException("This account can not be block !!!");
        }
        if (userResponseDTO != null&&!userResponseDTO.getRoles().contains("ADMIN")) {
            userRepository.changeStatus(id);
        }
    }

    @Override
    public void changeRole(Long id) throws CustomException {
        UserResponseDTO userResponseDTO = findById(id);
        User user = userRepository.findById(id).orElse(null);
        if (userResponseDTO.getRoles().stream().anyMatch(role -> role.equals("ADMIN"))) {
            throw new CustomException("This account can not be change!!!");
        }
        if (userResponseDTO.getRoles().stream().anyMatch(role -> role.equals("SUB_ADMIN"))) {
            userResponseDTO.getRoles().removeIf(role -> role.equals("SUB_ADMIN"));
            userResponseDTO.getRoles().add("USER");
        } else {
            userResponseDTO.getRoles().removeIf(role -> role.equals("USER"));
            userResponseDTO.getRoles().add("SUB_ADMIN");
        }
        Set<Role> roles = userResponseDTO.getRoles().stream().map(role -> roleService.findByRoleName(role)).collect(Collectors.toSet());
        userRepository.save(User.builder()
                .id(userResponseDTO.getId())
                .userName(userResponseDTO.getUserName())
                .address(userResponseDTO.getAddress())
                .roles(roles)
                .phone(userResponseDTO.getPhone())
                .status(userResponseDTO.getStatus())
                .email(userResponseDTO.getEmail())
                .password(user.getPassword())
                .gender(Boolean.valueOf(userResponseDTO.getGender()))
                .build());
    }

    @Override
    public UserResponseDTO findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(UserResponseDTO::new).orElse(null);
    }

    @Override
    public UserInfoResponseDTO findByUserId(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(UserInfoResponseDTO::new).orElse(null);
    }

    @Override
    public UserResponseDTO addNewRole(UserRequestDTO userRequestDTO, Long id) throws CustomException {
        UserResponseDTO userResponseDTO = findById(userRequestDTO.getId());
        User user = userRepository.findById(id).orElse(null);
        if (userResponseDTO.getRoles().stream().anyMatch(role -> role.equals("ADMIN"))) {
            throw new CustomException("This account can not add new role!!!");
        }
        if (userResponseDTO.getRoles().stream().anyMatch(role -> role.equals("ADMIN") || role.equals("SUB_ADMIN"))) {
            Set<Role> roles = userResponseDTO.getRoles().stream()
                    .map(role -> roleService.findByRoleName(role))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
            userRepository.save(user);
            userResponseDTO.setRoles(roles.stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet()));
        }
        return userResponseDTO;
    }

    @Override
    public boolean checkPass(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public void changePassword(User user, String confirmPassword) {
        user.setPassword(passwordEncoder.encode(confirmPassword));
        userRepository.save(user);
    }

    @Override
    public UserResponseDTO save(Long userId, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(userId).orElse(null);
        user.setId(user.getId());
        user.setAddress(userRequestDTO.getAddress());
        user.setUserName(userRequestDTO.getUserName());
        user.setGender(userRequestDTO.getGender());
        user.setPhone(userRequestDTO.getPhone());
        user.setEmail(userRequestDTO.getEmail());
        user.setAge(userRequestDTO.getAge());
        userRepository.save(user);
        return new UserResponseDTO(user);
    }

}
