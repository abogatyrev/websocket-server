package ru.abogatyrev.websocket.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Hamster on 27.01.2016.
 */
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "ApiToken")
public class ApiToken extends CommonItem  {
    @JsonProperty(value = "api_token")
    @Column(nullable = false, updatable = false)
    private String token;

    @JsonProperty(value = "api_token_expiration_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date expiration;

    @JsonIgnore
    @NotNull
    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false, updatable = false)
    private Customer customer;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
