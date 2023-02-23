package com.alibaba.nacos.example.spring.boot.model;

import javax.persistence.*;

/**
 * CREATE TABLE `user` (
 *  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
 *  `name` varchar(10) NOT NULL DEFAULT '',
 *  PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
@Entity
@Table(name = "user",schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
