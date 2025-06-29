---
name: 🐛 버그 리포트
description: 문제가 되는 동작을 보고합니다.
title: "[Bug] "
labels: ["bug"]
body:
  - type: textarea
    attributes:
      label: 문제 설명
      description: 어떤 문제가 발생했는지 적어주세요.
    validations:
      required: true

  - type: textarea
    attributes:
      label: 재현 방법
      description: 버그가 어떻게 발생하는지 재현 과정을 작성해주세요.

  - type: input
    attributes:
      label: 환경 정보
      placeholder: 예: macOS 14 / Chrome 124