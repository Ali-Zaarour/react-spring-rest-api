package example.restapi.entity;

import example.restapi.entity.config.BaseEntity;
import example.restapi.utils.Constants;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="app_user")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppUser extends BaseEntity implements UserDetails {

    @Builder
    public AppUser(
            UUID id,
            Timestamp deletedAt,
            String username,
            String password,
            AppUserStatus statusId,
            boolean active,
            Timestamp lastLoginAt,
            boolean isVerified,
            String verificationToken,
            String resetToken,
            String firstName,
            String lastName,
            LocalDate birthDate,
            String address,
            String phoneNumber,
            List<AppUserRoleMapping> appUserRoleMappings) {
        super(id, deletedAt);
        this.username = username;
        this.password = password;
        this.statusId = statusId;
        this.active = active;
        this.lastLoginAt = lastLoginAt;
        this.isVerified = isVerified;
        this.verificationToken = verificationToken;
        this.resetToken = resetToken;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.appUserRoleMappings = appUserRoleMappings;
    }

    private String username;

    private String password;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private AppUserStatus statusId;

    private boolean active;

    private Timestamp lastLoginAt;

    private boolean isVerified;

    private String verificationToken;

    private String resetToken;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String address;

    private String phoneNumber;

    @OneToMany(mappedBy = "appUser",fetch = FetchType.EAGER)
    private List<AppUserRoleMapping> appUserRoleMappings;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for(AppUserRoleMapping appUserRoleMapping : appUserRoleMappings){
            grantedAuthorities.add(new SimpleGrantedAuthority(
                    Constants.GRANTED_AUTHORITY_FIRST_KEY +appUserRoleMapping.getAppUserRole().getKey()));
            for(AppUserPermissionMapping appUserPermissionMapping:  appUserRoleMapping.getAppUserPermissionMappings()){
                grantedAuthorities.add(new SimpleGrantedAuthority(
                        appUserPermissionMapping.toString()));
            }
        }
        return grantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString(){
        return "UserId:"+getId()+",Username:"+getUsername();
    }
}
