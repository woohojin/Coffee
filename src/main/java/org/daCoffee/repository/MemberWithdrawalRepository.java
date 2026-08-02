package org.daCoffee.repository;

import org.daCoffee.entity.MemberWithdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MemberWithdrawalRepository extends JpaRepository<MemberWithdrawal, String>, JpaSpecificationExecutor<MemberWithdrawal> {
}
