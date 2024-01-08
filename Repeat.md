# Medium Mission Repeat

## 1. myList 구현

## 2. write 구현
- @NotBlank 어노테이션 사용으로 빈칸 허용 x
- 글 작성완료 되었다면 redirect 로 처리
- @PreAuthorize() 어노테이션으로 권한 제한
- 그에 맞게 write.html 생성하고 폼 맞추기
- 이번 판 에러.... 자꾸 

2024-01-08T14:38:17.635+09:00 ERROR 26612 --- [omcat-handler-9] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: org.thymeleaf.exceptions.TemplateInputException: An error happened during template parsing (template: "class path resource [templates/domain/post/post/myList.html]")] with root cause ...

란 에러가 떠서 html 내에 타임리프 부분이 잘못된 줄 알았지만, 컨트롤러 write 메서드 보면
return을 myList로 하고 있었음. myList가 아니라 write이다. 연계성 없는 메서드가 다른 html로 return 하면 해당 html에선 null 값이 들어올 수 밖에 없다.
