package com.ra.repository;

import com.ra.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);

    Page<User> findByUserNameContainingIgnoreCase(Pageable pageable, String name);

    @Modifying
    @Query("update User u set u.status = case when u.status = true then false else true end where u.id=?1")
    void changeStatus(Long id);

    User existsByUserNameContainingIgnoreCase(String name);
    User existsByEmailContainingIgnoreCase(String name);
    User existsByPasswordContainingIgnoreCase(String name);
}
