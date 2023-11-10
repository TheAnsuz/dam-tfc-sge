package dev.amrv.sge.module.providers;

import dev.amrv.sge.bbdd.Database;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class ProviderBuilder {

    public String reference = null;
    public String name = null;
    public String cif = null;
    public String mail = null;
    public String phone = null;

    private int id = -1;

    public ProviderBuilder() {
    }

    public ProviderBuilder(Provider reference, Map<String, ProviderAttribute> attributes) {
        this.cif = reference.CIF();
        this.name = reference.getName();
        this.id = reference.getID();

        ProviderAttribute attr;

        attr = attributes.get("MAIL");
        this.mail = attr == null ? null : attr.getValue();

        attr = attributes.get("REFERENCE");
        this.reference = attr == null ? null : attr.getValue();

        attr = attributes.get("PHONE");
        this.phone = attr == null ? null : attr.getValue();

    }

    public boolean isInvalid() {
        return name == null
                || name.trim().isEmpty()
                || cif == null
                || cif.trim().isEmpty();
    }

    public boolean exists() {
        return id != -1;
    }

    public Provider generate(Database database) throws SQLException {
        Provider provider;

        if (exists()) {
            provider = Provider.get(database, id);
        } else {
            provider = Provider.create(database, name, cif);
            id = provider.getID();
        }
        return provider;
    }

    // Always call after generate()
    public Map<String, ProviderAttribute> generateAttributes(Database database) throws SQLException {
        final Map<String, ProviderAttribute> attributes = ProviderAttribute.getAll(database, id);

        if (reference != null && !reference.trim().isEmpty())
            attributes.put("REFERENCE", ProviderAttribute.create(database, id, "REFERENCE", reference));

        if (mail != null && !mail.trim().isEmpty())
            attributes.put("MAIL", ProviderAttribute.create(database, id, "MAIL", mail));

        if (phone != null && !phone.trim().isEmpty())
            attributes.put("PHONE", ProviderAttribute.create(database, id, "PHONE", phone));

        return attributes;
    }
}
