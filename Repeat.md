# Medium Mission Repeat (https://www.scode.gg/p/13284)

## 1. myList 구현

## 2. write 구현
- @NotBlank 어노테이션 사용으로 빈칸 허용 x
- 글 작성완료 되었다면 redirect 로 처리
- @PreAuthorize() 어노테이션으로 권한 제한
- 그에 맞게 write.html 생성하고 폼 맞추기
- PostController 에서 사용한 writeForm에 isPublished를 이용해 공개 여부 true, false 사용
- 이번 판 에러.... 자꾸 

2024-01-08T14:38:17.635+09:00 ERROR 26612 --- [omcat-handler-9] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: org.thymeleaf.exceptions.TemplateInputException: An error happened during template parsing (template: "class path resource [templates/domain/post/post/myList.html]")] with root cause ...

란 에러가 떠서 html 내에 타임리프 부분이 잘못된 줄 알았지만, 컨트롤러 write 메서드 보면
return을 myList로 하고 있었음. myList가 아니라 write이다. 연계성 없는 메서드가 다른 html로 return 하면 해당 html에선 null 값이 들어올 수 밖에 없다.

## 3. QueryDSL 도입, 글 목록, 내글 목록

- querydsl 이용해 비공개 글은 전체 글 목록에서 숨겨질 수 있다.

## 4. 수정 구현

- Controller에 modify 메서드 생성
  - 예외처리 클래스를 따로 만들어 놓는 것이 나중에 편리함
  - 수정 처리할 땐 PutMapping으로 설정
- PostService에서 Modify 메서드 @Transactional로 처리
  - 매서드 내에서 발생하는 모든 DB 조작은 전부 성공하거나 전부 실패
  - 트랜잭션으로 묶이면 데이터의 일관성을 유지하고 에러가 발생할 경우 롤백하여 이전 상태로 돌아가게 함
- 나머지 처리는 html에서 타임리프 이용

## 5. 삭제 구현

- 글 삭제는 html에서 form으로 처리
- 컨트롤러, 서비스, html로 삭제 기능 구현했지만 다음과 같은 에러가 발생
![img.png](img.png)

삭제 처리가 제대로 안된 모습인데, 아마 csrf 토큰 관련한 문제라 강사님께서 그러셨다. global.js와 layout.html에 각각 다음과 같은 코드 쓰면 처리 가능

```javascript global.js
$(function () {
    $('select[value]').each(function (index, el) {
        const value = $(el).attr('value');
        if (value) $(el).val(value);
    });

    $('a[method="DELETE"], a[method="POST"], a[method="PUT"]').click(function (e) {
        if ($(this).attr('onclick-after')) {
            let onclickAfter = null;

            eval("onclickAfter = function() { " + $(this).attr('onclick-after') + "}");

            if (!onclickAfter()) return false;
        }

        const action = $(this).attr('href');
        const csfTokenValue = $("meta[name='_csrf']").attr("content");

        const formHtml = `
        <form action="${action}" method="POST">
            <input type="hidden" name="_csrf" value="${csfTokenValue}">
            <input type="hidden" name="_method" value="${$(this).attr('method')}">
        </form>
        `;

        const $form = $(formHtml);
        $('body').append($form);
        $form.submit();

        return false;
    });

    $('a[method="POST"][onclick], a[method="DELETE"][onclick], a[method="PUT"][onclick]').each(function (index, el) {
        const onclick = $(el).attr('onclick');

        $(el).removeAttr('onclick');

        $(el).attr('onclick-after', onclick);
    });
});
```

```html layout.html
    <!--이전내용-->
    <meta name="_csrf" th:content="${_csrf.token}"/>
    <meta name="_csrf_header" th:content="${_csrf.headerName}"/>
    <!--이후내용-->
```

## 6. GET /b/user1 구현 (step 9)

- 이번 편은 공개, 비공개 여부에 따라 내글에선 모두 보여야 하지만, 전체 글에선 제한을 둬야함.
- search 메서드에 boolean 매개변수 하나 추가해 줌으로써 전체 글에서 내가 내 글 보는 것 조차 보이지 않을 수 있음
- 이번 편 에러
![img_1.png](img_1.png)
500 코드는 주로 컨트롤러에서 발생한 오류로 오타 혹은 null값을 지닌 매개변수를 선언해 생기는 오류
이유는
![img_2.png](img_2.png)

BlogController에 써야할 매개변수인데 PostController에 똑같이 매개변수를 줘서 null 값이 들어간거였음. 결국에 컨트롤러 착각으로 중복으로 적었던거임

** 추가 **

- 커밋 메시지 바꾸고 싶을 땐
> git rebase -i HEAD~(커밋 개수)

해준 후 i 누른 후 pick 대신 reword로 하고 바꾸고 싶은 커밋으로 바꿔주면 된다.
그리고 esc 누를 때 마다 콘솔 창으로 커서가 이동되는데 밑에 그림에서 설정해주면 esc해도 콘솔창으로 이동하지 않음.
![img_3.png](img_3.png)

## 7. GET /b/user1/1 구현

- 이번 목적은 내글 목록 카테고리 클릭했을 때 url 맨 뒤에 자신의 id가 나오도록 구현

## 8. 조회수 증가

- @Setter(AccessLevel.PRIVATE) --> 클래스 내부에서만 setter 메서드 사용할 수 있도록 제한
- 본 내용에선 @Setter(PROTECTED) 로 변경
-  th:text="${#numbers.formatInteger(post.hit, 3, 'COMMA')}" 이용해 조회수 세미 콤마 생성

## 9. BaseEntity 도입

- BaseEntity --> 정보를 가지는 앤티티
- IdEntity --> 아이디를 가지는 앤티티 (@MappedSuperclass, @Getter 이용)
- @Transient 는 ? JPA와 관련없는 매서드란 것을 알려주는 애노테이션
- 점프 투 스프링부트에선 ManyToMany를 썼지만 실무에선 간단히 이용한 것 이외엔 잘 사용하지 않음. OneToMany로 대신 이용

## 10. PostLike 앤티티 도입

- cascade
- hasLike() 메서드 의미는 추천을 했는가 안 했는가를 확인
- 회원이 글을 추천하는데 이렇게 보면 N:N 관계, 글 입장에서 보면 글은 하나지만 여려 회원명이 추천을 누를 수 있다. 보통 이것을 해결하는게
중간 테이블 (postLike)

## 11. 추천 취소

- canLike 메서드에선 hasLike가 !hasLike 이어야하고 deleteLike는 그 반대
- 추천하는 앤티티 따로 만듦으로써 중복 확인 가능 (PostLike)

## 12. 댓글

- 댓글 입력은 아무 설정해주지 않으면 250글자가 최대이니 주의. @Column 어노테이션으로 설정바람
- th:id="|postComment-${postComment.id}|" 입력으로 자신이 댓글 입력할 때 마다 해당 댓글로 스크롤
- 