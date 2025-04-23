package com.myfirst.microservice.service;

import com.myfirst.microservice.entity.Role;
import com.myfirst.microservice.entity.User;
import com.myfirst.microservice.repository.RoleRepository;
import com.myfirst.microservice.repository.UserRepository;
import com.myfirst.microservice.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private ModelMapper mapper;

    @CachePut(value="USER_CACHE",key="#result.username")
    public UserDTO create(UserDTO dto) {
        User user = mapper.map(dto, User.class);
        User savedUser = userRepo.save(user);
        return mapper.map(savedUser, UserDTO.class);
    }

    public UserDTO update(Long id, UserDTO dto) {
        User user = userRepo.findById(id).orElseThrow();
        dto.setId(id);
        mapper.map(dto, user);
       // Set<Role> roles = new HashSet<>(roleRepo.findAllById(dto.getRoleIds()));
        //user.setRoles(roles);
       // user.setRoles(roleRepo.findAllById(dto.getRoleIds()).stream().collect(Collectors.toSet()));
        return mapper.map(userRepo.save(user), UserDTO.class);
    }

    public UserDTO getById(Long id) {
        return userRepo.findById(id).map(u -> mapper.map(u, UserDTO.class)).orElseThrow();
    }

    @Cacheable(value = "USER_CACHE", key = "#username")
    public UserDTO getByName(String username) {
        return userRepo.findByUsername(username).map(u -> mapper.map(u, UserDTO.class)).orElseThrow();
    }

    public List<UserDTO> getAll() {
        return userRepo.findAll().stream().map(u -> mapper.map(u, UserDTO.class)).toList();
    }

    public Page<UserDTO> getAllPaged(Pageable pageable) {
        return userRepo.findAll(pageable).map(u -> mapper.map(u, UserDTO.class));
    }

    @CacheEvict(value="USER_CACHE",key="#id")
    public void delete(Long id) {
        userRepo.deleteById(id);
    }

    public UserDTO patch(Long id, Map<String, Object> updates) {
        User user = userRepo.findById(id).orElseThrow();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(User.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, user, value);
            }
        });
        return mapper.map(userRepo.save(user), UserDTO.class);
    }
}
