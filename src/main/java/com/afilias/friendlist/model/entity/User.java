package com.afilias.friendlist.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "ID")
    private long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CITY", nullable = false)
    private String city;

}
