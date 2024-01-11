package com.ra.service.role;

import com.ra.model.entity.Role;

public interface RoleService {
    Role findByRoleName(String name);
}
