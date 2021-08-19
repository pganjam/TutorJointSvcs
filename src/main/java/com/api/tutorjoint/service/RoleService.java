package com.api.tutorjoint.service;

import com.api.tutorjoint.model.ERole;
import com.api.tutorjoint.model.Role;
import com.api.tutorjoint.model.User;
import com.api.tutorjoint.repository.RoleRepository;
import com.api.tutorjoint.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean addTutorRole(User user) {
        Role userRole = roleRepository.findByName(ERole.ROLE_TUTOR)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        Set<Role> roles = user.getRoles();
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);

        return true;
    }

    public boolean addStudentRole(User user) {
        Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        Set<Role> roles = user.getRoles();
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);

        return true;
    }


}
