package com.myfirst.microservice.service;

import com.myfirst.microservice.dto.RoleDTO;
import com.myfirst.microservice.entity.Role;
import com.myfirst.microservice.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private ModelMapper mapper;

    public RoleDTO create(RoleDTO dto) {
        Role role = mapper.map(dto, Role.class);
        return mapper.map(roleRepo.save(role), RoleDTO.class);
    }

    public RoleDTO update(Long id, RoleDTO dto) {
        Role role = roleRepo.findById(id).orElseThrow();
        mapper.typeMap(RoleDTO.class, Role.class).addMappings(m -> m.skip(Role::setId));
        mapper.map(dto, role);
        return mapper.map(roleRepo.save(role), RoleDTO.class);
    }

    public RoleDTO getById(Long id) {
        return roleRepo.findById(id).map(r -> mapper.map(r, RoleDTO.class)).orElseThrow();
    }

    public List<RoleDTO> getAll() {
        return roleRepo.findAll().stream().map(r -> mapper.map(r, RoleDTO.class)).toList();
    }

    public Page<RoleDTO> getAllPaged(Pageable pageable) {
        return roleRepo.findAll(pageable).map(r -> mapper.map(r, RoleDTO.class));
    }

    public void delete(Long id) {
        roleRepo.deleteById(id);
    }

    public RoleDTO patch(Long id, Map<String, Object> updates) {
        Role role = roleRepo.findById(id).orElseThrow();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Role.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, role, value);
            }
        });
        return mapper.map(roleRepo.save(role), RoleDTO.class);
    }
}
