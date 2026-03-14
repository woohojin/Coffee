package org.daCoffee.service;

import lombok.RequiredArgsConstructor;
import org.daCoffee.dto.request.MemberUpdateRequestDTO;
import org.daCoffee.entity.Member;
import org.daCoffee.entity.MemberWithdrawal;
import org.daCoffee.repository.CartRepository;
import org.daCoffee.repository.MemberRepository;
import org.daCoffee.repository.MemberWithdrawalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
  private final MemberRepository memberRepository;
  private final MemberWithdrawalRepository memberWithdrawalRepository;
  private final CartRepository cartRepository;

  // 회원 단건 조회
  @Transactional(readOnly = true)
  public Optional<Member> findById(String memberId) {
    return memberRepository.findById(memberId);
  }

  // 회원 존재 여부 확인
  @Transactional(readOnly = true)
  public boolean existsById(String memberId) {
    return memberRepository.existsById(memberId);
  }

  // 회원 비활성화 여부 확인
  @Transactional(readOnly = true)
  public boolean isDisabled(String memberId) {
    return memberRepository.countDisabledMember(memberId) > 0;
  }

  // 회원 저장 (가입/수정)
  @Transactional
  public void save(Member member) {
    memberRepository.save(member);
  }

  // 회원 삭제
  @Transactional
  public void delete(String memberId) {
    memberRepository.deleteById(memberId);
  }

  // 아이디 찾기
  @Transactional(readOnly = true)
  public List<String> findMemberIdByNameAndEmail(String memberName, String memberEmail) {
    return memberRepository.findMemberIdByNameAndEmail(memberName, memberEmail);
  }

  // 비밀번호 찾기
  @Transactional(readOnly = true)
  public Optional<String> findPasswordByMemberIdAndEmail(String memberId, String memberEmail) {
    return memberRepository.findPasswordByMemberIdAndEmail(memberId, memberEmail);
  }

  // 임시 비밀번호 업데이트
  @Transactional
  public void updatePassword(String memberId, String encodedPassword) {
    memberRepository.updateMemberPassword(memberId, encodedPassword);
  }

  // 등급 업데이트
  @Transactional
  public void updateMemberTier(String memberId, int memberTier) {
    memberRepository.updateMemberTier(memberId, memberTier);
  }

  // 사업자 코드 업데이트
  @Transactional
  public void updateMemberFranCode(String memberId, String memberFranCode) {
    memberRepository.updateMemberFranCode(memberId, memberFranCode);
  }

  // 회원 비활성화
  @Transactional
  public void disableMember(String memberId) {
    memberRepository.updateDisabledStatus(memberId, true);
  }

  // 회원 활성화
  @Transactional
  public void enableMember(String memberId) {
    memberRepository.updateDisabledStatus(memberId, false);
  }

  // 회원 탈퇴
  @Transactional
  public void withdrawMember(String memberId) {
    Member member = memberRepository.findById(memberId)
      .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + memberId));

    MemberWithdrawal withdrawal = MemberWithdrawal.builder()
      .memberId(member.getMemberId())
      .memberCompanyName(member.getMemberCompanyName())
      .memberTel(member.getMemberTel())
      .memberCompanyTel(member.getMemberCompanyTel())
      .memberWithdrawalDate(LocalDate.now())
      .build();

    memberWithdrawalRepository.save(withdrawal);
    cartRepository.deleteAllByMemberId(memberId);
    memberRepository.deleteById(memberId);
  }

  // 회원 정보 수정 (비밀번호 포함)
  @Transactional
  public void update(String memberId, String encodedPassword, MemberUpdateRequestDTO dto) {
    Member member = memberRepository.findById(memberId)
      .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + memberId));
    member.update(dto.getMemberName(), encodedPassword, dto.getMemberTel(),
      dto.getMemberCompanyTel(), dto.getMemberAddress(), dto.getMemberDetailAddress(),
      dto.getMemberDeliveryAddress(), dto.getMemberDetailDeliveryAddress(),
      dto.getMemberEmail(), dto.getMemberFile());
  }

  // 회원 정보 수정 (비밀번호 제외)
  @Transactional
  public void updateWithoutPassword(String memberId, MemberUpdateRequestDTO dto) {
    Member member = memberRepository.findById(memberId)
      .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + memberId));
    member.updateWithoutPassword(dto.getMemberName(), dto.getMemberTel(),
      dto.getMemberCompanyTel(), dto.getMemberAddress(), dto.getMemberDetailAddress(),
      dto.getMemberDeliveryAddress(), dto.getMemberDetailDeliveryAddress(),
      dto.getMemberEmail(), dto.getMemberFile());
  }

}
