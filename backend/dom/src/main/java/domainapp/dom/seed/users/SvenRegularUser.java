/*
 *  Copyright 2014 Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package domainapp.dom.seed.users;

import java.util.Arrays;

import org.isisaddons.module.security.dom.user.AccountType;
import org.isisaddons.module.security.seed.scripts.AbstractUserAndRolesFixtureScript;

import domainapp.dom.seed.roles.DomainAppRegularRoleAndPermissions;
import domainapp.dom.seed.tenancies.DomainAppAdminUserTenancy;
import domainapp.dom.seed.tenancies.UsersTenancy;

public class SvenRegularUser extends AbstractUserAndRolesFixtureScript {

    public static final String USER_NAME = "sven";
    public static final String TENANCY_PATH = UsersTenancy.TENANCY_PATH + USER_NAME;

    private static final String PASSWORD = "pass";

    public SvenRegularUser() {
        super(USER_NAME, PASSWORD, null,
                DomainAppAdminUserTenancy.TENANCY_PATH, AccountType.LOCAL,
                Arrays.asList(DomainAppRegularRoleAndPermissions.ROLE_NAME));
    }

    @Override
    protected void execute(ExecutionContext executionContext) {
        super.execute(executionContext);
    }

}
