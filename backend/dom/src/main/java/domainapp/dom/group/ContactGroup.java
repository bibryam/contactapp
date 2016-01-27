package domainapp.dom.group;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.schema.utils.jaxbadapters.PersistentEntityAdapter;

import domainapp.dom.contactable.ContactableEntity;
import domainapp.dom.country.Country;
import domainapp.dom.role.ContactRole;
import domainapp.dom.role.ContactRoleRepository;
import lombok.Getter;
import lombok.Setter;

@PersistenceCapable
@javax.jdo.annotations.Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Queries({
        @Query(
                name = "findByCountryAndName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.group.ContactGroup "
                        + "WHERE country == :country && name == :name "),
        @Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.group.ContactGroup "
                        + "WHERE name.matches(:regex) "),
})
@Unique(name = "ContactGroup_displayNumber_UNQ", members = {"displayOrder"})
@DomainObject(
        editing = Editing.DISABLED
)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
public class ContactGroup extends ContactableEntity implements Comparable<ContactGroup> {

    private Iterable<ContactRole> contactRoles;

    public String title() {
        return getName() + " (" +  getCountry().getName() + ")";
    }


    @MemberOrder(sequence = "1.1")
    @Column(allowsNull = "true")
    @Property()
    @Getter @Setter
    private Integer displayOrder;

    @MemberOrder(sequence = "2")
    @javax.jdo.annotations.Persistent(defaultFetchGroup = "true") // eager load
    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private Country country;

    @Column(allowsNull = "true")
    @Property
    @Getter @Setter
    private String address;


    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Edit Address")
    @MemberOrder(name = "address", sequence = "1")
    public ContactableEntity changeAddress(@ParameterLayout(named = "Address") String address) {
        setAddress(address);
        return this;
    }

    public String default0ChangeAddress() {
        return getAddress();
    }


    @Programmatic
    @NotPersistent
    public List<ContactRole> getContactRoles() {
        return contactRoleRepository.findByGroup(this);
    }

    private static final Ordering<ContactGroup> byDisplayNumberThenName =
            Ordering
                    .natural()
                    .nullsLast()
                    .onResultOf(displayNumberOf())
                    .compound(Ordering
                            .natural()
                            .onResultOf(nameOf())
                    );

    private static Function<ContactGroup, Integer> displayNumberOf() {
        return new Function<ContactGroup, Integer>() {
            @Nullable @Override
            public Integer apply(@Nullable final ContactGroup contactGroup) {
                return contactGroup.getDisplayOrder();
            }
        };
    }

    @Override
    public int compareTo(final ContactGroup other) {
        return byDisplayNumberThenName.compare(this, other);
    }

    @Inject
    ContactRoleRepository contactRoleRepository;



}
