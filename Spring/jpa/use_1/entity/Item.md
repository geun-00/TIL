# 상품 엔티티

```java
@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}
```

- 도서 엔티티
```java
@Entity
@Getter @Setter
@DiscriminatorValue("B")
public class Book extends Item{

    private String author;
    private String isbn;
}
```

- 음반 엔티티
```java
@Entity
@Getter @Setter
@DiscriminatorValue("A")
public class Album extends Item{
    
    private String artist;
    private String etc;
}
```

- 영화 엔티티
```java
@Entity
@Getter @Setter
@DiscriminatorValue("M")
public class Movie extends Item{
    
    private String director;
    private String actor;
}
```