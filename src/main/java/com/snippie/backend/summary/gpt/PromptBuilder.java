package com.snippie.backend.summary.gpt;

import com.snippie.backend.common.exception.ErrorCode;
import com.snippie.backend.common.exception.SnippieException;
import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {
    public String buildPrompt(String type, String inputText, String beforeCode, String afterCode) {
        switch (type.toUpperCase()) {
            case "ISSUE":
                return buildIssuePrompt(inputText);
            case "COMMIT":
                return buildCommitPrompt(inputText, beforeCode, afterCode);
            case "PR":
                return buildPrPrompt(inputText, beforeCode, afterCode);
            default:
                throw new SnippieException(ErrorCode.INVALID_INPUT_VALUE, "지원하지 않는 요약 타입입니다: " + type);
        }
    }

    private String buildIssuePrompt(String inputText) {
        if (inputText != null && !inputText.isBlank()) {
            return """
            다음 내용을 요약해 주세요.
            - `title`: 한 줄로 요약한 이슈 제목
            - `content`: 이슈의 핵심 내용을 아래 Markdown 형식으로 작성
                - "## 이슈 요약" 아래 한 문단
                - "## 작업 예정" 아래 예상 작업 내용 리스트 작성
                - "## 참고 사항" 은 '-'만 넣고 공란으로 두기
            - 반드시 아래 JSON 형식으로만 출력
            
            {
                "title": "요약 제목",
                "content": "## 이슈 요약\\n요약 내용입니다.\\n\\n## 작업 예정\\n- 작업 항목 1\\n- 작업 항목 2\\n- 작업 항목 3\\n\\n## 참고사항\n(관련 문서, 링크, 논의 내용 등)"
            }
            
            ### 요약 대상
            ~~~
            %s
            ~~~
            """.formatted(inputText);
        } else {
            throw new SnippieException(ErrorCode.MISSING_REQUIRED_FIELD, "ISSUE 타입은 inputText가 필수입니다.");
        }
    }


    private String buildCommitPrompt(String inputText, String beforeCode, String afterCode) {
        if (inputText != null && !inputText.isBlank()) {
            return """
                다음 커밋 메시지를 요약해 주세요.
                - `title`: 커밋의 주요 내용을 한 줄로 작성 (예: feat: 로그인 기능 추가)
                - `content`: 구현한 코드 내용을 Markdown 리스트로 작성
                - 반드시 아래 JSON 형식으로만 출력

                {
                  "title": "[Feat, fix 등]]: 요약 제목",
                  "content": "- 주요 구현 사항 1\\n- 주요 변경 사항 2\\n- 주요 변경 사항 3"
                }

                ### 요약 대상
                ~~~
                %s
                ~~~
                """.formatted(inputText);
        } else if (beforeCode != null && afterCode != null) {
            return """
                다음 코드 변경 내용을 요약해 주세요.
                - `title`: 한 줄 커밋 메시지로 작성 (예: feat: 로그인 예외 처리 개선)
                - `content`: 코드 변경 내용의 주요 사항을 Markdown 형식으로 작성
                - 반드시 아래 JSON 형식으로만 출력

                {
                  "title": "커밋메세지",
                  "content": "- \\n- "
                }
               
                ### 변경 전 코드
                ~~~
                %s
                ~~~

                ### 변경 후 코드
                ~~~
                %s
                ~~~
                """.formatted(beforeCode, afterCode);
        } else {
            throw new SnippieException(ErrorCode.MISSING_REQUIRED_FIELD, "COMMIT 타입은 inputText 또는 before/afterCode 중 하나가 필수입니다.");
        }
    }

    private String buildPrPrompt(String inputText, String beforeCode, String afterCode) {
        if (inputText != null && !inputText.isBlank()) {
            return """
                다음 입력된 코드 내용을 바탕으로 깃허브 Pull Request를 작성해 주세요.
                - `title`: PR의 목적과 핵심을 한 줄로 작성
                - `content`: PR 내용을 다음 Markdown 형식으로 작성
                    - `## 개요` 아래에 한 문단으로 요약 작성
                    - `## 작업 내용` 아래에 주요 작업 내용을 리스트로 작성
                - 반드시 아래 JSON 형식으로만 출력

                {
                  "title": "요약 제목",
                  "content": "## 개요\\n~\\n\\n## 작업 내용\\n- ~\\n- ~\\n- ~"
                }

                ### 요약 대상
                ~~~
                %s
                ~~~
                """.formatted(inputText);
        } else if (beforeCode != null && afterCode != null) {
            return """
                다음 코드 변경 내용을 요약해 주세요.
                - `title`: PR의 목적과 핵심을 한 줄로 작성
                - `content`: 코드 변경 내용 및 주요 작업 사항을 다음 Markdown 형식으로 작성
                    - `## 개요` 아래에 한 문단으로 요약 작성
                    - `## 작업 내용` 아래에 주요 변경 사항을 리스트로 작성
                - 반드시 아래 JSON 형식으로만 출력

                {
                  "title": "요약 제목",
                  "content": "## 개요\\n~\\n\\n## 작업 내용\\n- ~\\n- ~\\n- ~"
                }

                ### 변경 전 코드
                ~~~
                %s
                ~~~

                ### 변경 후 코드
                ~~~
                %s
                ~~~
                """.formatted(beforeCode, afterCode);
        } else {
            throw new SnippieException(ErrorCode.MISSING_REQUIRED_FIELD, "PR 타입은 inputText 또는 before/afterCode 중 하나가 필수입니다.");
        }
    }
}