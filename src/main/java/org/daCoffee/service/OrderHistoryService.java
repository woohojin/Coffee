package org.daCoffee.service;

import lombok.RequiredArgsConstructor;
import org.daCoffee.entity.OrderHistory;
import org.daCoffee.repository.OrderHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderHistoryService {

  private final OrderHistoryRepository orderHistoryRepository;

  // 주문 저장
  @Transactional
  public void save(OrderHistory orderHistory) {
    orderHistoryRepository.save(orderHistory);
  }

  // 특정 회원의 주문 목록 조회
  @Transactional(readOnly = true)
  public List<OrderHistory> findByMemberId(String memberId) {
    return orderHistoryRepository.findByMemberIdOrderByOrderDateDesc(memberId);
  }

  // 특정 회원의 날짜 범위 주문 목록
  @Transactional(readOnly = true)
  public List<OrderHistory> findByMemberIdBetween(String memberId, LocalDateTime start, LocalDateTime end) {
    return orderHistoryRepository.findByMemberIdAndOrderDateBetween(memberId, start, end);
  }

  // 단건 조회
  @Transactional(readOnly = true)
  public Optional<OrderHistory> findByOrderIdAndProductCode(String orderId, String productCode) {
    return orderHistoryRepository.findByOrderIdAndProductCode(orderId, productCode);
  }
}
