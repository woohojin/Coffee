package org.daCoffee.repository;

import org.daCoffee.entity.OrderHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Integer> {
  // 특정 회원의 주문 목록 조회
  List<OrderHistory> findByMemberIdOrderByOrderDateDesc(String memberId);

  // orderId + productCode로 단건 조회
  Optional<OrderHistory> findByOrderIdAndProductCode(String orderId, String productCode);

  // 특정 회원의 날짜 범위 주문 목록
  List<OrderHistory> findByMemberIdAndOrderDateBetween(String memberId, LocalDateTime start, LocalDateTime end);

}
