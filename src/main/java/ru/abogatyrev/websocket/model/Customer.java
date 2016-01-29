package ru.abogatyrev.websocket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Hamster on 26.01.2016.
 */
@Entity
@Table(name = "Customer")
public class Customer extends CommonItem {
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<ApiToken> apiTokens;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ApiToken> getApiTokens() {
        return apiTokens;
    }

    public void setApiTokens(List<ApiToken> apiTokens) {
        this.apiTokens = apiTokens;
    }
}
