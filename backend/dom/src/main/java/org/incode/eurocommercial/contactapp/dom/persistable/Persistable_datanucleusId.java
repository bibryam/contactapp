/*
 *  Copyright 2015-2016 Eurocommercial Properties NV
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
package org.incode.eurocommercial.contactapp.dom.persistable;

import javax.jdo.JDOHelper;

import org.datanucleus.enhancement.Persistable;
import org.datanucleus.identity.DatastoreId;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;

// @Mixin // see https://issues.apache.org/jira/browse/ISIS-1311
public class Persistable_datanucleusId {

    private final Persistable persistable;

    public Persistable_datanucleusId(Persistable persistable) {
        this.persistable = persistable;
    }

    @MemberOrder(name = "Metadata", sequence = "1")
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_ASSOCIATION)
    @PropertyLayout(named = "Id")
    public Object datanucleusId() {
        final Object objectId = JDOHelper.getObjectId(persistable);
        if(objectId instanceof DatastoreId) {
            final DatastoreId datastoreId = (DatastoreId) objectId;
            return datastoreId.getKeyAsObject();
        }
        return null;
    }

    public boolean hideDatanucleusId() {
        return datanucleusId() == null;
    }


}
