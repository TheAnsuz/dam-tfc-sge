package dev.amrv.sge.module.providers;

import dev.amrv.sge.bbdd.Database;
import java.sql.SQLException;
import java.util.Map;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class ProviderBuilder {

    // Datos identificadores
    public String name = null;
    public String cif = null;
    public String reference = null;

    // Datos contacto
    public String mail = null;
    public String phone = null;
    public String phone2 = null;

    // Datos localizacion
    public String address = null;
    public String postalCode = null;
    public String location = null;
    public String province = null;

    // Datos internos
    private int id = -1;

    public ProviderBuilder() {
    }

    public ProviderBuilder(Provider reference, Map<String, ProviderAttribute> attributes) {
        this.cif = reference.CIF();
        this.name = reference.getName();
        this.id = reference.getID();

        ProviderAttribute attr;

        // Datos identificadores
        attr = attributes.get("REFERENCE");
        this.reference = attr == null ? null : attr.getValue();

        // Datos de contacto
        attr = attributes.get("MAIL");
        this.mail = attr == null ? null : attr.getValue();

        attr = attributes.get("PHONE");
        this.phone = attr == null ? null : attr.getValue();

        attr = attributes.get("PHONE2");
        this.phone2 = attr == null ? null : attr.getValue();

        // Datos de ubicacion
        attr = attributes.get("ADDRESS");
        this.address = attr == null ? null : attr.getValue();

        attr = attributes.get("POSTAL_CODE");
        this.postalCode = attr == null ? null : attr.getValue();

        attr = attributes.get("LOCATION");
        this.location = attr == null ? null : attr.getValue();

        attr = attributes.get("PROVINCE");
        this.province = attr == null ? null : attr.getValue();

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
            provider.setName(name);
            provider.commit(database);
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

        if (phone2 != null && !phone2.trim().isEmpty())
            attributes.put("PHONE2", ProviderAttribute.create(database, id, "PHONE2", phone2));

        if (address != null && !address.trim().isEmpty())
            attributes.put("ADDRESS", ProviderAttribute.create(database, id, "ADDRESS", address));

        if (postalCode != null && !postalCode.trim().isEmpty())
            attributes.put("POSTAL_CODE", ProviderAttribute.create(database, id, "POSTAL_CODE", postalCode));

        if (location != null && !location.trim().isEmpty())
            attributes.put("LOCATION", ProviderAttribute.create(database, id, "LOCATION", location));

        if (province != null && !province.trim().isEmpty())
            attributes.put("PHONE", ProviderAttribute.create(database, id, "PROVINCE", province));

        return attributes;
    }
}
