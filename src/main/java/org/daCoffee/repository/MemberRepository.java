package org.daCoffee.repository;

import org.daCoffee.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String>, JpaSpecificationExecutor<Member> {

  // 이름 + 이메일로 아이디 조회
  @Query("SELECT m.memberId FROM Member m WHERE m.memberName = :memberName AND m.memberEmail = :memberEmail")
  List<String> findMemberIdByNameAndEmail(@Param("memberName") String memberName, @Param("memberEmail") String memberEmail);

  // 아이디 + 이메일로 비밀번호 조회
  @Query("SELECT m.memberPassword FROM Member m WHERE m.memberId = :memberId AND m.memberEmail = :memberEmail")
  Optional<String> findPasswordByMemberIdAndEmail(@Param("memberId") String memberId, @Param("memberEmail") String memberEmail);

  // 회원 등급 업데이트
  @Modifying
  @Query("UPDATE Member m SET m.memberTier = :memberTier WHERE m.memberId = :memberId")
  void updateMemberTier(@Param("memberId") String memberId, @Param("memberTier") int memberTier);

  // 회원 사업자코드 업데이트
  @Modifying
  @Query("UPDATE Member m SET m.memberFranCode = :memberFranCode WHERE m.memberId = :memberId")
  void updateMemberFranCode(@Param("memberId") String memberId, @Param("memberFranCode") String memberFranCode);

  // 회원 기존 비밀번호 임시 비밀번호로 업데이트
  @Modifying
  @Query("UPDATE Member m SET m.memberPassword = :memberPassword WHERE m.memberId = :memberId")
  void updateMemberPassword(@Param("memberId") String memberId, @Param("memberPassword") String memberPassword);

  // 회원 비활성화 상태 변경
  @Modifying
  @Query("UPDATE Member m SET m.memberDisabledStatus = :status WHERE m.memberId = :memberId")
  void updateDisabledStatus(@Param("memberId") String memberId, @Param("status") boolean status);

  // 회원 비활성화 여부 확인
  @Query("SELECT COUNT(m) FROM Member m WHERE m.memberId = :memberId AND m.memberDisabledStatus = true")
  int countDisabledMember(@Param("memberId") String memberId);

  // 전체 회원 목록
  @Query("SELECT m FROM Member m ORDER BY m.memberTier")
  List<Member> findAllOrderByMemberTier();

  // ===================== Admin =====================

  // 비활성화 회원 목록 페이징
  @Query("SELECT m FROM Member m WHERE m.memberDisabledStatus = true")
  Page<Member> findDisabledMembers(Pageable pageable);
}