package com.springboot.tools.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: User
 * @date: 2023/4/25
 * @Description:此类用于xxx
 */

@Entity
@Data
@Table(name="id_info")
public class IdInfo implements Serializable {

    private static final long serialVersionUID = 2595640145962054096L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

}
