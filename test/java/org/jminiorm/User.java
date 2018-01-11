package org.jminiorm;

import org.jminiorm.attributeconverter.JsonAttributeConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Table(name = "users", indexes = {
        @Index(name = "login_index", columnList = "login ASC")
})
public class User {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(length = 16)
    private String login;
    private String password;
    @Column(name = "registration_date")
    private LocalDate registrationDate;
    @Convert(converter = RolesJsonConverter.class)
    private List<Role> roles;

    public User() {
    }

    // ...getters and setters...

}

public class Role {

    private String name;

    public Role() {
    }

    // ... getters and setters...

}

public class RolesJsonConverter extends JsonAttributeConverter<List<Role>> {
    // Nothing to do here. The class is simply declared to capture de "List<Role>" type in a way that is available at
    // runtime for the JsonAttributeConverter parent class.
}


