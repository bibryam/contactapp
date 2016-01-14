/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
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
package domainapp.integtests.tests.contact;

import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScripts;

import domainapp.dom.number.ContactNumber;
import domainapp.dom.number.ContactNumberRepository;
import domainapp.fixture.scenarios.demo.DemoFixture;
import domainapp.integtests.tests.DomainAppIntegTest;
import static org.assertj.core.api.Assertions.assertThat;

public class ContactNumberRepositoryTest extends DomainAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;

    @Inject
    ContactNumberRepository contactNumberRepository;

    @Before
    public void setUp() throws Exception {
        // given
        FixtureScript fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);
    }

    public static class ListAll extends ContactNumberRepositoryTest {

        @Test
        public void happyCase() throws Exception {
            // given, when
            final List<ContactNumber> contactNumbers = contactNumberRepository.listAll();
            // then
            assertThat(contactNumbers.size()).isEqualTo(25);
        }

    }
}