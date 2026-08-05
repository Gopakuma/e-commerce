package com.ecommerce_demo.ecommerce.repository;

import com.ecommerce_demo.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
    
    List<User> findByEnabled(boolean enabled);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.enabled = :enabled WHERE u.email = :email")
    int updateUserStatus(@Param("email") String email, @Param("enabled") Boolean enabled);
}
