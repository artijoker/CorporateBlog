package com.example.blog.domain.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Title")
    private String title;

    @Column(name = "Anons")
    private String anons;

    @Column(name = "FullText")
    private String fullText;


    @Column(name = "IsAllowCommenting")
    private Boolean isAllowCommenting;

    @Column(name = "LastChange")
    private LocalDate lastChange;

    @ManyToOne
    @JoinColumn(name = "AccountId", referencedColumnName = "id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "CategoryId", referencedColumnName = "id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "StatusId", referencedColumnName = "id")
    private PostStatus status;

    @OneToMany(mappedBy = "post")
    private List<Comment> comment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnons() {
        return anons;
    }

    public void setAnons(String anons) {
        this.anons = anons;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public Boolean getIsAllowCommenting() {
        return isAllowCommenting;
    }

    public void setIsAllowCommenting(Boolean isAllowCommenting) {
        this.isAllowCommenting = isAllowCommenting;
    }

    public LocalDate getLastChange() {
        return lastChange;
    }

    public void setLastChange(LocalDate lastChange) {
        this.lastChange = lastChange;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }
}
