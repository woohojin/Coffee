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

  // ===================== Admin =====================

  // 전체 주문 수
  @Query("SELECT COUNT(h) FROM OrderHistory h")
  int countAll();

  // orderId 검색
  Page<OrderHistory> findByOrderIdContaining(String keyword, Pageable pageable);

  // memberId 검색
  Page<OrderHistory> findByMemberIdContaining(String keyword, Pageable pageable);

  // orderId + memberId 동시 검색
  Page<OrderHistory> findByOrderIdContainingAndMemberIdContaining(
    String orderId, String memberId, Pageable pageable);

  // 날짜 범위 검색 (전체)
  Page<OrderHistory> findByOrderDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

  // orderId + 날짜 범위 검색
  Page<OrderHistory> findByOrderIdContainingAndOrderDateBetween(
    String orderId, LocalDateTime start, LocalDateTime end, Pageable pageable);

  // memberId + 날짜 범위 검색
  Page<OrderHistory> findByMemberIdContainingAndOrderDateBetween(
    String memberId, LocalDateTime start, LocalDateTime end, Pageable pageable);

  // orderId + memberId + 날짜 범위 검색
  Page<OrderHistory> findByOrderIdContainingAndMemberIdContainingAndOrderDateBetween(
    String orderId, String memberId, LocalDateTime start, LocalDateTime end, Pageable pageable);

  // 주문 삭제
  @Modifying
  @Query("DELETE FROM OrderHistory h WHERE h.orderId = :orderId AND h.productCode = :productCode")
  int deleteByOrderIdAndProductCode(@Param("orderId") String orderId, @Param("productCode") String productCode);

}
