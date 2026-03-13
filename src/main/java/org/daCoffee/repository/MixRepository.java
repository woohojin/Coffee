package org.daCoffee.repository;

import org.daCoffee.entity.Mix;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MixRepository extends JpaRepository<Mix, String> {
}
