package com.project.fake_store_api.domain.user;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl extends QuerydslRepositorySupport {

    public UserRepositoryImpl() {
        super(User.class);
    }

    public List<User> findWithLimit(Long limit) {
        QUser user = QUser.user;
        return from(user).limit(limit).fetch();
    }

}
