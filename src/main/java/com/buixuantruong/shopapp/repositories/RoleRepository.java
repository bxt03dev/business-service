package com.buixuantruong.shopapp.repositories;

import com.buixuantruong.shopapp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
