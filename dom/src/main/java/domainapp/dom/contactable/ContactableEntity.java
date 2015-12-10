package domainapp.dom.contactable;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import domainapp.dom.number.ContactNumber;
import domainapp.dom.number.ContactNumberRepository;
import domainapp.dom.role.ContactRoleRepository;
import lombok.Getter;
import lombok.Setter;
import org.apache.isis.applib.annotation.*;

import javax.inject.Inject;
import javax.jdo.annotations.*;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE
)
@DatastoreIdentity(
        strategy = IdGeneratorStrategy.NATIVE,
        column = "id")
@Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@javax.jdo.annotations.Discriminator(
        strategy = DiscriminatorStrategy.CLASS_NAME,
        column = "discriminator")
@Queries({
})
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
@MemberGroupLayout(
        columnSpans={6,0,0,6}
)
public class ContactableEntity implements Comparable<ContactableEntity> {

    public String title() {
        return getName();
    }

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private String name;

    @Column(allowsNull = "true")
    @Property
    @Getter @Setter
    private String email;

    @Column(allowsNull = "true")
    @Property
    @PropertyLayout(multiLine = 6)
    @Getter @Setter
    private String notes;

    @Persistent(mappedBy = "contactableEntity", dependentElement = "true")
    @Collection()
    @CollectionLayout(render = RenderType.EAGERLY)
    @Getter @Setter
    private SortedSet<ContactNumber> contactNumbers = new TreeSet<ContactNumber>();

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Add")
    @MemberOrder(name = "contactNumbers", sequence = "1")
    public ContactableEntity addContactNumber(String type, String number) {
        contactNumberRepository.findOrCreate(this, type, number);
        return this;
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Remove")
    @MemberOrder(name = "contactNumbers", sequence = "2")
    public ContactableEntity removeContactNumber(String type) {
        final Optional<ContactNumber> contactNumberIfAny = Iterables
                .tryFind(getContactNumbers(), cn -> Objects.equal(cn.getType(), type));

        if(contactNumberIfAny.isPresent()) {
            getContactNumbers().remove(contactNumberIfAny.get());
        }
        return this;
    }

    public String default0RemoveContactNumber() {
        return getContactNumbers().size() == 1? getContactNumbers().iterator().next().getType(): null;
    }
    public List<String> choices0RemoveContactNumber() {
        return Lists.transform(Lists.newArrayList(getContactNumbers()), ContactNumber::getType);
    }

    public String disableRemoveContactNumber() {
        return getContactNumbers().isEmpty()? "No contact numbers to remove": null;
    }

    //region > compareTo, toString
    @Override
    public int compareTo(final ContactableEntity other) {
        return org.apache.isis.applib.util.ObjectContracts.compare(this, other, "name");
    }

    @Override
    public String toString() {
        return org.apache.isis.applib.util.ObjectContracts.toString(this, "name");
    }
    //endregion

    @Inject
    ContactRoleRepository contactRoleRepository;

    @Inject
    ContactNumberRepository contactNumberRepository;
}
