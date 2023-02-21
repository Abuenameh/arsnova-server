/*
 * This file is part of ARSnova Backend.
 * Copyright (C) 2012-2019 The ARSnova Team and Contributors
 *
 * ARSnova Backend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ARSnova Backend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.particify.arsnova.core.security.pac4j;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.pac4j.oidc.profile.OidcProfile;
import org.pac4j.saml.profile.SAML2Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.particify.arsnova.core.config.properties.AuthenticationProviderProperties;
import net.particify.arsnova.core.model.UserProfile;
import net.particify.arsnova.core.security.User;
import net.particify.arsnova.core.service.UserService;

/**
 * Loads UserDetails for an Pac4j SSO user (e.g. {@link UserProfile.AuthProvider#OIDC}) based on an unique identifier
 * extracted from the Pac4j profile.
 *
 * @author Daniel Gerhardt
 */
@Service
public class SsoUserDetailsService implements AuthenticationUserDetailsService<SsoAuthenticationToken> {
  public static final GrantedAuthority ROLE_OAUTH_USER = new SimpleGrantedAuthority("ROLE_OAUTH_USER");

  protected final Collection<GrantedAuthority> defaultGrantedAuthorities = Set.of(
      User.ROLE_USER,
      ROLE_OAUTH_USER
  );
  private final AuthenticationProviderProperties.Saml samlProperties;
  private final UserService userService;

  public SsoUserDetailsService(final UserService userService,
      final AuthenticationProviderProperties authenticationProviderProperties) {
    this.userService = userService;
    this.samlProperties = authenticationProviderProperties.getSaml();
  }

  public User loadUserDetails(final SsoAuthenticationToken token)
      throws UsernameNotFoundException {
    final Set<GrantedAuthority> grantedAuthorities = new HashSet<>(defaultGrantedAuthorities);
    if (token.getDetails() instanceof OidcProfile) {
      final OidcProfile profile = (OidcProfile) token.getDetails();
      if (userService.isAdmin(profile.getId(), UserProfile.AuthProvider.OIDC)) {
        grantedAuthorities.add(User.ROLE_ADMIN);
      }
      final Optional<UserProfile> userProfile = Optional.ofNullable(
          userService.getByAuthProviderAndLoginId(UserProfile.AuthProvider.OIDC, profile.getId()));
      return new User(
          userProfile.orElse(
              userService.create(
                  new UserProfile(UserProfile.AuthProvider.OIDC, profile.getId()))),
          grantedAuthorities);
    } else if (token.getDetails() instanceof SAML2Profile) {
      final SAML2Profile profile = (SAML2Profile) token.getDetails();
      final String uidAttr = samlProperties.getUserIdAttribute();
      final String uid;
      if (uidAttr == null || "".equals(uidAttr)) {
        uid = profile.getId();
      } else {
        uid = profile.getAttribute(uidAttr, List.class).get(0).toString();
      }
      if (userService.isAdmin(uid, UserProfile.AuthProvider.SAML)) {
        grantedAuthorities.add(User.ROLE_ADMIN);
      }
      final Optional<UserProfile> userProfile = Optional.ofNullable(
          userService.getByAuthProviderAndLoginId(UserProfile.AuthProvider.SAML, uid));
      return new User(
          userProfile.orElse(
              userService.create(
                  new UserProfile(UserProfile.AuthProvider.SAML, uid))),
          grantedAuthorities);
    } else {
      throw new IllegalArgumentException("AuthenticationToken not supported");
    }
  }
}
