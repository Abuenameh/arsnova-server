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

package de.thm.arsnova.security;

import java.util.HashSet;
import java.util.Set;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import de.thm.arsnova.model.UserProfile;
import de.thm.arsnova.service.UserService;

/**
 * Class to load a user based on the results from CAS.
 */
public class CasUserDetailsService extends AbstractCasAssertionUserDetailsService {
	public static final GrantedAuthority ROLE_CAS_USER = new SimpleGrantedAuthority("ROLE_CAS_USER");

	@Autowired
	private UserService userService;

	@Override
	protected UserDetails loadUserDetails(final Assertion assertion) {
		final Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		grantedAuthorities.add(User.ROLE_USER);
		grantedAuthorities.add(ROLE_CAS_USER);
		final String uid = assertion.getPrincipal().getName();
		if (userService.isAdmin(uid)) {
			grantedAuthorities.add(User.ROLE_ADMIN);
		}

		return userService.loadUser(UserProfile.AuthProvider.CAS, assertion.getPrincipal().getName(),
				grantedAuthorities, true);
	}
}
