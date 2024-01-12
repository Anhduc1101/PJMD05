package com.ra.service.user;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.UserRequestDTO;
import com.ra.model.dto.response.UserLoginResponseDTO;
import com.ra.model.dto.response.UserResponseDTO;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
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
    public void changeStatus(Long id) {
        UserResponseDTO userResponseDTO = findById(id);
        if (userResponseDTO != null) {
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
        userResponseDTO.getRoles().removeIf(role -> role.equals("USER"));
        userResponseDTO.getRoles().add("SUB_ADMIN");
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
    public UserResponseDTO addNewRole(UserRequestDTO userRequestDTO) throws CustomException {
        UserResponseDTO userResponseDTO = findById(userRequestDTO.getId());
        if (userResponseDTO.getRoles().contains("ADMIN")) {
            throw new CustomException("This Account can not be change !!!");
        }
        if (userResponseDTO.getRoles().size() == 1 && userResponseDTO.getRoles().contains("USER")) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleService.findByRoleName("SUB_ADMIN"));

        }
        return null;
    }

//    @Override
//    public UserResponseDTO deleteRole(UserRequestDTO userRequestDTO) throws CustomException {
//        UserResponseDTO userResponseDTO = findById(userRequestDTO.getId());
//        if (userResponseDTO.getRoles().equals("ADMIN")) {
//            throw new CustomException("This Account can not be change !!!");
//        }
//        if (userResponseDTO.getRoles().size() == 1 && userResponseDTO.getRoles().equals("USER")) {
//            throw new CustomException("This account does not have full permission");
//        }
//        if (userResponseDTO.getRoles().size() < 2) {
//            throw new CustomException("This account has only unique role, can not be delete role!!!");
//        }
//        if (userResponseDTO.getRoles().size() > 2 && !userResponseDTO.getRoles().equals("ADMIN")) {
//            Set<Role> roles=new HashSet<>();
//            roles.add(roleService.findByRoleName("SUB_ADMIN"));
//        }
//        return null;
//    }
}
