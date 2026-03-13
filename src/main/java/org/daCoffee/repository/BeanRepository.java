package org.daCoffee.repository;

import org.daCoffee.entity.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeanRepository extends JpaRepository<Bean, String> {
}
