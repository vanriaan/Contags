package walt.der.van.riaan.contags.dto;

/**
 * Created by riaanvanderwalt on 15/01/12.
 */
public class Tag {
    private static final String tableName = "tag";

    private String personIdentifier;
    private String mediaIdentifier;

    public String getPersonIdentifier() {
        return personIdentifier;
    }

    public void setPersonIdentifier(String personIdentifier) {
        this.personIdentifier = personIdentifier;
    }

    public String getMediaIdentifier() {
        return mediaIdentifier;
    }

    public void setMediaIdentifier(String mediaIdentifier) {
        this.mediaIdentifier = mediaIdentifier;
    }
}
