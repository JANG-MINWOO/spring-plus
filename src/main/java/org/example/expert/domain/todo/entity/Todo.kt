package org.example.expert.domain.todo.entity

import jakarta.persistence.*
import org.example.expert.domain.comment.entity.Comment
import org.example.expert.domain.common.entity.Timestamped
import org.example.expert.domain.manager.entity.Manager
import org.example.expert.domain.user.entity.User


@Entity
@Table(name = "todos")
data class Todo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null, //id 는 생성전 null 일수있음

    var title: String,
    var contents: String,
    var weather: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @OneToMany(mappedBy = "todo", cascade = [CascadeType.REMOVE])
    var comments: MutableList<Comment> = mutableListOf(), //mutableListOf() 를 통해 항상 비어있는 MutableList 객체 반환
    /**
     * mutableListOf() 를 사용해서 초기화되지않았거나 비어있는 comments 를 호출하는 메서드를 사용해도
     * 비어있는 MutableList 를 반환함으로써 NPE 방지
     */

    @OneToMany(mappedBy ="todo", cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    var managers: MutableList<Manager> = mutableListOf()
) : Timestamped() { //Timestamped 상속받기

    //JPA 용 매개변수 없는 기본생성자
    protected constructor() : this(
        title = "",
        contents = "",
        weather = "",
        user = User(),
        comments = mutableListOf(),
        managers = mutableListOf()
    ) {}

    fun initializeManagers() {
        managers.add(Manager(user, this))
    }

    /** copy() 메서드
     * 객체를 새로 생성할 떄 모든 값을 넣는게 아니라 특정 값만 넣으면 들어오지 않은 값은 null 을 적용했다가
     * 현재 객체인 this 에서 copy() 메서드를 호출하여 속성값을 일부만 변경하여 새 객체를 만듬
     * 엘비스연산자를 사용해서 null 로 전달된 부분은 기본객체의 값을 그대로 사용합니다.
     */
    fun copyWith(
        title: String? = null,
        contents: String? = null,
        weather: String? = null,
    ): Todo {
        return this.copy(
            title = title ?: this.title,
            contents = contents ?: this.contents,
            weather = weather ?: this.weather,
        )
    }
}
