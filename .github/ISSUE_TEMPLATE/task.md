---
name: 📌 일반 작업
description: 리팩토링, 문서, 기타 일반 작업을 위한 이슈
title: "[Task] "
labels: ["task"]
body:
  - type: textarea
  attributes:
    label: 작업 개요
    description: 작업 내용을 간단히 설명해주세요.
  validations:
    required: true

- type: checkboxes
  attributes:
    label: 체크리스트
    options:
      - label: 커밋 완료
      - label: PR 생성
      - label: 테스트 확인