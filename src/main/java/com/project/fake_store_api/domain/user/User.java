package com.project.fake_store_api.domain.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.fake_store_api.domain.cart.Cart;
import com.project.fake_store_api.domain.common.BaseTimeEntity;
import com.project.fake_store_api.domain.user.embeded_class.Address;
import com.project.fake_store_api.domain.user.embeded_class.Name;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String username;
    private String password;
    @Embedded
    private Name name;
    @Embedded
    private Address address;
    private String phone;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Cart> carts = new ArrayList<>();
}
