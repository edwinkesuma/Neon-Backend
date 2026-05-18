package com.edwin_kesuma.Neon.repositories;

import com.edwin_kesuma.Neon.domain.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
